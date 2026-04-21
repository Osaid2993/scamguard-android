package com.osaid.scamguard;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView emptyStateText;
    private TextView countHigh, countMedium, countLow, countMinimal;
    private TextView filterAll, filterHigh, filterMedium, filterLow, filterMinimal;
    private LinearLayout summaryContainer;

    private List<ScanRecord> allRecords = new ArrayList<>();
    private String activeFilter = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.historyRecyclerView);
        emptyStateText = findViewById(R.id.emptyStateText);
        summaryContainer = findViewById(R.id.summaryContainer);
        TextView clearButton = findViewById(R.id.clearButton);

        countHigh = findViewById(R.id.countHigh);
        countMedium = findViewById(R.id.countMedium);
        countLow = findViewById(R.id.countLow);
        countMinimal = findViewById(R.id.countMinimal);

        filterAll = findViewById(R.id.filterAll);
        filterHigh = findViewById(R.id.filterHigh);
        filterMedium = findViewById(R.id.filterMedium);
        filterLow = findViewById(R.id.filterLow);
        filterMinimal = findViewById(R.id.filterMinimal);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        filterAll.setOnClickListener(v -> applyFilter("All"));
        filterHigh.setOnClickListener(v -> applyFilter("High"));
        filterMedium.setOnClickListener(v -> applyFilter("Medium"));
        filterLow.setOnClickListener(v -> applyFilter("Low"));
        filterMinimal.setOnClickListener(v -> applyFilter("Minimal"));

        loadHistory();

        clearButton.setOnClickListener(v -> {
            Executors.newSingleThreadExecutor().execute(() -> {
                ScamGuardDatabase.getInstance(this).scanRecordDao().clearAll();
                runOnUiThread(() -> {
                    allRecords.clear();
                    applyFilter("All");
                    updateSummaryCounts();
                    loadHistory();
                });
            });
        });
    }

    private void loadHistory() {
        Executors.newSingleThreadExecutor().execute(() -> {
            allRecords = ScamGuardDatabase
                    .getInstance(this)
                    .scanRecordDao()
                    .getAllRecords();

            runOnUiThread(() -> {
                updateSummaryCounts();
                applyFilter(activeFilter);
            });
        });
    }

    private void applyFilter(String filter) {
        activeFilter = filter;
        updateFilterStyles();

        List<ScanRecord> filtered;
        if (filter.equals("All")) {
            filtered = allRecords;
        } else {
            filtered = new ArrayList<>();
            for (ScanRecord record : allRecords) {
                if (record.riskLevel.equals(filter)) {
                    filtered.add(record);
                }
            }
        }

        if (filtered.isEmpty()) {
            emptyStateText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            if (allRecords.isEmpty()) {
                emptyStateText.setText("No scans yet. Analyse a message to see it here.");
                summaryContainer.setVisibility(View.GONE);
            } else {
                emptyStateText.setText("No " + filter.toLowerCase() + " risk scans found.");
                summaryContainer.setVisibility(View.VISIBLE);
            }
        } else {
            emptyStateText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            summaryContainer.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(new HistoryAdapter(filtered));
        }
    }

    private void updateFilterStyles() {
        TextView[] filters = {filterAll, filterHigh, filterMedium, filterLow, filterMinimal};
        String[] labels = {"All", "High", "Medium", "Low", "Minimal"};
        int[] activeTextColors = {
                R.color.bg_base,
                R.color.risk_high,
                R.color.risk_medium,
                R.color.risk_low,
                R.color.risk_minimal
        };
        int[] activeBgDrawables = {
                R.drawable.bg_button_primary,
                R.drawable.bg_risk_high,
                R.drawable.bg_risk_medium,
                R.drawable.bg_risk_low,
                R.drawable.bg_risk_minimal
        };

        for (int i = 0; i < filters.length; i++) {
            if (labels[i].equals(activeFilter)) {
                filters[i].setBackground(
                        ContextCompat.getDrawable(this, activeBgDrawables[i]));
                filters[i].setTextColor(
                        ContextCompat.getColor(this, activeTextColors[i]));
            } else {
                filters[i].setBackground(
                        ContextCompat.getDrawable(this, R.drawable.bg_button_secondary));
                filters[i].setTextColor(
                        ContextCompat.getColor(this, R.color.text_secondary));
            }
        }
    }

    private void updateSummaryCounts() {
        int high = 0, medium = 0, low = 0, minimal = 0;
        for (ScanRecord record : allRecords) {
            switch (record.riskLevel) {
                case "High": high++; break;
                case "Medium": medium++; break;
                case "Low": low++; break;
                default: minimal++; break;
            }
        }
        countHigh.setText(String.valueOf(high));
        countMedium.setText(String.valueOf(medium));
        countLow.setText(String.valueOf(low));
        countMinimal.setText(String.valueOf(minimal));
    }
}
