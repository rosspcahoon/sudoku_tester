package com.example.sudoku_tester;

import javax.management.RuntimeErrorException;
import java.util.*;

public class SudokuModel {
    public static final int NUM_SQUARES_ON_BOARD = 81;
    public static final int MAX_SQUARE_INDEX = NUM_SQUARES_ON_BOARD - 1;
    public static final int NUM_VALUES_SEEN = 70;
    public static final int NUM_RANDOM_SHUFFLES = 10;

    private static final int NUM_SUB_UNITS = 3;
    private static final int SQ_PER_SINGLE_ROW = NUM_SUB_UNITS * NUM_SUB_UNITS;
    private static final int SQ_PER_SUB_UNIT = SQ_PER_SINGLE_ROW * NUM_SUB_UNITS;

    private int[] board;
    private int[] playerBoard;
    private boolean[] boardMask;

    public static void main(String[] args) {
//        getSubBoardIndex();

    }

    public SudokuModel() {
        board = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9,
                4, 5, 6, 7, 8, 9, 1, 2, 3,
                7, 8, 9, 1, 2, 3, 4, 5, 6,
                9, 1, 2, 3, 4, 5, 6, 7, 8,
                3, 4, 5, 6, 7, 8, 9, 1, 2,
                6, 7, 8, 9, 1, 2, 3, 4, 5,
                8, 9, 1, 2, 3, 4, 5, 6, 7,
                2, 3, 4, 5, 6, 7, 8, 9, 1,
                5, 6, 7, 8, 9, 1, 2, 3, 4
        };
        playerBoard = new int[NUM_SQUARES_ON_BOARD];
        boardMask = new boolean[NUM_SQUARES_ON_BOARD];

        shuffleBoard();
        randomizeBoardMask(NUM_VALUES_SEEN);
        populatePlayerBoard();

    }

    /**
     * Shuffles the default board.
     * Sub columns/rows are series of three consecutive 9-square boards that are vertical/horizontal, respectively.
     * Swapping columns/rows within one of these sub-units also results in a legitimate Sudoku board.
     */
    private void shuffleBoard() {
        Random randomgenerator = new Random();
        int numColShuffles = randomgenerator.nextInt(NUM_RANDOM_SHUFFLES);
        int numRowShuffles = randomgenerator.nextInt(NUM_RANDOM_SHUFFLES);

        for (int i = 0; i < numColShuffles; i++) {
            // Pick sub column
            int subCol = randomgenerator.nextInt(NUM_SUB_UNITS);
            // Pick first column to swap within subCol
            int subColFirst = randomgenerator.nextInt(NUM_SUB_UNITS);
            // Pick second column to swap within subCol
            int subColSecond = randomgenerator.nextInt(NUM_SUB_UNITS);

            shuffleHelper(true, subCol, subColFirst, subColSecond);
        }
        for (int i = 0; i < numRowShuffles; i++) {
            // Pick sub row
            int subRow = randomgenerator.nextInt(NUM_SUB_UNITS);
            // Pick first row index to swap within subRow
            int subRowFirst = randomgenerator.nextInt(NUM_SUB_UNITS);
            // Pick second row index to swap within subRow
            int subRowSecond = randomgenerator.nextInt(NUM_SUB_UNITS);

            shuffleHelper(false, subRow, subRowFirst, subRowSecond);
        }


    }

    /**
     * Aids in the shuffling of the board, performs one column/row swap.
     *
     * @param isCol       True if the swap that is occurring is for column, false for row.
     * @param subUnit     The index of the subunit we are shuffling. Potential values are 0,1,2
     *                    and start from the upper left of the Sudoku board and increase going
     *                    right/down.
     * @param firstIndex  Index of the first column/row being swapped.
     * @param secondIndex Index of the second column/row being swapped.
     */
    private void shuffleHelper(boolean isCol, int subUnit, int firstIndex, int secondIndex) {
        int from_start_index;
        int to_start_index;
        int iterator_multiplier;

        if (firstIndex == secondIndex) {
            return;
        }

        if (isCol) {
            from_start_index = NUM_SUB_UNITS * (subUnit % NUM_SUB_UNITS) + firstIndex;
            to_start_index = NUM_SUB_UNITS * (subUnit % NUM_SUB_UNITS) + secondIndex;
            iterator_multiplier = SQ_PER_SINGLE_ROW;
        } else {
            from_start_index = SQ_PER_SUB_UNIT * (subUnit % NUM_SUB_UNITS) + SQ_PER_SINGLE_ROW * firstIndex;
            to_start_index = SQ_PER_SUB_UNIT * (subUnit % NUM_SUB_UNITS) + SQ_PER_SINGLE_ROW * secondIndex;
            iterator_multiplier = 1;
        }

        for (int i = 0; i < SQ_PER_SINGLE_ROW; i++) {
            int from_iterated_index = from_start_index + (i * iterator_multiplier);
            int to_iterated_index = to_start_index + (i * iterator_multiplier);
            int temp_int = board[to_iterated_index];
            board[to_iterated_index] = board[from_iterated_index];
            board[from_iterated_index] = temp_int;
        }
    }

    /**
     * Provides a random boardMask base on the numbers of values that want to be seen.
     * Currently does it randomly without regard to balancing for of the board.
     *
     * @param numValuesSeen Number of values on the puzzle that will be seen.
     */
    private void randomizeBoardMask(int numValuesSeen) {
        ArrayList<Integer> valuesToBoard = new ArrayList<Integer>();

        // Add all valid indices to ArrayList
        for (int i = 0; i < NUM_SQUARES_ON_BOARD; i++) {
            valuesToBoard.add(new Integer(i));
        }

        // Shuffle
        Collections.shuffle(valuesToBoard);

        // Select the number that you want to be seen then set mask
        for (int i = 0; i < numValuesSeen; i++) {
            int index = valuesToBoard.get(i);
            boardMask[index] = true;
        }
    }

    /**
     * Fills in the playerBoard with the known starting values of the board.
     */
    private void populatePlayerBoard() {
        for (int i = 0; i < playerBoard.length; i++) {
            playerBoard[i] = boardMask[i] ? board[i] : 0;
        }
    }

    /**
     * Getter for the playerBoard
     * @return
     */
    public int[] getPlayerBoard() {
        return playerBoard;
    }

    /**
     * Setter for playerBoard.
     * @param index Index of playerBoard being set.
     * @param value Value of playerBoard.
     */
    public void setPlayerBoard (int index, int value) {
        if (index > MAX_SQUARE_INDEX || index < 0) {
            throw new RuntimeException("Invalid index, must be between 0 and 81, seen:" + index);
        }

        if (value > 9 || value < 0) {
            throw new RuntimeException("Invalid value, must be between 1 and 9, seen:" + value);
        }

        playerBoard[index] = value;
    }

    /** Given the boardIndex (0-81), it returns the corresponding subBoardIndex(0-8)
     *
     * @param boardIndex
     * @return
     */
    public int getSubBoardIndex(int boardIndex) {
        if (boardIndex > MAX_SQUARE_INDEX || boardIndex < 0) {
            throw new RuntimeException("Invalid index request, must be between 0 and 80, index: " + boardIndex);
        }

        int colCalc = ((boardIndex % SQ_PER_SINGLE_ROW) - (boardIndex % NUM_SUB_UNITS))/ NUM_SUB_UNITS;
        int rowCalc = NUM_SUB_UNITS *((boardIndex - (boardIndex % SQ_PER_SUB_UNIT))/ SQ_PER_SUB_UNIT);
        return colCalc + rowCalc;
    }

    /**
     * Provides the value of the index provided in the original board mask.
     * This allows view to know which board indices are edittable.
     * @param i
     * @return
     */
    public boolean getBoardMaskValue(int i ) {
        return boardMask[i];
    }

    /**
     * Checks to see if the playerBoard and board are equal then prints appropriate response
     */
    public boolean checkResult() {
        if (Arrays.equals(playerBoard, board)) {
            return true;
        } else {
            return false;
        }
    }
}
