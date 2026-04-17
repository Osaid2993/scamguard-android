package com.osaid.scamguard;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.Executors;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView emptyStateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Back button
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.historyRecyclerView);
        emptyStateText = findViewById(R.id.emptyStateText);
        TextView clearButton = findViewById(R.id.clearButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load history from Room on a background thread
        loadHistory();

        // Clear all history when tapped
        clearButton.setOnClickListener(v -> {
            Executors.newSingleThreadExecutor().execute(() -> {
                ScamGuardDatabase.getInstance(this).scanRecordDao().clearAll();
                // Refresh the list on the main thread
                runOnUiThread(this::loadHistory);
            });
        });
    }

    private void loadHistory() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<ScanRecord> records = ScamGuardDatabase
                    .getInstance(this)
                    .scanRecordDao()
                    .getAllRecords();

            runOnUiThread(() -> {
                if (records.isEmpty()) {
                    // Show empty state, hide the list
                    emptyStateText.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    // Show the list, hide empty state
                    emptyStateText.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setAdapter(new HistoryAdapter(records));
                }
            });
        });
    }
}
