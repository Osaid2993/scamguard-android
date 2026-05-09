# Key Decisions

- App name: ScamGuard
- Primary language: Java
- AI model: Gemma 2B via Ollama (local hybrid)
- AI integration: Dual-engine architecture (rule engine for detection, Gemma for explanation)
- Database: Room (scan history with full message and red flags)
- Theme: Dark teal, Material 3, single theme (no light mode)
- targetSdk / compileSdk: API 35
- Tested on: API 35 (Android 15) and API 36 (Android 16)
- Back navigation: System predictive back via finish(), no custom handling needed

## AI Decisions
- Chose Gemma 2B over Llama 3.2 for better stability via Ollama
- Hybrid approach chosen because MediaPipe LLM Inference requires physical device GPU
- Architecture designed for easy swap to on-device inference (ScamAnalyser interface)
- Prompt includes safety constraints: no absolute statements, no scam writing, Australian resources

## UI/UX Decisions
- Dark theme chosen as a deliberate brand identity for a security app
- Custom views: CharCountRing, SpannableString highlighting, animated confidence bar
- Scanning animation with dual rings and pulsing shield to communicate real processing
- Sticky headers on Results, History, and Scam Library screens
- Dynamic scan button: muted when empty, active when text is present
- Modal detail card for history items instead of a new screen

## Screens
- Splash (animated shield + staggered text)
- Main (source selector, suspicion tags, message input, trust stats)
- Scanning (dual rings, step indicators, progress pills)
- Results (risk verdict, highlighted message, red flags, AI explanation, confidence bar, Share/Report/New Scan)
- Scam Library (six scam types, search bar, Try Example loop)
- History (filter chips, summary cards, modal detail view, Room database)