package ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*; // Включает необходимый класс для файлового ввода/вывода
import models.*;
import controllers.SurveyController;
import services.QuestionLoader;
import java.io.IOException;
import java.util.Date;


// Принцип инверсии зависимостей и паттерн итератор
public class SurveyPanel extends JPanel {
    private SurveyController surveyController;
    private int currentQuestionIndex = 0;
    private Survey survey;
    private User currentUser;
    private List<Answer> collectedAnswers = new ArrayList<>();
    private List<Question> questions;
    private List<String> answers;
    private List<Component> currentQuestionOptions;
    private String questionFilePath;
    private MainFrame mainFrame;
    private JButton nextOrFinishButton;
    private JPanel currentQuestionPanel;
    private final QuestionLoader questionLoader = new QuestionLoader();

    public SurveyPanel(MainFrame mainFrame, User user, SurveyController surveyController, Survey survey) {
        this.mainFrame = mainFrame;
        this.currentUser = user;
        this.surveyController = surveyController;
        this.survey = survey;
        this.questionFilePath = survey.getQuestionFilePath();
        this.questions = surveyController.loadSurveyQuestion(survey);
        loadQuestionsForSurvey(survey);
        displayQuestion(currentQuestionIndex);

        if (questions.isEmpty()){
            JOptionPane.showMessageDialog(this, "Анкета не содержит вопросов", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }

        try {
            QuestionLoader loader = new QuestionLoader();
            this.questions = loader.loadQuestions(survey);
            currentQuestionIndex = 0;
            displayQuestion(currentQuestionIndex);
        }catch (IOException e){
            JOptionPane.showMessageDialog(this, "Ошибка загрузки вопросов: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            this.questions = new ArrayList<>();
        }


        setLayout(new BorderLayout());
        JLabel surveyTitle = new JLabel("Анкета" + survey.getTitle());
        add(surveyTitle,BorderLayout.NORTH);



        this.setLayout(new BorderLayout());

        answers = new ArrayList<>();


    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
        currentQuestionIndex = 0;
        displayQuestion(0);
    }


    private void loadQuestionsForSurvey(Survey survey) {
        try {
            this.questions = questionLoader.loadQuestions(survey);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Ошибка загрузки вопросов: " + e.getMessage());
            this.questions = new ArrayList<>();
        }
    }

    private void displayQuestion(int index) {
        removeAll();
        if (currentQuestionPanel != null) {
            remove(currentQuestionPanel);
        }


        if (index < questions.size()) {
            currentQuestionPanel = new JPanel();
            currentQuestionPanel.setLayout(new BorderLayout());

            Question currentQuestion = questions.get(index);
            JLabel questionLabel = new JLabel("<html><div style='width:300px;'>" + currentQuestion.getText() + "</div></html>");
            currentQuestionPanel.add(questionLabel, BorderLayout.NORTH);

            JPanel optionsPanel = new JPanel();
            optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
            currentQuestionOptions = new ArrayList<>(); // Сбрасываем варианты ответов

            // Настройка вариантов ответа
            if (currentQuestion instanceof ClosedQuestion) {
                setupClosedQuestion((ClosedQuestion) currentQuestion, optionsPanel);
            } else if (currentQuestion instanceof PartiallyOpenQuestion) {
                setupPartiallyOpenQuestion((PartiallyOpenQuestion) currentQuestion, optionsPanel);
            } else {
                setupOpenEndedQuestion(currentQuestion, optionsPanel);
            }

            currentQuestionPanel.add(optionsPanel, BorderLayout.CENTER);

            // Кнопка навигации
            JButton navigationButton = new JButton(index == questions.size() - 1 ? "Завершить анкету" : "Следующий вопрос");
            navigationButton.addActionListener(e -> handleNavigation(index));

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(navigationButton);
            currentQuestionPanel.add(buttonPanel, BorderLayout.SOUTH);

            add(currentQuestionPanel, BorderLayout.CENTER);
            revalidate();
            repaint();
        } else {
            endSurvey();
        }
    }

    private void handleNavigation(int currentIndex) {
        // Сохраняем ответ для текущего вопроса
        saveCurrentAnswer(currentIndex);

        if (currentIndex < questions.size() - 1) {
            displayQuestion(currentIndex + 1);
        } else {
            endSurvey();
        }
    }

    private void saveCurrentAnswer(int questionIndex) {
        Question question = questions.get(questionIndex);
        String answerText = "";

        if (question instanceof ClosedQuestion || question instanceof PartiallyOpenQuestion) {
            for (Component comp : currentQuestionOptions) {
                if (comp instanceof JRadioButton && ((JRadioButton) comp).isSelected()) {
                    answerText = ((JRadioButton) comp).getText();
                    break;
                }
            }
        } else {
            for (Component comp : currentQuestionOptions) {
                if (comp instanceof JTextField) {
                    answerText = ((JTextField) comp).getText();
                    break;
                }
            }
        }

        if (!answerText.isEmpty()) {
            collectedAnswers.add(new Answer(0, question.getId(), currentUser.getId(), answerText));
        }
    }


    private void setupClosedQuestion(ClosedQuestion question, JPanel optionsPanel) {
        ButtonGroup group = new ButtonGroup();
        for (String option : question.getOptions()) {
            JRadioButton radioButton = new JRadioButton(option);
            radioButton.addActionListener(e -> {
                collectedAnswers.add(new Answer(0, question.getId(), currentUser.getId(), option));
            });
            optionsPanel.add(radioButton);
            group.add(radioButton); // Группируем радиокнопки
        }
    }



    private void setupPartiallyOpenQuestion(PartiallyOpenQuestion question, JPanel optionsPanel) {
        ButtonGroup group = new ButtonGroup();
        for (String option : question.getOptions()) {
            JRadioButton radioButton = new JRadioButton(option);
            radioButton.addActionListener(e -> {
                collectedAnswers.add(new Answer(0, question.getId(), currentUser.getId(), option));
            });
            optionsPanel.add(radioButton);
            group.add(radioButton); // Группируем радиокнопки
        }
        JTextField otherField = new JTextField(20);
        otherField.addActionListener(e -> {
            collectedAnswers.add(new Answer(0, question.getId(), currentUser.getId(), otherField.getText()));
        });
        optionsPanel.add(otherField); // Добавляем поле для других вариантов
    }

    private void setupOpenEndedQuestion(Question question, JPanel optionsPanel) {
        JTextField answerField = new JTextField(20);
        answerField.addActionListener(e -> {
            collectedAnswers.add(new Answer(0, question.getId(), currentUser.getId(), answerField.getText())); // -1, т.к. нет ID вопроса
        });
        optionsPanel.add(answerField);
    }

    private void handleNextQuestion() {
        if (currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
            displayQuestion(currentQuestionIndex); // Показать следующий вопрос
        } else {
            endSurvey();
        }
    }


    private void loadAndInitializeQuestions(String filename){
        questions.clear();
        if (questions.isEmpty()){
            readQuestionsFromFile(filename);
        }
    }

    private void readQuestionsFromFile(String filename) {
        try (InputStream is = getClass().getResourceAsStream(filename);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Парсим строки для добавления вопросов
                String[] parts = line.split(";");

                if (parts.length < 2) {
                    // Проверяем есть ли хотя бы два элемента
                    System.err.println("Неверный формат строки: " + line);
                    continue; // Пропускаем эту итерацию и продолжаем со следующей строки
                }
                String questionType = parts[0].trim(); // Тип вопроса: Закрытый, Полуоткрытый или Открытый
                String questionText = parts[1].trim(); // Текст вопроса
                List<String> options = new ArrayList<>();

                // Если вопрос закрытого или полуоткрытого типа, добавляем варианты ответов
                if (!questionType.equals("OPEN_ENDED") && parts.length > 2) {
                    for (int i = 2; i < parts.length; i++) {
                        options.add(parts[i].trim());
                    }
                }

                // Создаем объект вопроса в зависимости от его типа
                Question question;
                switch (questionType) {
                    case "CLOSED":
                        question = new ClosedQuestion(questions.size(), survey.getId(), questionText, options);
                        break;
                    case "PARTIALLY_OPEN":
                        question = new PartiallyOpenQuestion(questions.size(), survey.getId(), questionText, options);
                        break;
                    case "OPEN_ENDED":
                        question = new OpenEndedQuestion(questions.size(), survey.getId(), questionText);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown question type: " + questionType);
                }

                // Добавляем вопрос в список вопросов опроса
                questions.add(question);
            }
        } catch (IOException | NullPointerException e) {
            JOptionPane.showMessageDialog(this, "Файл вопросов не найден: " + filename, "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void endSurvey() {
        // Сохраняем результаты в файл
        saveResultsToFile(collectedAnswers);

        // Показываем результаты пользователю
        ResultPanel resultPanel = new ResultPanel(mainFrame, collectedAnswers);
        mainFrame.setContentPane(resultPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private void saveResultsToFile(List<Answer> answers) {
        // Создаем директорию для отчетов, если ее нет
        File reportsDir = new File("reports");
        if (!reportsDir.exists()) {
            reportsDir.mkdirs();
        }

        // Формируем имя файла на основе пользователя и анкеты
        String fileName = String.format("%s_%s_%d.txt",
                currentUser.getUsername(),
                survey.getTitle(),
                System.currentTimeMillis());

        File reportFile = new File(reportsDir, fileName);

        try (PrintWriter writer = new PrintWriter(reportFile, "UTF-8")) {
            // Заголовок отчета
            writer.println("Отчёт о прохождении анкеты");
            writer.println("Анкета: " + survey.getTitle());
            writer.println("Пользователь: " + currentUser.getUsername());
            writer.println("Дата: " + new Date());
            writer.println("----------------------------------");

            // Собираем вопросы и ответы
            int questionNumber = 1;
            for (Answer answer : answers) {
                // Находим вопрос по ID
                for (Question question : survey.getQuestions()) {
                    if (question.getId() == answer.getQuestionId()) {
                        writer.println("Вопрос " + questionNumber + ": " + question.getText());
                        writer.println("Ответ: " + answer.getAnswerText());
                        writer.println("----------------------------------");
                        questionNumber++;
                        break;
                    }
                }
            }

            writer.println("Опрос завершен. Спасибо за участие!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Ошибка сохранения отчета: " + e.getMessage(),
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
