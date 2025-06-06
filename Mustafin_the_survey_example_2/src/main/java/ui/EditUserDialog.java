package ui;

import controllers.UserController;
import models.User;
import org.mindrot.jbcrypt.BCrypt;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditUserDialog extends JDialog {
    private User user;
    private UserController userController;
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;

    public EditUserDialog(User user, UserController userController) {
        this.user = user;
        this.userController = userController;
        setTitle("Редактирование пользователя");
        setSize(300, 200);
        setModal(true);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2));

        panel.add(new JLabel("Имя:"));
        usernameField = new JTextField(user.getUsername());
        panel.add(usernameField);

        panel.add(new JLabel("Email:"));
        emailField = new JTextField(user.getEmail());
        panel.add(emailField);

        panel.add(new JLabel("Новый пароль:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton saveButton = new JButton("Сохранить");
        saveButton.addActionListener(e -> saveUser());
        panel.add(saveButton);

        add(panel);
    }

    private void saveUser() {
        String newUsername = usernameField.getText().trim();
        String newEmail = emailField.getText().trim();
        String newPassword = new String(passwordField.getPassword()).trim();

        // Если пароль не введен, оставляем старый
        if (newPassword.isEmpty()) {
            user = new User(user.getId(), newUsername, newEmail, user.getPassword());
        } else {
            // Хешируем новый пароль
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            user = new User(user.getId(), newUsername, newEmail, hashedPassword);
        }

        try {
            userController.updateUser(user);
            JOptionPane.showMessageDialog(this, "Пользователь обновлен", "Успех", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ошибка: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
}
