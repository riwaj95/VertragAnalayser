package com.mietvertrag.VertragAnalyser.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "clauses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Clause {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "analysis_result_id")
    private AnalysisResult analysisResult;

    @Column(columnDefinition = "TEXT")
    private String clauseText;

    @Enumerated(EnumType.STRING)
    private RiskLevel riskLevel;

    @Column(columnDefinition = "TEXT")
    private String issueDescription;

    private String legalReference;

    @Column(columnDefinition = "TEXT")
    private String recommendation;

    private Integer clauseOrder;
}
