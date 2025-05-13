package services;

import models.Survey;
import models.Question;
import java.util.List;

public interface ISurveyService {
    void createSurvey(Survey survey);
    List<Survey> getAllSurveys();
    void addQuestionToSurvey(int surveyId, Question question);
}
