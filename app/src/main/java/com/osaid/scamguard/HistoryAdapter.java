package com.osaid.scamguard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private final List<ScanRecord> records;
    private final FragmentManager fragmentManager;

    public HistoryAdapter(List<ScanRecord> records, FragmentManager fragmentManager) {
        this.records = records;
        this.fragmentManager = fragmentManager;
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

        holder.timestamp.setText(getRelativeTime(record.timestamp));

        int colorRes;
        int iconRes;
        int badgeRes;

        switch (record.riskLevel) {
            case "High":
                colorRes = R.color.risk_high;
                iconRes = R.drawable.ic_risk_high;
                badgeRes = R.drawable.bg_risk_high;
                break;
            case "Medium":
                colorRes = R.color.risk_medium;
                iconRes = R.drawable.ic_risk_medium;
                badgeRes = R.drawable.bg_risk_medium;
                break;
            case "Low":
                colorRes = R.color.risk_low;
                iconRes = R.drawable.ic_risk_low;
                badgeRes = R.drawable.bg_risk_low;
                break;
            default:
                colorRes = R.color.risk_minimal;
                iconRes = R.drawable.ic_risk_minimal;
                badgeRes = R.drawable.bg_risk_minimal;
                break;
        }

        holder.riskLevel.setTextColor(
                ContextCompat.getColor(holder.itemView.getContext(), colorRes));
        holder.riskLevel.setBackgroundResource(badgeRes);
        holder.riskIcon.setImageResource(iconRes);

        holder.itemView.setOnClickListener(v -> {
            ScanDetailDialog dialog = ScanDetailDialog.newInstance(record);
            dialog.show(fragmentManager, "scan_detail");
        });
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    private String getRelativeTime(long timestamp) {
        long now = System.currentTimeMillis();
        long diff = now - timestamp;

        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (seconds < 60) return "Just now";
        if (minutes < 60) return minutes + " min ago";
        if (hours < 24) return hours + " hr ago";
        if (days == 1) return "Yesterday";
        if (days < 7) return days + " days ago";
        if (days < 30) return (days / 7) + " weeks ago";

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView riskLevel, scamType, snippet, timestamp;
        ImageView riskIcon;

        HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            riskLevel = itemView.findViewById(R.id.historyRiskLevel);
            scamType = itemView.findViewById(R.id.historyScamType);
            snippet = itemView.findViewById(R.id.historySnippet);
            timestamp = itemView.findViewById(R.id.historyTimestamp);
            riskIcon = itemView.findViewById(R.id.historyRiskIcon);
        }
    }
}
