package models;

import java.util.List;

public interface IQuestion {
    String getText();
    QuestionType getQuestionType();
    void addOption(String option); // Используется только для закрытых вопросов
    List<String> getOptions(); // Добавлено
    int getId(); // Добавлено для получения идентификатора вопроса
}
