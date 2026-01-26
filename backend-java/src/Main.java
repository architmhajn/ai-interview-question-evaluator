import dao.*;
import model.*;
import service.FlaskClient;

public class Main {

    public static void main(String[] args) {

        // 1. Insert user
        User user = new User();
        user.setName("Archit Mahajan");
        user.setEmail("archit@test.com");
        user.setRole("Java Developer");

        UserDAO userDAO = new UserDAO();
        int userId = userDAO.insertUser(user);

        // 2. Insert answer
        Answer answer = new Answer();
        answer.setUserId(userId);
        answer.setQuestionId(1);
        answer.setUserAnswer(
            "HashMap stores key value pairs and is not synchronized"
        );

        AnswerDAO answerDAO = new AnswerDAO();
        int answerId = answerDAO.insertAnswer(answer); // return generated id

        // 3. Call Flask AI
        String modelAnswer =
            "HashMap is part of java.util package and stores key value pairs and is not synchronized";

        EvaluationResult result =
            FlaskClient.evaluateAnswer(answer.getUserAnswer(), modelAnswer);

        System.out.println("Score: " + result.getScore());
        System.out.println("Feedback: " + result.getFeedback());

        // 4. Store evaluation
        EvaluationDAO evalDAO = new EvaluationDAO();
        evalDAO.saveEvaluation(answerId, result);

        System.out.println("Evaluation stored successfully.");
    }
}
