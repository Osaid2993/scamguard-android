package com.osaid.scamguard;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class ResultsActivity extends AppCompatActivity {

    private TextView sourceTextView;
    private TextView concernsTextView;
    private TextView riskLevelTextView;
    private TextView scamTypeTextView;
    private TextView redFlagsTextView;
    private TextView safeGuidanceTextView;
    private TextView originalMessageTextView;
    private LinearLayout riskBadgeContainer;
    private TextView aiExplanationTextView;
    private ProgressBar aiLoadingSpinner;
    private LinearLayout aiExplanationContainer;
    private View confidenceBarFill;
    private TextView confidenceTextView;
    private ImageView riskIconView;

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
        originalMessageTextView = findViewById(R.id.originalMessageTextView);
        riskBadgeContainer = findViewById(R.id.riskBadgeContainer);
        aiExplanationTextView = findViewById(R.id.aiExplanationTextView);
        aiLoadingSpinner = findViewById(R.id.aiLoadingSpinner);
        aiExplanationContainer = findViewById(R.id.aiExplanationContainer);
        confidenceBarFill = findViewById(R.id.confidenceBarFill);
        confidenceTextView = findViewById(R.id.confidenceTextView);
        riskIconView = findViewById(R.id.riskIconView);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Share button
        TextView shareButton = findViewById(R.id.shareButton);
        shareButton.setOnClickListener(v -> shareAnalysis());

        // Report button opens Scamwatch in the browser
        TextView reportButton = findViewById(R.id.reportButton);
        reportButton.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    android.net.Uri.parse("https://www.scamwatch.gov.au/report-a-scam"));
            startActivity(browserIntent);
        });

        // New scan button returns to main screen
        TextView newScanButton = findViewById(R.id.newScanButton);
        newScanButton.setOnClickListener(v -> {
            Intent intent = new Intent(ResultsActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        String message = getIntent().getStringExtra("message_text");
        String source = getIntent().getStringExtra("message_source");
        ArrayList<String> concerns = getIntent().getStringArrayListExtra("selected_concerns");

        if (message == null) message = "";
        if (source == null) source = "Unknown";

        sourceTextView.setText(source);
        originalMessageTextView.setText(highlightSuspiciousText(message));

        if (concerns == null || concerns.isEmpty()) {
            concernsTextView.setText("None selected");
        } else {
            concernsTextView.setText(String.join(", ", concerns));
        }

        analyzeMessage(message, source, concerns);
    }

    // Builds a readable text summary and opens Android's share sheet
    private void shareAnalysis() {
        String summary = "ScamGuard Analysis Report\n"
                + "━━━━━━━━━━━━━━━━━━━━\n\n"
                + "Risk Level: " + riskLevelTextView.getText() + "\n"
                + "Scam Type: " + scamTypeTextView.getText() + "\n"
                + "Source: " + sourceTextView.getText() + "\n\n"
                + "Red Flags:\n" + redFlagsTextView.getText() + "\n\n"
                + "Safe Guidance:\n" + safeGuidanceTextView.getText() + "\n\n"
                + "AI Explanation:\n" + aiExplanationTextView.getText() + "\n\n"
                + "━━━━━━━━━━━━━━━━━━━━\n"
                + "Analysed by ScamGuard - Private on-device scam analysis";

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, summary);
        startActivity(Intent.createChooser(shareIntent, "Share analysis via"));
    }

    private SpannableString highlightSuspiciousText(String message) {
        SpannableString spannable = new SpannableString(message);
        String lowerMessage = message.toLowerCase();

        String[][] keywordGroups = {
                {"urgent", "immediately", "act now", "final notice", "suspended", "locked", "warning", "within 24 hours"},
                {"http://", "https://", "www.", "bit.ly", "tinyurl", ".xyz"},
                {"verify", "confirm", "update details", "identity", "login"},
                {"otp", "one-time password", "security code", "password", "pin", "verification code"},
                {"payment", "pay now", "transfer", "bank transfer", "gift card", "bitcoin", "crypto", "fees"},
                {"bank", "account", "transaction", "card", "credit card"},
                {"delivery", "package", "parcel", "courier", "shipping"},
                {"ato", "government", "tax", "medicare", "centrelink", "fine", "penalty"},
                {"won", "winner", "claim prize", "reward", "gift", "congratulations"},
                {"marketplace", "buyer", "seller", "item", "listing", "pickup"}
        };

        int highlightColor = ContextCompat.getColor(this, R.color.risk_high);

        for (String[] group : keywordGroups) {
            for (String keyword : group) {
                int start = lowerMessage.indexOf(keyword);
                while (start >= 0) {
                    int end = start + keyword.length();
                    spannable.setSpan(
                            new ForegroundColorSpan(highlightColor),
                            start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannable.setSpan(
                            new StyleSpan(Typeface.BOLD),
                            start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    start = lowerMessage.indexOf(keyword, end);
                }
            }
        }

        return spannable;
    }

    private void analyzeMessage(String message, String source, ArrayList<String> concerns) {
        String lowerMessage = message.toLowerCase();

        StringBuilder redFlags = new StringBuilder();
        int riskScore = 0;

        int bankScore = 0;
        int deliveryScore = 0;
        int governmentScore = 0;
        int prizeScore = 0;
        int marketplaceScore = 0;
        int paymentScore = 0;

        if (containsAny(lowerMessage, "urgent", "immediately", "act now", "final notice", "suspended", "locked", "warning")) {
            redFlags.append("• Uses urgent or threatening language\n");
            riskScore += 2;
        }

        if (containsUrl(lowerMessage)) {
            redFlags.append("• Contains a link or web address\n");
            riskScore += 2;
        }

        if (containsAny(lowerMessage, "verify", "confirm", "update details", "identity", "login")) {
            redFlags.append("• Requests verification or personal details\n");
            riskScore += 2;
        }

        if (containsAny(lowerMessage, "otp", "one-time password", "security code", "password", "pin")) {
            redFlags.append("• Requests sensitive security information\n");
            riskScore += 3;
            bankScore += 1;
        }

        if (containsAny(lowerMessage, "payment", "pay now", "transfer", "bank transfer", "gift card", "bitcoin", "crypto", "fees")) {
            redFlags.append("• Requests money or unusual payment methods\n");
            riskScore += 2;
            paymentScore += 2;
        }

        if (containsAny(lowerMessage, "bank", "account", "transaction", "card", "credit card")) {
            redFlags.append("• Mentions bank or account-related details\n");
            riskScore += 1;
            bankScore += 2;
        }

        if (containsAny(lowerMessage, "delivery", "package", "parcel", "courier", "shipping")) {
            redFlags.append("• Mentions a package or delivery issue\n");
            riskScore += 1;
            deliveryScore += 2;
        }

        if (containsAny(lowerMessage, "ato", "government", "tax", "medicare", "centrelink", "fine", "penalty")) {
            redFlags.append("• Uses government or official authority language\n");
            riskScore += 2;
            governmentScore += 2;
        }

        if (containsAny(lowerMessage, "won", "winner", "claim prize", "reward", "gift", "congratulations")) {
            redFlags.append("• Promises prizes, rewards, or unexpected benefits\n");
            riskScore += 2;
            prizeScore += 2;
        }

        if (containsAny(lowerMessage, "marketplace", "buyer", "seller", "item", "listing", "pickup")) {
            redFlags.append("• Looks related to a marketplace buying or selling message\n");
            riskScore += 1;
            marketplaceScore += 2;
        }

        if (source.equalsIgnoreCase("Email")) {
            riskScore += 1;
        } else if (source.equalsIgnoreCase("Social Media")) {
            marketplaceScore += 1;
            prizeScore += 1;
        }

        if (concerns != null) {
            if (concerns.contains("Urgent")) riskScore += 1;
            if (concerns.contains("Contains Link")) riskScore += 1;
            if (concerns.contains("Asks for Money")) {
                riskScore += 2;
                paymentScore += 1;
            }
            if (concerns.contains("Requests OTP")) {
                riskScore += 2;
                bankScore += 1;
            }
            if (concerns.contains("Unknown Sender")) riskScore += 1;
        }

        String scamType = getScamType(bankScore, deliveryScore, governmentScore, prizeScore, marketplaceScore, paymentScore);

        String riskLevel;
        int riskColor;
        int badgeBackground;
        int riskIcon;

        if (riskScore >= 7) {
            riskLevel = "High";
            riskColor = R.color.risk_high;
            badgeBackground = R.drawable.bg_risk_high;
            riskIcon = R.drawable.ic_risk_high;
        } else if (riskScore >= 4) {
            riskLevel = "Medium";
            riskColor = R.color.risk_medium;
            badgeBackground = R.drawable.bg_risk_medium;
            riskIcon = R.drawable.ic_risk_medium;
        } else if (riskScore >= 2) {
            riskLevel = "Low";
            riskColor = R.color.risk_low;
            badgeBackground = R.drawable.bg_risk_low;
            riskIcon = R.drawable.ic_risk_low;
        } else {
            riskLevel = "Minimal";
            riskColor = R.color.risk_minimal;
            badgeBackground = R.drawable.bg_risk_minimal;
            riskIcon = R.drawable.ic_risk_minimal;
        }

        riskLevelTextView.setText(riskLevel);
        riskLevelTextView.setTextColor(ContextCompat.getColor(this, riskColor));
        riskBadgeContainer.setBackgroundResource(badgeBackground);
        riskIconView.setImageResource(riskIcon);

        scamTypeTextView.setText(scamType);

        if (redFlags.length() == 0) {
            redFlags.append("• No obvious scam indicators detected from the current rule-based check");
        }

        redFlagsTextView.setText(redFlags.toString().trim());
        safeGuidanceTextView.setText(getGuidance(scamType, riskLevel));

        // Map risk score to a meaningful confidence percentage
        int confidencePercent;
        if (riskScore >= 10) {
            confidencePercent = 95;
        } else if (riskScore >= 8) {
            confidencePercent = 85;
        } else if (riskScore >= 7) {
            confidencePercent = 75;
        } else if (riskScore >= 5) {
            confidencePercent = 60;
        } else if (riskScore >= 4) {
            confidencePercent = 45;
        } else if (riskScore >= 2) {
            confidencePercent = 25;
        } else {
            confidencePercent = 10;
        }

        confidenceTextView.setText(confidencePercent + "% confidence");

        // Animate the bar fill from 0 to the target width
        confidenceBarFill.setBackgroundColor(ContextCompat.getColor(this, riskColor));

        // Delay the animation slightly so the layout is measured first
        confidenceBarFill.getLayoutParams().width = 0;
        confidenceBarFill.requestLayout();

        confidenceBarFill.postDelayed(() -> {
            int parentWidth = ((FrameLayout) confidenceBarFill.getParent()).getWidth();
            int targetWidth = (parentWidth * confidencePercent) / 100;

            android.animation.ValueAnimator animator =
                    android.animation.ValueAnimator.ofInt(0, targetWidth);
            animator.setDuration(1000);
            animator.setInterpolator(new android.view.animation.DecelerateInterpolator());
            animator.addUpdateListener(animation -> {
                confidenceBarFill.getLayoutParams().width = (int) animation.getAnimatedValue();
                confidenceBarFill.requestLayout();
            });
            animator.start();
        }, 300);

        // Save this analysis to the local Room database
        String snippet = message.length() > 80
                ? message.substring(0, 80) + "..."
                : message;

        ScanRecord record = new ScanRecord(
                snippet, riskLevel, scamType, source, System.currentTimeMillis()
        );

        Executors.newSingleThreadExecutor().execute(() -> {
            ScamGuardDatabase.getInstance(this).scanRecordDao().insert(record);
        });

        // Request AI explanation using the rule engine findings
        requestAiExplanation(message, source, riskLevel, scamType,
                redFlags.toString().trim());
    }

    private void requestAiExplanation(String message, String source,
                                      String riskLevel, String scamType,
                                      String redFlagsText) {

        // Show the loading spinner while the AI processes
        aiLoadingSpinner.setVisibility(View.VISIBLE);
        aiExplanationTextView.setText("Analysing with on-device AI...");
        aiExplanationTextView.setTextColor(
                ContextCompat.getColor(this, R.color.text_muted));

        // Convert the red flags text block into a list for the prompt builder
        List<String> redFlagsList = Arrays.asList(redFlagsText.split("\n"));

        // Build the structured prompt from rule engine output
        String prompt = PromptBuilder.build(
                message, source, riskLevel, scamType, redFlagsList);

        // Send to the AI model via the analyser
        ScamAnalyser analyser = new OllamaAnalyser();
        analyser.analyse(prompt, new ScamAnalyser.AnalysisCallback() {
            @Override
            public void onSuccess(String explanation) {
                runOnUiThread(() -> {
                    // Hide spinner and show the AI response
                    aiLoadingSpinner.setVisibility(View.GONE);
                    aiExplanationTextView.setText(explanation);
                    aiExplanationTextView.setTextColor(
                            ContextCompat.getColor(ResultsActivity.this, R.color.text_secondary));
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {
                    // Hide spinner and show a graceful fallback message
                    aiLoadingSpinner.setVisibility(View.GONE);
                    aiExplanationTextView.setText(
                            "AI explanation unavailable. The rule-based analysis above "
                                    + "remains valid. Please check that the AI service is running.");
                    aiExplanationTextView.setTextColor(
                            ContextCompat.getColor(ResultsActivity.this, R.color.text_muted));
                });
            }
        });
    }

    private boolean containsAny(String text, String... keywords) {
        for (String keyword : keywords) {
            if (text.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsUrl(String text) {
        return Pattern.compile("(http://|https://|www\\.|bit\\.ly|tinyurl)", Pattern.CASE_INSENSITIVE)
                .matcher(text)
                .find();
    }

    private String getScamType(int bankScore, int deliveryScore, int governmentScore,
                               int prizeScore, int marketplaceScore, int paymentScore) {

        int maxScore = Math.max(
                Math.max(bankScore, deliveryScore),
                Math.max(Math.max(governmentScore, prizeScore), Math.max(marketplaceScore, paymentScore))
        );

        if (maxScore == 0) return "General suspicious message";
        if (bankScore == maxScore) return "Bank impersonation scam";
        if (deliveryScore == maxScore) return "Fake delivery scam";
        if (governmentScore == maxScore) return "Government or tax scam";
        if (prizeScore == maxScore) return "Prize or reward scam";
        if (marketplaceScore == maxScore) return "Marketplace scam";
        return "Payment request scam";
    }

    private String getGuidance(String scamType, String riskLevel) {
        if (riskLevel.equals("High")) {
            return "Do not click links, reply, or share any details. Verify the sender through the official website or app.";
        }

        switch (scamType) {
            case "Bank impersonation scam":
                return "Do not share OTPs, passwords, or account details. Contact your bank through the official app or phone number.";
            case "Fake delivery scam":
                return "Do not click tracking or payment links. Check delivery updates through the official courier website.";
            case "Government or tax scam":
                return "Do not panic or make immediate payments. Contact the official agency using trusted contact details.";
            case "Prize or reward scam":
                return "Be cautious of unexpected prizes. Never pay fees or share personal details to claim rewards.";
            case "Marketplace scam":
                return "Avoid unusual payment requests or external links. Keep communication and payment inside the official platform.";
            case "Payment request scam":
                return "Do not send money until you independently verify the sender and the reason for payment.";
            default:
                return "No strong indicators were found, but always verify unusual messages carefully before taking action.";
        }
    }
}
