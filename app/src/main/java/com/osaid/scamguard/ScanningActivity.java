package com.osaid.scamguard;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class ScanningActivity extends AppCompatActivity {

    private TextView stepText;
    private TextView subText;
    private View dot1, dot2, dot3, dot4;
    private final Handler handler = new Handler(Looper.getMainLooper());

    private final String[] steps = {
            "Reading message structure...",
            "Scanning for malicious URLs...",
            "Analysing tone & tactics...",
            "Running AI verdict..."
    };

    private final String[] subTexts = {
            "Analysing...",
            "Analysing..",
            "Analysing...",
            "Analysing.."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);

        stepText = findViewById(R.id.scanningStepText);
        subText = findViewById(R.id.scanningSubText);
        dot1 = findViewById(R.id.dot1);
        dot2 = findViewById(R.id.dot2);
        dot3 = findViewById(R.id.dot3);
        dot4 = findViewById(R.id.dot4);
        ImageView outerRing = findViewById(R.id.outerRing);
        ImageView innerRing = findViewById(R.id.innerRing);
        ImageView aiIcon = findViewById(R.id.aiIcon);

        String message = getIntent().getStringExtra("message_text");
        String source = getIntent().getStringExtra("message_source");
        ArrayList<String> concerns = getIntent().getStringArrayListExtra("selected_concerns");

        View[] dots = {dot1, dot2, dot3, dot4};

        // Outer ring spins clockwise continuously
        ObjectAnimator outerSpin = ObjectAnimator.ofFloat(outerRing, "rotation", 0f, 360f);
        outerSpin.setDuration(900);
        outerSpin.setRepeatCount(ObjectAnimator.INFINITE);
        outerSpin.setInterpolator(new LinearInterpolator());
        outerSpin.start();

        // Inner ring spins counter-clockwise continuously
        ObjectAnimator innerSpin = ObjectAnimator.ofFloat(innerRing, "rotation", 0f, -360f);
        innerSpin.setDuration(1400);
        innerSpin.setRepeatCount(ObjectAnimator.INFINITE);
        innerSpin.setInterpolator(new LinearInterpolator());
        innerSpin.start();

        // Shield fades in and scales up
        aiIcon.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(500)
                .setStartDelay(300)
                .start();

        // Continuous pulse effect on the shield
        ObjectAnimator pulseX = ObjectAnimator.ofFloat(aiIcon, "scaleX", 1f, 1.15f, 1f);
        pulseX.setDuration(1200);
        pulseX.setRepeatCount(ObjectAnimator.INFINITE);
        pulseX.setStartDelay(800);
        pulseX.start();

        ObjectAnimator pulseY = ObjectAnimator.ofFloat(aiIcon, "scaleY", 1f, 1.15f, 1f);
        pulseY.setDuration(1200);
        pulseY.setRepeatCount(ObjectAnimator.INFINITE);
        pulseY.setStartDelay(800);
        pulseY.start();

        // Cycle through steps with progress dots
        for (int i = 0; i < steps.length; i++) {
            int step = i;
            handler.postDelayed(() -> {
                stepText.setText(steps[step]);
                subText.setText(subTexts[step]);

                // Widen the current dot into a pill shape
                dots[step].getLayoutParams().width =
                        (int) (20 * getResources().getDisplayMetrics().density);
                dots[step].setBackgroundColor(
                        ContextCompat.getColor(this, R.color.accent));
                dots[step].requestLayout();
            }, i * 550L);
        }

        // Navigate to results after all steps complete
        handler.postDelayed(() -> {
            outerSpin.cancel();
            innerSpin.cancel();
            pulseX.cancel();
            pulseY.cancel();

            Intent intent = new Intent(ScanningActivity.this, ResultsActivity.class);
            intent.putExtra("message_text", message);
            intent.putExtra("message_source", source);
            intent.putStringArrayListExtra("selected_concerns", concerns);
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }, 2500L);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
