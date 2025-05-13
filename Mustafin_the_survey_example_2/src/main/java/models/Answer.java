package models;

import abstract_models.BaseEntity;

// Принцип единственной ответственности и открытости/закрытости

public class Answer extends BaseEntity {
    private int id;
    private int questionId;
    private int userId;
    private String answer;

    public Answer(int id, int questionId, int userId, String answer) {
        this.id = id;
        this.questionId = questionId;
        this.userId = userId;
        this.answer = answer;
    }

    public int getUserId() {
        return userId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public String getAnswerText() {
        return answer;
    }

    // Конструктор, геттеры и сеттеры
}