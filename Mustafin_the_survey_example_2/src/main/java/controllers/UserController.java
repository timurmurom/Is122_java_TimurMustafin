package controllers;

import services.UserService;
import models.User;

import java.util.Scanner;

public class UserController {
    private UserService userService;
    private Scanner scanner;
    private User currentUser;

    public UserController() {
        this.userService = new UserService();
        this.scanner = new Scanner(System.in);
    }

    public void register(String username, String email, String password) {
        try {
            userService.registerUser(username, email, password);
            System.out.println("Регистрация прошла успешно!");
            // Важно: Здесь нужно сохранить данные пользователя в переменные для доступа из UI
            //  Это можно сделать, сохранив их в соответствующем поле в модели.
            currentUser = userService.loginUser(username, password);
        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка регистрации: " + e.getMessage());
        }
    }

    public User login(String username, String password) {
        return userService.loginUser(username, password);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    // Добавляем метод для ввода данных с консоли
    public void registerFromConsole() {
        System.out.print("Введите имя пользователя: ");
        String username = scanner.nextLine();
        System.out.print("Введите email: ");
        String email = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();  //Вводим пароль с консоли

        register(username, email, password);  //Регистрируем пользователя
    }
}
