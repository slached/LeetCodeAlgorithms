package org.slached.sudokusolver;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author OmerPC
 */
public class Sudoku_solver {

    public static void main(String[] args) {

        char[][] temp = {
            {'5', '3', '.', '.', '7', '.', '.', '.', '.'},
            {'6', '.', '.', '1', '9', '5', '.', '.', '.'},
            {'.', '9', '8', '.', '.', '.', '.', '6', '.'},
            {'8', '.', '.', '.', '6', '.', '.', '.', '3'},
            {'4', '.', '.', '8', '.', '3', '.', '.', '1'},
            {'7', '.', '.', '.', '2', '.', '.', '.', '6'},
            {'.', '6', '.', '.', '.', '.', '2', '8', '.'},
            {'.', '.', '.', '4', '1', '9', '.', '.', '5'},
            {'.', '.', '.', '.', '8', '.', '.', '7', '9'}
        };
        char[][] temp2 = {
            {'.', '.', '9', '7', '4', '8', '.', '.', '.'},
            {'7', '.', '.', '.', '.', '.', '.', '.', '.'},
            {'.', '2', '.', '1', '.', '9', '.', '.', '.'},
            {'.', '.', '7', '.', '.', '.', '2', '4', '.'},
            {'.', '6', '4', '.', '1', '.', '5', '9', '.'},
            {'.', '9', '8', '.', '.', '.', '3', '.', '.'},
            {'.', '.', '.', '8', '.', '3', '.', '2', '.'},
            {'.', '.', '.', '.', '.', '.', '.', '.', '6'},
            {'.', '.', '.', '2', '7', '5', '9', '.', '.'}
        };
        Solve solve = new Solve(temp2);
        solve.doTheThing();
        solve.seeBoard();
    }
}

class Solve {

    public enum RowOrColumnE {
        Row, Column
    }

    // board contains all elements as row 0. index first row 1. index second row ...
    private final char[][] board;
    private int spaceCount;
    private int cutTheWire = 1000;

    private ArrayList<ArrayList<Character>> boxesMatrix = new ArrayList<>(9);
    private ArrayList<ArrayList<Character>> rowMatrix = new ArrayList<>(9);
    private ArrayList<ArrayList<Character>> columnMatrix = new ArrayList<>(9);

    private ArrayList<ArrayList<ArrayList<Character>>> possibilityMatrix = new ArrayList<>(9);

    private final ArrayList<Character> numbers = new ArrayList<>(Arrays.asList('1', '2', '3', '4', '5', '6', '7', '8', '9'));

    Solve(char[][] board) {
        this.board = board;
    }

    void doTheThing() {
        this.arrangeArrays();
        this.createPossibilityArray();
        this.applyTheRules();
    }

    void arrangeArrays() {

        // fill the 3 arrangement array lists first dimension with empty char Arrays
        for (int i = 0; i < 9; i++) {
            this.boxesMatrix.add(new ArrayList<>(9));
            this.rowMatrix.add(new ArrayList<>(9));
            this.columnMatrix.add(new ArrayList<>(9));
            this.possibilityMatrix.add(new ArrayList<>(9));
        }

        // this makes our possibility Matrix ready to go
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                this.possibilityMatrix.get(i).add(new ArrayList<>(9));
            }
        }

        // first loop row
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                //count space amounts
                if (board[i][j] == '.') {
                    spaceCount++;
                }
                this.boxesMatrix.get(foundInWhichBox(i, j)).add(board[i][j]);
                this.rowMatrix.get(i).add(board[i][j]);
                this.columnMatrix.get(j).add(board[i][j]);
            }
        }

    }

    void refreshArray() {
        //clear all arrays
        this.boxesMatrix = new ArrayList<>(9);
        this.rowMatrix = new ArrayList<>(9);
        this.columnMatrix = new ArrayList<>(9);
        this.possibilityMatrix = new ArrayList<>(9);

        // rearrange arrays according to board
        this.arrangeArrays();

    }

    void createPossibilityArray() {

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                // i represents rows and j for columns
                // if cell is empty
                if (this.board[i][j] == '.') {
                    // note that our diffList is contains possibilities
                    // box section
                    // first we need to look for this cell in which box
                    int inWhichBox = this.foundInWhichBox(i, j);
                    // add numbers in an temporary array
                    ArrayList<Character> diffList = new ArrayList<>(numbers);
                    // remove cell's box numbers(for example if cell 0,2 is the current cell we should look for 0. boxes containing numbers) from all numbers thus we created few possibility but this stage is not enough.
                    diffList.removeAll(this.boxesMatrix.get(inWhichBox));

                    // now we should check for this cell's row and after that to his columns
                    // row section
                    diffList.removeAll(this.rowMatrix.get(i));
                    // columns section
                    diffList.removeAll(this.columnMatrix.get(j));

                    // set the last possibilities to the matrix
                    this.possibilityMatrix.get(i).set(j, diffList);
                }

            }
        }
    }

    void applyTheRules() {
        cutTheWire--;
        // First check for if there any possibilities has only one possibility
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (this.possibilityMatrix.get(i).get(j).size() == 1) {
                    this.board[i][j] = this.possibilityMatrix.get(i).get(j).get(0);
                }
            }
        }

        // Second check is if any row possibilities has only one number
        for (int i = 0; i < 9; i++) {
            // reset frequencyForRow after passing the next row
            ArrayList<Integer> frequencyForRow = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0));
            ArrayList<Integer> frequencyForColumn = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0));

            for (int j = 0; j < 9; j++) {
                // this creates frequencyForRow
                this.frequencyArrayCreate(i, j, this.possibilityMatrix.get(i).get(j).size(), frequencyForRow);
                // this creates frequencyForCol
                this.frequencyArrayCreate(j, i, this.possibilityMatrix.get(j).get(i).size(), frequencyForColumn);
            }
            // add board the values
            this.rowAndColumnModifier(this.foundTheNumberAccordingToIndex(frequencyForRow), i, RowOrColumnE.Row);
            this.rowAndColumnModifier(this.foundTheNumberAccordingToIndex(frequencyForColumn), i, RowOrColumnE.Column);
        }

        // clear the 3 array(row,column and boxes)
        this.refreshArray();
        // recreate possibility array
        this.createPossibilityArray();

        if (this.spaceCount > 0 && this.cutTheWire > 0) {
            applyTheRules();
        }
    }

    void seeArray() {
        //System.out.println("Boxes Matrix:" + Arrays.asList(boxesMatrix));
        System.out.println("Possibility Matrix:" + Arrays.asList(possibilityMatrix) + "\n");
        //this.seeBoard();
    }

    void seeBoard() {
        for (char[] board1 : this.board) {
            for (int i = 0; i < 9; i++) {
                System.out.print(board1[i]);
                if (i != 8) {
                    System.out.print("  ");
                }

            }
            System.out.println();
        }
    }

    int foundInWhichBox(int i, int j) {
        return (int) Math.floor((j / 3)) + 3 * (int) Math.floor(i / 3);
    }

    // this number's index + 1 represents the number's itself(alone number)
    char foundTheNumberAccordingToIndex(ArrayList<Integer> arr) {
        return (char) (arr.indexOf(1) + '0' + 1);

    }

    // this creates frequency
    // this stores data according to the numbers value so we could use this array to find out if there is a specific value alone or not
    void frequencyArrayCreate(int row, int column, int size, ArrayList<Integer> frequencyArray) {
        for (int possibility = 0; possibility < size; possibility++) {
            int num = ((this.possibilityMatrix.get(row).get(column).get(possibility)) - '0');
            frequencyArray.set(num - 1, frequencyArray.get(num - 1) + 1);
        }
    }

    // this alter the board using frequency data
    void rowAndColumnModifier(char foundedNumber, int i, RowOrColumnE rowOrColumn) {
        if (foundedNumber != '0') {
            for (int k = 0; k < 9; k++) {
                switch (rowOrColumn) {
                    case Row -> {
                        if (this.possibilityMatrix.get(i).get(k).contains(foundedNumber)) {
                            this.board[i][k] = foundedNumber;
                        }
                    }
                    case Column -> {
                        if (this.possibilityMatrix.get(k).get(i).contains(foundedNumber)) {
                            this.board[k][i] = foundedNumber;
                        }
                    }
                    default ->
                        throw new AssertionError();
                }
            }
        }
    }

}
