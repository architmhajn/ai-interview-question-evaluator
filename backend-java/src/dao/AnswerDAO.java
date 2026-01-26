package dao;

import db.DBConnection;
import model.Answer;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class AnswerDAO {

    public void insertAnswer(Answer answer) {

        String sql = "INSERT INTO answers (user_id, question_id, user_answer) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, answer.getUserId());
            ps.setInt(2, answer.getQuestionId());
            ps.setString(3, answer.getUserAnswer());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
