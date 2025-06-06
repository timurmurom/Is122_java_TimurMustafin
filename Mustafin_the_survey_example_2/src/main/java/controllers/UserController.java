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

    public void updateUser(User user) {
        userService.updateUser(user);
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
}
