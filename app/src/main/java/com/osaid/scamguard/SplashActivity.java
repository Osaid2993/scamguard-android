package com.osaid.scamguard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView shield = findViewById(R.id.splashShield);
        TextView title = findViewById(R.id.splashTitle);
        TextView tagline = findViewById(R.id.splashTagline);

        shield.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(600)
                .setInterpolator(new DecelerateInterpolator())
                .start();

        title.animate()
                .alpha(1f)
                .setStartDelay(300)
                .setDuration(500)
                .start();

        tagline.animate()
                .alpha(1f)
                .setStartDelay(500)
                .setDuration(500)
                .start();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }, 1600);
    }
}
