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
    private String questionFilePath;

    public Survey(int id, String title, String description, String questionFilePath) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.questions = new ArrayList<>(); // Инициализация списка
        this.questionFilePath = questionFilePath;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static Survey createWithResources(String title, String resourcePath){
        Survey survey = new Survey(0, title, "", resourcePath);
        survey.loadQuestionsFromResource();
        return survey;
    }

    private void loadQuestionsFromResource(){
        // Загрузка вопросов из ресурсов

    }
    public String getQuestionFilePath() {
        return questionFilePath;
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

    public Question getQuestionById(int questionId) {
        for (Question question : questions) {
            if (question.getId() == questionId) {
                return question;
            }
        }
        return null;
    }


    // Геттеры и сеттеры
}
