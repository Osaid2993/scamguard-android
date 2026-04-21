package com.osaid.scamguard;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "scan_history")
public class ScanRecord {

    @PrimaryKey(autoGenerate = true)
    public int id;

    // First 80 characters of the original message for preview
    public String messageSnippet;

    // Risk level: High, Medium, Low, Minimal
    public String riskLevel;

    // Detected scam type
    public String scamType;

    // Where the message came from (SMS, Email, Social Media)
    public String source;

    // When the scan was performed
    public long timestamp;

    // Full original message for the modal detail view
    public String fullMessage;

    // Red flags as a single string, separated by newlines
    public String redFlags;

    public ScanRecord(String messageSnippet, String riskLevel, String scamType,
                      String source, long timestamp, String fullMessage, String redFlags) {
        this.messageSnippet = messageSnippet;
        this.riskLevel = riskLevel;
        this.scamType = scamType;
        this.source = source;
        this.timestamp = timestamp;
        this.fullMessage = fullMessage;
        this.redFlags = redFlags;
    }
}