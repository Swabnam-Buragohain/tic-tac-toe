# ⚡ NEO TIC-TAC-TOE // Quantum Cyber Arena

[![Java Version](https://img.shields.io/badge/Java-21%20LTS-orange.svg?style=flat-square&logo=openjdk)](https://openjdk.org/)
[![JavaFX](https://img.shields.io/badge/JavaFX-13%2B-blue.svg?style=flat-square&logo=oracle)](https://openjfx.io/)
[![Maven](https://img.shields.io/badge/Build-Maven-C71A36.svg?style=flat-square&logo=apache-maven)](https://maven.apache.org/)
[![Platform](https://img.shields.io/badge/Platform-Linux%20%7C%20Windows%20%7C%20macOS-brightgreen.svg?style=flat-square)]()
[![License](https://img.shields.io/badge/License-MIT-lightgrey.svg?style=flat-square)](LICENSE)

A high-budget, cybernetic production remake of the classic Tic-Tac-Toe game. Built using pure **JavaFX** with extruded 2.5D physical depth, dynamic glowing lighting, procedural cyber audio synthesis, and an unbeatable **Minimax AI** engine.

---

## 🌟 Key Features

### 🎮 Game Modes
- **⚔️ 2 Players (Local)**: Classic head-to-head turn-based duel on the same machine.
- **🤖 Player vs CPU**: Challenge an adaptive artificial intelligence.

### 🧠 3 AI Difficulty Levels
- **Easy**: Randomized moves for relaxed, casual play.
- **Medium**: Strategically takes winning opportunities, blocks immediate player threats, and contests center board control.
- **Unbeatable (Minimax)**: Pure game-theory optimal decision-making. Evaluates the full decision tree recursively—impossible to defeat (at best a draw).

### 🎨 Visuals & High-Budget Feel
- **Extruded 2.5D Physical Depth**: Multi-layered beveled obsidian blocks with drop-lip insets and dynamic shadows.
- **3D Elevation on Hover**: Empty tiles physically lift upward (`-6px`) with an electric-cyan specular border.
- **Spring Pop Placement**: Placing marks triggers a punchy scale-and-rotation pop effect.
- **Neon Protocol Styling**: High-contrast glowing Crimson Ruby (`#ff2a55`) for **X** and Electric Cyan (`#00f0ff`) for **O**.
- **Victory Celebration**: Winning lines illuminate in pulsating emerald green with oscillating piece bounce animations.
- **Ambient Floating Arena**: The underlying cybernetic platform gently bobs with subtle idle breathing animation.

### 🔊 Procedural Audio Engine
- **Zero Asset Dependencies**: Real-time sound synthesis using standard Java `javax.sound.sampled`.
- Distinct audio cues for:
  - Laser strike on **X** placement.
  - Plasma swell on **O** placement.
  - Subtle cyber blips on tile hover.
  - Celebratory multi-note arpeggio upon victory.
  - Strategic descending chords for stalemates.
- Dedicated **SFX ON / MUTED** toggle in the top HUD.

### 🖥️ Fullscreen & Cyber HUD
- **Automatic Fullscreen Launch**: Launches in full immersion with dynamic resolution adaptation.
- **Quick Controls**: Toggle between Fullscreen and Windowed mode with a click or by pressing <kbd>ESC</kbd>.
- **Live Scorecards**: Side cards for **Player X** and **Player O / CPU** pulse with active neon borders during that player's turn.
- **In-Engine Victory Banner**: Holographic modal card for instant rematches (`Play Again` / `Inspect Arena`) with zero window termination.

---

## 📋 Prerequisites

Ensure you have the following installed on your system:

- **Java JDK 11** or higher (Recommended: **JDK 21 LTS**)
- **Apache Maven 3.8+**

Check your versions:
```bash
java -version
mvn -version
```

---

## 🚀 Getting Started

### 1. Clone the Repository
```bash
git clone https://github.com/<your-username>/tic-tac-toe.git
cd tic-tac-toe
```

### 2. Build the Project
Compile and download dependencies using Maven:
```bash
mvn clean compile
```

### 3. Launch the Game
Run the JavaFX application directly:
```bash
mvn javafx:run
```

> **Tip (Specifying JDK)**: If your default system `java` is different from your JDK 21 installation:
> ```bash
> JAVA_HOME=/path/to/jdk-21 mvn javafx:run
> ```

---

## 🕹️ Controls & Shortcuts

| Action | Control |
|---|---|
| **Place Mark** | Left Click on any empty tile |
| **Toggle Fullscreen** | Click **⛶ FULLSCREEN** button or press <kbd>ESC</kbd> |
| **New Match** | Click **⚡ NEW MATCH** |
| **Reset Scores** | Click **↺ RESET STATS** |
| **Toggle Audio** | Click **🔊 SFX ON** / **🔇 MUTED** |
| **Switch Mode** | Click **⚔️ 2 PLAYERS** or **🤖 VS CPU** |
| **Change Difficulty** | Click **EASY**, **MEDIUM**, or **UNBEATABLE** (when in VS CPU mode) |

---

## 📂 Project Architecture

```
tic-tac-toe/
├── pom.xml                                      # Maven build and plugin configuration
├── .gitignore                                   # Git exclusion rules
├── README.md                                    # Project documentation
└── src/
    └── main/
        └── java/
            ├── module-info.java                 # Java module descriptor
            └── com/pqca/user/tic/tac/toe/
                ├── App.java                     # Main UI, Cyber HUD, and game state controller
                ├── CpuPlayer.java               # AI decision algorithms (Random, Heuristic, Minimax)
                ├── SoundEngine.java             # Procedural real-time audio synthesizer
                └── SystemInfo.java              # Java & JavaFX runtime diagnostic helper
```

---

## 🛠️ Built With

- **[JavaFX 13](https://openjfx.io/)** - Modern desktop UI controls, animations, and scene graph.
- **[Java Sound API (`javax.sound.sampled`)](https://docs.oracle.com/en/java/javase/21/docs/api/java.desktop/javax/sound/sampled/package-summary.html)** - Waveform generation & ADSR audio envelope synthesis.
- **[Maven](https://maven.apache.org/)** - Dependency management and build automation.

---

## 📄 License

This project is open-source and available under the [MIT License](LICENSE).
