// import dao.*;
// import model.*;
// import service.FlaskClient;

// public class Main {

//     public static void main(String[] args) {

//         UserDAO userDAO = new UserDAO();
//         AnswerDAO answerDAO = new AnswerDAO();
//         EvaluationDAO evaluationDAO = new EvaluationDAO();

//         // 1️⃣ Insert user
//         int userId = userDAO.insertUser("Archit Mahajan");

//         // 2️⃣ Insert answer
//         int answerId = answerDAO.insertAnswer(
//             userId,
//             "What is HashMap in Java?",
//             "HashMap stores key value pairs and is not synchronized"
//         );

//         // 3️⃣ Call Flask AI
//         String modelAnswer =
//             "HashMap is part of java.util package and stores key value pairs and is not synchronized";

//         EvaluationResult result =
//             FlaskClient.evaluateAnswer(
//                 "HashMap stores key value pairs and is not synchronized",
//                 modelAnswer
//             );

//         System.out.println("Score: " + result.getScore());
//         System.out.println("Feedback: " + result.getFeedback());

//         // 4️⃣ Store evaluation
//         evaluationDAO.saveEvaluation(answerId, result);

//         System.out.println("✅ Evaluation stored successfully.");
//     }
// }
