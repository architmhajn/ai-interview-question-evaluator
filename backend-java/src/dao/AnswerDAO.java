package dao;

import db.DBConnection;
import java.sql.*;

public class AnswerDAO {

    public int insertAnswer(int userId, String question, String answer) {
        int generatedId = -1;
        String sql = "INSERT INTO answers (user_id, question, answer) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, userId);
            ps.setString(2, question);
            ps.setString(3, answer);
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
