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
import ui.MainFrame;
import services.SurveyAnalysis;

public class ResultPanel extends JPanel {
    private final List<Answer> collectedAnswers;
    private final MainFrame mainFrame;

    public ResultPanel(MainFrame mainFrame, List<Answer> collectedAnswers) {
        this.mainFrame = mainFrame;
        this.collectedAnswers = collectedAnswers;
        initializeUi();
    }

    private void initializeUi() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Заголовок
        JLabel titleLabel = new JLabel("Результаты анкетирования");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        // Панель с результатами
        JTextArea resultsArea = new JTextArea();
        resultsArea.setEditable(false);
        resultsArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        StringBuilder results = new StringBuilder("Ваши ответы:\n\n");
        for (int i = 0; i < collectedAnswers.size(); i++) {
            Answer answer = collectedAnswers.get(i);
            results.append("Вопрос ").append(i + 1).append(":\n")
                    .append(answer.getAnswerText()).append("\n\n");
        }

        resultsArea.setText(results.toString());
        JScrollPane scrollPane = new JScrollPane(resultsArea);
        add(scrollPane, BorderLayout.CENTER);

        // Панель кнопок
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JButton exportCsvButton = createStyledButton("Экспорт в CSV", Color.BLUE);
        JButton exportPdfButton = createStyledButton("Экспорт в PDF", Color.GREEN);
        JButton mainMenuButton = createStyledButton("Главное меню", Color.ORANGE);

        exportCsvButton.addActionListener(e -> exportResults("CSV"));
        exportPdfButton.addActionListener(e -> exportResults("PDF"));
        mainMenuButton.addActionListener(e -> returnToMainMenu());

        buttonPanel.add(exportCsvButton);
        buttonPanel.add(exportPdfButton);
        buttonPanel.add(mainMenuButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(180, 40));
        return button;
    }

    private void exportResults(String format) {
        try {
            SurveyAnalysis.exportResults(collectedAnswers, format);
            JOptionPane.showMessageDialog(
                    this,
                    "Результаты успешно экспортированы в формат " + format,
                    "Экспорт завершен",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ошибка при экспорте: " + ex.getMessage(),
                    "Ошибка экспорта",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void returnToMainMenu() {
        mainFrame.showMainMenu();
    }
}
