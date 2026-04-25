# Progress Log

## 18 March 2026
- Created Android Studio project for ScamGuard.
- Confirmed app concept after tutor discussion.
- Decided the app will use guided input, not just a text box.
- Planned main UI features: source selector, suspicion tags, text input, paste button, try example, and analyse button.
- Added initial main screen layout.
- Connected local Git project to public GitHub repository.
- Pushed first two commits successfully.
- Verified version history and remote origin.
- Included title, source selector, message input area, and analyse button.

### Next step
Add suspicion tags and helper actions to the main screen.

## 19 March 2026
- Committed and pushed the updated main UI to GitHub with the commit: "Ad## 20 March 2026
- Created the results screen layout for ScamGuard.
- Added sections for risk level, scam type, red flags, and safe action guidance.
- Added ResultsActivity and registered it in the Android manifest.

### Next step
Connect navigation from the main screen to the results screen.d suspicion tags and helper actions to main screen".
- Verified the project history and remote repository.
- Prepared for the next stage: building the results screen with risk level, scam type, red flags, and safe action guidance.

## 23 March 2026
- Created the results screen layout for ScamGuard.
- Added sections for risk level, scam type, red flags, and safe action guidance.
- Added ResultsActivity and registered it in the Android manifest.
- Connected navigation from the main screen to the results screen.
- Added back button functionality to return to the main screen.

### Next step
make the results dynamic instead of hardcoded.

## 24 March 2026
- Made the results screen dynamic instead of hardcoded.
- Passed the user message from the main screen to the results screen.
- Added simple rule-based scam analysis for risk level, scam type, red flags, and safe guidance.

### Next step
Add helper actions such as Paste from Clipboard and Try Example.

- Added Paste from Clipboard action to the main screen.
- Added Try Example action with a sample scam message.
- Improved the guided input flow before analysis.
- Passed the selected message source and suspicion tags to the results screen.
- Added source and selected concerns sections to the results UI.
- Improved the rule-based result by using user-selected context.

## 29 March 2026
- Added a safety disclaimer to the results screen.
- Polished the results UI with cleaner card-style sections.
- Improved visual clarity by styling the risk level output.

### Next step
Strengthen the rule-based analysis by detecting more scam patterns and improving the result quality before integrating the AI flow.

## 2 April 2026
- Improved the rule-based scam analysis with more scam indicators and categories.
- Added better detection for links, urgency, money requests, OTP requests, delivery scams, bank scams, government scams, prize scams, and marketplace scams.
- Improved scam type selection and safe guidance based on the detected pattern.

### Next step
Add more context to the rule-based logic or begin preparing the AI flow.

## 16 April 2026

- Started major UI overhaul to lift the app toward HD-level visual quality.
- Introduced a dark teal visual identity across the app using a full colour palette in colors.xml, covering surfaces, accents, text tiers, and four risk levels (high, medium, low, minimal) with matched foreground and background pairs.
- Rewrote themes.xml to use Material 3 dark theme with teal as the primary accent. Also added a Theme.ScamGuard.Splash variant using the Android 12+ SplashScreen API for a branded app launch.
- Updated build.gradle to pull in Material 3 (1.12.0) and the AndroidX core-splashscreen library, and confirmed compileSdk and targetSdk are set to 35 to meet the task requirement.
- Built an icon system using vector drawables: ic_shield and ic_shield_splash for app branding, ic_alert (red) for red flags, ic_check (green) for safe guidance, ic_info (amber) for disclaimers, ic_source and ic_tag (teal) for neutral context on the results screen, ic_scan for the Analyse button, ic_paste and ic_bulb for secondary actions, and ic_arrow_back for top-bar navigation.
- Built a shape drawable system for visual consistency: bg_button_primary (filled teal pill), bg_button_secondary (dark outlined pill), bg_card (card surface with outline), bg_input (rounded text input), and four risk badge backgrounds (bg_risk_high, bg_risk_medium, bg_risk_low, bg_risk_minimal).
- Chose a unified corner radius system: 14dp for interactive elements (buttons, inputs) and 18dp for surfaces (cards, risk badges). This creates a deliberate hierarchy where surfaces feel softer than interactive elements.
- Kept the existing rule engine logic untouched. The redesign is purely a presentation layer change.

## Discussing 
- Discussed ScamGuard progress with Swathi during the weekly HD task discussion.
- Updated her on the current prototype, including the working guided input flow, results screen, and rule-based analysis.
- Received positive feedback on the amount of progress completed so far.
- Swathi advised that the priority should be completing the app functionality to HD level first, and then focusing on the AI integration.
- Discussed the goal of making the app strong enough to be worth including in my CV.

### Next step
Redesign activity_main.xml using the new theme system, then redesign activity_results.xml with the risk badge hero, followed by the Scam Library screen and splash wiring.

## 16 April 2026 (continued)

- Redesigned activity_main.xml with the dark teal theme. Added a branded header with shield icon and tagline, wrapped source radios in a card, switched chips to Material 3 filter style, replaced TextInputLayout with a rounded EditText, and paired two outlined secondary buttons with a filled teal primary Analyse button.
- Updated MainActivity.java to rotate through five realistic scam examples instead of a single hardcoded one, and added an empty-message check before navigating to results.
- Redesigned activity_results.xml as a layered analysis report: top bar with circular back button, a hero Risk Verdict badge with large coloured text, and four icon-led cards (red alert, green check, teal envelope, teal tag) plus an amber disclaimer panel.
- Updated ResultsActivity.java to swap the risk badge background drawable dynamically based on risk level, and moved all colours from inline hex to ContextCompat references against colors.xml. Rule engine left untouched.
- Fixed a rendering issue where Material 3 was auto-promoting custom Buttons to MaterialButton and wiping their icons and backgrounds. Switched the three custom buttons to TextView with clickable and focusable set to true, and updated MainActivity.java to match.

### Next step
Build the splash screen, then the Scam Library screen.

## 17 April 2026

- Verified Android 16 predictive back compatibility on the Results screen. The back button uses finish() which is fully supported by the system's predictive back API by default, no opt-out needed. Will be documented on the presentation's compatibility slide.
- Built SplashActivity with the Android 12+ SplashScreen API, keeping the shield on the dark base for a short moment before fading into MainActivity. Updated the manifest to make SplashActivity the launcher.

### Next step
Add a subtle fade and scale animation to the splash to lift the branded moment, then build the Scam Library screen.

- Replaced the static system splash with a custom animated splash screen. Shield scales up from 70% with a fade-in over 600ms using a decelerate curve, followed by the app name fading in at 300ms and the tagline at 500ms. After a 1600ms hold, the screen cross-fades into the main activity.

### Next step
Add original message card to the results screen.

- Added an Original Message card to the results screen using monospace font to visually separate the raw pasted message from the analysis text.
- Built suspicious phrase highlighting using SpannableString. The app now marks matched keywords (urgency, links, bank language, OTP requests, etc.) in bold red inside the original message so the user can see exactly what triggered the detection.

### Next step
Build the Scam Library screen and add a Learn button to the main screen header.

- Added a Learn button to the main screen header that opens the new Scam Library screen.
- Built the Scam Library screen with six scam pattern cards (bank impersonation, fake delivery, government/tax, prize/reward, OTP, marketplace). Each card includes a short description and a Try Example button.
- Try Example sends a realistic scam message back to the main screen via Intent, pre-filling the text input so the user can immediately tap Analyse and see the detection in action.
- Fixed a back-stack issue where the pre-filled message was not appearing because MainActivity was being reused. Added onNewIntent to handle intents when the activity already exists.

### Next step
Add an AI Explanation placeholder card to the results screen, then begin Gemma on-device integration.

## 18 April 2026 

- Added a Learn button to the main screen header that opens the Scam Library.
- Built the Scam Library screen with six scam pattern cards, each with a description and a Try Example button that sends a realistic scam message back to the main screen via Intent.
- Added an AI Explanation placeholder card to the results screen with a hidden loading spinner, ready for Gemma integration.
- Built a scan history feature using Room database. Each analysis is saved automatically with a message snippet, risk level, scam type, source, and timestamp. Added a History screen accessible via a clock icon in the main screen header, showing past scans in a RecyclerView sorted newest first. Included a Clear button to wipe history and an empty state message when no scans exist.

## 19 April 2026

- Added three sticky action buttons to the bottom of the results screen: Share (opens Android share sheet with a text summary of the analysis), Report (opens Scamwatch report page in the browser), and New Scan (returns to the main screen). Buttons sit outside the ScrollView so they stay visible while scrolling.
- Created ic_share, ic_report, and ic_new_scan vector drawables for the button icons.

### Next step
Add a confidence bar to the results screen, then the scanning animation.

- Added an animated confidence bar inside the risk verdict badge. The bar calculates a confidence percentage from the rule engine's risk score, then animates from zero to the target width over one second with a decelerate curve. The fill colour matches the risk level (red, amber, blue, green) and sits on a dark navy track.

### Next step
Add a scanning animation screen between the main screen and the results screen.

## 20 April 2026

- Built a scanning animation screen that plays between the main screen and the results screen. Shows a teal spinner, cycling step text (Reading message, Checking patterns, Analysing risk, Preparing results), and progress dots that light up sequentially. The screen passes all intent data through to ResultsActivity and fades out after 2.2 seconds. This gives the impression of real processing and makes the app feel alive.
- Added the clear button and added a live character counter inside the message input box, separated by a thin divider line matching the app's outline style. Renamed the primary button from "Analyse for Scams" to "Scan Message" to match the new scanning animation flow.

### Next step
Continue refining the UI and UX across all screens.

- Added four risk-specific icons to the verdict badge: shield-X for High, warning triangle for Medium, info circle for Low, and shield-check for Minimal. Each icon matches the risk colour and swaps dynamically based on the analysis result.
- Centred all text and the icon inside the risk verdict badge for a cleaner, more impactful layout.
- Added a trust stats bar to the main screen showing "1.2M Scams Caught", "98.7% Accuracy", and "<1s Scan Time" as prototype-level design elements to build user confidence.
- Updated the tagline below the app name to "Paste any suspicious message below. We'll analyse it instantly." for a clearer call to action.

### Next step
Continue refining the UI and UX across all screens.

## 21 April 2026 

- Enhanced the scanning screen with dual concentric rings using sweep gradients that spin in opposite directions (outer clockwise at 0.9s, inner counter-clockwise at 1.4s), creating a radar/sonar feel. Added the shield icon at the centre with a fade-in and continuous pulse animation. Updated step text to four analysis stages: reading message structure, scanning for malicious URLs, analysing tone and tactics, running AI verdict. Progress dots now widen into pills as each step completes.
- Added scan button activation effect: button starts muted/dim when no text is entered and transitions to bright teal when the user types or pastes a message.
- Added risk-specific icons to the verdict badge (shield-X for High, triangle for Medium, info circle for Low, shield-check for Minimal) and centred all badge content.
- Added trust stats bar to the main screen with scams caught, accuracy, and scan time.
- Made clear button only visible when text is present.
- Made headers sticky on results and Scam Library screens.

### Next step
Continue refining the UI and UX across all screens.

- Added a bounce-in entry animation and 3-pulse effect on the risk verdict icon so the verdict feels like it's landing with impact.
- Added thin divider lines between the source selector radio buttons for visual separation.

### Next step
Continue refining the UI and UX across all screens.

- Styled the suspicion tag chips to match the dark teal theme. Unselected chips show transparent fill with a subtle outline and grey text. Selected chips transition to dim teal fill, bright teal border, bright teal text, and a teal checkmark. Handled in Java via setOnCheckedChangeListener because Material 3 Chip overrides XML colour attributes.

### Next step
Build History screen filter chips and summary cards.

## 22 April 2026 

- Added risk-specific icons (shield-X, triangle, info circle, shield-check) to each history item alongside coloured risk level badges that match the results screen's visual language.
- Switched history timestamps from full date format to relative time ("Just now", "3 min ago", "Yesterday", "2 weeks ago") for quicker scanning.
- Updated filter chips to change colour based on risk tier when selected: red for High, amber for Medium, blue for Low, green for Minimal, teal for All.

### Next step
Continue refining the UI and UX, await tutor confirmation on AI approach.

- Updated Room entity (ScanRecord) to store the full message and red flags alongside the existing snippet, and bumped the database version with destructive migration.
- Built a modal detail card (ScanDetailDialog) that appears when a history item is tapped. The modal centres on screen with a dimmed background, showing the full message in monospace, a risk-coloured accent bar, risk icon, verdict badge, confidence percentage with animated bar, red flags with count, and Share/Report/Close action buttons. Tap outside or Close to dismiss.
- Added a "View details" pill button to each history card to indicate tappability.

### Next step
Continue refining the UI and UX, await tutor confirmation on AI approach.

## 23 April 2026

- Added a search bar to the Scam Library screen with live filtering by scam type tags.
- Added a muted X clear button in the search bar that appears when typing and hides when empty. Added "No results for '...'" message when search matches nothing.
- Made the scan button dynamic: shows "Paste a message to scan" in muted state when empty, switches to "Scan with AI" on bright teal when text is present.
- Replaced the text-based character counter with a custom circular ring view. The arc fills smoothly as the user types, turns amber at 70% and red at 90% of the 500-char limit.

### Next step
Continue refining UI and UX, await tutor confirmation on AI approach.

## 23 April 2026 (continued)

- Discussed current ScamGuard progress with Swathi.
- Presented the updated prototype, including the redesigned UI/UX, scam library, scan history, and AI explanation flow.
- Received very positive feedback on the amount and quality of progress.
- Discussed the AI deployment constraint and proposed local hybrid setup as a practical prototype solution.
- Received confirmation that a local hybrid Gemma setup is acceptable for the prototype.
- Was advised to explain this clearly in the final presentation.

## 26 April 2026

- Built the AI integration pipeline: PromptBuilder for structured prompts with safety constraints, ScamAnalyser interface for swappable backends, and OllamaAnalyser calling Gemma 2B via localhost.
- Wired AI into the results screen with loading state and graceful fallback if the AI fails.
- Added INTERNET permission and cleartext traffic flag for the localhost connection.

### Next step
Test AI across all scam types and tighten prompt.