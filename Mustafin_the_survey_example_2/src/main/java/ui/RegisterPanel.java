package ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import controllers.UserController;
import controllers.SurveyController;
import interface_for_panel.IUserPanel;
import models.Question;
import models.Survey;
import models.SurveyFactory;
import models.User;
import services.QuestionLoader;

// Принцип инверсии зависимостей

public class RegisterPanel extends JPanel implements IUserPanel {
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private UserController userController;
    private SurveyController surveyController;
    private User currentUser;

    public RegisterPanel(MainFrame mainFrame, SurveyController surveyController, UserController userController) {
        this.userController = userController;
        this.surveyController = surveyController;


        // Создаем текстовое поле для ввода имени пользователя
        add(new JLabel("Username:"));
        usernameField = new JTextField(20);
        add(usernameField);

        // Создаем текстовое поле для ввода электронной почты
        add(new JLabel("Email:"));
        emailField = new JTextField(20);
        add(emailField);

        // Создаем поле для ввода пароля
        add(new JLabel("Password:"));
        passwordField = new JPasswordField(20);
        add(passwordField);

        // Создаем кнопку "Register"
        JButton registerButton = new JButton("Register");
        add(registerButton);

        // Добавляем слушатель событий для кнопки
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                //ВАЖНО: используйте try-catch для обработки исключений
                try {
                    //Регистрация с использованием метода из UserController
                    userController.register(username, email, password);
                    //Устанавливаем значения логина и пароля в соответствующие поля UI
                    JOptionPane.showMessageDialog(RegisterPanel.this, "Регистрация прошла успешно!");

                    User registeredUser = userController.getCurrentUser();

                    //Переходим к опросу
                    Survey survey = surveyController.getSurveys().get(0);
                    mainFrame.setContentPane(new SurveyPanel(mainFrame, new User(0,username,email, password), surveyController, survey));
                    mainFrame.revalidate();
                    mainFrame.repaint();
                 showSurveySelectionDialog(mainFrame, registeredUser);// Выбор анкеты после успешной регистрации
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(RegisterPanel.this, "Ошибка регистрации: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(RegisterPanel.this, "Произошла ошибка: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Кнопка для администратора
        JButton adminButton = new JButton("Я администратор");
        add(adminButton);
        adminButton.addActionListener(e -> {
            String password = JOptionPane.showInputDialog(this, "Введите пароль администратора:", "Пароль администратора", JOptionPane.QUESTION_MESSAGE);
            if ("admin123".equals(password)) {
                // Открываем AdminPanel
                AdminPanel adminPanel = new AdminPanel(userController);
                mainFrame.setContentPane(adminPanel);
                mainFrame.revalidate();
            } else {
                JOptionPane.showMessageDialog(this, "Неверный пароль администратора", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void showSurveySelectionDialog(MainFrame mainFrame, User user){

        String[] surveyTitles = SurveyFactory.getSurveyMap().keySet().toArray(new String[0]);
        //String [] surveyTitles = {"IT-технологии", "Победа в Великой Отечественной Войне", "IT-технологии в современном мире"};
        JComboBox<String> surveyDropdown = new JComboBox<>(surveyTitles);
        JLabel descriptionLabel = new JLabel("Выберите анкету:");

        JPanel panel = new JPanel();
        panel.add(descriptionLabel);
        panel.add(surveyDropdown);

        int result = JOptionPane.showConfirmDialog(mainFrame, panel, "Выбор анкеты", JOptionPane.OK_CANCEL_OPTION);
        if(result == JOptionPane.OK_OPTION){
            String selectedSurvey = (String) surveyDropdown.getSelectedItem();
            displaySurvey(selectedSurvey, mainFrame, user);
        }
    }

    private void displaySurvey(String surveyTitle, MainFrame mainFrame, User user){

       try {
           Survey survey = SurveyFactory.createSurvey(surveyTitle);
           QuestionLoader loader = new QuestionLoader();
           List<Question> questions = loader.loadQuestions(survey);
           SurveyPanel surveyPanel = new SurveyPanel(mainFrame, user, surveyController, survey);
           surveyPanel.setQuestions(questions);
           mainFrame.setContentPane(surveyPanel);
           mainFrame.revalidate();
       } catch (IOException e){
           JOptionPane.showMessageDialog(this, "Ошибка загрузки вопросов: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
       }

    }

    @Override
    public void display() {
        setVisible(true);
    }
}
