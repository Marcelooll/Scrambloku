package com.sudoku.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SudokuBoard {
    private final int SIZE = 9;
    private final int EMPTY = 0;

    // Solução completa, puzzle original e estado atual
    private List<List<Integer>> solutionBoard;
    private List<List<Integer>> puzzleBoard;
    private List<List<Integer>> currentBoard;

    public SudokuBoard() {
        solutionBoard = createEmptyBoard();
        puzzleBoard = createEmptyBoard();
        currentBoard = createEmptyBoard();
    }

    public void newPuzzle() {
        List<List<Integer>> board = createEmptyBoard();
        if (fillBoard(board, 0, 0)) {
            solutionBoard = copyBoard(board);
            puzzleBoard = copyBoard(solutionBoard);
            removeNumbers(puzzleBoard, 40); // Ajuste a dificuldade alterando o número removido
            currentBoard = copyBoard(puzzleBoard);
        }
    }

    public void replay() {
        currentBoard = copyBoard(puzzleBoard);
    }

    public void scramble() {
        newPuzzle();
    }

    private List<List<Integer>> createEmptyBoard() {
        List<List<Integer>> board = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < SIZE; j++) {
                row.add(EMPTY);
            }
            board.add(row);
        }
        return board;
    }

    private List<List<Integer>> copyBoard(List<List<Integer>> source) {
        List<List<Integer>> dest = new ArrayList<>();
        for (List<Integer> row : source) {
            dest.add(new ArrayList<>(row));
        }
        return dest;
    }

    private boolean fillBoard(List<List<Integer>> board, int row, int col) {
        if (row == SIZE) return true;
        int nextRow = (col == SIZE - 1) ? row + 1 : row;
        int nextCol = (col + 1) % SIZE;
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= SIZE; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);
        for (Integer num : numbers) {
            if (isSafe(board, row, col, num)) {
                board.get(row).set(col, num);
                if (fillBoard(board, nextRow, nextCol)) return true;
                board.get(row).set(col, EMPTY);
            }
        }
        return false;
    }

    private boolean isSafe(List<List<Integer>> board, int row, int col, int num) {
        for (int i = 0; i < SIZE; i++) {
            if (board.get(row).get(i) == num || board.get(i).get(col) == num) {
                return false;
            }
        }
        int boxRow = (row / 3) * 3;
        int boxCol = (col / 3) * 3;
        for (int i = boxRow; i < boxRow + 3; i++) {
            for (int j = boxCol; j < boxCol + 3; j++) {
                if (board.get(i).get(j) == num) {
                    return false;
                }
            }
        }
        return true;
    }

    private void removeNumbers(List<List<Integer>> board, int count) {
        List<int[]> positions = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                positions.add(new int[]{i, j});
            }
        }
        Collections.shuffle(positions);
        int removed = 0;
        for (int[] pos : positions) {
            if (removed >= count) break;
            int row = pos[0], col = pos[1];
            if (board.get(row).get(col) != EMPTY) {
                board.get(row).set(col, EMPTY);
                removed++;
            }
        }
    }

    // Métodos de acesso
    public List<List<Integer>> getBoard() {
        return currentBoard;
    }

    public int getInitialValue(int row, int col) {
        return puzzleBoard.get(row).get(col);
    }

    public int getSolutionValue(int row, int col) {
        return solutionBoard.get(row).get(col);
    }

    public void setCurrentValue(int row, int col, int value) {
        currentBoard.get(row).set(col, value);
    }

    public boolean isCorrectValue(int row, int col, int value) {
        return value == getSolutionValue(row, col);
    }
}
