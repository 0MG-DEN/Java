package package1;

public class TicTacToe {
    private static class Cell {

        private enum states {empty, X, O}
        private states state;

        Cell() {
            state = states.empty;
        }

        public boolean setState(states inState) {
            if(state == states.empty) {
                state = inState;
                return true;
            }
            else return false;
        }

        public String toString() {
            switch (state) {
                case X:
                    return states.X.toString();
                case O:
                    return states.O.toString();
                default:
                    return " ";
            }
        }
    }

    public static class GameException extends Exception {
        /*
        If game throws GameException it means that player's turn is
        NOT over for some reason (exception message is always required!).
         */
        public GameException(String message) {
            super(message);
        }
    }

    /*
    "X" -> Player1 -> -1
    "O" -> Player2 ->  1
     */
    private int player = -1;

    private Cell[][] grid;
    private final int _SIZE;

    private boolean gameOn;

    public TicTacToe(int inSize) throws IllegalArgumentException {
        if(inSize <= 2)
            throw new IllegalArgumentException(String.valueOf(inSize));
        _SIZE = inSize;
        gameOn = true;

        grid = new Cell[_SIZE][_SIZE];
        for(int i = 0; i < _SIZE; i++)
            for(int j = 0; j < _SIZE; j++)
                grid[i][j] = new Cell();
    }

    public int getCurrentPlayer() {
        return (player < 0) ? 1 : 2;
    }

    public int getLastPlayer() {
        return (player * -1 < 0) ? 1 : 2;
    }

    public boolean isOn() {
        return gameOn;
    }

    public boolean takePosition(int inPos) throws GameException{
        if(gameOn) { // positions from 1 to _SIZE^2 !
            if (inPos > 0 && inPos <= _SIZE * _SIZE) {
                if (grid[(inPos - 1) / _SIZE][(inPos - 1) % _SIZE]
                    .setState((player < 0) ? Cell.states.X : Cell.states.O)) {
                    player *= -1;
                    return true;
                } else throw new GameException("This position is already taken.");
            } else throw new GameException("Position is out of bounds.");
        } else throw new GameException("Game is over.");
        /*
        Explanation code:
        int S = 5;
        for(int pos = 1; pos <= (int)Math.pow(S, 2); pos++)
            System.out.println(pos + ") " + (pos - 1) / S + ", " + (pos - 1) % S);
         */
    }

    public String currentState() {
        StringBuilder out = new StringBuilder();

        for(int i = -1; i < _SIZE; i++)
            out.append(String.format("[%2d]|", i));
        out.append('\n');

        int rowNum = 1;
        for(int i = 0; i < _SIZE; i++) {

            for (int j = 0; j < 5 * (_SIZE + 1); j++)
                out.append('-');
            out.append('\n');

            out.append(String.format("[%2d]|", rowNum));
            for (int j = 0; j < _SIZE; j++)
                out.append(String.format("%4s|", grid[i][j].toString()));
            out.append('\n');

            rowNum += _SIZE;
        }

        return out.toString();
        /*
        Explanation code:
        TicTacToe game1 = new TicTacToe(3);
        game1.takePosition(2);
        game1.takePosition(6);
        game1.takePosition(9);
        System.out.print(game1.currentState());

        Output:
        [-1]|[ 0]|[ 1]|[ 2]|
        --------------------
        [ 1]|    |   X|    | <- X is on 2nd position (2 = 1 + 1)
        --------------------
        [ 4]|    |    |   O| <- O is on 6th position (6 = 4 + 2)
        --------------------
        [ 7]|    |    |   X| <- X is on 9th position (9 = 7 + 2)

        ! Position = [rowNum] + [colNum]
         */
    }

    public boolean checkGrid() throws GameException{
        if(gameOn) {
            for (int i = 0; i < _SIZE; i++) {

                for (int j = 0; j < _SIZE; j++) {
                    if ((grid[i][j].state == Cell.states.empty) ||
                        (grid[i][j].state != grid[i][0].state)) break;

                    if (j == _SIZE - 1) gameOn = false;
                }

                for (int j = 0; j < _SIZE; j++) {
                    if ((grid[j][i].state == Cell.states.empty) ||
                        (grid[j][i].state != grid[0][i].state)) break;

                    if (j == _SIZE - 1) gameOn = false;
                }
            }

            for (int j = 0; j < _SIZE; j++) {
                if ((grid[j][j].state == Cell.states.empty) ||
                    (grid[j][j].state != grid[0][0].state)) break;

                if (j == _SIZE - 1) gameOn = false;
            }

            for (int j = 0; j < _SIZE; j++) {
                if ((grid[j][_SIZE - 1 - j].state == Cell.states.empty) ||
                    (grid[j][_SIZE - 1 - j].state != grid[0][_SIZE - 1].state)) break;

                if (j == _SIZE - 1) gameOn = false;
            }

            if(!gameOn) {
                throw new GameException("Player" + ((getLastPlayer() < 0) ? '1' : '2') + " wins.");
            }

            if(gameOn) {
                boolean draw = true;
                for (int i = 0; i < _SIZE && draw; i++) {
                    for (int j = 0; j < _SIZE && draw; j++) {
                        if (grid[i][j].state == Cell.states.empty) draw = false;
                    }
                }
                if (draw) {
                    gameOn = false;
                    throw new GameException("Draw.");
                }
            }
        }
        /*
         Can't merge all this cycles in one big cycle because
         "j" is used in "if" condition in each one separately.
         */

        return gameOn;

        /*
        if it returns -1 - Player1 wins
                       1 - Player2 wins
                       0 - game is not over yet

        Explanation code:
        TicTacToe game1 = new TicTacToe(3);
        game1.takePosition(1); // player1's turn
        game1.takePosition(5); // player2's turn1
        game1.takePosition(2); // player1's turn
        game1.takePosition(9); // player2's turn
        game1.takePosition(3); // player1's turn
        System.out.println(game1.checkGrid());

        Output:
        -1
         */
    }
}
