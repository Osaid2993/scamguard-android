package com.osaid.scamguard;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button analyzeButton = findViewById(R.id.analyzeButton);
        Button pasteClipboardButton = findViewById(R.id.pasteClipboardButton);
        Button tryExampleButton = findViewById(R.id.tryExampleButton);
        EditText messageEditText = findViewById(R.id.messageEditText);

        pasteClipboardButton.setOnClickListener(v -> {
            ClipboardManager clipboard =
                    (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

            if (clipboard != null && clipboard.hasPrimaryClip()) {
                ClipData clipData = clipboard.getPrimaryClip();

                if (clipData != null && clipData.getItemCount() > 0) {
                    CharSequence pastedText = clipData.getItemAt(0).getText();

                    if (pastedText != null) {
                        messageEditText.setText(pastedText.toString());
                    } else {
                        Toast.makeText(this, "Clipboard does not contain text", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(this, "Clipboard is empty", Toast.LENGTH_SHORT).show();
            }
        });

        tryExampleButton.setOnClickListener(v -> {
            String exampleMessage =
                    "URGENT: Your bank account will be suspended. " +
                            "Click this link immediately to verify your identity and avoid service interruption.";
            messageEditText.setText(exampleMessage);
        });

        analyzeButton.setOnClickListener(v -> {
            String message = messageEditText.getText().toString().trim();

            Intent intent = new Intent(MainActivity.this, ResultsActivity.class);
            intent.putExtra("message_text", message);
            startActivity(intent);
        });
    }
}