package com.mietvertrag.VertragAnalyser.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "analysis_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "contract_id")
    private Contract contract;

    @Enumerated(EnumType.STRING)
    private RiskLevel overallRiskScore;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @OneToMany(mappedBy = "analysisResult", cascade = CascadeType.ALL)
    private List<Clause> clauses = new ArrayList<>();

    @Column(name = "analysis_date")
    private LocalDateTime analysisDate;

    @PrePersist
    protected void onCreate() {
        analysisDate = LocalDateTime.now();
    }
}
