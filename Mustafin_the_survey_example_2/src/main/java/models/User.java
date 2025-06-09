package models;
import abstract_models.BaseEntity;
import org.mindrot.jbcrypt.BCrypt;

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

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }
    // Конструктор, геттеры и сеттеры
}
