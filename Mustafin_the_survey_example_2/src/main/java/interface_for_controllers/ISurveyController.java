package interface_for_controllers;

import models.Survey;
import java.util.List;

public interface ISurveyController {
    void createSurvey(Survey survey);
    List<Survey> getSurveys();
}