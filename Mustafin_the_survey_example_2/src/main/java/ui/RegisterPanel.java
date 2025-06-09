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
import java.util.Arrays;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.BorderLayout;
import models.User;

// Принцип инверсии зависимостей

public class RegisterPanel extends JPanel implements IUserPanel {
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private UserController userController;
    private SurveyController surveyController;
    private User currentUser;
    private JButton manageUsersButton = new JButton("Управление пользователями");

    public RegisterPanel(MainFrame mainFrame, SurveyController surveyController, UserController userController) {
        this.userController = userController;
        this.surveyController = surveyController;


        add(manageUsersButton);

        manageUsersButton.addActionListener(e -> showUserManagementDialog(userController));

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

    private void showUserManagementDialog(UserController userController) {
        JDialog managementDialog = new JDialog();
        managementDialog.setTitle("Управление пользователями");
        managementDialog.setSize(500, 400);
        managementDialog.setLayout(new BorderLayout());

        // Таблица пользователей
        String[] columns = {"ID", "Логин", "Email"};
        List<User> users = userController.getAllUsers();
        Object[][] data = new Object[users.size()][3];

        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            data[i] = new Object[]{u.getId(), u.getUsername(), u.getEmail()};
        }

        JTable userTable = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(userTable);
        managementDialog.add(scrollPane, BorderLayout.CENTER);

        // Панель действий
        JPanel actionPanel = new JPanel();
        JButton editButton = new JButton("Редактировать");
        JButton deleteButton = new JButton("Удалить");

        actionPanel.add(editButton);
        actionPanel.add(deleteButton);
        managementDialog.add(actionPanel, BorderLayout.SOUTH);

        // Обработчики событий
        editButton.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow >= 0) {
                int userId = (Integer) userTable.getValueAt(selectedRow, 0);
                showEditUserDialog(userId, userController, managementDialog);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow >= 0) {
                int userId = (Integer) userTable.getValueAt(selectedRow, 0);
                userController.deleteUser(userId);
                managementDialog.dispose();
                showUserManagementDialog(userController);
            }
        });

        managementDialog.setVisible(true);
    }

    private void showEditUserDialog(int userId, UserController userController, JDialog parent) {
        JDialog editDialog = new JDialog(parent, "Редактирование пользователя", true);
        editDialog.setSize(400, 250);
        editDialog.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        User user = userController.getUserById(userId);

        // Метка и поле для логина
        gbc.gridx = 0;
        gbc.gridy = 0;
        editDialog.add(new JLabel("Логин:"), gbc);

        gbc.gridx = 1;
        JTextField usernameField = new JTextField(user.getUsername(), 20);
        editDialog.add(usernameField, gbc);

        // Метка и поле для email
        gbc.gridy = 1;
        gbc.gridx = 0;
        editDialog.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        JTextField emailField = new JTextField(user.getEmail(), 20);
        editDialog.add(emailField, gbc);

        // Метка и поле для нового пароля
        gbc.gridy = 2;
        gbc.gridx = 0;
        editDialog.add(new JLabel("Новый пароль:"), gbc);

        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(20);
        editDialog.add(passwordField, gbc);

        // Метка и поле для подтверждения пароля
        gbc.gridy = 3;
        gbc.gridx = 0;
        editDialog.add(new JLabel("Подтвердите пароль:"), gbc);

        gbc.gridx = 1;
        JPasswordField confirmPasswordField = new JPasswordField(20);
        editDialog.add(confirmPasswordField, gbc);

        // Кнопка сохранения
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton saveButton = new JButton("Сохранить");
        editDialog.add(saveButton, gbc);

        saveButton.addActionListener(e -> {
            char[] passwordChars = passwordField.getPassword();
            char[] confirmChars = confirmPasswordField.getPassword();

            // Проверка совпадения паролей
            if (!Arrays.equals(passwordChars, confirmChars)) {
                JOptionPane.showMessageDialog(editDialog,
                        "Пароли не совпадают!",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String updatedUsername = usernameField.getText();
            String updatedEmail = emailField.getText();

            // Сохраняем изменения
            userController.updateUser(user.getId(), updatedUsername, updatedEmail);

            // Если указан новый пароль, обновляем его отдельно
            if (passwordChars.length > 0) {
                String newPassword = new String(passwordChars);
                user.setPassword(newPassword); // Здесь также надо вызвать service.saveUsersToFile()

                // Очистка чувствительных данных
                Arrays.fill(passwordChars, ' ');
                Arrays.fill(confirmChars, ' ');
            }

            editDialog.dispose();
            JOptionPane.showMessageDialog(parent,
                    "Изменения сохранены",
                    "Успех",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        editDialog.setLocationRelativeTo(parent);
        editDialog.setVisible(true);
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







