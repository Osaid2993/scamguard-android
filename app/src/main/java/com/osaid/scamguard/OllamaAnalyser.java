package com.osaid.scamguard;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;

public class OllamaAnalyser implements ScamAnalyser {

    // Emulator alias for host machine's localhost
    private static final String OLLAMA_URL = "http://10.0.2.2:11434/api/generate";
    private static final String MODEL_NAME = "gemma2:2b";

    @Override
    public void analyse(String prompt, AnalysisCallback callback) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                // Build the JSON request body
                JSONObject requestBody = new JSONObject();
                requestBody.put("model", MODEL_NAME);
                requestBody.put("prompt", prompt);
                requestBody.put("stream", false);

                // Open connection to Ollama
                URL url = new URL(OLLAMA_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(60000);

                // Send the request
                OutputStream os = conn.getOutputStream();
                os.write(requestBody.toString().getBytes(StandardCharsets.UTF_8));
                os.close();

                // Read the response
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    // Parse the response JSON to extract the generated text
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    String generatedText = jsonResponse.getString("response");

                    // Return result on the main thread
                    new Handler(Looper.getMainLooper()).post(() ->
                            callback.onSuccess(generatedText.trim()));
                } else {
                    new Handler(Looper.getMainLooper()).post(() ->
                            callback.onError("AI server returned error code: " + responseCode));
                }

                conn.disconnect();

            } catch (Exception e) {
                // Return error on the main thread
                new Handler(Looper.getMainLooper()).post(() ->
                        callback.onError("AI analysis unavailable: " + e.getMessage()));
            }
        });
    }
}
