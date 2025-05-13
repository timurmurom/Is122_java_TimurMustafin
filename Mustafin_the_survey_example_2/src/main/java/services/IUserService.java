package services;

import models.User;

public interface IUserService {
    void registerUser(String username, String email, String password);
    User loginUser(String username, String password);
}
