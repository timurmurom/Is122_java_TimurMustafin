package interface_for_controllers;

import models.User;
public interface IUserController {
    void register(String username, String email, String password);
    User login(String username, String password);
}
