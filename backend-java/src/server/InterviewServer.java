package server;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import dao.*;
import model.*;
import service.FlaskClient;

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

    // ---- CORS HEADERS (IMPORTANT) ----
    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
    exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
    exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

    // ---- Handle preflight request ----
    if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
        exchange.sendResponseHeaders(204, -1);
        return;
    }

    if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
        exchange.sendResponseHeaders(405, -1);
        return;
    }

    // ---- Read request body ----
    String requestBody = new String(
        exchange.getRequestBody().readAllBytes(),
        java.nio.charset.StandardCharsets.UTF_8
    );

    // Basic parsing
    String userAnswer = requestBody.split("\"answer\":\"")[1].split("\"")[0];

    String modelAnswer =
        "HashMap is part of java.util package and stores key value pairs and is not synchronized";

    // Call Flask
    EvaluationResult result =
        FlaskClient.evaluateAnswer(userAnswer, modelAnswer);

    String jsonResponse = String.format(
        "{\"score\":%d,\"feedback\":\"%s\"}",
        result.getScore(),
        result.getFeedback()
    );

    exchange.getResponseHeaders().add("Content-Type", "application/json");
    exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);

    try (OutputStream os = exchange.getResponseBody()) {
        os.write(jsonResponse.getBytes());
    }
}

    }}
