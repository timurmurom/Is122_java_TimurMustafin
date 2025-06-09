package controllers;

import services.UserService;
import models.User;
import java.util.List;

public class UserController {
    private UserService userService;
    private User currentUser;

    public UserController() {
        this.userService = new UserService();
    }

    public void register(String username, String email, String password) {
        userService.registerUser(username, email, password);
        currentUser = userService.loginUser(username, password); // Автоматический вход после регистрации
    }

    public User login(String username, String password) {
        currentUser = userService.loginUser(username, password);
        return currentUser;
    }

    public void updateUser(int userId, String newUsername, String newEmail) {
        // Получаем пользователя по ID
        User user = userService.getUserById(userId);
        if (user != null) {
            user.setUsername(newUsername);
            user.setEmail(newEmail);
            // Сохранение в файл теперь делается внутри userService.updateUser
            userService.updateUser(user);
        }
    }

    public void deleteUser(int userId) {
        userService.deleteUser(userId);
    }

    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public User getUserById(int userId) {
        return userService.getUserById(userId);
    }
}
