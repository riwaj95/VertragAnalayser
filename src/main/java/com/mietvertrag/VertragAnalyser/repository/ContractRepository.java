package com.mietvertrag.VertragAnalyser.repository;

import com.mietvertrag.VertragAnalyser.model.Contract;
import com.mietvertrag.VertragAnalyser.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, Long> {
    List<Contract> findByUserOrderByUploadDateDesc(User user);
    List<Contract> findByUserId(Long userId);
}
