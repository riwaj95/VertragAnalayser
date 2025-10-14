package com.mietvertrag.VertragAnalyser.service;

import com.mietvertrag.VertragAnalyser.dto.AnalysisResponse;
import com.mietvertrag.VertragAnalyser.dto.ClauseDto;
import com.mietvertrag.VertragAnalyser.model.AnalysisResult;
import com.mietvertrag.VertragAnalyser.model.Clause;
import com.mietvertrag.VertragAnalyser.model.Contract;
import com.mietvertrag.VertragAnalyser.model.User;
import com.mietvertrag.VertragAnalyser.repository.AnalysisResultRepository;
import com.mietvertrag.VertragAnalyser.repository.ContractRepository;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class AnalysisService {

    private final AnalysisResultRepository analysisResultRepository;
    private final ContractRepository contractRepository;

    public AnalysisService(AnalysisResultRepository analysisResultRepository,
                           ContractRepository contractRepository) {
        this.analysisResultRepository = analysisResultRepository;
        this.contractRepository = contractRepository;
    }

    public AnalysisResponse getAnalysisByContractId(Long contractId, User user) {
        // Verify contract belongs to user
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new RuntimeException("Contract not found"));

        if (!contract.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You don't have access to this analysis");
        }

        // Get analysis result
        AnalysisResult result = analysisResultRepository.findByContractId(contractId)
                .orElseThrow(() -> new RuntimeException("Analysis not found for this contract"));

        return mapToDto(result);
    }

    private AnalysisResponse mapToDto(AnalysisResult result) {
        AnalysisResponse response = new AnalysisResponse();
        response.setId(result.getId());
        response.setContractId(result.getContract().getId());
        response.setFilename(result.getContract().getFilename());
        response.setOverallRiskScore(result.getOverallRiskScore());
        response.setSummary(result.getSummary());
        response.setAnalysisDate(result.getAnalysisDate());

        // Map clauses
        response.setClauses(
                result.getClauses().stream()
                        .map(this::mapClauseToDto)
                        .collect(Collectors.toList())
        );

        return response;
    }

    private ClauseDto mapClauseToDto(Clause clause) {
        ClauseDto dto = new ClauseDto();
        dto.setId(clause.getId());
        dto.setClauseText(clause.getClauseText());
        dto.setRiskLevel(clause.getRiskLevel());
        dto.setIssueDescription(clause.getIssueDescription());
        dto.setLegalReference(clause.getLegalReference());
        dto.setRecommendation(clause.getRecommendation());
        dto.setClauseOrder(clause.getClauseOrder());
        return dto;
    }
}
