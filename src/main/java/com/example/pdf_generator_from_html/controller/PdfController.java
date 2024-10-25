package com.example.pdf_generator_from_html.controller;

import com.example.pdf_generator_from_html.service.PdfGeneratorService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PdfController {
    private final PdfGeneratorService pdfGeneratorService;

    public PdfController(PdfGeneratorService pdfGeneratorService) {
        this.pdfGeneratorService = pdfGeneratorService;
    }



    @GetMapping("/")
    public String home(){
        return "Hello World!";
    }

    @GetMapping("/generate-pdf")
    public ResponseEntity<byte[]> generatePdf() {
        try {
            // Define file paths
            String htmlFilePath = "templates/document.html";
            String cssFilePath = "templates/styles.css";

            byte[] pdfContent = pdfGeneratorService.generatePdf(htmlFilePath, cssFilePath);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=generated.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                    .body(pdfContent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}