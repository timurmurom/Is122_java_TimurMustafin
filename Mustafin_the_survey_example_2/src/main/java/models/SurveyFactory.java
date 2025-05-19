package models;

import java.util.HashMap;
import java.util.Map;

public class SurveyFactory {
    private static final Map<String, String> SURVEY_MAP = new HashMap<>();

    static {
        SURVEY_MAP.put("IT-технологии", "/questions/questionsdevexample.txt");
        SURVEY_MAP.put("IT-технологии в современном мире", "/questions/questionsdevexample2.txt");
        SURVEY_MAP.put("Победа в Великой Отечественной Войне", "/questions/questionsdevexample3.txt");
    }

    public static Survey createSurvey(String title){
        String questionPath = SURVEY_MAP.getOrDefault(title, "");
        return new Survey(0, title, "", questionPath);
    }
}
