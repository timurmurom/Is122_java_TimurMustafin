package services;
import models.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class QuestionLoader {
    public List<Question> loadQuestions(Survey survey) throws IOException {
        List<Question> questions = new ArrayList<>();
        String path = survey.getQuestionFilePath();
        try (InputStream is = getClass().getResourceAsStream(survey.getQuestionFilePath())) {
            if (is == null) {
                throw new IOException("Файл не найден: " + path);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null){
                questions.add(parseQuestion(line, survey.getId()));
            }
        }
        return questions;
    }
    private Question parseQuestion(String line, int surveyId) throws IOException{
        String [] parts = line.split(";");

        if (parts.length < 2){
            throw new IOException("Invalid question format" + line);
        }


        String typeStr = parts[0].trim();
        String text = parts[1].trim();
        List<String> options = new ArrayList<>();
        try{
            QuestionType type = QuestionType.valueOf(typeStr);


            if (parts.length > 2){
                for (int i = 2; i < parts.length; i++) {
                    options.add(parts[i].trim());
                }
            }

            switch (type) {
                case CLOSED:
                    return new ClosedQuestion(0, surveyId, text, options);
                case PARTIALLY_OPEN:
                    return new PartiallyOpenQuestion(0, surveyId, text, options);
                case OPEN_ENDED:
                    return new OpenEndedQuestion(0, surveyId, text);
                default:
                    throw new IllegalArgumentException("Unknown question type: " + type);
            }

        } catch (IllegalArgumentException e){
            throw new IOException("Invalid question type "+ typeStr);
        }

    }
}
