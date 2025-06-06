package ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import controllers.UserController;
import models.Question;
import models.SurveyFactory;
import models.User;
import interface_for_panel.IUserPanel;
import controllers.SurveyController;
import models.Survey;
import services.QuestionLoader;

// Принцип инверсии зависимостей

public class LoginPanel extends JPanel implements IUserPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private UserController userController;
    private SurveyController surveyController;

    public LoginPanel(MainFrame mainFrame, UserController userController, SurveyController surveyController) {
        this.userController = userController;
        this.surveyController = surveyController;

        add(new JLabel("Username:"));
        usernameField = new JTextField(20);
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField(20);
        add(passwordField);

        JButton loginButton = new JButton("Login");
        add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();

                if (surveyController.getSurveys().isEmpty()) {
                    JOptionPane.showMessageDialog(LoginPanel.this,
                            "Нет доступных анкет.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return; // Выходит из метода, предотвращая дальнейшие действия.
                }

                User user = userController.login(username, password);
                if (user != null) {
                    showSurveySelectionDialog1(mainFrame, user);
                    Survey survey = surveyController.getSurveys().get(0); // Здесь теперь будет доступна первая анкета
                    mainFrame.setContentPane(new SurveyPanel(mainFrame, user, surveyController, survey));
                    mainFrame.revalidate();
                    mainFrame.repaint();
                } else {
                    JOptionPane.showMessageDialog(LoginPanel.this,
                            "Invalid credentials, please try again.", "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void showSurveySelectionDialog1(MainFrame mainFrame, User user){

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
            displaySurvey1(selectedSurvey, mainFrame, user);
        }
    }

    private void displaySurvey1(String surveyTitle, MainFrame mainFrame, User user){

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

