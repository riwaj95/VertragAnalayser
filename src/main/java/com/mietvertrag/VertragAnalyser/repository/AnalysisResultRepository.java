package com.mietvertrag.VertragAnalyser.repository;

import com.mietvertrag.VertragAnalyser.model.AnalysisResult;
import com.mietvertrag.VertragAnalyser.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnalysisResultRepository extends JpaRepository<AnalysisResult, Long> {
    Optional<AnalysisResult> findByContract(Contract contract);
    Optional<AnalysisResult> findByContractId(Long contractId);
}
