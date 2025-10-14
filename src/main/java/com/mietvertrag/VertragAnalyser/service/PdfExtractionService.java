package com.mietvertrag.VertragAnalyser.service;

import com.mietvertrag.VertragAnalyser.utils.PdfUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PdfExtractionService {
    private final PdfUtil pdfUtil;

    public PdfExtractionService(PdfUtil pdfUtil) {
        this.pdfUtil = pdfUtil;
    }

    public String extractText(String filePath) {
        try {
            String text = pdfUtil.extractTextFromPdf(filePath);

            if (text == null || text.trim().isEmpty()) {
                throw new RuntimeException("Could not extract text from PDF. File might be scanned or encrypted.");
            }

            // Clean up text
            text = cleanText(text);

            return text;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read PDF file: " + e.getMessage());
        }
    }

    private String cleanText(String text) {
        // Remove excessive whitespace
        text = text.replaceAll("\\s+", " ");

        // Remove special characters that might confuse AI
        text = text.replaceAll("[^\\p{L}\\p{N}\\p{P}\\s€$%]", "");

        return text.trim();
    }
}
