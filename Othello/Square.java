package Othello;

class Square{
    private static final int BLACK = 1;
    private static final int WHITE = 2;
    private static final int WALL = -1;
    private static final int EMPTY = 0;
    private static final char cBLACK = '●';
    private static final char cWHITE = '○';
    private static final char cWALL = '#';
    private static final char cEMPTY = '□';
    private int state;
    private char character;
    private int value;

    void setValue(int v){
        value = v;
    }
    int getValue(){
        return value;
    }

    void setBlack(){
        state = BLACK;
        character = cBLACK;
    }
    void setWhite(){
        state = WHITE;
        character = cWHITE;
    }
    void setWall(){
        state = WALL;
        character = cWALL;
    }
    void setEmpty(){
        state = EMPTY;
        character = cEMPTY;
    }
    char getCharacter(){
        return character;
    }
    int getState(){
        return state;
    }

}