package com.sudoku;

import com.sudoku.model.SudokuBoard;
import com.sudoku.ascii.TerminalSudoku;

public class MainAscii {
    public static void main(String[] args) {
        SudokuBoard board = new SudokuBoard();
        board.newPuzzle();
        TerminalSudoku terminalUI = new TerminalSudoku(board);
        terminalUI.run();
    }
}

