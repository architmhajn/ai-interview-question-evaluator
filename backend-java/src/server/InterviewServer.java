package server;


import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import dao.*;
import model.*;
import service.FlaskClient;
import com.google.gson.Gson;
import model.EvaluationRequest;
import model.EvaluationResult;
import dao.UserDAO;
import dao.AnswerDAO;
import dao.EvaluationDAO;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class InterviewServer {

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/submit", new InterviewHandler());
        server.setExecutor(null);
        server.start();

        System.out.println("🚀 Java Server running on http://localhost:8080");
    }

    static class InterviewHandler implements HttpHandler {

    @Override
public void handle(HttpExchange exchange) throws IOException {

    // ---------- CORS HEADERS ----------
    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
    exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
    exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

    // ---------- Preflight ----------
    if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
        exchange.sendResponseHeaders(204, -1);
        return;
    }

    if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
        exchange.sendResponseHeaders(405, -1);
        return;
    }

    // ---------- Read request body ----------
    String body = new String(
        exchange.getRequestBody().readAllBytes(),
        java.nio.charset.StandardCharsets.UTF_8
    );

    // ---------- Parse JSON safely using Gson ----------
    Gson gson = new Gson();
    EvaluationRequest req = gson.fromJson(body, EvaluationRequest.class);

    String userAnswer = req.getAnswer();
    String modelAnswer = req.getModelAnswer();

    // ---------- Call Flask AI ----------
UserDAO userDAO = new UserDAO();
AnswerDAO answerDAO = new AnswerDAO();
EvaluationDAO evaluationDAO = new EvaluationDAO();

// 1️⃣ Save user
int userId = userDAO.insertUser(req.getName());

// 2️⃣ Save answer
int answerId = answerDAO.insertAnswer(
    userId,
    req.getQuestion(),
    userAnswer
);

// 3️⃣ Call Flask AI
EvaluationResult result =
    FlaskClient.evaluateAnswer(userAnswer, modelAnswer);

// 4️⃣ Save evaluation
evaluationDAO.saveEvaluation(answerId, result);


    // ---------- Build response ----------
    String jsonResponse = gson.toJson(result);

    exchange.getResponseHeaders().add("Content-Type", "application/json");
    exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);

    try (OutputStream os = exchange.getResponseBody()) {
        os.write(jsonResponse.getBytes());
    }
}

    }
}