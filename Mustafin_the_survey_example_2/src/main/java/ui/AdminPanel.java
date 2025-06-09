package ui;

import controllers.UserController;
import models.User;
import org.mindrot.jbcrypt.BCrypt;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import controllers.UserController;
import models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminPanel extends JPanel {
    private JTable userTable;
    private DefaultTableModel tableModel;
    private UserController userController;

    public AdminPanel(UserController userController) {
        this.userController = userController;
        setLayout(new BorderLayout());

        // Создаем таблицу
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Имя");
        tableModel.addColumn("Email");

        userTable = new JTable(tableModel);
        add(new JScrollPane(userTable), BorderLayout.CENTER);

        // Кнопки управления
        JPanel buttonPanel = new JPanel();
        JButton deleteButton = new JButton("Удалить");
        JButton editButton = new JButton("Редактировать");

        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Загружаем данные
        loadUsers();

        // Обработчики событий
        deleteButton.addActionListener(e -> deleteSelectedUser());
        editButton.addActionListener(e -> editSelectedUser());
    }

    private void loadUsers() {
        tableModel.setRowCount(0); // Очищаем таблицу
        List<User> users = userController.getAllUsers();
        for (User user : users) {
            tableModel.addRow(new Object[]{user.getId(), user.getUsername(), user.getEmail()});
        }
    }

    private void deleteSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow >= 0) {
            int userId = (Integer) tableModel.getValueAt(selectedRow, 0);
            userController.deleteUser(userId);
            loadUsers(); // Обновляем таблицу
        } else {
            JOptionPane.showMessageDialog(this, "Выберите пользователя для удаления", "Ошибка", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void editSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow >= 0) {
            int userId = (Integer) tableModel.getValueAt(selectedRow, 0);
            User user = userController.getAllUsers().stream()
                    .filter(u -> u.getId() == userId)
                    .findFirst()
                    .orElse(null);
            if (user != null) {
                new EditUserDialog(user, this.userController).setVisible(true);
                loadUsers(); // Обновляем таблицу
            }
        } else {
            JOptionPane.showMessageDialog(this, "Выберите пользователя для редактирования", "Ошибка", JOptionPane.WARNING_MESSAGE);
        }
    }
}




