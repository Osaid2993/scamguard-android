package com.osaid.scamguard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class ScanningActivity extends AppCompatActivity {

    private TextView stepText;
    private View dot1, dot2, dot3, dot4;
    private final Handler handler = new Handler(Looper.getMainLooper());

    // Step labels shown during the scanning animation
    private final String[] steps = {
            "Reading message...",
            "Checking patterns...",
            "Analysing risk...",
            "Preparing results..."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);

        stepText = findViewById(R.id.scanningStepText);
        dot1 = findViewById(R.id.dot1);
        dot2 = findViewById(R.id.dot2);
        dot3 = findViewById(R.id.dot3);
        dot4 = findViewById(R.id.dot4);

        View[] dots = {dot1, dot2, dot3, dot4};

        // Retrieve the data passed from MainActivity
        String message = getIntent().getStringExtra("message_text");
        String source = getIntent().getStringExtra("message_source");
        ArrayList<String> concerns = getIntent().getStringArrayListExtra("selected_concerns");

        // Run each step with a delay
        for (int i = 0; i < steps.length; i++) {
            int step = i;
            handler.postDelayed(() -> {
                // Update the step text
                stepText.setText(steps[step]);

                // Light up the current dot in teal
                dots[step].setBackgroundResource(0);
                dots[step].setBackgroundColor(
                        ContextCompat.getColor(this, R.color.accent));

                // Make the dot circular by clipping
                dots[step].setClipToOutline(true);
            }, i * 500L);
        }

        // After all steps complete, navigate to ResultsActivity
        handler.postDelayed(() -> {
            Intent intent = new Intent(ScanningActivity.this, ResultsActivity.class);
            intent.putExtra("message_text", message);
            intent.putExtra("message_source", source);
            intent.putStringArrayListExtra("selected_concerns", concerns);
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }, 2200L);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
