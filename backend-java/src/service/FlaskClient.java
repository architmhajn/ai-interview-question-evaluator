package service;

import com.google.gson.Gson;
import model.EvaluationResult;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class FlaskClient {

    public static EvaluationResult evaluateAnswer(String userAnswer, String modelAnswer) {
        EvaluationResult result = new EvaluationResult();

        try {
            URL url = new URL("http://127.0.0.1:5000/evaluate");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            String jsonInput = String.format(
                "{\"user_answer\":\"%s\",\"model_answer\":\"%s\"}",
                userAnswer.replace("\"", "'"),
                modelAnswer.replace("\"", "'")
            );

            try (OutputStream os = con.getOutputStream()) {
                os.write(jsonInput.getBytes());
            }

            BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream())
            );

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }

            Gson gson = new Gson();
            result = gson.fromJson(response.toString(), EvaluationResult.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
