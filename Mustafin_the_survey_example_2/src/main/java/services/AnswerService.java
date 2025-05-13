package services;

import  models.Answer;
import  java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import interface_for_service.IAnswerService;

// Разделение интерфейса

public class AnswerService implements  IAnswerService {
    private DatabaseManager dbManager;

    public AnswerService() {

        dbManager = DatabaseManager.getInstance();
    }

    @Override
    public void saveAnswer(Answer answer) {
        try (Connection conn = dbManager.getConnection()) {
            String sql = "INSERT INTO answers (questionId, userId, answerText) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, answer.getQuestionId());
            stmt.setInt(2, answer.getUserId());
            stmt.setString(3, answer.getAnswerText());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Логирование ошибок
        }
    }
}
