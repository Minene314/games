package Geister;

class Board {

    private static final int MAXWIDTH = 8;

    private Square field[][] = new Square[MAXWIDTH][MAXWIDTH];

    void init() {
        for(int i = 0; i < MAXWIDTH; i++) {
            for(int j = 0; j < MAXWIDTH; j++) {
                field[i][j] = new Square();
            }
        }
        for(int i =1; i < MAXWIDTH-1; i++) {
            for(int j = 1; j < MAXWIDTH-1; j++) {
                field[i][j].setEmpty();
            }
        }
        for(int i = 2; i < 6; i++) {
            field[1][i].setEnemyRed();
            field[2][i].setEnemyRed();
            field[5][i].setRed();
            field[6][i].setRed();
        }
    }

    void display() {
        System.out.println("    a b c d e f");
        for(int i = 0; i < MAXWIDTH; i++) {
            if(0 < i && i < MAXWIDTH-1) System.out.print(i+" ");
            else System.out.print("  ");
            for(int j = 0; j < MAXWIDTH; j++){
                System.out.print(field[i][j].getCharacter()+" ");
            }
            System.out.println("");
        }
    }

    void move(int cRow, int cColumn, int nRow, int nColumn) {
        Square tmp = new Square();
        tmp.copy(field[cRow][cColumn]);
        field[cRow][cColumn].copy(field[nRow][nColumn]);
        field[nRow][nColumn].copy(tmp);
    }

    void takePiece(int row, int column) {
        field[row][column].setEmpty();
    }

    int getSize() {
        return MAXWIDTH;
    }
    
    int getField(int row, int column) {
        return field[row][column].getState();
    }

    void setInitBlue(int row, int column) {
        if(row < 3)field[row][column].setEnemyBlue();
        else field[row][column].setBlue();
    }
}