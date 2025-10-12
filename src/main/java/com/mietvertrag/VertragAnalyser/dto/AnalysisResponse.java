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
}
