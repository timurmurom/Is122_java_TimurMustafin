package controllers;

import services.QuestionLoader;
import services.SurveyService;
import models.Survey;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import interface_for_controllers.ISurveyController;
import models.Question;
import models.QuestionType;
import models.OpenEndedQuestion;
import models.ClosedQuestion;
import models.PartiallyOpenQuestion;

import javax.swing.*;

// Принцип инверсии зависимости Паттерн Интерфейс

public class SurveyController implements ISurveyController {
    private final SurveyService surveyService;

    // Добавить метод для редактирования анкеты
    public void editSurvey(Survey survey) {
        surveyService.updateSurvey(survey);
    }

    // Добавить метод для удаления анкеты
    public void deleteSurvey(int surveyId) {
        surveyService.removeSurvey(surveyId);
    }

    // Метод для получения анкеты по идентификатору
    public Survey getSurveyById(int surveyId) {
        return surveyService.getSurveyById(surveyId);
    }

    public Survey findSurveyByTitle(String title){
        return surveyService.getAllSurveys().stream().filter(s -> s.getTitle().equals(title)).findFirst().orElse(null);
    }

    public void createQuestion(int surveyId, String text, QuestionType type, List<String> options) {
        Question question;

        // Создайте вопросы разных типов
        switch(type) {
            case OPEN_ENDED:
                question = new OpenEndedQuestion(0, surveyId, text);
                break;
            case CLOSED:
                question = new ClosedQuestion(0, surveyId, text, options);
                for (String option : options) {
                    question.addOption(option);
                }
                break;
            case PARTIALLY_OPEN:
                question = new PartiallyOpenQuestion(0, surveyId, text, options);
                for (String option : options) {
                    question.addOption(option);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown question type");
        }

        surveyService.addQuestionToSurvey(surveyId, question);
    }

    public SurveyController(SurveyService surveyService) { // Внедрение зависимости
        this.surveyService = surveyService;
    }

    public List<Question> loadSurveyQuestion( Survey survey) {
        QuestionLoader loader = new QuestionLoader();
        try {
            return loader.loadQuestions(survey);
        } catch (IOException e){
            JOptionPane.showMessageDialog( null, "Ошибка загрузки вопросов: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }
    @Override
    public void createSurvey(Survey survey) {
        surveyService.createSurvey(survey);
    }

    @Override
    public List<Survey> getSurveys() {
        return surveyService.getAllSurveys();
    }
    public void addQuestionToSurvey(int surveyId, Question question) {
        surveyService.addQuestionToSurvey(surveyId, question);
    }
}
