package services;

import models.User;
import org.mindrot.jbcrypt.BCrypt;
import java.util.Optional;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private List<User> users = new ArrayList<>();
    private final String USERS_FILE = "users.txt";


    public UserService() {
        loadUsersFromFile();
    }

    public User getUserById(int userId) {
        return users.stream()
                .filter(u -> u.getId() == userId)
                .findFirst()
                .orElse(null);
    }

    public void updateUser(User updatedUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == updatedUser.getId()) {
                users.set(i, updatedUser);
                saveUsersToFile(); // сохраняем изменения в файл
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

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    private void loadUsersFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length == 4) {
                    int id = Integer.parseInt(data[0]);
                    String username = data[1];
                    String email = data[2];
                    String passwordHash = data[3]; // Пароль уже хеширован

                    // Прямое добавление пользователя
                    users.add(new User(id, username, email, passwordHash));
                }
            }
        } catch (IOException e) {
            System.out.println("Файл пользователей не найден");
        }
    }

    private void saveUsersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE))) {
            for (User user : users) {
                writer.write(String.format("%d;%s;%s;%s\n",
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getPassword()));
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
        saveUsersToFile(); // Сохраняем изменения
    }

    public User loginUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && BCrypt.checkpw(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }

}