package models;
import java.util.ArrayList;
import java.util.List;
import abstract_models.BaseEntity;

// Принцип единственной ответственности

public class Survey extends  BaseEntity{
    private int id;
    private String title;
    private String description;
    private List<Question> questions; // Список вопросов

    public Survey(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.questions = new ArrayList<>(); // Инициализация списка
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }


    // Геттеры и сеттеры
}
