package com.osaid.scamguard;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private final String[] exampleMessages = {
            "URGENT: Your bank account will be suspended. Click this link immediately to verify your identity and avoid service interruption.",
            "Hi, your Australia Post parcel could not be delivered. Pay the $2.99 redelivery fee here: http://auspost-redelivery.xyz/pay",
            "Congratulations! You have won a $1000 gift card. Claim your prize now by sending your card details to this number.",
            "ATO Notice: You have an outstanding tax penalty of $847. Pay within 24 hours to avoid legal action. Ref: 77291.",
            "Your verification code is 549281. Please share this code with our support agent to confirm your identity."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView analyzeButton = findViewById(R.id.analyzeButton);
        TextView pasteClipboardButton = findViewById(R.id.pasteClipboardButton);
        TextView tryExampleButton = findViewById(R.id.tryExampleButton);
        EditText messageEditText = findViewById(R.id.messageEditText);

        // Check if we arrived from the Scam Library with a pre-filled example
        String prefill = getIntent().getStringExtra("prefill_message");
        if (prefill != null && !prefill.isEmpty()) {
            messageEditText.setText(prefill);
        }

        RadioGroup sourceGroup = findViewById(R.id.sourceGroup);
        RadioButton radioSms = findViewById(R.id.radioSms);
        RadioButton radioEmail = findViewById(R.id.radioEmail);
        RadioButton radioSocial = findViewById(R.id.radioSocial);

        Chip chipUrgent = findViewById(R.id.chipUrgent);
        Chip chipLink = findViewById(R.id.chipLink);
        Chip chipMoney = findViewById(R.id.chipMoney);
        Chip chipOtp = findViewById(R.id.chipOtp);
        Chip chipUnknown = findViewById(R.id.chipUnknown);

        // Learn button opens the Scam Library screen
        TextView learnButton = findViewById(R.id.learnButton);
        learnButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ScamLibraryActivity.class));
        });

        // History button opens past scan results
        ImageView historyButton = findViewById(R.id.historyButton);
        historyButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, HistoryActivity.class));
        });

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
            int index = new Random().nextInt(exampleMessages.length);
            messageEditText.setText(exampleMessages[index]);
        });

        analyzeButton.setOnClickListener(v -> {
            String message = messageEditText.getText().toString().trim();

            if (message.isEmpty()) {
                Toast.makeText(this, "Please paste or type a message first", Toast.LENGTH_SHORT).show();
                return;
            }

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

    // Handles intents when MainActivity is reused from the back stack
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String prefill = intent.getStringExtra("prefill_message");
        if (prefill != null && !prefill.isEmpty()) {
            EditText messageEditText = findViewById(R.id.messageEditText);
            messageEditText.setText(prefill);
        }
    }
}