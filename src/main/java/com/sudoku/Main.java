package com.sudoku;

import com.sudoku.model.SudokuBoard;
import com.sudoku.ui.SudokuFrame;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            SudokuBoard board = new SudokuBoard();
            board.newPuzzle();
            new SudokuFrame(board);
        });
    }
}
