package com.example.pdf_generator_from_html.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
public class PdfGeneratorService {

    public byte[] generatePdf(String htmlFilePath, String cssFilePath) {
        try {
            // Load HTML and CSS
            String htmlContent = loadFileFromResources(htmlFilePath);
            String cssContent = loadFileFromResources(cssFilePath);

            // Combine HTML and CSS
            Document document = Jsoup.parse(htmlContent);
            document.outputSettings().syntax(Document.OutputSettings.Syntax.xml); // Ensure XHTML
            document.head().appendElement("style").attr("type", "text/css").text(cssContent);

            // Convert to PDF
            return renderPdf(document.html());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }

    private String loadFileFromResources(String filePath) throws Exception {
        Path path = new ClassPathResource(filePath).getFile().toPath();
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    }

    private byte[] renderPdf(String xhtml) throws Exception {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();

            // Set the document
            renderer.setDocumentFromString(xhtml);

            // Render PDF
            renderer.layout();
            renderer.createPDF(outputStream);

            return outputStream.toByteArray();
        }
    }
}