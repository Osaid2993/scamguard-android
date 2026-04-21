package com.osaid.scamguard;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ScanDetailDialog extends DialogFragment {

    private ScanRecord record;

    public static ScanDetailDialog newInstance(ScanRecord record) {
        ScanDetailDialog dialog = new ScanDetailDialog();
        dialog.record = record;
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setDimAmount(0.7f);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_scan_detail, container, false);

        if (record == null) {
            dismiss();
            return view;
        }

        View accentBar = view.findViewById(R.id.accentBar);
        ImageView riskIcon = view.findViewById(R.id.detailRiskIcon);
        TextView riskLevel = view.findViewById(R.id.detailRiskLevel);
        TextView timestamp = view.findViewById(R.id.detailTimestamp);
        TextView scamType = view.findViewById(R.id.detailScamType);
        TextView confidence = view.findViewById(R.id.detailConfidence);
        TextView fullMessage = view.findViewById(R.id.detailFullMessage);
        View confidenceBar = view.findViewById(R.id.detailConfidenceBar);
        TextView redFlagsLabel = view.findViewById(R.id.detailRedFlagsLabel);
        TextView redFlags = view.findViewById(R.id.detailRedFlags);
        TextView shareButton = view.findViewById(R.id.detailShareButton);
        TextView reportButton = view.findViewById(R.id.detailReportButton);
        TextView closeButton = view.findViewById(R.id.detailCloseButton);

        int colorRes;
        int iconRes;
        int badgeRes;
        int confidencePercent;

        switch (record.riskLevel) {
            case "High":
                colorRes = R.color.risk_high;
                iconRes = R.drawable.ic_risk_high;
                badgeRes = R.drawable.bg_risk_high;
                confidencePercent = 95;
                break;
            case "Medium":
                colorRes = R.color.risk_medium;
                iconRes = R.drawable.ic_risk_medium;
                badgeRes = R.drawable.bg_risk_medium;
                confidencePercent = 60;
                break;
            case "Low":
                colorRes = R.color.risk_low;
                iconRes = R.drawable.ic_risk_low;
                badgeRes = R.drawable.bg_risk_low;
                confidencePercent = 25;
                break;
            default:
                colorRes = R.color.risk_minimal;
                iconRes = R.drawable.ic_risk_minimal;
                badgeRes = R.drawable.bg_risk_minimal;
                confidencePercent = 10;
                break;
        }

        accentBar.setBackgroundColor(ContextCompat.getColor(requireContext(), colorRes));
        riskIcon.setImageResource(iconRes);
        riskLevel.setText(record.riskLevel);
        riskLevel.setTextColor(ContextCompat.getColor(requireContext(), colorRes));
        riskLevel.setBackgroundResource(badgeRes);
        scamType.setText(record.scamType);
        confidence.setText(confidencePercent + "%");
        confidence.setTextColor(ContextCompat.getColor(requireContext(), colorRes));

        timestamp.setText(getRelativeTime(record.timestamp));

        if (record.fullMessage != null && !record.fullMessage.isEmpty()) {
            fullMessage.setText(record.fullMessage);
        } else {
            fullMessage.setText(record.messageSnippet);
        }

        confidenceBar.setBackgroundColor(ContextCompat.getColor(requireContext(), colorRes));
        confidenceBar.post(() -> {
            int parentWidth = ((FrameLayout) confidenceBar.getParent()).getWidth();
            int targetWidth = (parentWidth * confidencePercent) / 100;
            confidenceBar.getLayoutParams().width = targetWidth;
            confidenceBar.requestLayout();
        });

        if (record.redFlags != null && !record.redFlags.isEmpty()) {
            redFlags.setText(record.redFlags);
            int flagCount = record.redFlags.split("\n").length;
            redFlagsLabel.setText("RED FLAGS (" + flagCount + ")");
        } else {
            redFlags.setText("No red flags detected.");
            redFlagsLabel.setText("RED FLAGS");
        }

        shareButton.setOnClickListener(v -> {
            String summary = "ScamGuard Analysis\n"
                    + "Risk: " + record.riskLevel + "\n"
                    + "Type: " + record.scamType + "\n\n"
                    + "Message:\n" + (record.fullMessage != null ? record.fullMessage : record.messageSnippet) + "\n\n"
                    + "Red Flags:\n" + (record.redFlags != null ? record.redFlags : "None");

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, summary);
            startActivity(Intent.createChooser(shareIntent, "Share analysis via"));
        });

        reportButton.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.scamwatch.gov.au/report-a-scam"));
            startActivity(browserIntent);
        });

        closeButton.setOnClickListener(v -> dismiss());

        return view;
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
}
