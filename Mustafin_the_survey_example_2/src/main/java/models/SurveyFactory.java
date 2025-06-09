package models;

import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class SurveyFactory {
    private static final Map<String, String> SURVEY_MAP = new HashMap<>();
    private static int idCounter = 1;

    static {
        loadSurveysFromConfig();
    }

    private static void loadSurveysFromConfig() {
        try (InputStream input = SurveyFactory.class.getResourceAsStream("/surveys.properties")) {
            if (input == null) throw new IOException("Файл конфигурации не найден");

            Properties prop = new Properties();
            prop.load(input);

            for (String name : prop.stringPropertyNames()) {
                String questionPath = prop.getProperty(name);
                System.out.println("Анкета: " + name + ", Путь к файлами вопросов" + questionPath);
                SURVEY_MAP.put(name, prop.getProperty(name));
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки конфигурации анкет", e);
        }
    }

    public static Map<String, String> getSurveyMap() {
        return new HashMap<>(SURVEY_MAP);
    }

    public static List<String> getSurveyNames() {
        return new ArrayList<>(SURVEY_MAP.keySet());
    }

    public static Survey createSurvey(String title) {
        String path = SURVEY_MAP.get(title);
        if (path == null) throw new IllegalArgumentException("Анкета не найдена: " + title);
        return new Survey(0, title, "", path);
    }
}
