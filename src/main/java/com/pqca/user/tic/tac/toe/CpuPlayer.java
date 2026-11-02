package com.pqca.user.tic.tac.toe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CpuPlayer {

    public enum Difficulty {
        EASY("Easy"),
        MEDIUM("Medium"),
        HARD("Hard (Unbeatable)");

        private final String displayName;

        Difficulty(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    public static class Move {
        public final int row;
        public final int col;

        public Move(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    private static final Random RANDOM = new Random();

    // Constant winning lines
    private static final int[][][] WIN_COMBINATIONS = {
            {{0, 0}, {0, 1}, {0, 2}},
            {{1, 0}, {1, 1}, {1, 2}},
            {{2, 0}, {2, 1}, {2, 2}},
            {{0, 0}, {1, 0}, {2, 0}},
            {{0, 1}, {1, 1}, {2, 1}},
            {{0, 2}, {1, 2}, {2, 2}},
            {{0, 0}, {1, 1}, {2, 2}},
            {{0, 2}, {1, 1}, {2, 0}}
    };

    public static Move getMove(char[][] board, Difficulty difficulty) {
        switch (difficulty) {
            case EASY:
                return getRandomMove(board);
            case MEDIUM:
                return getMediumMove(board);
            case HARD:
            default:
                return getBestMoveMinimax(board);
        }
    }

    public static Move getRandomMove(char[][] board) {
        List<Move> available = getAvailableMoves(board);
        if (available.isEmpty()) {
            return null;
        }
        return available.get(RANDOM.nextInt(available.size()));
    }

    public static Move getMediumMove(char[][] board) {
        List<Move> available = getAvailableMoves(board);
        if (available.isEmpty()) {
            return null;
        }

        // 1. Check if CPU ('O') can win in one move
        for (Move move : available) {
            board[move.row][move.col] = 'O';
            boolean win = (checkWinner(board) == 'O');
            board[move.row][move.col] = ' ';
            if (win) {
                return move;
            }
        }

        // 2. Check if Player ('X') can win in one move and block it
        for (Move move : available) {
            board[move.row][move.col] = 'X';
            boolean win = (checkWinner(board) == 'X');
            board[move.row][move.col] = ' ';
            if (win) {
                return move;
            }
        }

        // 3. Take center if available with 70% probability
        if (board[1][1] == ' ' && RANDOM.nextDouble() < 0.7) {
            return new Move(1, 1);
        }

        // 4. Otherwise pick random move from available
        return available.get(RANDOM.nextInt(available.size()));
    }

    public static Move getBestMoveMinimax(char[][] board) {
        List<Move> available = getAvailableMoves(board);
        if (available.isEmpty()) {
            return null;
        }

        // Fast opening move optimization: pick center or corners instantly
        if (available.size() == 9) {
            int[][] startingMoves = {{0, 0}, {0, 2}, {2, 0}, {2, 2}, {1, 1}};
            int[] pick = startingMoves[RANDOM.nextInt(startingMoves.length)];
            return new Move(pick[0], pick[1]);
        }

        int bestVal = Integer.MIN_VALUE;
        Move bestMove = null;

        for (Move move : available) {
            board[move.row][move.col] = 'O';
            int moveVal = minimax(board, 0, false);
            board[move.row][move.col] = ' ';

            if (moveVal > bestVal) {
                bestVal = moveVal;
                bestMove = move;
            }
        }

        return bestMove != null ? bestMove : available.get(0);
    }

    private static int minimax(char[][] board, int depth, boolean isMax) {
        char winner = checkWinner(board);
        if (winner == 'O') {
            return 10 - depth;
        }
        if (winner == 'X') {
            return depth - 10;
        }
        if (isBoardFull(board)) {
            return 0;
        }

        if (isMax) {
            int best = Integer.MIN_VALUE;
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 3; c++) {
                    if (board[r][c] == ' ') {
                        board[r][c] = 'O';
                        best = Math.max(best, minimax(board, depth + 1, false));
                        board[r][c] = ' ';
                    }
                }
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 3; c++) {
                    if (board[r][c] == ' ') {
                        board[r][c] = 'X';
                        best = Math.min(best, minimax(board, depth + 1, true));
                        board[r][c] = ' ';
                    }
                }
            }
            return best;
        }
    }

    public static char checkWinner(char[][] b) {
        for (int[][] line : WIN_COMBINATIONS) {
            char first = b[line[0][0]][line[0][1]];
            if (first != ' ' &&
                first == b[line[1][0]][line[1][1]] &&
                first == b[line[2][0]][line[2][1]]) {
                return first;
            }
        }
        return ' ';
    }

    public static boolean isBoardFull(char[][] b) {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (b[r][c] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    public static List<Move> getAvailableMoves(char[][] board) {
        List<Move> list = new ArrayList<>();
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board[r][c] == ' ') {
                    list.add(new Move(r, c));
                }
            }
        }
        return list;
    }
}
