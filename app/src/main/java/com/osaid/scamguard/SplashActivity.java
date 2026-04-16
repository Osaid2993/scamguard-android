package com.osaid.scamguard;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.DecelerateInterpolator;
import android.view.View;
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

        ObjectAnimator shieldFade = ObjectAnimator.ofFloat(shield, View.ALPHA, 0f, 1f);
        ObjectAnimator shieldScaleX = ObjectAnimator.ofFloat(shield, View.SCALE_X, 0.7f, 1f);
        ObjectAnimator shieldScaleY = ObjectAnimator.ofFloat(shield, View.SCALE_Y, 0.7f, 1f);

        AnimatorSet shieldSet = new AnimatorSet();
        shieldSet.playTogether(shieldFade, shieldScaleX, shieldScaleY);
        shieldSet.setDuration(600);
        shieldSet.setInterpolator(new DecelerateInterpolator());

        ObjectAnimator titleFade = ObjectAnimator.ofFloat(title, View.ALPHA, 0f, 1f);
        titleFade.setDuration(500);
        titleFade.setStartDelay(300);

        ObjectAnimator taglineFade = ObjectAnimator.ofFloat(tagline, View.ALPHA, 0f, 1f);
        taglineFade.setDuration(500);
        taglineFade.setStartDelay(500);

        AnimatorSet fullSet = new AnimatorSet();
        fullSet.playTogether(shieldSet, titleFade, taglineFade);
        fullSet.start();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }, 1700);
    }
}