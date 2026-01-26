import java.sql.Connection;
import db.DBConnection;

public class Main {
    public static void main(String[] args) {
        Connection con = DBConnection.getConnection();
        System.out.println(con);
    }
}
