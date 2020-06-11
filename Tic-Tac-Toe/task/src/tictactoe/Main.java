package tictactoe;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    private static final int FIELD_SIZE = 3;
    public static final char X_CELL = 'X';
    public static final char O_CELL = 'O';
    private static final char EMPTY_CELL = '_';
    private static final Scanner sc = new Scanner(System.in);
    public static char[][] field = new char[FIELD_SIZE][FIELD_SIZE];

    public static void main(String[] args) {
        char ch = O_CELL;
        fillField("_________".toCharArray());
        GameStatus gameStatus = GameStatus.GAME_NOT_FINISHED;
        while (gameStatus == GameStatus.GAME_NOT_FINISHED) {
            ch = ch == X_CELL ? O_CELL : X_CELL;
            printField();
            userMove(ch);
            gameStatus = getGameStatus();
        }
        printField();
        System.out.println(gameStatus.getText());
    }

    public static void userInput() {
        System.out.println("Enter cells : ");
        String input = sc.nextLine();
        char[] chars = input.toCharArray();
        fillField(chars);
    }

    public static void fillField(char[] chars) {
        for (int row = 0; row < FIELD_SIZE; row++) {
            for (int col = 0; col < FIELD_SIZE; col++) {
                int index = (row * FIELD_SIZE + col);
                field[row][col] = chars[index];
            }
        }
    }

    public static void userMove(char ch) {
        int row = 0;
        int col = 0;
        Pattern pattern = Pattern.compile("a");
        while (true) {
            System.out.println("Enter the coordinates:");
            String coordinates = sc.nextLine();
            if (coordinates.split(" ").length != 2) {
                System.out.println("Should not be more than 2 coordinates!");
                continue;
            }
            if (coordinates.matches("(.*)[A-Za-z](.*)")) {
                System.out.println("You should enter numbers!");
                continue;
            }
            if (coordinates.matches("(.*)[4-90](.*)")) {
                System.out.println("Coordinates should be from 1 to 3!");
                continue;
            }
            String[] str = coordinates.split(" ");
            col = Integer.parseInt(str[0]);
            row = Integer.parseInt(str[1]);
            col -= 1;
            row = FIELD_SIZE - row;
            if (field[row][col] != EMPTY_CELL) {
                System.out.println("This cell is occupied! Choose another one!");
                continue;
            }
            break;
        }
        field[row][col] = ch;
    }

    public static void printField() {
        for (int row = 0; row < FIELD_SIZE + 2; row++) {
            for (int col = 0; col < FIELD_SIZE * FIELD_SIZE; col++) {
                if (row == 0 || row == FIELD_SIZE + 1) {
                    System.out.print('-');
                } else if (col == 0 || col == FIELD_SIZE * FIELD_SIZE - 1) {
                    System.out.print('|');
                } else if (col % 2 == 0) {
                    int r = row - 1;
                    int c = col / 2 - 1;
                    System.out.print(field[r][c]);
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    public static GameStatus getGameStatus() {
        int xSum = X_CELL * FIELD_SIZE;
        int oSum = O_CELL * FIELD_SIZE;
        int xTotal = 0;
        int oTotal = 0;
        boolean xWin = false;
        boolean oWin = false;
        boolean isEmpty = false;
        int diag1Sum = 0;
        int diag2Sum = 0;
        for (int row = 0; row < FIELD_SIZE; row++) {
            int rowSum = 0;
            int colSum = 0;
            diag1Sum += field[row][row];
            diag2Sum += field[row][FIELD_SIZE - row - 1];
            for (int col = 0; col < FIELD_SIZE; col++) {
                char ch = field[row][col];
                xTotal += ch == X_CELL ? 1 : 0;
                oTotal += ch == O_CELL ? 1 : 0;
                isEmpty = ch == EMPTY_CELL || isEmpty;
                rowSum += field[row][col];
                colSum += field[col][row];
            }
            xWin = rowSum == xSum || xWin;
            oWin = rowSum == oSum || oWin;
            xWin = colSum == xSum || xWin;
            oWin = colSum == oSum || oWin;
            xWin = diag1Sum == xSum || xWin;
            oWin = diag1Sum == oSum || oWin;
            xWin = diag2Sum == xSum || xWin;
            oWin = diag2Sum == oSum || oWin;
        }
        if (xWin && oWin || Math.abs(oTotal - xTotal) > 1) {
            return GameStatus.IMPOSSIBLE;
        }
        if (xWin) {
            return GameStatus.X_WINS;
        }
        if (oWin) {
            return GameStatus.O_WINS;
        }
        if (isEmpty) {
            return GameStatus.GAME_NOT_FINISHED;
        }
        return GameStatus.DRAW;
    }


}

enum GameStatus {
    GAME_NOT_FINISHED("Game not finished"),
    DRAW("Draw"),
    X_WINS("X wins"),
    O_WINS("O wins"),
    IMPOSSIBLE("Impossible");

    private final String text;

    GameStatus(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
