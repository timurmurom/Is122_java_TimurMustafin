package models;

import java.util.HashMap;
import java.util.Map;

public class SurveyFactory {
    private static final Map<String, String> SURVEY_MAP = new HashMap<>();
    private static int idCounter = 1;

    static {
        SURVEY_MAP.put("IT-технологии", "/questions/question_for_first_questionnaire.txt");
        SURVEY_MAP.put("IT-технологии в современном мире", "/questions/question_for_second_questionnaire.txt");
        SURVEY_MAP.put("Победа в Великой Отечественной Войне", "/questions/question_for_third_questionnaire.txt");
    }

    public static Map <String, String> getSurveyMap(){
        return new HashMap<>(SURVEY_MAP);
    }

    private static int generateUniqueId(){
        return idCounter++;
    }

    public static Survey createSurvey(String title){
        String questionPath = SURVEY_MAP.getOrDefault(title, "");
        Survey survey = new Survey(0, title, "", questionPath);
        survey.setId(generateUniqueId());
        return survey;
    }
}
