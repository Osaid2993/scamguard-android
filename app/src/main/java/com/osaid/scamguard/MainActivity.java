package com.osaid.scamguard;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button analyzeButton = findViewById(R.id.analyzeButton);
        Button pasteClipboardButton = findViewById(R.id.pasteClipboardButton);
        Button tryExampleButton = findViewById(R.id.tryExampleButton);
        EditText messageEditText = findViewById(R.id.messageEditText);

        RadioGroup sourceGroup = findViewById(R.id.sourceGroup);
        RadioButton radioSms = findViewById(R.id.radioSms);
        RadioButton radioEmail = findViewById(R.id.radioEmail);
        RadioButton radioSocial = findViewById(R.id.radioSocial);

        Chip chipUrgent = findViewById(R.id.chipUrgent);
        Chip chipLink = findViewById(R.id.chipLink);
        Chip chipMoney = findViewById(R.id.chipMoney);
        Chip chipOtp = findViewById(R.id.chipOtp);
        Chip chipUnknown = findViewById(R.id.chipUnknown);

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

            String source = "Unknown";
            int selectedSourceId = sourceGroup.getCheckedRadioButtonId();
            if (selectedSourceId != -1) {
                if (radioSms.isChecked()) {
                    source = "SMS";
                } else if (radioEmail.isChecked()) {
                    source = "Email";
                } else if (radioSocial.isChecked()) {
                    source = "Social Media";
                }
            }

            ArrayList<String> selectedConcerns = new ArrayList<>();

            if (chipUrgent.isChecked()) selectedConcerns.add("Urgent");
            if (chipLink.isChecked()) selectedConcerns.add("Contains Link");
            if (chipMoney.isChecked()) selectedConcerns.add("Asks for Money");
            if (chipOtp.isChecked()) selectedConcerns.add("Requests OTP");
            if (chipUnknown.isChecked()) selectedConcerns.add("Unknown Sender");

            Intent intent = new Intent(MainActivity.this, ResultsActivity.class);
            intent.putExtra("message_text", message);
            intent.putExtra("message_source", source);
            intent.putStringArrayListExtra("selected_concerns", selectedConcerns);
            startActivity(intent);
        });
    }
}