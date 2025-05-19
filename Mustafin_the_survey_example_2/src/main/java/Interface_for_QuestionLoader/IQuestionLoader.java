package Interface_for_QuestionLoader;

import models.Question;

import java.io.IOException;
import java.util.List;

public interface IQuestionLoader {
    List<Question> loadQuestions(String resourcePath) throws IOException;
}
