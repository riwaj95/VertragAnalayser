package com.mietvertrag.VertragAnalyser.dto;

import com.mietvertrag.VertragAnalyser.model.RiskLevel;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AnalysisResponse {
    private Long id;
    private Long contractId;
    private String filename;
    private RiskLevel overallRiskScore;
    private String summary;
    private List<ClauseDto> clauses;
    private LocalDateTime analysisDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public RiskLevel getOverallRiskScore() {
        return overallRiskScore;
    }

    public void setOverallRiskScore(RiskLevel overallRiskScore) {
        this.overallRiskScore = overallRiskScore;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<ClauseDto> getClauses() {
        return clauses;
    }

    public void setClauses(List<ClauseDto> clauses) {
        this.clauses = clauses;
    }

    public LocalDateTime getAnalysisDate() {
        return analysisDate;
    }

    public void setAnalysisDate(LocalDateTime analysisDate) {
        this.analysisDate = analysisDate;
    }
}
