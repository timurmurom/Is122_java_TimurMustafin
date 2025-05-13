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


// Принцип инверсии зависимостей и паттерн итератор
public class SurveyPanel extends JPanel {
    private SurveyController surveyController;
    private int currentQuestionIndex = 0;
    private Survey survey;
    private User currentUser;
    private List<Answer> collectedAnswers = new ArrayList<>();
    private List<Question> questions;
    private List<String> answers;

    public SurveyPanel(MainFrame mainFrame, User user, SurveyController surveyController, Survey survey) {
        this.currentUser = user;
        this.surveyController = surveyController;
        this.survey = survey;
        setLayout(new BorderLayout());

        JLabel surveyTitle = new JLabel("Анкета" + survey.getTitle());
        add(surveyTitle,BorderLayout.NORTH);

        questions = new ArrayList<>();
        loadAndInitializeQuestions(questionFilePath);// для анкеты с названием IT-технологии из выпадающего списка анкет в файле класса RegisterPanel.java
        //loadAndInitializeQuestions2();// для анкеты с названием IT-технологии в современном мире из выпадающего списка анкет в файле класса RegisterPanel.java
       // loadAndInitializeQuestions3();// для анкеты с названием Победа в Великой Отечественной Войне из выпадающего списка анкет в файле класса RegisterPanel.java

        //readQuestionsFromFile("/home/studentlin/IdeaProjects/Mustafin_the_survey_example_2/questionsdevexample.txt");// для анкеты с названием IT-технологии из выпадающего списка анкет в файле класса RegisterPanel.java
        //readQuestionsFromFile2("/home/studentlin/IdeaProjects/Mustafin_the_survey_example_2/questionsdevexample2.txt"); // для анкеты с названием IT-технологии в современном мире из выпадающего списка анкет в файле класса RegisterPanel.java
       // readQuestionsFromFile3("/home/studentlin/IdeaProjects/Mustafin_the_survey_example_2/questionsdevexample3.txt"); // для анкеты с названием Победа в Великой Отечественной Войне в современном из выпадающего списка анкет в файле класса RegisterPanel.java

        //initializeQuestions();
        displayQuestion(currentQuestionIndex);
       // writeQuestionsToFile("questionsdevexample.txt");// для анкеты с названием IT-технологии из выпадающего списка анкет в файле класса RegisterPanel.java
       // writeQuestionsToFile2("questionsdevexample2.txt");// для анкеты с названием IT-технологии в современном мире из выпадающего списка анкет в файле класса RegisterPanel.java
      //  writeQuestionsToFile3("questionsdevexample3.txt");// для анкеты с названием Победа в Великой Отечественной Войне из выпадающего списка анкет в файле класса RegisterPanel.java


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
        // Добавляем кнопку завершения анкетирования
        //JButton finishButton = new JButton("Завершить анкету");
        //finishButton.addActionListener(new ActionListener() {
           // @Override
           // public void actionPerformed(ActionEvent e) {
                //endSurvey();
            //}
       // });
       // add(finishButton, BorderLayout.EAST);
    }

    //private void initializeQuestions(){
        //questions = survey.getQuestions();

        //questions.add(new ClosedQuestion(0, survey.getId(), "Ваша специальность:", List.of("Информационные технологии", "Радиоэлектроника", "Другие (укажите)")));
        //questions.add(new PartiallyOpenQuestion(1, survey.getId(), "Какой из следующих языков программирования вы изучали?", List.of("Java", "Python","C++", "SQL", "Другие (укажите):")));
        //questions.add(new ClosedQuestion(2, survey.getId(), "Какой у вас курс?", List.of("1 курс","2 курс","3 курс","4 курс")));
        //questions.add(new ClosedQuestion(3, survey.getId(), "Какую технологию вы считаете самой перспективной для своей карьеры?", List.of("Искусственный интеллект","Большие данные","Web-разработка","Кибербезопасность", "Мобильные приложения")));
       // questions.add(new PartiallyOpenQuestion(4, survey.getId(), "Какую дополнительную литературу по IT вы могли бы рекомендовать своим однокурсникам?", List.of("Программирование на C++ в примерах и задачах (Васильев А.Н.)", "Другие (укажите):")));
       // questions.add(new ClosedQuestion(5, survey.getId(), "Какой из форматов обучения вам удобнее всего?", List.of("Очное","Заочное","Смешанное")));
        //questions.add(new ClosedQuestion(6, survey.getId(), "Какова ваша степень удовлетворенности учебными материалами?", List.of("1","2","3","4","5")));
        //questions.add(new ClosedQuestion(7, survey.getId(), "Участвова ли вы в каких-либо курсах или мастер-классах?", List.of("Да","Нет")));
       // questions.add(new ClosedQuestion(8, survey.getId(), "Если да, то какие курсы вы посещали?(выберите несколько вариантов)", List.of("Программирование","Дизайн", "Управление проектами", "Другие(укажите):")));
       // questions.add(new ClosedQuestion(9, survey.getId(), "На каком ресурсе вы предпочитаете проходить онлайн-курсы?", List.of("База знаний Red OS","Coursera","GeekBrains","Другие(укажите):")));
       // questions.add(new ClosedQuestion(10, survey.getId(), "Как часто вы занимаетесь самостоятельным обучением?", List.of("Каждый день","Несколько раз в неделю","Реже")));
       // questions.add(new ClosedQuestion(11, survey.getId(), "Какое направление разработки вам интересно?", List.of("Backend","Frontend","DevOps","Data Science")));
        //readQuestionsFromFile("/home/studentlin/IdeaProjects/Mustafin_the_survey_example_2/question.txt");
    //}

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
        //List<JCheckBox> checkBoxes = new ArrayList<>();
        //for (String option1 : question.getOptions()) {
            //JCheckBox checkBox = new JCheckBox(option1);
            //checkBox.addActionListener(e -> {
                //if (checkBox.isSelected()){
                    //collectedAnswers.add(new Answer(0, question.getId(), currentUser.getId(), option1));
                //} else {
                   // collectedAnswers.removeIf(answer -> answer.getAnswerText().equals(option1) && answer.getQuestionId() == question.getId());
               // }
           // });
       // }
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

    //private void loadAndInitializeQuestions2(){
       // if (questions.isEmpty()){
          //  readQuestionsFromFile("/home/studentlin/IdeaProjects/Mustafin_the_survey_example_2/questionsdevexample2.txt");
       // }
    //}

    //private void loadAndInitializeQuestions3(){
    //    if (questions.isEmpty()){
      //      readQuestionsFromFile("/home/studentlin/IdeaProjects/Mustafin_the_survey_example_2/questionsdevexample3.txt");
      //  }
  //  }

    private void readQuestionsFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readQuestionsFromFile2(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   // private void readQuestionsFromFile3(String filename) {
       // try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
          //  String line;
          //  while ((line = reader.readLine()) != null) {
                // Парсим строки для добавления вопросов
             //   String[] parts = line.split(";");

              //  if (parts.length < 2) {
                    // Проверяем есть ли хотя бы два элемента
               //     System.err.println("Неверный формат строки: " + line);
               //     continue; // Пропускаем эту итерацию и продолжаем со следующей строки
               // }
              //  String questionType = parts[0].trim(); // Тип вопроса: Закрытый, Полуоткрытый или Открытый
              //  String questionText = parts[1].trim(); // Текст вопроса
              //  List<String> options = new ArrayList<>();

                // Если вопрос закрытого или полуоткрытого типа, добавляем варианты ответов
             //   if (!questionType.equals("OPEN_ENDED") && parts.length > 2) {
                  //  for (int i = 2; i < parts.length; i++) {
                   //     options.add(parts[i].trim());
                   // }
              //  }

                // Создаем объект вопроса в зависимости от его типа
              //  Question question;
              //  switch (questionType) {
                 //   case "CLOSED":
                    //    question = new ClosedQuestion(questions.size(), survey.getId(), questionText, options);
                   //     break;
                  //  case "PARTIALLY_OPEN":
                     //   question = new PartiallyOpenQuestion(questions.size(), survey.getId(), questionText, options);
                    //    break;
              //      case "OPEN_ENDED":
                 //       question = new OpenEndedQuestion(questions.size(), survey.getId(), questionText);
                 //       break;
                  //  default:
                   //     throw new IllegalArgumentException("Unknown question type: " + questionType);
             //   }

                // Добавляем вопрос в список вопросов опроса
               // questions.add(question);
           // }
      //  } catch (IOException e) {
        //    e.printStackTrace();
        //}
   // }

    private void writeQuestionsToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Question question : questions) {
                StringBuilder line = new StringBuilder();

                // Записываем тип вопроса, текст и варианты ответов
                line.append(question.getQuestionType().toString()).append(";"); // Тип вопроса
                line.append(question.getText()).append(";");

                if (question instanceof ClosedQuestion || question instanceof PartiallyOpenQuestion) {
                    for (String option : question.getOptions()) {
                        line.append(option).append(";"); // Варианты ответов
                    }
                }

                writer.write(line.toString());
                writer.newLine(); // Переход на новую строку
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeQuestionsToFile2(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Question question : questions) {
                StringBuilder line = new StringBuilder();

                // Записываем тип вопроса, текст и варианты ответов
                line.append(question.getQuestionType().toString()).append(";"); // Тип вопроса
                line.append(question.getText()).append(";");

                if (question instanceof ClosedQuestion || question instanceof PartiallyOpenQuestion) {
                    for (String option : question.getOptions()) {
                        line.append(option).append(";"); // Варианты ответов
                    }
                }

                writer.write(line.toString());
                writer.newLine(); // Переход на новую строку
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //private void writeQuestionsToFile3(String filename) {
       // try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
          //  for (Question question : questions) {
              //  StringBuilder line = new StringBuilder();

                // Записываем тип вопроса, текст и варианты ответов
              //  line.append(question.getQuestionType().toString()).append(";"); // Тип вопроса
               // line.append(question.getText()).append(";");

               // if (question instanceof ClosedQuestion || question instanceof PartiallyOpenQuestion) {
              //      for (String option : question.getOptions()) {
                  //      line.append(option).append(";"); // Варианты ответов
                 //   }
               // }

              //  writer.write(line.toString());
               // writer.newLine(); // Переход на новую строку
           // }
       // } catch (IOException e) {
           // e.printStackTrace();
      //  }
  //  }


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
