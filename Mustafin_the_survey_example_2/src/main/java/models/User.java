package models;
import abstract_models.BaseEntity;

// Принцип единственной ответственности

public class User extends  BaseEntity {
    //private int id;
    private String username;
    private String email;
    private String password; // Хранить в хэшированном виде

    public User(int id, String username, String email, String password){
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }



    // Конструктор, геттеры и сеттеры
}
