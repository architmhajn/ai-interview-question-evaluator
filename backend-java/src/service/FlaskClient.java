package service;

import com.google.gson.Gson;
import model.EvaluationResult;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class FlaskClient {

    public static EvaluationResult evaluateAnswer(String userAnswer, String modelAnswer) {
        EvaluationResult result = new EvaluationResult();

        try {
            URL url = new URL("http://127.0.0.1:5000/evaluate");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setDoOutput(true);

            // ✅ Build JSON safely using Gson
            Map<String, String> payload = new HashMap<>();
            payload.put("user_answer", userAnswer);
            payload.put("model_answer", modelAnswer);

            Gson gson = new Gson();
            String jsonInput = gson.toJson(payload);

            try (OutputStream os = con.getOutputStream()) {
                os.write(jsonInput.getBytes(StandardCharsets.UTF_8));
            }

            BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8)
            );

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }

            // 🔍 DEBUG (optional, remove later)
            System.out.println("Flask response: " + response);

            result = gson.fromJson(response.toString(), EvaluationResult.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
