package dao;

import db.DBConnection;
import model.EvaluationResult;
import java.sql.*;

public class EvaluationDAO {

    public void saveEvaluation(int answerId, EvaluationResult result) {

        String sql =
            "INSERT INTO evaluations (answer_id, score, feedback) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, answerId);
            ps.setInt(2, result.getScore());
            ps.setString(3, result.getFeedback());

            ps.executeUpdate();

            System.out.println("✅ Evaluation saved");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
