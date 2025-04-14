package com.sudoku.ui;

import com.sudoku.model.SudokuBoard;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class SudokuFrame extends JFrame {
    private final SudokuBoard board;
    private final JTextField[][] cells;
    private final JLabel statusLabel;
    private final int SIZE = 9;

    public SudokuFrame(SudokuBoard board) {
        this.board = board;
        this.cells = new JTextField[SIZE][SIZE];
        this.statusLabel = new JLabel("Status: Aguardando ação...");

        setTitle("Scrambloku - Sudoku");
        setSize(600, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        updateUIFromBoard();
        setVisible(true);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Criação do grid com bordas destacadas para blocos 3x3
        JPanel gridPanel = new JPanel(new GridLayout(SIZE, SIZE));
        Font cellFont = new Font("SansSerif", Font.BOLD, 20);

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                JTextField cell = new JTextField();
                cell.setHorizontalAlignment(JTextField.CENTER);
                cell.setFont(cellFont);

                // Define borda mais espessa para os limites dos sub-blocos 3x3
                int top = (row % 3 == 0) ? 4 : 1;
                int left = (col % 3 == 0) ? 4 : 1;
                int bottom = ((row + 1) % 3 == 0) ? 4 : 1;
                int right = ((col + 1) % 3 == 0) ? 4 : 1;
                Border border = new MatteBorder(top, left, bottom, right, Color.BLACK);
                cell.setBorder(border);

                // Células fixas (do puzzle) ficam não editáveis e com fundo diferenciado
                if (board.getInitialValue(row, col) != 0) {
                    cell.setText(String.valueOf(board.getInitialValue(row, col)));
                    cell.setEditable(false);
                    cell.setBackground(Color.LIGHT_GRAY);
                }

                cells[row][col] = cell;
                gridPanel.add(cell);
            }
        }

        // Painel de botões com listeners via lambda
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton replayBtn = new JButton("Re-Play");
        replayBtn.addActionListener(e -> {
            board.replay();
            updateUIFromBoard();
            JOptionPane.showMessageDialog(null, "Puzzle restaurado para o estado original.");
        });

        JButton scrambleBtn = new JButton("Scramble");
        scrambleBtn.addActionListener(e -> {
            board.scramble();
            updateUIFromBoard();
            JOptionPane.showMessageDialog(null, "Novo puzzle gerado.");
        });

        JButton statusBtn = new JButton("Verificar Status");
        statusBtn.addActionListener(e -> {
            syncBoardFromUI();
            int corretas = 0, erradas = 0;
            for (int row = 0; row < SIZE; row++) {
                for (int col = 0; col < SIZE; col++) {
                    String text = cells[row][col].getText().trim();
                    if (!text.isEmpty()) {
                        try {
                            int value = Integer.parseInt(text);
                            if (board.isCorrectValue(row, col, value)) {
                                corretas++;
                            } else {
                                erradas++;
                            }
                        } catch (NumberFormatException ex) {
                            erradas++;
                        }
                    } else {
                        erradas++;
                    }
                }
            }
            statusLabel.setText("Status: " + corretas + " corretas, " + erradas + " erradas.");
            JOptionPane.showMessageDialog(null, "Status Atualizado:\n" +
                    corretas + " células corretas\n" + erradas + " células erradas");
        });

        JButton completeBtn = new JButton("Completar o Jogo");
        completeBtn.addActionListener(e -> {
            syncBoardFromUI();
            if (isBoardCompleteAndCorrect()) {
                JOptionPane.showMessageDialog(null, "Parabéns, Você completou o jogo!");
            } else {
                JOptionPane.showMessageDialog(null, "O jogo está incompleto ou há algo errado, verifique os Status.");
            }
        });

        buttonPanel.add(replayBtn);
        buttonPanel.add(scrambleBtn);
        buttonPanel.add(statusBtn);
        buttonPanel.add(completeBtn);

        add(statusLabel, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateUIFromBoard() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                int value = board.getBoard().get(row).get(col);
                cells[row][col].setText(value != 0 ? String.valueOf(value) : "");
                if (board.getInitialValue(row, col) != 0) {
                    cells[row][col].setEditable(false);
                    cells[row][col].setBackground(Color.LIGHT_GRAY);
                } else {
                    cells[row][col].setEditable(true);
                    cells[row][col].setBackground(Color.WHITE);
                }
            }
        }
        statusLabel.setText("Status: Aguardando ação...");
    }

    private void syncBoardFromUI() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                String text = cells[row][col].getText().trim();
                int value = 0;
                try {
                    value = text.isEmpty() ? 0 : Integer.parseInt(text);
                } catch (NumberFormatException ex) {
                    value = 0;
                }
                board.setCurrentValue(row, col, value);
            }
        }
    }

    private boolean isBoardCompleteAndCorrect() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                String text = cells[row][col].getText().trim();
                if (text.isEmpty()) return false;
                try {
                    int value = Integer.parseInt(text);
                    if (!board.isCorrectValue(row, col, value)) return false;
                } catch (NumberFormatException ex) {
                    return false;
                }
            }
        }
        return true;
    }
}
