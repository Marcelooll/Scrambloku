package com.sudoku.ascii;

import com.sudoku.model.SudokuBoard;
import java.util.List;
import java.util.Scanner;

public class TerminalSudoku {
    private final SudokuBoard board;
    private final Scanner scanner;
    private final int SIZE = 9;

    public TerminalSudoku(SudokuBoard board) {
        this.board = board;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        String command;
        do {
            printBoard();
            System.out.println("Comandos: input | replay | scramble | status | complete | exit");
            System.out.print("Digite o comando: ");
            command = scanner.nextLine().trim().toLowerCase();
            switch (command) {
                case "input":
                    processInput();
                    break;
                case "replay":
                    board.replay();
                    System.out.println("Puzzle restaurado para o estado original.");
                    break;
                case "scramble":
                    board.scramble();
                    System.out.println("Novo puzzle gerado.");
                    break;
                case "status":
                    printStatus();
                    break;
                case "complete":
                    if (isBoardCompleteAndCorrect()) {
                        System.out.println("Parabéns, você completou o jogo!");
                    } else {
                        System.out.println("O jogo está incompleto ou há erros. Verifique os Status.");
                    }
                    break;
                case "exit":
                    System.out.println("Encerrando o jogo. Até logo!");
                    break;
                default:
                    System.out.println("Comando não reconhecido.");
            }
        } while (!command.equals("exit"));
    }

    private void printBoard() {
        List<List<Integer>> current = board.getBoard();
        System.out.println("\n=========================");
        for (int row = 0; row < SIZE; row++) {
            if (row % 3 == 0 && row != 0) {
                System.out.println("-------------------------");
            }
            for (int col = 0; col < SIZE; col++) {
                if (col % 3 == 0 && col != 0) {
                    System.out.print(" |");
                }
                int value = current.get(row).get(col);
                System.out.printf(" %s", (value == 0 ? "." : value));
            }
            System.out.println();
        }
        System.out.println("=========================\n");
    }

    private void processInput() {
        try {
            System.out.print("Linha (0-8): ");
            int row = Integer.parseInt(scanner.nextLine());
            System.out.print("Coluna (0-8): ");
            int col = Integer.parseInt(scanner.nextLine());
            System.out.print("Valor (1-9 ou 0 para limpar): ");
            int value = Integer.parseInt(scanner.nextLine());
            if (row < 0 || row >= SIZE || col < 0 || col >= SIZE || value < 0 || value > 9) {
                System.out.println("Entrada inválida.");
                return;
            }
            if (board.getInitialValue(row, col) != 0) {
                System.out.println("Esta célula é fixa e não pode ser alterada.");
                return;
            }
            board.setCurrentValue(row, col, value);
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Use números.");
        }
    }

    private void printStatus() {
        List<List<Integer>> current = board.getBoard();
        int corretas = 0, erradas = 0;
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                int val = current.get(row).get(col);
                if (val == 0) {
                    erradas++;
                } else if (board.isCorrectValue(row, col, val)) {
                    corretas++;
                } else {
                    erradas++;
                }
            }
        }
        System.out.println("Status: " + corretas + " corretas, " + erradas + " erradas.");
    }

    private boolean isBoardCompleteAndCorrect() {
        List<List<Integer>> current = board.getBoard();
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                int val = current.get(row).get(col);
                if (val == 0 || !board.isCorrectValue(row, col, val)) {
                    return false;
                }
            }
        }
        return true;
    }
}
