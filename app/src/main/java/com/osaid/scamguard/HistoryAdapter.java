package com.osaid.scamguard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private final List<ScanRecord> records;

    public HistoryAdapter(List<ScanRecord> records) {
        this.records = records;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        ScanRecord record = records.get(position);

        holder.riskLevel.setText(record.riskLevel);
        holder.scamType.setText(record.scamType);
        holder.snippet.setText(record.messageSnippet);
        holder.source.setText(record.source);

        // Format the timestamp into a readable date string
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
        holder.timestamp.setText(sdf.format(new Date(record.timestamp)));

        // Set the risk level text colour based on severity
        int colorRes;
        switch (record.riskLevel) {
            case "High":
                colorRes = R.color.risk_high;
                break;
            case "Medium":
                colorRes = R.color.risk_medium;
                break;
            case "Low":
                colorRes = R.color.risk_low;
                break;
            default:
                colorRes = R.color.risk_minimal;
                break;
        }
        holder.riskLevel.setTextColor(
                ContextCompat.getColor(holder.itemView.getContext(), colorRes));
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView riskLevel, scamType, snippet, source, timestamp;

        HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            riskLevel = itemView.findViewById(R.id.historyRiskLevel);
            scamType = itemView.findViewById(R.id.historyScamType);
            snippet = itemView.findViewById(R.id.historySnippet);
            source = itemView.findViewById(R.id.historySource);
            timestamp = itemView.findViewById(R.id.historyTimestamp);
        }
    }
}
