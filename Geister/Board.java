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
    }

    void setInitPiece() {
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

    void move(int beforeRow, int beforeColumn, int afterRow, int afterColumn) {
        Square tmp = new Square();

        tmp.copy(field[beforeRow][beforeColumn]);
        field[beforeRow][beforeColumn].copy(field[afterRow][afterColumn]);
        field[afterRow][afterColumn].copy(tmp);
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

    void copy(Board newBoard){
        newBoard.init();
        for(int i=0;i<MAXWIDTH;i++) {
            for(int j=0;j<MAXWIDTH;j++) {
                int state = field[i][j].getState();
                if(state==-1) newBoard.field[i][j].setWall();
                else if(state == 2) newBoard.field[i][j].setBlue();
                else if(state == -2) newBoard.field[i][j].setEnemyBlue();
                else if(state == 3) newBoard.field[i][j].setRed();
                else if(state == -3) newBoard.field[i][j].setEnemyRed();
            }
        }
    }
}