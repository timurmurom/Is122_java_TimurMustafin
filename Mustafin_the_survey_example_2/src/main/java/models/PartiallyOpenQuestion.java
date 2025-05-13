package models;

import java.util.List;

public class PartiallyOpenQuestion extends Question {

    private List<String> options;

    public PartiallyOpenQuestion(int id, int surveyId, String text, List<String> options) {
        super(id, surveyId, text, QuestionType.PARTIALLY_OPEN);
        this.options = options;
    }

    public List<String> getOptions(){
        return  options;
    }
}
