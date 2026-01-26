package dao;

import db.DBConnection;
import model.Question;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDAO {

    public List<Question> getQuestionsByRoleAndDifficulty(String role, String difficulty) {
        List<Question> questions = new ArrayList<>();

        String sql = "SELECT * FROM questions WHERE role = ? AND difficulty = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, role);
            ps.setString(2, difficulty);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Question q = new Question();
                q.setQuestionId(rs.getInt("question_id"));
                q.setRole(rs.getString("role"));
                q.setTopic(rs.getString("topic"));
                q.setDifficulty(rs.getString("difficulty"));
                q.setQuestionText(rs.getString("question_text"));
                q.setModelAnswer(rs.getString("model_answer"));
                questions.add(q);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return questions;
    }
}
