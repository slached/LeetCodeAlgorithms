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

        Solve sudoku = new Solve(temp2);
        sudoku.doTheThing();
        sudoku.seeBoard();

    }
}

class Solve {

    boolean solved = false;
    int runningQuantity = 0;
    int logMitigator = 50;

    public enum RowOrColumnE {
        Row, Column
    }

    // board contains all elements as row 0. index first row 1. index second row ...
    private final char[][] board;
    private int spaceCount;

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

    void refresh() {
        //reset space count 
        this.spaceCount = 0;
        //clear all arrays
        this.boxesMatrix = new ArrayList<>(9);
        this.rowMatrix = new ArrayList<>(9);
        this.columnMatrix = new ArrayList<>(9);
        this.possibilityMatrix = new ArrayList<>(9);

        // rearrange arrays according to board
        this.arrangeArrays();
        // recreate possibility matrix
        this.createPossibilityArray();

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

    void firstRule() {
        // First check for if there any possibilities has only one possibility
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (this.possibilityMatrix.get(i).get(j).size() == 1) {
                    this.board[i][j] = this.possibilityMatrix.get(i).get(j).get(0);
                }
            }
        }
    }

    void secondRule() {
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
    }

    void thirdRule() {
        ArrayList<Integer> frequencyForBox = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0));
        ArrayList<ArrayList<Integer>> visitedIndexes = new ArrayList<>();
        // this rule for check for if there any box(3x3)'s has specific possibility number alone
        int row = 0;
        int innerLoopIterationCount = 0;
        int colStartIndex = 0;
        int rowStartIndex = 0;
        boolean resetCondition = false;

        while (row < 9) {
            for (int col = colStartIndex; col < 3 + colStartIndex; col++) {
                //code goes here
                this.frequencyArrayCreate(row, col, this.possibilityMatrix.get(row).get(col).size(), frequencyForBox);
                innerLoopIterationCount++;
                visitedIndexes.add(new ArrayList<>(Arrays.asList(row, col)));
                // one box ended
                if (innerLoopIterationCount != 0
                        && innerLoopIterationCount % 9 == 0) {
                    row = rowStartIndex;
                    colStartIndex += 3;
                    // !!! explanation
                    // find the number in the box that single possibility
                    // what i mean by single possibility in possibility matrix ->
                    // if in a box any number only in one cell (for example [0,0] has (1,2,3,4) possibilities and others cells has 2,3,4 but none of the other cells has 1 so that rule applies the [0,0]. cell the number 1)
                    char number = this.foundTheNumberAccordingToIndex(frequencyForBox);
                    for (ArrayList<Integer> whichCell : visitedIndexes) {
                        // whichCell's 0. index row and 1. index are column
                        // possibility matrix's visited cell's does contain foundedNumber (which has to)
                        int rowW = whichCell.get(0);
                        int colW = whichCell.get(1);
                        if (this.possibilityMatrix.get(rowW).get(colW).contains(number)) {
                            this.board[rowW][colW] = number;
                        }
                    }

                    //reset frequency for later usage
                    frequencyForBox = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0));
                    //reset visited indexes for later usage
                    visitedIndexes = new ArrayList<>();
                }
                // 3 box in row ended and this condition is reset condition
                if (innerLoopIterationCount != 0 && innerLoopIterationCount % 27 == 0) {
                    colStartIndex = 0;
                    rowStartIndex += 3;
                    row = rowStartIndex;
                    innerLoopIterationCount = 0;
                    resetCondition = true;
                }
            }

            // pass this in reset condition
            if (!resetCondition) {
                // do not increase row in reset condition
                row++;
            }
            // disable reset condition
            resetCondition = false;
        }

    }

    void backTracking(int row, int column) {
        runningQuantity++;
        this.log();
        // if board didn't solved
        if (!solved) {
            // work only if the cell is empty
            if (this.board[row][column] == '.') {
                for (int number = 1; number < 10; number++) {

                    // if it is ok with this number
                    if (this.isItSuitable(row, column, number)) {
                        // set number 
                        this.board[row][column] = (char) (number + '0');

                        this.goToTheNextCell(row, column);
                        // if code goes here that mean future cell failed and that means we need to reset current cell
                        if (!solved) {
                            this.board[row][column] = '.';
                        }
                    } else {
                        if (!solved) {
                            this.board[row][column] = '.';
                        }
                    }
                }
            } else {
                this.goToTheNextCell(row, column);
            }
        }

        // if the for loop ended and still could not found the right element that means we go back
    }

    boolean isItSuitable(int row, int column, int number) {
        // refresh these matrix
        this.refresh();
        // for row column and box
        return !this.rowMatrix.get(row).contains((char) (number + '0'))
                && !this.columnMatrix.get(column).contains((char) (number + '0'))
                && !this.boxesMatrix.get(this.foundInWhichBox(row, column)).contains((char) (number + '0'));
    }

    void applyTheRules() {
        runningQuantity++;
        this.log();

        // apply first rule
        this.firstRule();
        // clear the 3 array(row,column and boxes) and recreate possibility matrix
        this.refresh();

        // apply second rule
        this.secondRule();
        // clear the 3 array(row,column and boxes) and recreate possibility matrix
        this.refresh();

        // apply third rule
        this.thirdRule();
        this.refresh();

        this.log();
        if (this.spaceCount > 0 && this.runningQuantity < 10) {
            applyTheRules();
        } else {
            this.backTracking(0, 0);
        }
    }

    void seeBoard() {
        System.out.println();
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

    void log() {
        if (this.runningQuantity % this.logMitigator == 0) {
            System.out.printf("Complete Percentage:%d%% Remaining Spaces:%d | Tried %d times... \n", (int) (Math.floor(this.spaceCount * 100 / 81)), this.spaceCount, this.runningQuantity);
        }
    }

    void goToTheNextCell(int row, int column) {
        // last element prevent
        if (!(row == 8 && column == 8)) {
            if (column == 8) {
                this.backTracking(row + 1, 0);
            } else {
                this.backTracking(row, column + 1);
            }
        } else {
            solved = true;
        }

    }

}
