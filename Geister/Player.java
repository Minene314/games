package Geister;


import java.util.*;
import java.lang.Math;

class Player {
    String name;
    int num;
    int takenPieceB;
    int takenPieceR;
    Board nowBoard = new Board();
    private int dir[][] = {{-1,0},{0,-1},{0,1},{1,0}};  //方向


    Player(String str, int n) {
        name = str;
        num = n;
        takenPieceB = 0;
        takenPieceR = 0;
    }

    void setName(String str) {
        name = str;
    }
    int getNum() {
        return num;
    }

    void addTakenPiece(int piece){
        if(piece == 2 || piece ==-2) takenPieceB++;
        else takenPieceR++;
    }
    
    String getName() {
        return name;
    }

    int getTakenPieceB() {
        return takenPieceB;
    }

    int getTakenPieceR() {
        return takenPieceR;
    }

    private int[] getNextPoint(int row, int column) {
        int[] nextPoint = new int[4];
        int count = 0;
        for(int i = 0; i < 4; i++) {
            if(nowBoard.getField(row+dir[i][0],column+dir[i][1]) >= 0){
                nextPoint[count] = 10*(column+dir[i][1]) + row+dir[i][0];
                count++;
            }
        }
        return nextPoint;
    }

    private int[] getCandPoint() {
        int count = 0;
        int[] candPoint = new int[36];
        for(int i = 0; i < nowBoard.getSize(); i++) {
            for(int j = 0; j < nowBoard.getSize(); j++) {
                if(nowBoard.getField(i, j) < -1){
                    for(int k = 0; k < 4; k++) {
                        if(nowBoard.getField(i+dir[k][0],j+dir[k][1]) >= 0){
                            candPoint[count] = 10*j + i;
                            count++;
                            break;
                        }
                    }
                }
            }
        }
        return candPoint;
    }

    private int countCandPoint(int[] candPoint, int n){
        int count = 0;
        for(int i = 0; i < n; i++){
            if(candPoint[i]!=0) count++;
        }
        return count;
    }
    
    int getComMove(Board realBoard) {
        realBoard.copy(nowBoard);
        int move = 0;
        int point;
        int row;
        int column;

        int count;
        Random r = new Random();
        int[] candPoint = new int[32];
        int [] nextPoint = new int[4];

        candPoint = getCandPoint();
        count = countCandPoint(candPoint, 32);
        System.out.println("counr: "+count);
        point = candPoint[r.nextInt(count)];
        column = point/10;
        row = point%10;
        move = point*100;

        nextPoint = getNextPoint(row, column);
        System.out.println((char)(column+'a'-1)+","+row);
        System.out.println();
        for(int i=0;i<4;i++) System.out.println((char)(nextPoint[i]/10+'a'-1)+","+nextPoint[i]%10);
        count = countCandPoint(nextPoint, 4);
        point = nextPoint[r.nextInt(count)];
        move += point;

        System.out.println("move: "+move);

        return move;
    }

}