package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.io.*; // Включает необходимый класс для файлового ввода/вывода
import models.*;
import controllers.SurveyController;
import services.AnswerService;
import services.QuestionLoader;


// Принцип инверсии зависимостей и паттерн итератор
public class SurveyPanel extends JPanel {
    private SurveyController surveyController;
    private int currentQuestionIndex = 0;
    private Survey survey;
    private User currentUser;
    private List<Answer> collectedAnswers = new ArrayList<>();
    private List<Question> questions;
    private List<String> answers;
    private String questionFilePath;

    public SurveyPanel(MainFrame mainFrame, User user, SurveyController surveyController, Survey survey) {
        this.currentUser = user;
        this.surveyController = surveyController;
        this.survey = survey;
        this.questionFilePath = survey.getQuestionFilePath();
        this.questions = new ArrayList<>();

        try {
            QuestionLoader loader = new QuestionLoader();
            this.questions = loader.loadQuestions(survey);
            displayQuestion(0);
        }catch (IOException e){
            JOptionPane.showMessageDialog(this, "Ошибка загрузки вопросов: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            this.questions = new ArrayList<>();
        }

        loadAndInitializeQuestions(questionFilePath);

        setLayout(new BorderLayout());
        JLabel surveyTitle = new JLabel("Анкета" + survey.getTitle());
        add(surveyTitle,BorderLayout.NORTH);



        this.setLayout(new BorderLayout());

        answers = new ArrayList<>();

        JButton nextButton = new JButton("Далее");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleNextQuestion();
            }
        });
        add(nextButton, BorderLayout.SOUTH);
        // Кнопка "Завершить анкету"
        JButton finishButton = new JButton("Завершить");
        finishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                endSurvey();
            }
        });
        add(finishButton, BorderLayout.SOUTH);

    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
        currentQuestionIndex = 0;
        displayQuestion(0);
    }

    private void displayQuestion(int index) {
        removeAll();


        if (index < questions.size()) {


            Question currentQuestion = questions.get(index);
            JLabel questionLabel = new JLabel(currentQuestion.getText());
            add(questionLabel, BorderLayout.NORTH);

            // Создаем панель для вариантов ответа
            JPanel optionsPanel = new JPanel();
            optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));


            if (currentQuestion instanceof ClosedQuestion) {
                setupClosedQuestion((ClosedQuestion) currentQuestion, optionsPanel);
            } else if (currentQuestion instanceof PartiallyOpenQuestion) {
                setupPartiallyOpenQuestion((PartiallyOpenQuestion) currentQuestion, optionsPanel);
            } else if (currentQuestion instanceof OpenEndedQuestion) {
                setupOpenEndedQuestion(optionsPanel);
                revalidate(); // Обновление панели
                repaint(); // Перерисовка интерфейса
            }

            add(optionsPanel, BorderLayout.CENTER);

            JButton nextButton = new JButton("Далее");
            nextButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    currentQuestionIndex++;
                    displayQuestion(currentQuestionIndex);
                }
            });
            add(nextButton, BorderLayout.SOUTH);
            revalidate();
            repaint();
        } else {
            endSurvey();
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

    private void setupOpenEndedQuestion(JPanel optionsPanel) {
        JTextField answerField = new JTextField(20);
        answerField.addActionListener(e -> {
            collectedAnswers.add(new Answer(0, -1, currentUser.getId(), answerField.getText())); // -1, т.к. нет ID вопроса
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


    private void saveAnswers(List<Answer> answers) {
        AnswerService answerService = new AnswerService();
        for (Answer answer : answers) {
            answerService.saveAnswer(answer);
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
        JLabel endLabel = new JLabel("Спасибо за участие!");
        add(endLabel, BorderLayout.CENTER);
        saveAnswers(collectedAnswers);

        // Здесь будет возможность показать результаты и отправить их на экспорт
        //ResultPanel resultPanel = new ResultPanel(collectedAnswers);
       // JFrame resultsFrame = new JFrame("Результаты анкеты");
      //  resultsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      //  resultsFrame.setSize(600, 400);
      //  resultsFrame.getContentPane().add(resultPanel);
      //  resultsFrame.setVisible(true);

        // Добавляем кнопку "Вернуться в главное меню"
      //  JButton backButton = new JButton("Вернуться в главное меню");
     //   backButton.addActionListener(new ActionListener() {
     //       @Override
     //       public void actionPerformed(ActionEvent e) {
     //           ((JFrame) SwingUtilities.getWindowAncestor(SurveyPanel.this)).dispose(); // Закрываем текущее окно
      //          MainFrame mainFrame = new MainFrame(); // Сначала создаём новое главное окно
      //          mainFrame.setVisible(true); // Показываем главное окно
       //     }
       // });

       // JPanel buttonPanel = new JPanel();
       // buttonPanel.add(backButton);
      //  add(buttonPanel, BorderLayout.SOUTH); // Добавляем кнопку на панель

      //  revalidate();
      //  repaint();
    }
}
