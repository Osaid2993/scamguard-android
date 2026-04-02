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