package com.tictactoe;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class App extends Application {

    public enum GameMode {
        PVP("2 PLAYERS"),
        PV_CPU("VS CPU");

        private final String label;

        GameMode(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    // Pre-allocated winning combinations (Zero-allocation checkWin optimization)
    private static final int[][][] WIN_COMBINATIONS = {
            // Rows
            {{0, 0}, {0, 1}, {0, 2}},
            {{1, 0}, {1, 1}, {1, 2}},
            {{2, 0}, {2, 1}, {2, 2}},
            // Columns
            {{0, 0}, {1, 0}, {2, 0}},
            {{0, 1}, {1, 1}, {2, 1}},
            {{0, 2}, {1, 2}, {2, 2}},
            // Diagonals
            {{0, 0}, {1, 1}, {2, 2}},
            {{0, 2}, {1, 1}, {2, 0}}
    };

    // 3D Tile Button Styling (extruded beveled obsidian block)
    private static final String TILE_IDLE_STYLE =
            "-fx-background-color: " +
            "    #050811, " +
            "    linear-gradient(to bottom, #1e293b, #090e1a), " +
            "    linear-gradient(to bottom, #111827 0%, #0d1322 100%); " +
            "-fx-background-insets: 0, 0 0 6 0, 1 1 7 1; " +
            "-fx-background-radius: 18px; " +
            "-fx-border-color: rgba(56, 189, 248, 0.25); " +
            "-fx-border-radius: 18px; " +
            "-fx-border-width: 1.5px; " +
            "-fx-cursor: hand; " +
            "-fx-padding: 0; " +
            "-fx-alignment: center; " +
            "-fx-font-family: 'Segoe UI', 'DejaVu Sans', sans-serif; " +
            "-fx-font-size: 64px; " +
            "-fx-font-weight: 900; " +
            "-fx-text-fill: #ffffff;";

    private static final String TILE_HOVER_STYLE =
            "-fx-background-color: " +
            "    #050811, " +
            "    linear-gradient(to bottom, #0284c7, #0369a1), " +
            "    linear-gradient(to bottom, #1e293b 0%, #0f172a 100%); " +
            "-fx-background-insets: 0, 0 0 8 0, 1 1 9 1; " +
            "-fx-background-radius: 18px; " +
            "-fx-border-color: #38bdf8; " +
            "-fx-border-radius: 18px; " +
            "-fx-border-width: 2px; " +
            "-fx-cursor: hand; " +
            "-fx-padding: 0; " +
            "-fx-alignment: center; " +
            "-fx-font-family: 'Segoe UI', 'DejaVu Sans', sans-serif; " +
            "-fx-font-size: 64px; " +
            "-fx-font-weight: 900; " +
            "-fx-text-fill: #ffffff;";

    private static final String TILE_X_STYLE =
            "-fx-background-color: " +
            "    #050811, " +
            "    linear-gradient(to bottom, #991b1b, #450a0a), " +
            "    linear-gradient(to bottom, #1e1115 0%, #130a0d 100%); " +
            "-fx-background-insets: 0, 0 0 6 0, 1 1 7 1; " +
            "-fx-background-radius: 18px; " +
            "-fx-border-color: #ef4444; " +
            "-fx-border-radius: 18px; " +
            "-fx-border-width: 1.8px; " +
            "-fx-padding: 0; " +
            "-fx-alignment: center; " +
            "-fx-font-family: 'Segoe UI', 'DejaVu Sans', sans-serif; " +
            "-fx-font-size: 64px; " +
            "-fx-font-weight: 900; " +
            "-fx-text-fill: #ff2a55;";

    private static final String TILE_O_STYLE =
            "-fx-background-color: " +
            "    #050811, " +
            "    linear-gradient(to bottom, #0369a1, #082f49), " +
            "    linear-gradient(to bottom, #0a1726 0%, #06101c 100%); " +
            "-fx-background-insets: 0, 0 0 6 0, 1 1 7 1; " +
            "-fx-background-radius: 18px; " +
            "-fx-border-color: #00e5ff; " +
            "-fx-border-radius: 18px; " +
            "-fx-border-width: 1.8px; " +
            "-fx-padding: 0; " +
            "-fx-alignment: center; " +
            "-fx-font-family: 'Segoe UI', 'DejaVu Sans', sans-serif; " +
            "-fx-font-size: 64px; " +
            "-fx-font-weight: 900; " +
            "-fx-text-fill: #00f0ff;";

    private static final String TILE_WIN_STYLE =
            "-fx-background-color: " +
            "    #022c22, " +
            "    linear-gradient(to bottom, #22c55e, #15803d), " +
            "    linear-gradient(to bottom, #166534 0%, #052e16 100%); " +
            "-fx-background-insets: 0, 0 0 7 0, 1 1 8 1; " +
            "-fx-background-radius: 18px; " +
            "-fx-border-color: #4ade80; " +
            "-fx-border-radius: 18px; " +
            "-fx-border-width: 2.5px; " +
            "-fx-padding: 0; " +
            "-fx-alignment: center; " +
            "-fx-font-family: 'Segoe UI', 'DejaVu Sans', sans-serif; " +
            "-fx-font-size: 64px; " +
            "-fx-font-weight: 900; " +
            "-fx-text-fill: #ffffff;";

    // Game State
    private final char[][] board = new char[3][3];
    private final Button[][] buttons = new Button[3][3];
    private final List<Animation> activeAnimations = new ArrayList<>();

    private char currentPlayer = 'X';
    private boolean gameOver = false;
    private boolean cpuThinking = false;

    private GameMode currentMode = GameMode.PV_CPU;
    private CpuPlayer.Difficulty currentDifficulty = CpuPlayer.Difficulty.MEDIUM;

    private int scoreX = 0;
    private int scoreO = 0;
    private int scoreDraw = 0;

    // UI Elements
    private Label statusBadge;
    private VBox playerXCard;
    private VBox playerOCard;
    private Label scoreLabelX;
    private Label scoreLabelO;
    private Label scoreLabelDraw;
    private Button pvpBtn;
    private Button cpuBtn;
    private Button easyBtn;
    private Button medBtn;
    private Button hardBtn;
    private Button soundBtn;
    private Button fullscreenBtn;
    private HBox diffBox;
    private StackPane modalOverlay;
    private Label modalTitle;
    private Label modalSubtitle;
    private Stage stage;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;

        // Root container with deep cyber space gradient
        StackPane root = new StackPane();
        RadialGradient spaceGrad = new RadialGradient(
                0, 0, 0.5, 0.45, 0.85, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#0e172e")),
                new Stop(0.5, Color.web("#060a14")),
                new Stop(1, Color.web("#020409"))
        );
        root.setBackground(new Background(new BackgroundFill(spaceGrad, CornerRadii.EMPTY, Insets.EMPTY)));

        // Main Layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(20, 36, 20, 36));

        // 1. Top HUD Header
        mainLayout.setTop(buildTopHUD());

        // 2. Center 3D Cyber Arena
        mainLayout.setCenter(buildCenterArena());

        // 3. Side Scorecards
        playerXCard = buildPlayerCard("PLAYER X", "CRIMSON PROTOCOL", "#ef4444");
        scoreLabelX = (Label) playerXCard.lookup("#scoreLabel");

        playerOCard = buildPlayerCard("CPU (AI)", "CYAN PROTOCOL", "#00e5ff");
        scoreLabelO = (Label) playerOCard.lookup("#scoreLabel");

        VBox leftWrapper = new VBox(playerXCard);
        leftWrapper.setAlignment(Pos.CENTER);
        mainLayout.setLeft(leftWrapper);

        VBox rightWrapper = new VBox(playerOCard);
        rightWrapper.setAlignment(Pos.CENTER);
        mainLayout.setRight(rightWrapper);

        // 4. Bottom Controls Bar
        mainLayout.setBottom(buildBottomHUD());

        // 5. Victory Holographic Modal Overlay
        modalOverlay = buildModalOverlay();

        root.getChildren().addAll(mainLayout, modalOverlay);

        Scene scene = new Scene(root, 1280, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("NEO TIC-TAC-TOE // Cyber Arena");
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.valueOf("ESCAPE"));
        primaryStage.setFullScreenExitHint("ESC or Fullscreen Button to Toggle");
        primaryStage.setFullScreen(true);

        primaryStage.fullScreenProperty().addListener((obs, oldVal, isFull) -> {
            fullscreenBtn.setText(isFull ? "🗗 WINDOWED" : "⛶ FULLSCREEN");
        });

        // Initialize board
        resetBoard();

        primaryStage.show();
    }

    private VBox buildTopHUD() {
        VBox topBox = new VBox(14);
        topBox.setAlignment(Pos.CENTER);

        BorderPane headerBar = new BorderPane();

        // Left Logo
        VBox logoBox = new VBox(2);
        Label title = new Label("NEO // TIC-TAC-TOE");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: 900; -fx-text-fill: #ffffff; -fx-letter-spacing: 2px;");
        title.setEffect(new DropShadow(18, Color.web("#38bdf8")));

        Label subtitle = new Label("CYBERNETIC ARENA // HD FULLSCREEN");
        subtitle.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #0284c7; -fx-letter-spacing: 1.5px;");
        logoBox.getChildren().addAll(title, subtitle);
        headerBar.setLeft(logoBox);

        // Center Controls (Mode & Difficulty Switcher)
        HBox centerControls = new HBox(12);
        centerControls.setAlignment(Pos.CENTER);

        HBox modeSwitcher = new HBox(4);
        modeSwitcher.setStyle("-fx-background-color: rgba(15, 23, 42, 0.85); -fx-padding: 4px; -fx-background-radius: 24px; -fx-border-color: rgba(56, 189, 248, 0.35); -fx-border-radius: 24px;");

        pvpBtn = createPillButton("⚔️ 2 PLAYERS", currentMode == GameMode.PVP);
        cpuBtn = createPillButton("🤖 VS CPU", currentMode == GameMode.PV_CPU);

        pvpBtn.setOnAction(e -> setMode(GameMode.PVP));
        cpuBtn.setOnAction(e -> setMode(GameMode.PV_CPU));
        modeSwitcher.getChildren().addAll(pvpBtn, cpuBtn);

        diffBox = new HBox(4);
        diffBox.setStyle("-fx-background-color: rgba(15, 23, 42, 0.85); -fx-padding: 4px; -fx-background-radius: 24px; -fx-border-color: rgba(56, 189, 248, 0.35); -fx-border-radius: 24px;");

        easyBtn = createPillButton("EASY", currentDifficulty == CpuPlayer.Difficulty.EASY);
        medBtn = createPillButton("MEDIUM", currentDifficulty == CpuPlayer.Difficulty.MEDIUM);
        hardBtn = createPillButton("UNBEATABLE", currentDifficulty == CpuPlayer.Difficulty.HARD);

        easyBtn.setOnAction(e -> setDifficulty(CpuPlayer.Difficulty.EASY));
        medBtn.setOnAction(e -> setDifficulty(CpuPlayer.Difficulty.MEDIUM));
        hardBtn.setOnAction(e -> setDifficulty(CpuPlayer.Difficulty.HARD));

        diffBox.getChildren().addAll(easyBtn, medBtn, hardBtn);
        diffBox.setVisible(currentMode == GameMode.PV_CPU);
        diffBox.setManaged(currentMode == GameMode.PV_CPU);

        centerControls.getChildren().addAll(modeSwitcher, diffBox);
        headerBar.setCenter(centerControls);

        // Right Controls
        HBox rightControls = new HBox(10);
        rightControls.setAlignment(Pos.CENTER_RIGHT);

        soundBtn = createIconButton("🔊 SFX ON");
        soundBtn.setOnAction(e -> {
            boolean next = !SoundEngine.isSoundEnabled();
            SoundEngine.setSoundEnabled(next);
            soundBtn.setText(next ? "🔊 SFX ON" : "🔇 MUTED");
            SoundEngine.playClick();
        });

        fullscreenBtn = createIconButton("🗗 WINDOWED");
        fullscreenBtn.setOnAction(e -> {
            stage.setFullScreen(!stage.isFullScreen());
            SoundEngine.playClick();
        });

        rightControls.getChildren().addAll(soundBtn, fullscreenBtn);
        headerBar.setRight(rightControls);

        // Floating Turn Status Pill
        statusBadge = new Label("YOUR TURN (X)");
        statusBadge.setStyle(
                "-fx-background-color: rgba(15, 23, 42, 0.9); " +
                "-fx-text-fill: #38bdf8; " +
                "-fx-font-weight: 900; " +
                "-fx-font-size: 15px; " +
                "-fx-padding: 8 28; " +
                "-fx-background-radius: 20px; " +
                "-fx-border-color: rgba(56, 189, 248, 0.6); " +
                "-fx-border-radius: 20px; " +
                "-fx-border-width: 1.5px; " +
                "-fx-letter-spacing: 1px;"
        );
        statusBadge.setEffect(new DropShadow(16, Color.web("#38bdf8")));

        topBox.getChildren().addAll(headerBar, statusBadge);
        return topBox;
    }

    private StackPane buildCenterArena() {
        StackPane arenaWrapper = new StackPane();
        arenaWrapper.setAlignment(Pos.CENTER);

        // Cybernetic Platform Base Deck
        StackPane deck = new StackPane();
        deck.setMaxSize(510, 510);
        deck.setStyle(
                "-fx-background-color: " +
                "    rgba(2, 6, 23, 0.9), " +
                "    linear-gradient(to bottom, #0f172a, #030712), " +
                "    linear-gradient(to bottom, #090d16, #050811); " +
                "-fx-background-insets: 0, 4, 12; " +
                "-fx-background-radius: 28px; " +
                "-fx-border-color: rgba(56, 189, 248, 0.45); " +
                "-fx-border-width: 2px; " +
                "-fx-border-radius: 28px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.9), 36, 0.5, 0, 16);"
        );

        // 3x3 Grid of 3D Extruded Tiles
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(14);
        grid.setVgap(14);
        grid.setPadding(new Insets(24));

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                final int row = r;
                final int col = c;

                Button btn = new Button();
                btn.setPrefSize(130, 130);
                btn.setMinSize(130, 130);
                btn.setMaxSize(130, 130);
                btn.setStyle(TILE_IDLE_STYLE);
                btn.setEffect(new DropShadow(16, Color.web("rgba(0,0,0,0.85)")));

                // Hover 3D elevation
                btn.setOnMouseEntered(e -> {
                    if (!gameOver && !cpuThinking && board[row][col] == ' ') {
                        btn.setStyle(TILE_HOVER_STYLE);
                        btn.setEffect(new DropShadow(22, Color.web("#38bdf8")));
                        TranslateTransition tt = new TranslateTransition(Duration.millis(120), btn);
                        tt.setToY(-6);
                        tt.play();
                        SoundEngine.playHover();
                    }
                });

                btn.setOnMouseExited(e -> {
                    if (board[row][col] == ' ') {
                        btn.setStyle(TILE_IDLE_STYLE);
                        btn.setEffect(new DropShadow(16, Color.web("rgba(0,0,0,0.85)")));
                    }
                    TranslateTransition tt = new TranslateTransition(Duration.millis(120), btn);
                    tt.setToY(0);
                    tt.play();
                });

                btn.setOnAction(e -> {
                    if (!gameOver && !cpuThinking && board[row][col] == ' ') {
                        SoundEngine.playClick();
                        handleCellClick(row, col);
                    }
                });

                buttons[r][c] = btn;
                grid.add(btn, c, r);
            }
        }

        deck.getChildren().add(grid);
        arenaWrapper.getChildren().add(deck);

        // Subtle ambient floating breathing animation on the deck
        Timeline breath = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(deck.translateYProperty(), -3, Interpolator.EASE_BOTH)),
                new KeyFrame(Duration.seconds(3.0), new KeyValue(deck.translateYProperty(), 3, Interpolator.EASE_BOTH))
        );
        breath.setAutoReverse(true);
        breath.setCycleCount(Animation.INDEFINITE);
        breath.play();

        return arenaWrapper;
    }

    private VBox buildPlayerCard(String name, String sub, String hexColor) {
        VBox card = new VBox(6);
        card.setPrefWidth(170);
        card.setStyle(
                "-fx-background-color: rgba(15, 23, 42, 0.75); " +
                "-fx-padding: 18 16; " +
                "-fx-background-radius: 16px; " +
                "-fx-border-color: " + hexColor + "55; " +
                "-fx-border-radius: 16px; " +
                "-fx-border-width: 1.5px;"
        );
        card.setEffect(new DropShadow(14, Color.web(hexColor + "33")));

        Label nameLbl = new Label(name);
        nameLbl.setId("nameLabel");
        nameLbl.setStyle("-fx-font-size: 14px; -fx-font-weight: 900; -fx-text-fill: " + hexColor + "; -fx-letter-spacing: 1px;");

        Label subLbl = new Label(sub);
        subLbl.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: #64748b;");

        Label scoreLbl = new Label("0");
        scoreLbl.setId("scoreLabel");
        scoreLbl.setStyle("-fx-font-size: 38px; -fx-font-weight: 900; -fx-text-fill: #ffffff;");

        card.getChildren().addAll(nameLbl, subLbl, scoreLbl);
        return card;
    }

    private VBox buildBottomHUD() {
        VBox bottomBox = new VBox(10);
        bottomBox.setAlignment(Pos.CENTER);

        HBox actionButtons = new HBox(16);
        actionButtons.setAlignment(Pos.CENTER);

        Button newMatchBtn = new Button("⚡ NEW MATCH");
        newMatchBtn.setStyle(
                "-fx-background-color: linear-gradient(to right, #0284c7, #06b6d4); " +
                "-fx-text-fill: #ffffff; " +
                "-fx-font-size: 13px; " +
                "-fx-font-weight: 900; " +
                "-fx-padding: 10 26; " +
                "-fx-background-radius: 20px; " +
                "-fx-cursor: hand;"
        );
        newMatchBtn.setEffect(new DropShadow(14, Color.web("#06b6d4")));
        newMatchBtn.setOnAction(e -> {
            SoundEngine.playReset();
            resetBoard();
        });

        Button resetScoresBtn = createIconButton("↺ RESET STATS");
        resetScoresBtn.setOnAction(e -> {
            scoreX = 0;
            scoreO = 0;
            scoreDraw = 0;
            updateScoreDisplays();
            SoundEngine.playClick();
        });

        scoreLabelDraw = new Label("DRAWS: 0");
        scoreLabelDraw.setStyle("-fx-font-size: 12px; -fx-font-weight: 800; -fx-text-fill: #94a3b8;");

        actionButtons.getChildren().addAll(newMatchBtn, resetScoresBtn, scoreLabelDraw);

        Label hintText = new Label("⚡ CYBERNETIC ENGINE ACTIVE   •   PRESS ESC TO TOGGLE FULLSCREEN");
        hintText.setStyle("-fx-font-size: 10.5px; -fx-font-weight: bold; -fx-text-fill: #64748b; -fx-letter-spacing: 1px;");

        bottomBox.getChildren().addAll(actionButtons, hintText);
        return bottomBox;
    }

    private StackPane buildModalOverlay() {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(2, 6, 23, 0.78);");
        overlay.setVisible(false);
        overlay.setMouseTransparent(true); // Don't block board when hidden!

        VBox card = new VBox(14);
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(400);
        card.setMaxHeight(270);
        card.setStyle(
                "-fx-background-color: rgba(15, 23, 42, 0.96); " +
                "-fx-padding: 28 32; " +
                "-fx-background-radius: 22px; " +
                "-fx-border-color: #38bdf8; " +
                "-fx-border-width: 2px; " +
                "-fx-border-radius: 22px;"
        );
        card.setEffect(new DropShadow(34, Color.web("#0284c7")));

        modalTitle = new Label("VICTORY ACHIEVED");
        modalTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: 900; -fx-text-fill: #ffffff; -fx-letter-spacing: 2px;");

        modalSubtitle = new Label("Player X has conquered the arena!");
        modalSubtitle.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #94a3b8;");

        HBox btnBox = new HBox(12);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.setPadding(new Insets(10, 0, 0, 0));

        Button playAgainBtn = new Button("🔄 PLAY AGAIN");
        playAgainBtn.setStyle(
                "-fx-background-color: linear-gradient(to right, #0284c7, #06b6d4); " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 12px; " +
                "-fx-font-weight: 900; " +
                "-fx-padding: 10 22; " +
                "-fx-background-radius: 18px; " +
                "-fx-cursor: hand;"
        );
        playAgainBtn.setOnAction(e -> {
            hideModal();
            SoundEngine.playReset();
            resetBoard();
        });

        Button inspectBtn = createIconButton("✕ INSPECT ARENA");
        inspectBtn.setOnAction(e -> hideModal());

        btnBox.getChildren().addAll(playAgainBtn, inspectBtn);
        card.getChildren().addAll(modalTitle, modalSubtitle, btnBox);
        overlay.getChildren().add(card);

        return overlay;
    }

    private void showModal(String titleText, String subtitleText, Color titleColor) {
        modalTitle.setText(titleText);
        modalTitle.setTextFill(titleColor);
        modalSubtitle.setText(subtitleText);
        modalOverlay.setVisible(true);
        modalOverlay.setMouseTransparent(false);
    }

    private void hideModal() {
        modalOverlay.setVisible(false);
        modalOverlay.setMouseTransparent(true);
    }

    private Button createPillButton(String text, boolean active) {
        Button btn = new Button(text);
        updatePillButtonStyle(btn, active);
        return btn;
    }

    private void updatePillButtonStyle(Button btn, boolean active) {
        if (active) {
            btn.setStyle(
                    "-fx-background-color: #0284c7; " +
                    "-fx-text-fill: #ffffff; " +
                    "-fx-font-size: 11px; " +
                    "-fx-font-weight: 900; " +
                    "-fx-padding: 6 14; " +
                    "-fx-background-radius: 18px; " +
                    "-fx-cursor: hand;"
            );
            btn.setEffect(new DropShadow(10, Color.web("#38bdf8")));
        } else {
            btn.setStyle(
                    "-fx-background-color: transparent; " +
                    "-fx-text-fill: #94a3b8; " +
                    "-fx-font-size: 11px; " +
                    "-fx-font-weight: 800; " +
                    "-fx-padding: 6 14; " +
                    "-fx-background-radius: 18px; " +
                    "-fx-cursor: hand;"
            );
            btn.setEffect(null);
        }
    }

    private Button createIconButton(String text) {
        Button btn = new Button(text);
        btn.setStyle(
                "-fx-background-color: rgba(15, 23, 42, 0.85); " +
                "-fx-text-fill: #cbd5e1; " +
                "-fx-font-size: 11px; " +
                "-fx-font-weight: 800; " +
                "-fx-padding: 8 16; " +
                "-fx-background-radius: 20px; " +
                "-fx-border-color: rgba(56, 189, 248, 0.3); " +
                "-fx-border-radius: 20px; " +
                "-fx-cursor: hand;"
        );
        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: rgba(30, 41, 59, 0.95); " +
                "-fx-text-fill: #38bdf8; " +
                "-fx-font-size: 11px; " +
                "-fx-font-weight: 800; " +
                "-fx-padding: 8 16; " +
                "-fx-background-radius: 20px; " +
                "-fx-border-color: #38bdf8; " +
                "-fx-border-radius: 20px; " +
                "-fx-cursor: hand;"
        ));
        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: rgba(15, 23, 42, 0.85); " +
                "-fx-text-fill: #cbd5e1; " +
                "-fx-font-size: 11px; " +
                "-fx-font-weight: 800; " +
                "-fx-padding: 8 16; " +
                "-fx-background-radius: 20px; " +
                "-fx-border-color: rgba(56, 189, 248, 0.3); " +
                "-fx-border-radius: 20px; " +
                "-fx-cursor: hand;"
        ));
        return btn;
    }

    private void setMode(GameMode mode) {
        if (this.currentMode == mode) return;
        this.currentMode = mode;
        updatePillButtonStyle(pvpBtn, mode == GameMode.PVP);
        updatePillButtonStyle(cpuBtn, mode == GameMode.PV_CPU);

        diffBox.setVisible(mode == GameMode.PV_CPU);
        diffBox.setManaged(mode == GameMode.PV_CPU);

        Label oName = (Label) playerOCard.lookup("#nameLabel");
        oName.setText(mode == GameMode.PV_CPU ? "CPU (AI)" : "PLAYER O");

        scoreX = 0;
        scoreO = 0;
        scoreDraw = 0;
        updateScoreDisplays();

        SoundEngine.playClick();
        resetBoard();
    }

    private void setDifficulty(CpuPlayer.Difficulty diff) {
        if (this.currentDifficulty == diff) return;
        this.currentDifficulty = diff;
        updatePillButtonStyle(easyBtn, diff == CpuPlayer.Difficulty.EASY);
        updatePillButtonStyle(medBtn, diff == CpuPlayer.Difficulty.MEDIUM);
        updatePillButtonStyle(hardBtn, diff == CpuPlayer.Difficulty.HARD);

        SoundEngine.playClick();
        resetBoard();
    }

    private void handleCellClick(int row, int col) {
        if (gameOver || cpuThinking || board[row][col] != ' ') {
            return;
        }

        applyMove(row, col, currentPlayer);

        int[][] winLine = checkWin(currentPlayer);
        if (winLine != null) {
            handleGameOver(currentPlayer, winLine);
            return;
        }

        if (isBoardFull()) {
            handleGameOver('D', null);
            return;
        }

        // Switch turn
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
        updateTurnDisplay();

        if (currentMode == GameMode.PV_CPU && currentPlayer == 'O') {
            triggerCpuTurn();
        }
    }

    private void applyMove(int row, int col, char player) {
        board[row][col] = player;
        Button btn = buttons[row][col];

        // Create glowing high-contrast mark as graphic node (immune to button CSS overrides)
        Label mark = new Label(String.valueOf(player));
        String textFill = (player == 'X') ? "#ff2a55" : "#00f0ff";
        String glowColor = (player == 'X') ? "#ef4444" : "#00e5ff";

        mark.setStyle(
                "-fx-font-size: 66px; " +
                "-fx-font-weight: 900; " +
                "-fx-font-family: 'Segoe UI', 'DejaVu Sans', sans-serif; " +
                "-fx-text-fill: " + textFill + "; " +
                "-fx-alignment: center;"
        );
        mark.setEffect(new DropShadow(26, Color.web(glowColor)));

        btn.setGraphic(mark);
        btn.setStyle(player == 'X' ? TILE_X_STYLE : TILE_O_STYLE);
        btn.setEffect(new DropShadow(22, Color.web(glowColor + "88")));

        // Punchy 3D Pop Animation on the mark
        mark.setScaleX(0.15);
        mark.setScaleY(0.15);
        ScaleTransition st = new ScaleTransition(Duration.millis(240), mark);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setInterpolator(Interpolator.EASE_OUT);

        RotateTransition rt = new RotateTransition(Duration.millis(240), mark);
        rt.setFromAngle(-30);
        rt.setToAngle(0);
        rt.setInterpolator(Interpolator.EASE_OUT);

        ParallelTransition pt = new ParallelTransition(st, rt);
        pt.play();

        if (player == 'X') {
            SoundEngine.playMoveX();
        } else {
            SoundEngine.playMoveO();
        }
    }

    private void triggerCpuTurn() {
        if (gameOver) return;
        cpuThinking = true;
        statusBadge.setText("CPU IS COMPUTING... ⚡");
        statusBadge.setEffect(new DropShadow(18, Color.web("#00e5ff")));

        PauseTransition thinkDelay = new PauseTransition(Duration.millis(420));
        thinkDelay.setOnFinished(e -> {
            CpuPlayer.Move move = CpuPlayer.getMove(board, currentDifficulty);
            cpuThinking = false;

            if (move != null && board[move.row][move.col] == ' ') {
                applyMove(move.row, move.col, 'O');

                int[][] winLine = checkWin('O');
                if (winLine != null) {
                    handleGameOver('O', winLine);
                    return;
                }

                if (isBoardFull()) {
                    handleGameOver('D', null);
                    return;
                }

                currentPlayer = 'X';
                updateTurnDisplay();
            }
        });
        thinkDelay.play();
    }

    private void handleGameOver(char winner, int[][] winLine) {
        gameOver = true;

        if (winner == 'X') {
            scoreX++;
            highlightWinningLine(winLine);
            statusBadge.setText("VICTORY: PLAYER X 🎉");
            statusBadge.setEffect(new DropShadow(24, Color.web("#ef4444")));
            SoundEngine.playWin();

            PauseTransition delay = new PauseTransition(Duration.millis(600));
            delay.setOnFinished(e -> showModal("PLAYER X VICTORIOUS!", "Crimson protocol achieved complete domination.", Color.web("#ef4444")));
            delay.play();
        } else if (winner == 'O') {
            scoreO++;
            highlightWinningLine(winLine);
            String name = (currentMode == GameMode.PV_CPU) ? "CPU AI" : "PLAYER O";
            statusBadge.setText("VICTORY: " + name + " 🤖");
            statusBadge.setEffect(new DropShadow(24, Color.web("#00e5ff")));
            SoundEngine.playWin();

            PauseTransition delay = new PauseTransition(Duration.millis(600));
            delay.setOnFinished(e -> showModal(name + " WINS!", "Cyan protocol conquered the board matrix.", Color.web("#00e5ff")));
            delay.play();
        } else {
            scoreDraw++;
            statusBadge.setText("STALEMATE DETECTED 🤝");
            statusBadge.setEffect(new DropShadow(18, Color.web("#f59e0b")));
            SoundEngine.playDraw();

            PauseTransition delay = new PauseTransition(Duration.millis(600));
            delay.setOnFinished(e -> showModal("HONORABLE DRAW", "Both strategic protocols countered each other perfectly.", Color.web("#f59e0b")));
            delay.play();
        }

        updateScoreDisplays();
    }

    private void highlightWinningLine(int[][] line) {
        if (line == null) return;
        for (int[] pos : line) {
            Button btn = buttons[pos[0]][pos[1]];
            btn.setStyle(TILE_WIN_STYLE);
            btn.setEffect(new DropShadow(28, Color.web("#4ade80")));

            // Oscillating celebration bounce on mark
            if (btn.getGraphic() != null) {
                btn.getGraphic().setEffect(new DropShadow(30, Color.web("#4ade80")));
                TranslateTransition bounce = new TranslateTransition(Duration.millis(350), btn.getGraphic());
                bounce.setByY(-10);
                bounce.setAutoReverse(true);
                bounce.setCycleCount(Animation.INDEFINITE);
                bounce.play();
                activeAnimations.add(bounce);
            }
        }
    }

    private int[][] checkWin(char p) {
        for (int[][] line : WIN_COMBINATIONS) {
            if (board[line[0][0]][line[0][1]] == p &&
                board[line[1][0]][line[1][1]] == p &&
                board[line[2][0]][line[2][1]] == p) {
                return line;
            }
        }
        return null;
    }

    private boolean isBoardFull() {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board[r][c] == ' ') return false;
            }
        }
        return true;
    }

    private void resetBoard() {
        for (Animation anim : activeAnimations) {
            anim.stop();
        }
        activeAnimations.clear();

        hideModal();
        gameOver = false;
        cpuThinking = false;
        currentPlayer = 'X';

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                board[r][c] = ' ';
                buttons[r][c].setGraphic(null);
                buttons[r][c].setText("");
                buttons[r][c].setStyle(TILE_IDLE_STYLE);
                buttons[r][c].setEffect(new DropShadow(16, Color.web("rgba(0,0,0,0.85)")));
                buttons[r][c].setTranslateY(0);
                buttons[r][c].setScaleX(1.0);
                buttons[r][c].setScaleY(1.0);
            }
        }

        updateTurnDisplay();
    }

    private void updateTurnDisplay() {
        if (currentMode == GameMode.PV_CPU) {
            if (currentPlayer == 'X') {
                statusBadge.setText("YOUR TURN (X)");
                statusBadge.setEffect(new DropShadow(18, Color.web("#ef4444")));
                setCardActive(playerXCard, true, "#ef4444");
                setCardActive(playerOCard, false, "#00e5ff");
            } else {
                statusBadge.setText("CPU'S TURN (O)");
                statusBadge.setEffect(new DropShadow(18, Color.web("#00e5ff")));
                setCardActive(playerXCard, false, "#ef4444");
                setCardActive(playerOCard, true, "#00e5ff");
            }
        } else {
            if (currentPlayer == 'X') {
                statusBadge.setText("PLAYER X'S TURN");
                statusBadge.setEffect(new DropShadow(18, Color.web("#ef4444")));
                setCardActive(playerXCard, true, "#ef4444");
                setCardActive(playerOCard, false, "#00e5ff");
            } else {
                statusBadge.setText("PLAYER O'S TURN");
                statusBadge.setEffect(new DropShadow(18, Color.web("#00e5ff")));
                setCardActive(playerXCard, false, "#ef4444");
                setCardActive(playerOCard, true, "#00e5ff");
            }
        }
    }

    private void setCardActive(VBox card, boolean active, String hexColor) {
        if (active) {
            card.setStyle(
                    "-fx-background-color: rgba(15, 23, 42, 0.9); " +
                    "-fx-padding: 18 16; " +
                    "-fx-background-radius: 16px; " +
                    "-fx-border-color: " + hexColor + "; " +
                    "-fx-border-radius: 16px; " +
                    "-fx-border-width: 2px;"
            );
            card.setEffect(new DropShadow(22, Color.web(hexColor)));
        } else {
            card.setStyle(
                    "-fx-background-color: rgba(15, 23, 42, 0.65); " +
                    "-fx-padding: 18 16; " +
                    "-fx-background-radius: 16px; " +
                    "-fx-border-color: " + hexColor + "44; " +
                    "-fx-border-radius: 16px; " +
                    "-fx-border-width: 1.5px;"
            );
            card.setEffect(new DropShadow(8, Color.web(hexColor + "22")));
        }
    }

    private void updateScoreDisplays() {
        scoreLabelX.setText(String.valueOf(scoreX));
        scoreLabelO.setText(String.valueOf(scoreO));
        scoreLabelDraw.setText("DRAWS: " + scoreDraw);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
