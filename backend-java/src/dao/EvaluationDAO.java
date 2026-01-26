package dao;

import db.DBConnection;
import model.EvaluationResult;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class EvaluationDAO {

    public void saveEvaluation(int answerId, EvaluationResult result) {

        String scoreSql =
            "INSERT INTO scores(answer_id, similarity_score, final_score) VALUES (?, ?, ?)";

        String feedbackSql =
            "INSERT INTO feedback(score_id, feedback_text) VALUES (LAST_INSERT_ID(), ?)";

        try (Connection con = DBConnection.getConnection()) {

            PreparedStatement ps1 = con.prepareStatement(scoreSql);
            ps1.setInt(1, answerId);
            ps1.setDouble(2, result.getSimilarity());
            ps1.setInt(3, result.getScore());
            ps1.executeUpdate();

            PreparedStatement ps2 = con.prepareStatement(feedbackSql);
            ps2.setString(1, result.getFeedback());
            ps2.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
