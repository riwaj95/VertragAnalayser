package com.mietvertrag.VertragAnalyser.dto;

import com.mietvertrag.VertragAnalyser.model.RiskLevel;
import lombok.Data;

@Data
public class ClauseDto {
    private Long id;
    private String clauseText;
    private RiskLevel riskLevel;
    private String issueDescription;
    private String legalReference;
    private String recommendation;
    private Integer clauseOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClauseText() {
        return clauseText;
    }

    public void setClauseText(String clauseText) {
        this.clauseText = clauseText;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }

    public String getLegalReference() {
        return legalReference;
    }

    public void setLegalReference(String legalReference) {
        this.legalReference = legalReference;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public Integer getClauseOrder() {
        return clauseOrder;
    }

    public void setClauseOrder(Integer clauseOrder) {
        this.clauseOrder = clauseOrder;
    }
}
