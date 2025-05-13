package services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import models.Survey;
import models.Question;

// Принцип единственной ответственности и паттерн Фасад

public class SurveyService {
    private List<Survey> surveys = new ArrayList<>();

    public void updateSurvey(Survey survey) {
        Optional<Survey> existingSurvey = surveys.stream()
                .filter(s -> s.getId() == survey.getId())
                .findFirst();

        if (existingSurvey.isPresent()) {
            surveys.remove(existingSurvey.get());
            surveys.add(survey);
        }
    }

    // Метод для удаления анкеты
    public void removeSurvey(int surveyId) {
        surveys.removeIf(survey -> survey.getId() == surveyId);
    }

    // Метод для получения анкеты по ее идентификатору
    public Survey getSurveyById(int surveyId) {
        return surveys.stream()
                .filter(s -> s.getId() == surveyId)
                .findFirst()
                .orElse(null);
    }

    public void createSurvey(Survey survey) {
        surveys.add(survey);
    }

    public List<Survey> getAllSurveys() {
        return surveys;
    }

    public Optional<Question> getQuestionById(int questionId) {
        for (Survey survey : surveys) {
            for (Question question : survey.getQuestions()) {
                if (question.getId() == questionId) {
                    return Optional.of(question);
                }
            }
        }
        return Optional.empty();
    }

    public void addQuestionToSurvey(int surveyId, Question question) {
        for (Survey survey : surveys) {
            if (survey.getId() == surveyId) {
                survey.getQuestions().add(question);
            }
        }
    }
}