package com.tictactoe;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SoundEngine {

    private static final int SAMPLE_RATE = 44100;
    private static final ExecutorService POOL = Executors.newCachedThreadPool(r -> {
        Thread thread = new Thread(r, "SFX-Daemon-Thread");
        thread.setDaemon(true);
        return thread;
    });

    private static volatile boolean soundEnabled = true;

    public static void setSoundEnabled(boolean enabled) {
        soundEnabled = enabled;
    }

    public static boolean isSoundEnabled() {
        return soundEnabled;
    }

    public static void playHover() {
        if (!soundEnabled) return;
        POOL.submit(() -> playTone(900, 700, 35, 0.08, ToneType.SINE));
    }

    public static void playClick() {
        if (!soundEnabled) return;
        POOL.submit(() -> playTone(1200, 950, 40, 0.12, ToneType.TRIANGLE));
    }

    public static void playMoveX() {
        if (!soundEnabled) return;
        POOL.submit(() -> playTone(580, 260, 110, 0.22, ToneType.SAWTOOTH_SOFT));
    }

    public static void playMoveO() {
        if (!soundEnabled) return;
        POOL.submit(() -> playTone(320, 680, 110, 0.22, ToneType.SINE));
    }

    public static void playWin() {
        if (!soundEnabled) return;
        POOL.submit(() -> {
            int[] notes = {523, 659, 784, 1046}; // C5, E5, G5, C6
            for (int note : notes) {
                playTone(note, note, 90, 0.25, ToneType.SINE);
                try {
                    Thread.sleep(75);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    public static void playDraw() {
        if (!soundEnabled) return;
        POOL.submit(() -> {
            int[] notes = {440, 415, 392}; // A4, Ab4, G4
            for (int note : notes) {
                playTone(note, note, 120, 0.2, ToneType.SINE);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    public static void playReset() {
        if (!soundEnabled) return;
        POOL.submit(() -> playTone(240, 880, 180, 0.18, ToneType.SINE));
    }

    private enum ToneType {
        SINE, TRIANGLE, SAWTOOTH_SOFT
    }

    private static void playTone(double startFreq, double endFreq, int durationMs, double maxVolume, ToneType type) {
        try {
            int totalSamples = (SAMPLE_RATE * durationMs) / 1000;
            byte[] buffer = new byte[totalSamples];

            double phase = 0;
            for (int i = 0; i < totalSamples; i++) {
                double progress = (double) i / totalSamples;
                double freq = startFreq + (endFreq - startFreq) * progress;

                // ADSR envelope: fast attack, linear decay
                double env;
                if (progress < 0.1) {
                    env = progress / 0.1;
                } else {
                    env = Math.max(0, 1.0 - (progress - 0.1) / 0.9);
                }

                double val;
                switch (type) {
                    case TRIANGLE:
                        double normPhase = (phase / (2 * Math.PI)) % 1.0;
                        val = 4.0 * Math.abs(normPhase - 0.5) - 1.0;
                        break;
                    case SAWTOOTH_SOFT:
                        double saw = 2.0 * ((phase / (2 * Math.PI)) % 1.0) - 1.0;
                        double sine = Math.sin(phase);
                        val = 0.6 * saw + 0.4 * sine;
                        break;
                    case SINE:
                    default:
                        val = Math.sin(phase);
                        break;
                }

                phase += (2.0 * Math.PI * freq) / SAMPLE_RATE;
                double sample = val * env * maxVolume;
                buffer[i] = (byte) (Math.max(-128, Math.min(127, (int) (sample * 127))));
            }

            AudioFormat format = new AudioFormat(SAMPLE_RATE, 8, 1, true, false);
            try (SourceDataLine line = AudioSystem.getSourceDataLine(format)) {
                line.open(format, buffer.length);
                line.start();
                line.write(buffer, 0, buffer.length);
                line.drain();
            }
        } catch (Exception ignored) {
            // Gracefully ignore audio hardware absence
        }
    }
}
