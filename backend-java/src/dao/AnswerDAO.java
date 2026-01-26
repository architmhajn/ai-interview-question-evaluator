package dao;

import db.DBConnection;
import model.Answer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AnswerDAO {

    public int insertAnswer(Answer answer) {
        int generatedId = -1;

        String sql =
            "INSERT INTO answers (user_id, question_id, user_answer) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            if (con == null) {
                System.out.println("❌ DB connection failed. Answer not inserted.");
                return -1;
            }

            ps.setInt(1, answer.getUserId());
            ps.setInt(2, answer.getQuestionId());
            ps.setString(3, answer.getUserAnswer());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                generatedId = rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return generatedId;
    }
}
