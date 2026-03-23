package com.osaid.scamguard;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultsActivity extends AppCompatActivity {

    private TextView riskLevelTextView;
    private TextView scamTypeTextView;
    private TextView redFlagsTextView;
    private TextView safeGuidanceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        riskLevelTextView = findViewById(R.id.riskLevelTextView);
        scamTypeTextView = findViewById(R.id.scamTypeTextView);
        redFlagsTextView = findViewById(R.id.redFlagsTextView);
        safeGuidanceTextView = findViewById(R.id.safeGuidanceTextView);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        String message = getIntent().getStringExtra("message_text");
        if (message == null) {
            message = "";
        }

        analyzeMessage(message);
    }

    private void analyzeMessage(String message) {
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

        String riskLevel;
        if (riskScore >= 4) {
            riskLevel = "High";
        } else if (riskScore >= 2) {
            riskLevel = "Medium";
        } else if (riskScore >= 1) {
            riskLevel = "Low";
        } else {
            riskLevel = "Minimal";
        }

        if (redFlags.length() == 0) {
            redFlags.append("• No obvious scam indicators detected from the simple rule-based check");
        }

        String guidance;
        if (riskScore >= 4) {
            guidance = "Do not click links or reply. Verify through the official website or app.";
        } else if (riskScore >= 2) {
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