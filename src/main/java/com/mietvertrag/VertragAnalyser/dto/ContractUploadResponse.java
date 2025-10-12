package com.mietvertrag.VertragAnalyser.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ContractUploadResponse {
    private Long contractId;
    private String filename;
    private String status;
    private String message;
}
