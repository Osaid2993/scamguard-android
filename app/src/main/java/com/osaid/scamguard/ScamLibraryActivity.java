package com.osaid.scamguard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ScamLibraryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scam_library);

        // Back button returns to main screen
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Each Try Example button sends a realistic scam message back to MainActivity
        setupExampleButton(
                R.id.tryBankExample,
                "URGENT: Your bank account will be suspended. "
                + "Click this link immediately to verify your identity "
                + "and avoid service interruption. https://secure-bank-login.xyz/verify"
        );

        setupExampleButton(
                R.id.tryDeliveryExample,
                "Hi, your Australia Post parcel could not be delivered. "
                + "Pay the $2.99 redelivery fee here: http://auspost-redelivery.xyz/pay"
        );

        setupExampleButton(
                R.id.tryGovernmentExample,
                "ATO Notice: You have an outstanding tax penalty of $847. "
                + "Pay within 24 hours to avoid legal action. Call 0400 000 000. Ref: 77291."
        );

        setupExampleButton(
                R.id.tryPrizeExample,
                "Congratulations! You have won a $1000 Woolworths gift card. "
                + "Claim your prize now by sending your card details to this number."
        );

        setupExampleButton(
                R.id.tryOtpExample,
                "Your verification code is 549281. Please share this code "
                + "with our support agent to confirm your identity and secure your account."
        );

        setupExampleButton(
                R.id.tryMarketplaceExample,
                "Hi, I want to buy the item you listed. I have already sent payment "
                + "through PayID. Click here to confirm and receive your funds: http://payid-confirm.xyz"
        );
    }

    // Sends the example message back to MainActivity via Intent
    private void setupExampleButton(int buttonId, String exampleMessage) {
        TextView button = findViewById(buttonId);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(ScamLibraryActivity.this, MainActivity.class);
            // Clear the back stack so pressing back from main doesn't return here
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("prefill_message", exampleMessage);
            startActivity(intent);
        });
    }
}
