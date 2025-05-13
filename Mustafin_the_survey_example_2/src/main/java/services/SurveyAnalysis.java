package services;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import models.Answer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

// Принцип единственной ответственности и паттерн одиночка

public class SurveyAnalysis {

    public static void exportResults(List<Answer> answers, String format) throws IOException {
        if ("CSV".equalsIgnoreCase(format)) {
            exportToCSV(answers);
        } else if ("PDF".equalsIgnoreCase(format)) {
            exportToPDF(answers);
        } else {
            throw new IllegalArgumentException("Unsupported format: " + format);
        }

    }

    private static void exportToCSV(List<Answer> answers) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("results.csv"))) {
            writer.write("Question ID, User ID, Answer\n");
            for (Answer answer : answers) {
                writer.write(answer.getQuestionId() + "," + answer.getUserId() + "," + answer.getAnswerText() + "\n");
            }
        }
    }

    private static void exportToPDF(List<Answer> answers) {
        String pdfFileName = "results.pdf";
        PdfWriter writer;

        try {
            writer = new PdfWriter(pdfFileName);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Заголовок документа
            document.add(new Paragraph("Survey Results"));

            // Добавление данных в документ
            for (Answer answer : answers) {
                document.add(new Paragraph(String.format("Question ID: %d - User ID: %d - Answer: %s",
                        answer.getQuestionId(), answer.getUserId(), answer.getAnswerText())));
            }

            document.close();
            System.out.println("PDF exported successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error exporting PDF: " + e.getMessage());
        }
    }

}
