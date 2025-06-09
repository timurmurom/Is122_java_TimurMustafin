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
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) {
                throw new IOException("Файл не найден: " + path);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String line;
            int questionIndex = 0;
            while ((line = reader.readLine()) != null) {
                Question question = parseQuestion( line, survey.getId(), questionIndex);
                if (question != null) {
                    questions.add(question);
                    questionIndex++;
                }
            }
        }
        return questions;
    }
    private Question parseQuestion(String line, int surveyId, int questionIndex) throws IOException{
        line = line.trim();
        if(line.trim().isEmpty()) return null;


        String [] parts = line.split(";");
        if (parts.length < 2){
            throw new IOException("Invalid question format" + line);
        }

        String typeStr = parts[0].trim();
        String text = parts[1].trim();
        List<String> options = new ArrayList<>();

        if (parts.length > 2){
            for (int i = 2; i < parts.length; i++) {
                options.add(parts[i].trim());
            }
        }

        try{
            QuestionType type = QuestionType.valueOf(typeStr);

            // Генерируем уникальный ID для вопроса
            int questionId = surveyId * 1000 + questionIndex;

            switch (type) {
                case CLOSED:
                    return new ClosedQuestion(questionId, surveyId, text, options);
                case PARTIALLY_OPEN:
                    return new PartiallyOpenQuestion(questionId, surveyId, text, options);
                case OPEN_ENDED:
                    return new OpenEndedQuestion(questionId, surveyId, text);
                default:
                    throw new IllegalArgumentException("Unknown question type: " + type);
            }

        } catch (IllegalArgumentException e){
            throw new IOException("Invalid question type "+ typeStr);
        }

    }
}
