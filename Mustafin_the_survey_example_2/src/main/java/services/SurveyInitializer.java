package services;

import controllers.SurveyController;
import models.Survey;
import models.SurveyFactory;
import models.Question;
import java.util.List;
import java.io.*;
import java.util.Map;

public class SurveyInitializer {
    private final SurveyController surveyController;
    private final QuestionLoader questionLoader;

    public SurveyInitializer(SurveyController  surveyController){
        this.surveyController = surveyController;
        this.questionLoader = new QuestionLoader();
    }

    public void initializeSurveys() {
        Map<String, String> surveyMap = SurveyFactory.getSurveyMap();
        surveyMap.forEach((title, path) -> {
            Survey survey = SurveyFactory.createSurvey(title);
            try {
                List<Question> questions = questionLoader.loadQuestions(survey);
                questions.forEach(survey::addQuestion);
                surveyController.createSurvey(survey);
            } catch (IOException e){
                System.err.println("Ошибка загрузки анкеты '" + title + "': " + e.getMessage());
            }
        });

    }
}
