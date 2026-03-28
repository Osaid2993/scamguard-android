package com.osaid.scamguard;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity {

    private TextView sourceTextView;
    private TextView concernsTextView;
    private TextView riskLevelTextView;
    private TextView scamTypeTextView;
    private TextView redFlagsTextView;
    private TextView safeGuidanceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        sourceTextView = findViewById(R.id.sourceTextView);
        concernsTextView = findViewById(R.id.concernsTextView);
        riskLevelTextView = findViewById(R.id.riskLevelTextView);
        scamTypeTextView = findViewById(R.id.scamTypeTextView);
        redFlagsTextView = findViewById(R.id.redFlagsTextView);
        safeGuidanceTextView = findViewById(R.id.safeGuidanceTextView);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        String message = getIntent().getStringExtra("message_text");
        String source = getIntent().getStringExtra("message_source");
        ArrayList<String> concerns = getIntent().getStringArrayListExtra("selected_concerns");

        if (message == null) message = "";
        if (source == null) source = "Unknown";

        sourceTextView.setText(source);

        if (concerns == null || concerns.isEmpty()) {
            concernsTextView.setText("None selected");
        } else {
            concernsTextView.setText(String.join(", ", concerns));
        }

        analyzeMessage(message, source, concerns);
    }

    private void analyzeMessage(String message, String source, ArrayList<String> concerns) {
        String lowerMessage = message.toLowerCase();

        StringBuilder redFlags = new StringBuilder();
        int riskScore = 0;
        String scamType = "General suspicious message";

        if (lowerMessage.contains("urgent")) {
            redFlags.append("• Uses urgent language\n");
            riskScore++;
        }

        if (lowerMessage.contains("click")) {
            redFlags.append("• Contains a call to click a link\n");
            riskScore++;
        }

        if (lowerMessage.contains("verify")) {
            redFlags.append("• Requests verification\n");
            riskScore++;
        }

        if (lowerMessage.contains("otp")) {
            redFlags.append("• Requests OTP or security code\n");
            riskScore += 2;
        }

        if (lowerMessage.contains("bank") || lowerMessage.contains("account")) {
            redFlags.append("• Mentions bank or account details\n");
            riskScore++;
            scamType = "Bank impersonation";
        }

        if (lowerMessage.contains("delivery") || lowerMessage.contains("package")) {
            redFlags.append("• Mentions delivery or package issue\n");
            riskScore++;
            scamType = "Fake delivery scam";
        }

        if (source.equalsIgnoreCase("Email")) {
            riskScore++;
        }

        if (concerns != null) {
            if (concerns.contains("Urgent")) riskScore++;
            if (concerns.contains("Contains Link")) riskScore++;
            if (concerns.contains("Asks for Money")) riskScore++;
            if (concerns.contains("Requests OTP")) riskScore += 2;
            if (concerns.contains("Unknown Sender")) riskScore++;
        }

        String riskLevel;
        if (riskScore >= 5) {
            riskLevel = "High";
            riskLevelTextView.setTextColor(Color.parseColor("#B91C1C"));
        } else if (riskScore >= 3) {
            riskLevel = "Medium";
            riskLevelTextView.setTextColor(Color.parseColor("#B45309"));
        } else if (riskScore >= 1) {
            riskLevel = "Low";
            riskLevelTextView.setTextColor(Color.parseColor("#1D4ED8"));
        } else {
            riskLevel = "Minimal";
            riskLevelTextView.setTextColor(Color.parseColor("#047857"));
        }

        if (redFlags.length() == 0) {
            redFlags.append("• No obvious scam indicators detected from the current rule-based check");
        }

        String guidance;
        if (riskScore >= 5) {
            guidance = "Do not click links or reply. Verify through the official website or app.";
        } else if (riskScore >= 3) {
            guidance = "Be careful. Do not share personal details until you verify the sender.";
        } else {
            guidance = "No strong indicators were found, but always verify unusual messages carefully.";
        }

        riskLevelTextView.setText(riskLevel);
        scamTypeTextView.setText(scamType);
        redFlagsTextView.setText(redFlags.toString().trim());
        safeGuidanceTextView.setText(guidance);
    }
}