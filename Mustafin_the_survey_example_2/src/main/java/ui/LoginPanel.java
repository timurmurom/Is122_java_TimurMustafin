package ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import controllers.UserController;
import models.User;
import interface_for_panel.IUserPanel;
import controllers.SurveyController;
import models.Survey;

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

    @Override
    public void display() {
        setVisible(true);
    }
}

