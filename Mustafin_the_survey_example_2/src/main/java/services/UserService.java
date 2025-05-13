package services;

import org.mindrot.jbcrypt.BCrypt;
import models.User;
import java.util.ArrayList;
import java.util.List;

// Принцип инверсии зависимостей
public class UserService {
    private List<User> users = new ArrayList<>();

    public void registerUser(String username, String email, String password) {
        if (emailExists(email)) { // Метод для проверки существования email
            throw new IllegalArgumentException("Email already in use");
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = new User(0, username, email, hashedPassword);
        users.add(user);
    }

    // Методы, которые необходимо добавить
    private boolean emailExists(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    public User loginUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && BCrypt.checkpw(password, user.getPassword())) {
                return user;
            }
        }
        return null; // вернуть объект пользователя или null
    }
}