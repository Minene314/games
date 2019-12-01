package Geister;

class Square {
    private static final int BLUE = 2;
    private static final int RED = 3;
    private static final int ENEMYBLUE = -2;
    private static final int ENEMYRED = -3;
    private static final int WALL = -1;
    private static final int EMPTY = 0;
    private static final char cBLUE = '○';
    private static final char cRED = '●';
    private static final char cENEMY = '◎';
    private static final char cWALL = '#';
    private static final char cEMPTY = '□';
    private int state;
    private char character;
    private int value;

    Square() {
        state = WALL;
        character = cWALL;
    }

    void setValue(int v) {
        value = v;
    }

    int getValue() {
        return value;
    }

    void setBlue() {
        state = BLUE;
        character = cBLUE;
    }

    void setRed() {
        state = RED;
        character = cRED;
    }

    void setEnemyBlue() {
        state = ENEMYBLUE;
        character = cENEMY;
    }

    void setEnemyRed() {
        state = ENEMYRED;
        character = cENEMY;
    }

    void setWall() {
        state = WALL;
        character = cWALL;
    }

    void setEmpty() {
        state = EMPTY;
        character = cEMPTY;
    }

    char getCharacter() {
        return character;
    }

    int getState() {
        return state;
    }

    void copy(Square s) {
        state = s.getState();
        character = s.getCharacter();
    }
}