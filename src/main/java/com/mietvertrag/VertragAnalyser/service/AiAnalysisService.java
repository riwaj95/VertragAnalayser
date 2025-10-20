package com.mietvertrag.VertragAnalyser.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mietvertrag.VertragAnalyser.model.AnalysisResult;
import com.mietvertrag.VertragAnalyser.model.Clause;
import com.mietvertrag.VertragAnalyser.model.Contract;
import com.mietvertrag.VertragAnalyser.model.RiskLevel;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class AiAnalysisService {

    @Value("${openai.api.key:}")
    private String openAiApiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public AnalysisResult analyzeContract(String contractText, Contract contract) {

        if (openAiApiKey == null || openAiApiKey.isEmpty()) {
            // For testing without API key - return mock data
            return createMockAnalysis(contract);
        }

        try {
            OpenAiService service = new OpenAiService(openAiApiKey, Duration.ofSeconds(60));

            String prompt = buildAnalysisPrompt(contractText);

            ChatMessage systemMessage = new ChatMessage("system",
                    "You are a German rental law expert. Analyze the contract and return structured JSON.");
            ChatMessage userMessage = new ChatMessage("user", prompt);

            ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                    .model("gpt-4")
                    .messages(List.of(systemMessage, userMessage))
                    .temperature(0.3)
                    .maxTokens(2000)
                    .build();

            ChatCompletionResult result = service.createChatCompletion(completionRequest);
            String aiResponse = result.getChoices().get(0).getMessage().getContent();

            // Parse AI response
            return parseAiResponse(aiResponse, contract);

        } catch (Exception e) {
            throw new RuntimeException("AI analysis failed: " + e.getMessage());
        }
    }

    private String buildAnalysisPrompt(String contractText) {
        return String.format("""
            Analyze this German rental contract (Mietvertrag) and identify problematic clauses.
            
            For each problematic clause, provide:
            1. The exact clause text
            2. Risk level: GREEN (legal), YELLOW (questionable), RED (illegal)
            3. Issue description in German
            4. Relevant BGB reference if illegal
            5. Recommendation for tenant in German
            
            Common illegal clauses to check:
            - Schönheitsreparaturen (renovation obligations)
            - Excessive deposit (over 3 months rent)
            - Complete pet prohibition
            - Unrestricted landlord entry rights
            - Subletting prohibitions
            - Indexmietvertrag issues
            
            Return response as JSON with this structure:
            {
              "overallRisk": "RED|YELLOW|GREEN",
              "summary": "Brief summary in German",
              "clauses": [
                {
                  "clauseText": "exact text",
                  "riskLevel": "RED|YELLOW|GREEN",
                  "issue": "description in German",
                  "bgbReference": "BGB §XXX",
                  "recommendation": "advice in German"
                }
              ]
            }
            
            Contract text:
            %s
            """, contractText.substring(0, Math.min(contractText.length(), 8000)));
    }

    private AnalysisResult parseAiResponse(String aiResponse, Contract contract) {
        try {
            // Extract JSON from response (AI might add extra text)
            String jsonStr = extractJson(aiResponse);
            JsonNode jsonNode = objectMapper.readTree(jsonStr);

            AnalysisResult analysisResult = new AnalysisResult();
            analysisResult.setContract(contract);

            // Parse overall risk
            String overallRisk = jsonNode.get("overallRisk").asText();
            analysisResult.setOverallRiskScore(RiskLevel.valueOf(overallRisk));

            // Parse summary
            analysisResult.setSummary(jsonNode.get("summary").asText());

            // Parse clauses
            List<Clause> clauses = new ArrayList<>();
            JsonNode clausesNode = jsonNode.get("clauses");

            if (clausesNode != null && clausesNode.isArray()) {
                int order = 1;
                for (JsonNode clauseNode : clausesNode) {
                    Clause clause = new Clause();
                    clause.setAnalysisResult(analysisResult);
                    clause.setClauseText(clauseNode.get("clauseText").asText());
                    clause.setRiskLevel(RiskLevel.valueOf(clauseNode.get("riskLevel").asText()));
                    clause.setIssueDescription(clauseNode.get("issue").asText());
                    clause.setLegalReference(clauseNode.get("bgbReference").asText());
                    clause.setRecommendation(clauseNode.get("recommendation").asText());
                    clause.setClauseOrder(order++);
                    clauses.add(clause);
                }
            }

            analysisResult.setClauses(clauses);
            return analysisResult;

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI response: " + e.getMessage());
        }
    }

    private String extractJson(String text) {
        // Find JSON object in text
        int start = text.indexOf("{");
        int end = text.lastIndexOf("}");

        if (start != -1 && end != -1 && start < end) {
            return text.substring(start, end + 1);
        }

        return text;
    }

    // Mock analysis for testing without OpenAI API
    private AnalysisResult createMockAnalysis(Contract contract) {
        AnalysisResult result = new AnalysisResult();
        result.setContract(contract);
        result.setOverallRiskScore(RiskLevel.RED);
        result.setSummary("Ihr Mietvertrag enthält 3 problematische Klauseln, von denen 2 möglicherweise unwirksam sind.");

        List<Clause> clauses = new ArrayList<>();

        Clause clause1 = new Clause();
        clause1.setAnalysisResult(result);
        clause1.setClauseText("Der Mieter ist verpflichtet, alle Schönheitsreparaturen während der Mietzeit durchzuführen...");
        clause1.setRiskLevel(RiskLevel.RED);
        clause1.setIssueDescription("Unrenoviert übernommene Wohnung - Schönheitsreparaturklausel unwirksam");
        clause1.setLegalReference("BGB §535, §538");
        clause1.setRecommendation("Diese Klausel ist unwirksam. Sie müssen keine Renovierungen durchführen.");
        clause1.setClauseOrder(1);
        clauses.add(clause1);

        Clause clause2 = new Clause();
        clause2.setAnalysisResult(result);
        clause2.setClauseText("Die Kaution beträgt 4 Monatsmieten und ist bei Vertragsabschluss zu zahlen...");
        clause2.setRiskLevel(RiskLevel.RED);
        clause2.setIssueDescription("Kaution überschreitet gesetzliche Grenze von 3 Monatsmieten");
        clause2.setLegalReference("BGB §551");
        clause2.setRecommendation("Nur maximal 3 Monatsmieten sind zulässig. Fordern Sie die Differenz zurück.");
        clause2.setClauseOrder(2);
        clauses.add(clause2);

        Clause clause3 = new Clause();
        clause3.setAnalysisResult(result);
        clause3.setClauseText("Der Vermieter kann die Wohnung jederzeit ohne Ankündigung betreten...");
        clause3.setRiskLevel(RiskLevel.YELLOW);
        clause3.setIssueDescription("Besichtigungsrecht ohne angemessene Frist ist problematisch");
        clause3.setLegalReference("BGB §535");
        clause3.setRecommendation("Vermieter muss berechtigtes Interesse haben und Besuch 48h vorher ankündigen.");
        clause3.setClauseOrder(3);
        clauses.add(clause3);

        result.setClauses(clauses);
        return result;
    }
}
