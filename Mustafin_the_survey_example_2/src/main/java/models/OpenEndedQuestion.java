package models;

// Принцип подстановки LSP

public class OpenEndedQuestion extends Question{
    public OpenEndedQuestion(int id, int surveyId, String text) {
        super(id, surveyId, text, QuestionType.OPEN_ENDED);
    }
}
