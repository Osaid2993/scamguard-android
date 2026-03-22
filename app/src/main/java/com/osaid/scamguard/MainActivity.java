package com.osaid.scamguard;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button analyzeButton = findViewById(R.id.analyzeButton);

        analyzeButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ResultsActivity.class);
            startActivity(intent);
        });
    }
}