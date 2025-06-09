package services;

import controllers.SurveyController;
import models.SurveyFactory;
import models.Survey;


public class SurveyInitializer {
    private final SurveyController surveyController;

    public SurveyInitializer(SurveyController surveyController) {
        this.surveyController = surveyController;
    }

    public void initializeSurveys() {
        SurveyFactory.getSurveyMap().forEach((title, path) -> {
            Survey survey = SurveyFactory.createSurvey(title);
            surveyController.createSurvey(survey);

        });
    }
}
