# ScamGuard

Private AI-powered scam analysis for Android.

ScamGuard helps users check suspicious SMS, email, marketplace, and social media messages by combining a rule-based detection engine with AI-powered explanation using Gemma 2. The prototype uses a local hybrid architecture where the AI model runs on the development machine via Ollama, communicating over localhost. No message data reaches the internet.

Built as a High Distinction prototype for SIT305 Mobile Application Development at Deakin University (Trimester 1, 2026).


## Features

**Guided Scam Analysis**  
Users choose a message source, select suspicion tags, paste a message, and scan it. ScamGuard uses a rule engine for fast detection and Gemma 2 2B for AI explanation.

**Intelligent Results**  
The results screen shows a risk verdict, confidence bar, highlighted suspicious phrases, red flags, AI explanation, and safe action guidance.

**Scam Library**  
A learning screen with six common scam types, short explanations, searchable cards, and “Try Example” messages.

**Scan History**  
Past scans are saved locally using Room. Users can filter results, view summary counts, and open each scan in a detail view.

**Visual Identity**  
Dark teal theme with animated splash screen, scanning animation, dynamic scan button, character counter, icons, and consistent card layouts.

## Technology Stack

| Component | Technology |
|-----------|------------|
| IDE | Android Studio Panda 2 (2025.3.2) |
| Language | Java |
| UI Framework | Android XML layouts with Material 3 components |
| AI Model | Gemma 2 2B via Ollama (local hybrid for prototype) |
| Local Database | Room (AndroidX) |
| Min SDK | API 24 (Android 7.0) |
| Target SDK | API 35 (Android 15) |
| Compile SDK | API 35 |


## Architecture

ScamGuard uses a dual-engine architecture for scam analysis:

1. **Rule Engine** (deterministic, instant): Pattern-matches the message against 10+ scam indicators including urgency language, suspicious URLs, verification requests, OTP/password phishing, financial pressure, bank/government/delivery impersonation, prize scams, and marketplace fraud. Produces a risk score, scam type classification, and red flag list.

2. **AI Engine** (contextual, async): Takes the rule engine output and the original message, builds a structured prompt with safety constraints, and sends it to Gemma 2 2B for a plain-English explanation. The AI adds interpretive value that rules alone cannot provide: connecting patterns, explaining tactics, and generating Australian-specific guidance.

The rule engine results display immediately. The AI explanation loads asynchronously and appears in its own card. If the AI is unavailable, the rule-based analysis remains fully visible and functional.

**Privacy**: In the prototype, the AI runs on the development machine via Ollama and communicates over localhost (10.0.2.2 from the emulator). No message data reaches the internet. The architecture includes a ScamAnalyser interface abstraction so that swapping to a fully on-device inference backend (via MediaPipe or llama.cpp) requires changing a single class with zero UI or prompt changes.


## AI Integration Details

**Model**: Gemma 2 2B (via Ollama)

**Integration Mode**: Local hybrid (device UI + local server inference over localhost). Designed for on-device deployment as a future step.

**Prompt Engineering**: The PromptBuilder class constructs structured prompts that include a system role definition, safety constraints, the original message with source context, the rule engine's findings (risk level, scam type, red flags), and a specific output format. Safety rules prevent the model from making absolute claims, writing scam messages, or providing financial/legal advice.

**Safety Handling**:
- Input validation: empty or very short messages are caught before the AI is called
- Prompt-level constraints: the system prompt includes rules against harmful outputs
- Graceful fallback: if the AI fails or times out, the rule-based results remain visible with a clear fallback message
- Careful language: the prompt instructs the model to use phrases like "this message contains high-risk indicators" rather than "this is a scam"

**What data leaves the device**: In the prototype, message text travels to a local Ollama server on the same machine over localhost. No data reaches external servers or the internet. In production, inference would run entirely on-device.

**Offline capability**: The rule engine works fully offline. The AI explanation requires Ollama to be running in the prototype. In a production on-device deployment, the AI would also work offline.


## Setup and Running

### Prerequisites
- Android Studio Panda 1 (2025.3.1 Patch 1) or later
- Android SDK with API 35
- Android Emulator with API 35 or API 36
- Ollama installed on the development machine (for AI features)
- Gemma 2 2B model pulled via Ollama

### Step 1: Clone the repository
```bash
git clone https://github.com/Osaid2993/scamguard-android.git
cd scamguard-android
```

### Step 2: Open in Android Studio
Open Android Studio, select "Open an Existing Project", and navigate to the cloned directory.

### Step 3: Sync Gradle
Click "Sync Now" when prompted. Wait for all dependencies to download.

### Step 4: Set up the AI model
```bash
# Install Ollama (macOS)
brew install ollama

# Or download from https://ollama.com

# Pull the Gemma 2 2B model
ollama pull gemma2:2b

# Start Ollama (keep this running while testing)
ollama serve
```

### Step 5: Configure the emulator
Create an emulator with:
- Device: Pixel 4 or similar
- System image: API 35 (Android 15) or API 36 (Android 16)
- RAM: 2048 MB minimum

### Step 6: Run the app
Select your emulator and click Run. The app will launch with the animated splash screen.

**Note**: Ensure Ollama is running before scanning messages. Without Ollama, the rule-based analysis still works but the AI Explanation card will show a fallback message.


## Testing

The prototype has been tested on:
- **API 35 emulator** (Android 15) — full functionality verified
- **API 36 emulator** (Android 16) — full functionality verified, predictive back navigation confirmed working via finish()
  
*Note: Weekly tutor discussion notes are included inside progress_log.md.*

### Android 16 Back Navigation
The app uses `finish()` for all back navigation, which is fully compatible with Android 16's predictive back API. No opt-out or custom back handling is required.


## Project Structure

```
app/src/main/
├── java/com/osaid/scamguard/
│   ├── MainActivity.java              # Main scan screen
│   ├── SplashActivity.java            # Animated splash
│   ├── ScanningActivity.java          # Scanning animation
│   ├── ResultsActivity.java           # Analysis results
│   ├── ScamLibraryActivity.java       # Scam pattern library
│   ├── HistoryActivity.java           # Scan history dashboard
│   ├── HistoryAdapter.java            # RecyclerView adapter for history
│   ├── ScanDetailDialog.java          # Modal detail card
│   ├── CharCountRing.java             # Custom circular char counter
│   ├── ScanRecord.java                # Room entity
│   ├── ScanRecordDao.java             # Room DAO
│   ├── ScamGuardDatabase.java         # Room database singleton
│   ├── PromptBuilder.java             # Structured AI prompt builder
│   ├── ScamAnalyser.java              # AI inference interface
│   └── OllamaAnalyser.java           # Ollama HTTP implementation
├── res/
│   ├── layout/                        # Screen layouts
│   ├── drawable/                      # Icons, shapes, backgrounds
│   ├── values/colors.xml              # Full colour palette
│   └── values/themes.xml             # Material 3 dark theme
└── AndroidManifest.xml
```


## Key Decisions and Trade-offs

| Decision | Rationale |
|----------|-----------|
| Dark theme only | Consistent visual identity for a security app. Light theme support planned as future work. |
| Gemma 2 via Ollama instead of on-device Llama 3.2 | MediaPipe LLM Inference requires a physical device with GPU. Emulator lacks GPU acceleration. Local hybrid via Ollama achieves identical AI functionality. Architecture supports single-class swap to on-device. |
| Rule engine + AI dual architecture | Rules provide instant, reliable, deterministic detection. AI adds contextual explanation. If AI fails, rules still work. This is more robust than pure-AI. |
| Room for scan history | Familiar from prior coursework (iStream app). Provides typed queries, migration support, and lifecycle-safe async access. |
| SpannableString for phrase highlighting | Allows per-word styling within a single TextView without needing a WebView or custom text layout. |
| No onboarding screens | The app is self-explanatory. The guided input flow, trust stats bar, and clear button labels remove the need for a tutorial. |


## Future Work

- **On-device inference**: Migrate from Ollama to MediaPipe or llama.cpp when a physical device with GPU acceleration is available
- **Light theme support**: Add a toggle for users who prefer light mode
- **Scan history analytics**: Weekly/monthly scan trends and threat exposure scoring
- **Notification integration**: Alert users about new scam patterns
- **Accessibility audit**: Full TalkBack support and WCAG contrast compliance
- **Model optimisation**: Quantisation and caching for faster on-device inference
- **Scamwatch API integration**: Submit reports directly from the app
- **Multi-language support**: Extend beyond English for Australia's multilingual population


## Dependencies

```gradle
implementation 'com.google.android.material:material:1.12.0'
implementation 'androidx.core:core-splashscreen:1.0.1'
implementation 'androidx.appcompat:appcompat:1.7.0'
implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
implementation 'androidx.recyclerview:recyclerview:1.3.2'
implementation 'androidx.cardview:cardview:1.0.0'
implementation 'androidx.room:room-runtime:2.6.1'
annotationProcessor 'androidx.room:room-compiler:2.6.1'
```


## Author

Osaid Bahabri
Deakin University — SIT305 Mobile Application Development
Trimester 1, 2026
