package services;

import models.User;
import org.mindrot.jbcrypt.BCrypt;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private List<User> users = new ArrayList<>();
    private String userFilePath = "users.txt"; // Файл будет в корне проекта

    public UserService() {
        loadUsersFromFile();
    }

    private void loadUsersFromFile() {
        File file = new File(userFilePath);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(userFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 4) {
                    int id = Integer.parseInt(parts[0]);
                    String username = parts[1];
                    String email = parts[2];
                    String password = parts[3];
                    users.add(new User(id, username, email, password));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveUsersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userFilePath))) {
            for (User user : users) {
                writer.write(String.format("%d;%s;%s;%s\n",
                        user.getId(), user.getUsername(), user.getEmail(), user.getPassword()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean emailExists(String email) {
        return users.stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(email.trim()));
    }

    public void registerUser(String username, String email, String password) {
        if (emailExists(email)) {
            throw new IllegalArgumentException("Email уже зарегистрирован");
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        int newId = users.isEmpty() ? 1 : users.get(users.size()-1).getId() + 1;
        User user = new User(newId, username, email, hashedPassword);
        users.add(user);
        saveUsersToFile();
    }

    public User loginUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && BCrypt.checkpw(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public void updateUser(User updatedUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == updatedUser.getId()) {
                users.set(i, updatedUser);
                saveUsersToFile();
                return;
            }
        }
        throw new IllegalArgumentException("Пользователь с ID " + updatedUser.getId() + " не найден");
    }

    public void deleteUser(int userId) {
        if (users.removeIf(user -> user.getId() == userId)) {
            saveUsersToFile();
        } else {
            throw new IllegalArgumentException("Пользователь с ID " + userId + " не найден");
        }
    }
}