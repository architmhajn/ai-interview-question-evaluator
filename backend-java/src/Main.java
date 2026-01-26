import dao.UserDAO;
import dao.AnswerDAO;
import model.User;
import model.Answer;

public class Main {

    public static void main(String[] args) {

        // Create user
        User user = new User();
        user.setName("Archit Mahajan");
        user.setEmail("archit@test.com");
        user.setRole("Java Developer");

        UserDAO userDAO = new UserDAO();
        int userId = userDAO.insertUser(user);
        System.out.println("User created with ID: " + userId);

        // Store answer
        Answer answer = new Answer();
        answer.setUserId(userId);
        answer.setQuestionId(1); // existing question
        answer.setUserAnswer("HashMap stores key-value pairs and is not synchronized.");

        AnswerDAO answerDAO = new AnswerDAO();
        answerDAO.insertAnswer(answer);

        System.out.println("Answer stored successfully.");
    }
}
