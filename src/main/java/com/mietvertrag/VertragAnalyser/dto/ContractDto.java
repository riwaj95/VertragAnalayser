package com.mietvertrag.VertragAnalyser.dto;

import com.mietvertrag.VertragAnalyser.model.ContractStatus;
import com.mietvertrag.VertragAnalyser.model.RiskLevel;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ContractDto {
    private Long id;
    private String filename;
    private ContractStatus status;
    private LocalDateTime uploadDate;
    private RiskLevel overallRiskScore;
    private Integer issuesFound;
}
