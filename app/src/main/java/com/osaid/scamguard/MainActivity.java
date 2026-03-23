package com.osaid.scamguard;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button analyzeButton = findViewById(R.id.analyzeButton);
        EditText messageEditText = findViewById(R.id.messageEditText);

        analyzeButton.setOnClickListener(v -> {
            String message = messageEditText.getText().toString().trim();

            Intent intent = new Intent(MainActivity.this, ResultsActivity.class);
            intent.putExtra("message_text", message);
            startActivity(intent);
        });
    }
}