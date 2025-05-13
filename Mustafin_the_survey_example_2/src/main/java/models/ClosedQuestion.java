package models;

import java.util.List;

// Принцип подстановки Барбары Лисков

public class ClosedQuestion extends Question {
    private List<String> options;


    public ClosedQuestion(int id, int surveyId, String text, List<String> options) {
        super(id, surveyId, text, QuestionType.CLOSED);
        this.options = options;
    }

    public List<String> getOptions(){
        return options;
    }
}
