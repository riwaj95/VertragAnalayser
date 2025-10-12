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
}
