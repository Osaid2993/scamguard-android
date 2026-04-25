package com.osaid.scamguard;

// Abstraction layer for AI inference
// Allows swapping between local server and on-device engine
public interface ScamAnalyser {

    // Callback to receive the AI response on the main thread
    interface AnalysisCallback {
        void onSuccess(String explanation);
        void onError(String errorMessage);
    }

    // Sends the prompt to the AI model and returns the response via callback
    void analyse(String prompt, AnalysisCallback callback);
}
