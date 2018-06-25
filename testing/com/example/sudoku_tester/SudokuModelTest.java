package com.example.sudoku_tester;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class SudokuModelTest {

    @Test
    public void getPlayerBoard(){
        SudokuModel model = new SudokuModel();
        int[] playerBoard = model.getPlayerBoard();
        assertTrue(playerBoard instanceof int[]);
    }

    @Test
    public void setPlayerBoard(){
        SudokuModel model = new SudokuModel();
        Random randomGenerator = new Random();
        int[] playerBoard = model.getPlayerBoard();
        // Valid indices from 0 to 80
        int randomIndex = randomGenerator.nextInt(81);
        // Valid values from 1 to 9
        int randomValue = randomGenerator.nextInt(9) + 1;
        model.setPlayerBoard(randomIndex,randomValue);
        playerBoard = model.getPlayerBoard();
        assertTrue(playerBoard[randomIndex] == randomValue);
    }

    @Test (expected = RuntimeException.class)
    public void setPlayerBoardInvalidValueHigh(){
        SudokuModel model = new SudokuModel();
        Random randomGenerator = new Random();
        int[] playerBoard = model.getPlayerBoard();
        // Valid indices from 0 to 80
        int randomIndex = randomGenerator.nextInt(81);
        // Invalid values greater than 9
        int randomInvalidValue = randomGenerator.nextInt(10) + 10;
        model.setPlayerBoard(randomIndex,randomInvalidValue);
    }
    @Test (expected = RuntimeException.class)
    public void setPlayerBoardInvalidValueLow(){
        SudokuModel model = new SudokuModel();
        Random randomGenerator = new Random();
        int[] playerBoard = model.getPlayerBoard();
        // Valid values from 0 to 80
        int randomIndex = randomGenerator.nextInt(81);
        // Invalid values less than 1
        int randomInvalidValue = randomGenerator.nextInt(10) - 10;
        model.setPlayerBoard(randomIndex,randomInvalidValue);
    }

    @Test (expected = RuntimeException.class)
    public void setPlayerBoardInvalidIndexValueHigh(){
        SudokuModel model = new SudokuModel();
        Random randomGenerator = new Random();
        int[] playerBoard = model.getPlayerBoard();
        // Invalid indicies greater than 81
        int randomInvalidIndex = randomGenerator.nextInt(81) + 81;
        // Valid values from 1 to 9
        int randomValue = randomGenerator.nextInt(9) + 1;
        model.setPlayerBoard(randomInvalidIndex,randomValue);
    }

    @Test (expected = RuntimeException.class)
    public void setPlayerBoardInvalidIndexValueLow(){
        SudokuModel model = new SudokuModel();
        Random randomGenerator = new Random();
        int[] playerBoard = model.getPlayerBoard();
        // Invalid indices lower than 0
        int randomInvalidIndex = randomGenerator.nextInt(81) - 81;
        // Valid values from 1 to 9
        int randomValue = randomGenerator.nextInt(9) + 1;
        model.setPlayerBoard(randomInvalidIndex,randomValue);
    }

    @Test
    public void getSubBoardIndex() {
        SudokuModel model = new SudokuModel();
        int subBoardIndex;
        subBoardIndex = model.getSubBoardIndex(0);
        assertEquals( 0,subBoardIndex);
        subBoardIndex = model.getSubBoardIndex(2);
        assertEquals( 0,subBoardIndex);

        subBoardIndex = model.getSubBoardIndex(3);
        assertEquals( 1,subBoardIndex);
        subBoardIndex = model.getSubBoardIndex(12);
        assertEquals( 1,subBoardIndex);

        subBoardIndex = model.getSubBoardIndex(16);
        assertEquals( 2,subBoardIndex);
        subBoardIndex = model.getSubBoardIndex(26);
        assertEquals( 2,subBoardIndex);

        subBoardIndex = model.getSubBoardIndex(45);
        assertEquals( 3,subBoardIndex);
        subBoardIndex = model.getSubBoardIndex(47);
        assertEquals( 3,subBoardIndex);

        subBoardIndex = model.getSubBoardIndex(32);
        assertEquals( 4,subBoardIndex);
        subBoardIndex = model.getSubBoardIndex(48);
        assertEquals( 4,subBoardIndex);

        subBoardIndex = model.getSubBoardIndex(42);
        assertEquals( 5,subBoardIndex);
        subBoardIndex = model.getSubBoardIndex(44);
        assertEquals( 5,subBoardIndex);

        subBoardIndex = model.getSubBoardIndex(55);
        assertEquals( 6,subBoardIndex);
        subBoardIndex = model.getSubBoardIndex(73);
        assertEquals( 6,subBoardIndex);

        subBoardIndex = model.getSubBoardIndex(67);
        assertEquals( 7,subBoardIndex);
        subBoardIndex = model.getSubBoardIndex(68);
        assertEquals( 7,subBoardIndex);

        subBoardIndex = model.getSubBoardIndex(60);
        assertEquals( 8,subBoardIndex);
        subBoardIndex = model.getSubBoardIndex(61);
        assertEquals( 8,subBoardIndex);
        subBoardIndex = model.getSubBoardIndex(80);
        assertEquals( 8,subBoardIndex);
    }

    @Test(expected = RuntimeException.class)
    public void getSubBoardIndexInvalidHigh() {
        SudokuModel model = new SudokuModel();
        model.getSubBoardIndex(90);
    }

    @Test(expected = RuntimeException.class)
    public void getSubBoardIndexInvalidLow() {
        SudokuModel model = new SudokuModel();
        model.getSubBoardIndex(-1);
    }

    @Test
    public void getBoardMaskValue() {
    }

    @Test
    public void checkResult() {
        SudokuModel model = new SudokuModel();
        assertFalse(model.checkResult());
    }
}