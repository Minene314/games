package Othello;


class Board{
    //壁を含める盤面の大きさ
    private static final int MAXWIDTH = 10;
    //盤面を構成
    private Square field[][] = new Square[MAXWIDTH][MAXWIDTH];
    
    //盤面の初期化
    void init(){
        for(int i=0;i<MAXWIDTH;i++){
            for(int j=0;j<MAXWIDTH;j++){
                field[i][j] = new Square();
            }
        }
        for(int i=0;i<MAXWIDTH;i++){
            for(int j=0;j<MAXWIDTH;j++){
                field[i][j].setWall();
            }
        }
        for(int i=1;i<MAXWIDTH-1;i++){
            for(int j=1;j<MAXWIDTH-1;j++){
                field[i][j].setEmpty();
            }
        }
        field[4][5].setBlack();
        field[5][4].setBlack();
        field[4][4].setWhite();
        field[5][5].setWhite();


        //マスの評価値
        field[1][1].setValue(48);
        field[1][8].setValue(48);
        field[8][1].setValue(48);
        field[8][8].setValue(48);
        field[1][2].setValue(-11);
        field[1][7].setValue(-11);
        field[2][1].setValue(-11);
        field[2][8].setValue(-11);
        field[7][1].setValue(-11);
        field[7][8].setValue(-11);
        field[8][2].setValue(-11);
        field[8][7].setValue(-11);
        field[7][2].setValue(-16);
        field[2][7].setValue(-16);
        field[7][2].setValue(-16);
        field[7][7].setValue(-16);
        for(int i=4;i<=5;i++){
            for(int j=1;j<=8;j++){
                if(j==4 || j==5){
                    field[i][j].setValue(0);
                }else{
                    field[i][j].setValue(-1);
                    field[j][i].setValue(-1);
                }
            }
        }
        for(int i=3;i<=6;i++){
            field[2][i].setValue(-3);
            field[i][2].setValue(-3);
            field[7][i].setValue(-3);
            field[i][7].setValue(-3);
        }
        /*
        field[1][1].setValue(256);
        field[8][8].setValue(256);
        field[1][8].setValue(256);
        field[8][1].setValue(256);
        for(int i=1;i<=3;i++){
            for(int j=1;j<=3;j++){
                if(i==2 || j==2){
                    field[i][j].setValue(1);
                    field[9-i][9-j].setValue(1);
                    field[9-i][j].setValue(1);
                    field[i][9-j].setValue(1);
                }else if(i==3 || j==3){
                    field[i][j].setValue(10);
                    field[9-i][9-j].setValue(10);
                    field[9-i][j].setValue(10);
                    field[i][9-j].setValue(10);
                }
            }
        }
        for(int i=4;i<=5;i++){
            for(int j=1;j<=3;j++){
                if(j==1 || j==3){
                    field[i][j].setValue(4);
                    field[i][9-j].setValue(4);
                    field[j][i].setValue(4);
                    field[9-j][i].setValue(4);
                }else if(j==2){
                    field[i][j].setValue(2);
                    field[i][9-j].setValue(2);
                    field[j][i].setValue(2);
                    field[9-j][i].setValue(2);
                }
            }
        }
        field[4][4].setValue(8);
        field[4][5].setValue(8);
        field[5][4].setValue(8);
        field[5][5].setValue(8);
        */
    }
    
    //盤面を表示
    void display(){
        System.out.println("    a b c d e f g h");
        for(int i=0;i<MAXWIDTH;i++){
            if(0<i && i<MAXWIDTH-1) System.out.print(i+" ");
            else System.out.print("  ");
            for(int j=0;j<MAXWIDTH;j++){
                System.out.print(field[i][j].getCharacter()+" ");
            }
            System.out.println("");
        }

        //盤面の評価値
        /*
        System.out.println("    a b c d e f g h");
        for(int i=0;i<MAXWIDTH;i++){
            if(0<i && i<MAXWIDTH-1) System.out.print(i+" ");
            else System.out.print("  ");
            for(int j=0;j<MAXWIDTH;j++){
                System.out.print(field[i][j].getValue()+" ");
            }
            System.out.println("");
        }
        */
    }

    //盤面の大きさを返す
    int getSize(){
        return MAXWIDTH;
    }

    //マスの状態を返す
    int getField(int row,int column){
        return field[row][column].getState();
    }

    //マスの更新
    void update(int row,int column,int player){
        if(player==1) field[row][column].setBlack();
        else if(player==2) field[row][column].setWhite();
    }

    //プレイヤーの石を数える
    int countStone(int num){
        int count=0;
        for(int i=1;i<=MAXWIDTH-2;i++){
            for(int j=1;j<=MAXWIDTH-2;j++){
                if(num==field[i][j].getState()) count++;
            }
        }
        return count;
    }

    //現局面の静的評価値を計算
    int getValue(int number){
        int value = 0;
        for(int i=1;i<=MAXWIDTH-2;i++){
            for(int j=1;j<=MAXWIDTH-2;j++){
                if(number==field[i][j].getState()) value += field[i][j].getValue();
            }
        }
        return value;
    }

    //盤面のコピー
    void copy(Board newBoard){
        newBoard.init();
        for(int i=0;i<MAXWIDTH;i++){
            for(int j=0;j<MAXWIDTH;j++){
                int state = field[i][j].getState();
                if(state==-1) newBoard.field[i][j].setWall();
                else if(state==1) newBoard.field[i][j].setBlack();
                else if(state==2) newBoard.field[i][j].setWhite();
            }
        }
    }
}