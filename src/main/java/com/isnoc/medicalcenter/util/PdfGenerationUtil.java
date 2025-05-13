package com.isnoc.medicalcenter.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.isnoc.medicalcenter.entity.Report;
import com.isnoc.medicalcenter.entity.ReportType;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Map;

@Component
public class PdfGenerationUtil {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final float MARGIN = 50;
    private static final float FONT_SIZE = 12;
    private static final float LEADING = 1.5f * FONT_SIZE;

    /**
     * Generates a PDF document from a report
     * 
     * @param report The report entity to generate PDF from
     * @return PDF document as byte array
     * @throws IOException if there's an error generating the PDF
     */
    public byte[] generateReportPdf(Report report) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                float yPosition = page.getMediaBox().getHeight() - MARGIN;
                
                // Set font for the document
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
                
                // Add report title
                ReportType reportType = report.getReportType();
                contentStream.beginText();
                contentStream.newLineAtOffset(MARGIN, yPosition);
                contentStream.showText(reportType.getReportName());
                contentStream.endText();
                yPosition -= LEADING * 1.5;
                
                // Add report generation info
                contentStream.setFont(PDType1Font.HELVETICA, FONT_SIZE);
                contentStream.beginText();
                contentStream.newLineAtOffset(MARGIN, yPosition);
                contentStream.showText("Date: " + DATE_FORMATTER.format(report.getCreatedDate()));
                contentStream.endText();
                yPosition -= LEADING;
                
                // Add patient info
                contentStream.beginText();
                contentStream.newLineAtOffset(MARGIN, yPosition);
                contentStream.showText("Patient: " + report.getVisit().getPatient().getName());
                contentStream.endText();
                yPosition -= LEADING;
                
                contentStream.beginText();
                contentStream.newLineAtOffset(MARGIN, yPosition);
                contentStream.showText("Patient ID: " + report.getVisit().getPatient().getPatientId());
                contentStream.endText();
                yPosition -= LEADING * 1.5;
                
                // Header for report content
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
                contentStream.beginText();
                contentStream.newLineAtOffset(MARGIN, yPosition);
                contentStream.showText("Report Details");
                contentStream.endText();
                yPosition -= LEADING * 1.2;
                
                // Render report data based on JSON
                contentStream.setFont(PDType1Font.HELVETICA, FONT_SIZE);
                yPosition = renderJsonNodeToPdf(contentStream, report.getReportData(), yPosition, MARGIN, 0);
            }
            
            // Convert document to byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            return baos.toByteArray();
        }
    }
    
    /**
     * Recursively render JSON data to PDF
     */
    private float renderJsonNodeToPdf(PDPageContentStream contentStream, JsonNode node, 
                                     float yPosition, float xPosition, int depth) throws IOException {
        if (node.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                
                // Render field name
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, FONT_SIZE);
                contentStream.newLineAtOffset(xPosition + (depth * 10), yPosition);
                contentStream.showText(field.getKey() + ":");
                contentStream.endText();
                yPosition -= LEADING;
                
                // Render field value (which might be another object or array)
                yPosition = renderJsonNodeToPdf(contentStream, field.getValue(), 
                                               yPosition, xPosition, depth + 1);
                
                // Add some spacing between fields
                if (fields.hasNext()) {
                    yPosition -= FONT_SIZE / 2;
                }
            }
        } else if (node.isArray()) {
            for (int i = 0; i < node.size(); i++) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, FONT_SIZE);
                contentStream.newLineAtOffset(xPosition + (depth * 10), yPosition);
                contentStream.showText("Item " + (i + 1) + ":");
                contentStream.endText();
                yPosition -= LEADING;
                
                yPosition = renderJsonNodeToPdf(contentStream, node.get(i), 
                                               yPosition, xPosition, depth + 1);
                
                // Add spacing between array items
                if (i < node.size() - 1) {
                    yPosition -= FONT_SIZE / 2;
                }
            }
        } else {
            // Render simple value
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, FONT_SIZE);
            contentStream.newLineAtOffset(xPosition + (depth * 10), yPosition);
            contentStream.showText(node.asText());
            contentStream.endText();
            yPosition -= LEADING;
        }
        
        return yPosition;
    }
}