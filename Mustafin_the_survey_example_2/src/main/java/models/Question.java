package models;

import abstract_models.BaseEntity;
import java.util.ArrayList;
import java.util.List;

// Принцип разделения интерфейса

public class Question extends BaseEntity implements IQuestion {
    private int id;
    private int surveyId;
    private String text;
    private QuestionType questionType;
    private List<String> options; // Хранит параметры закрытых вопросов

    public Question(int id, int surveyId, String text, QuestionType questionType) {
        this.id = id;
        this.surveyId = surveyId;
        this.text = text;
        this.questionType = questionType;
        this.options = new ArrayList<>();
    }

    @Override
    public void addOption(String option) {
        if (questionType == QuestionType.CLOSED || questionType == QuestionType.PARTIALLY_OPEN) {
            options.add(option);
        }
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public QuestionType getQuestionType() {
        return questionType;
    }

    @Override
    public List<String> getOptions() {
        return options;
    }

    @Override
    public int getId() {
        return id;
    }
}

