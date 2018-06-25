package com.example.sudoku_tester;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SudokuView extends JFrame {
    public static final int NUM_SQUARES_ON_BOARD = SudokuModel.NUM_SQUARES_ON_BOARD;
    private SudokuModel sudokuModel;
    private JLabel[] squares = new JLabel[NUM_SQUARES_ON_BOARD];
    private JLabel messageLabel;
    private String message = "Click the squares to toggle value";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SudokuView().setVisible(true);
            }

        });
    }

    /**
     * Constructor
     */
    public SudokuView() {
        super("Sudoku");
        sudokuModel = new SudokuModel();

        createSudokuView();
        setTitle("Sudoku");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    /**
     * Initializes the first view by creating all JComponents and setting appropriate values
     */
    private void createSudokuView() {
        int[] playerBoard = sudokuModel.getPlayerBoard();
        JPanel[] subBoardPanel = new JPanel[9];

        JPanel superPanel = new JPanel();
        superPanel.setLayout(new BoxLayout(superPanel, BoxLayout.Y_AXIS));
        superPanel.setPreferredSize(new Dimension(360, 300));

        GridLayout boardLayout = new GridLayout(3, 3);
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(boardLayout);
        boardPanel.setPreferredSize(new Dimension(300, 300));

        GridLayout subBoardLayout = new GridLayout(3, 3, 1, 1);
        Border subBorder = BorderFactory.createLineBorder(Color.BLACK, 1);
        for (int i = 0; i < 9; i++) {
            subBoardPanel[i] = new JPanel();
            subBoardPanel[i].setLayout(subBoardLayout);
            subBoardPanel[i].setBorder(subBorder);
            subBoardPanel[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            subBoardPanel[i].setAlignmentY(Component.CENTER_ALIGNMENT);
            boardPanel.add(subBoardPanel[i]);
        }

        for (int i = 0; i < NUM_SQUARES_ON_BOARD; i++) {
            int subBoardIndex = sudokuModel.getSubBoardIndex(i);
            if (playerBoard[i] == 0) {
                squares[i] = new JLabel("");
                Border border = BorderFactory.createLineBorder(Color.BLUE, 1);
                squares[i].setBorder(border);
            } else {
                squares[i] = new JLabel((Integer.toString(playerBoard[i])));
                Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
                squares[i].setBorder(border);
            }

            squares[i].setName(Integer.toString(i));
            squares[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            squares[i].setAlignmentY(Component.CENTER_ALIGNMENT);

            squares[i].setHorizontalAlignment(JLabel.CENTER);
            squares[i].setVerticalAlignment(JLabel.CENTER);

            squares[i].addMouseListener(
                    new BoardMouseListener()
            );
            subBoardPanel[subBoardIndex].add(squares[i]);
        }

        messageLabel = new JLabel(message);
        messageLabel.setSize(new Dimension(30, 300));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton submitButton = new JButton("Check Board");
        submitButton.setSize(new Dimension(30, 300));
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        checkGame();
                    }
                }
        );

        superPanel.add(boardPanel);
        superPanel.add(messageLabel);
        superPanel.add(submitButton);
        getContentPane().add(superPanel);
    }

    /**
     * Compares the active board with the correct answer via model call and updates
     * message appropriately to user.
     */
    private void checkGame() {
        if (sudokuModel.checkResult()) {
            message = "Congratulations, you won!";
        } else {
            message = "Your board is not correct";
        }
        updateMessage();
    }

    /**
     * Checks with the model to see if square is editable.
     * If the original boardmask was false, then it is editable
     * @param index Index of the square being checked.
     * @return Returns true if editable.
     */
    private boolean checkEditable(int index) {
        return !sudokuModel.getBoardMaskValue(index);
    }

    /**
     * Highlights the square with yellow border.
     * @param index Index of the square being highlighted.
     */
    private void highlightSquares(int index) {
        Border subBorder = BorderFactory.createLineBorder(Color.yellow, 3);
        squares[index].setBorder(subBorder);
    }

    /**
     * Unhighlights the square with block border
     * @param index Index of the square being unhighlighted.
     */
    private void unHighlightSquares(int index) {
        Border subBorder = BorderFactory.createLineBorder(Color.BLACK, 1);
        squares[index].setBorder(subBorder);
    }

    /**
     * Updates the messageLabel in the superPanel with the global message variable.
     */
    private void updateMessage() {
        messageLabel.setText(message);
    }

    /**
     * Increments the value of the square on the board and wraps at 9.
     * Set the square to 1 if the square has not been set yet.
     * It updates the model with this value and displays it in the JLabel.
     * @param index Index of the square being toggled.
     */
    private void toggleSquareValue(int index) {
        String text = squares[index].getText();
        int count;

        if (text == "") {
            count = 1;
        } else {
            count = Integer.parseInt(squares[index].getText());
            if (count == 9) {
                count = 1;
            } else {
                count += 1;
            }
        }
        sudokuModel.setPlayerBoard(index, count);
        squares[index].setText(Integer.toString(count));
    }

    /**
     * Mouse listener for components in the boardPanel area
     */
    private class BoardMouseListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            int index = Integer.parseInt(((JLabel) mouseEvent.getSource()).getName());
            if (checkEditable(index)) {
                toggleSquareValue(index);
            }
        }

        @Override
        public void mousePressed(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseReleased(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseEntered(MouseEvent mouseEvent) {
            int index = Integer.parseInt(((JLabel) mouseEvent.getSource()).getName());
            if (checkEditable(index)) {
                highlightSquares(index);
            }

        }

        @Override
        public void mouseExited(MouseEvent mouseEvent) {
            int index = Integer.parseInt(((JLabel) mouseEvent.getSource()).getName());
            if (checkEditable(index)) {
                unHighlightSquares(index);
            }
        }
    }
}
