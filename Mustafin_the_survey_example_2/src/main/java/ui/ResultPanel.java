package ui;
import models.Answer;
import controllers.UserController;
import controllers.SurveyController;
import services.SurveyService;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import services.SurveyAnalysis;

public class ResultPanel extends JFrame {

    private List<Answer> collectedAnswers; // Список ответов пользователя

    public ResultPanel(List<Answer> collectedAnswers) {

        setTitle("Анкетирование");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        this.collectedAnswers = collectedAnswers;

        JTextArea resultsArea = new JTextArea();
        StringBuilder results = new StringBuilder("Ваши ответы: \n");
        for (Answer answer: collectedAnswers){
            results.append(String.format("Вопрос ID: %d, Ответ: %s\n", answer.getQuestionId(),answer.getAnswerText()));
        }

        resultsArea.setText(results.toString());
        resultsArea.setEditable(false);
        add(new JScrollPane(resultsArea));

        JButton exportCsvButton = new JButton("Экспорт в CSV");
//
        add(exportCsvButton);
        setVisible(true);


        UserController userController = new UserController();

        // Создаем экземпляр SurveyService
        SurveyService surveyService = new SurveyService();

        // Теперь передаем его в SurveyController
        SurveyController surveyController = new SurveyController(surveyService);

        // Установка панели входа
        LoginPanel loginPanel = new LoginPanel(new MainFrame(), userController, surveyController);
        add(loginPanel);

        setVisible(true);

        //this.collectedAnswers = collectedAnswers;
        //setLayout(new BorderLayout());

        //JTextArea resultsArea = new JTextArea();
        //StringBuilder results = new StringBuilder("Ваши ответы:\n");

       // for (Answer answer : collectedAnswers) {
          //  results.append(String.format("Вопрос ID: %d, Ответ: %s\n", answer.getQuestionId(), answer.getAnswerText()));
       // }

       // resultsArea.setText(results.toString());
       // resultsArea.setEditable(false);

       // add(new JScrollPane(resultsArea), BorderLayout.CENTER);

        //JButton exportCsvButton = new JButton("Экспорт в CSV");
        //JButton exportPdfButton = new JButton("Экспорт в PDF");

        //exportCsvButton.addActionListener(e -> {
          //  try {
          //      SurveyAnalysis.exportResults(collectedAnswers, "CSV");
          //      JOptionPane.showMessageDialog(this, "Результаты успешно экспортированы в CSV.", "Экспорт успешен", JOptionPane.INFORMATION_MESSAGE);
         //   } catch (Exception ex) {
         //       JOptionPane.showMessageDialog(this, "Ошибка при экспорте в CSV: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
         //   }
        //});

       // exportPdfButton.addActionListener(e -> {
         //   try {
         //       SurveyAnalysis.exportResults(collectedAnswers, "PDF");
         //       JOptionPane.showMessageDialog(this, "Результаты успешно экспортированы в PDF.", "Экспорт успешен", JOptionPane.INFORMATION_MESSAGE);
         //   } catch (Exception ex) {
         //       JOptionPane.showMessageDialog(this, "Ошибка при экспорте в PDF: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
         //   }
       // });

       // JPanel buttonPanel = new JPanel();
       // buttonPanel.add(exportCsvButton);
       // buttonPanel.add(exportPdfButton);

       // add(buttonPanel, BorderLayout.SOUTH);
   // }
    }

    public static void main(String[] args) {
        new MainFrame();
    }
}
