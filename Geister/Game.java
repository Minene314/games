package Geister;

import java.util.*;
import java.lang.Math;
 

class Game {
    Board board = new Board();
    private Input in = new Input();
    private Player[] players = new Player[2];
    private int player;
    private int dir[][] = {{-1,0},{0,-1},{0,1},{1,0}};  //方向

    Game() {
        players[0] = new Player("you");
        players[1] = new Player("com");
    }
    //ゲーム本体
    void play() {

        //盤面初期化と初期配置決定
        init();

        //先行決め
        Random r = new Random();
        player = r.nextInt(2);

        while(true) {
            int seed = 1;
            for(int i = 0; i < player; i++) seed *= -1;

            if(isGameOver(seed)) break;
            
            System.out.println("your turn: "+players[player].getName());
            System.out.println("you have taken blue piece: "+players[player].getTakenPieceB());
            System.out.println("you have taken red piece: "+players[player].getTakenPieceR());

            getMove(seed);
            
            player = opposite(player);
            board.display();
            

        }

        System.out.println("Winner: "+players[player].getName());
    }

    private int opposite(int player) {
        return (player+1)%2;
    }

    private void init() {
        ArrayList<ArrayList<Integer>> list = new ArrayList<ArrayList<Integer>>();
        for(int i = 2; i < 6; i++) {
            ArrayList<Integer> sub1 = new ArrayList<Integer>();
            sub1.add(1);
            sub1.add(i);
            list.add(sub1);
            ArrayList<Integer> sub2 = new ArrayList<Integer>();
            sub2.add(2);
            sub2.add(i);
            list.add(sub2);
        }

        board.init();

        Collections.shuffle(list);

        for(int i = 0; i < 4; i++) {
            int row = list.get(i).get(0);
            int column = list.get(i).get(1);
            System.out.println(row+","+column);
            board.setInitBlue(row, column);
        }
        board.display();

        int count = 0;
        while(count < 4) {
            String point = in.point("青駒の座標を入力してください(ex.(a1, f3))", count+1);
            if(point.length() != 2) System.out.println("ex.(a1, f3)");  //入力方法
            else{
                int column = point.charAt(0)-'a'+1;
                int row = point.charAt(1)-'1'+1;
                if(row < 5 || 6 < row || column < 2 || 5 < column) {
                    System.out.println("b-e列　かつ　5-6行のどこかを指定してください");
                }
                else if(board.getField(row, column) == 2) {
                    System.out.println("まだ駒が置かれていないところを指定してください");
                }
                else {
                    board.setInitBlue(row,column);
                    count++;
                }
                board.display();
            }
        }
    }

    private boolean isGameOver(int seed) {
        int count = 0;
        //自分ターンのはじめに脱出マスに青駒がある場合勝ち
        if(player == 0 && (board.getField(1, 1) == 2 || board.getField(1, 6) == 2)) return true;
        else if(player == 1 && (board.getField(6, 1) == -2 || board.getField(6, 6) == -2)) return true;

        //自分のターンのはじめに赤駒がすべてなくなっていれば勝ち
        if(player == 0) {
            count = 0;
            for(int i = 0; i < board.getSize(); i++) {
                for(int j = 0; j < board.getSize(); j++) {
                    if(board.getField(i, j) == 3) count++;
                }
            }
            if(count == 0) return true;
        }
        else if(player == 1) {
            count = 0;
            for(int i = 0; i < board.getSize(); i++) {
                for(int j = 0; j < board.getSize(); j++) {
                    if(board.getField(i, j) == -3) count++;
                }
            }
            if(count == 0) return true;
        }

        //自分のターンのはじめに青駒がすべてなくなっていれば負け
        if(player == 0) {
            count = 0;
            for(int i = 0; i < board.getSize(); i++) {
                for(int j = 0; j < board.getSize(); j++) {
                    if(board.getField(i, j) == 2) count++;
                }
            }
            if(count == 0){
                player = opposite(player);
                return true;
            }
        }
        else if(player == 1) {
            count = 0;
            for(int i = 0; i < board.getSize(); i++) {
                for(int j = 0; j < board.getSize(); j++) {
                    if(board.getField(i, j) == -2) count++;
                }
            }
            if(count == 0) {
                player = opposite(player);
                return true;
            }
        }
        return false;
    }


    private void getComMove() {
        int count;
        int point;
        int cRow, nRow;
        int cColumn, nColumn;
        Random r = new Random();
        int[] candPoint = new int[32];
        int [] nextPoint = new int[4];
        candPoint = getCandPoint();
        count = countCandPoint(candPoint, 32);
        point = candPoint[r.nextInt(count)];
        cColumn = point/10;
        cRow = point%10;

        nextPoint = getNextPoint(cRow, cColumn);
        System.out.println((char)(cColumn+'a'-1)+","+cRow);
        System.out.println();
        for(int i=0;i<4;i++) System.out.println((char)(nextPoint[i]/10+'a'-1)+","+nextPoint[i]%10);
        count = countCandPoint(nextPoint, 4);
        point = nextPoint[r.nextInt(count)];
        nColumn = point/10;
        nRow = point%10;

        move(cRow, cColumn, nRow, nColumn);

    }

    private void move(int cRow, int cColumn, int nRow, int nColumn) {
        if((board.getField(nRow, nColumn) != 0)) {
            players[player].addTakenPiece(board.getField(nRow, nColumn));
            board.takePiece(nRow, nColumn);
        }
        board.move(cRow, cColumn, nRow, nColumn);
    }

    private int[] getNextPoint(int row, int column) {
        int[] nextPoint = new int[4];
        int count = 0;
        for(int i = 0; i < 4; i++) {
            if(board.getField(row+dir[i][0],column+dir[i][1]) >= 0){
                nextPoint[count] = 10*(column+dir[i][1]) + row+dir[i][0];
                count++;
            }
        }
        return nextPoint;
    }

    private int[] getCandPoint() {
        int count = 0;
        int[] candPoint = new int[36];
        for(int i = 0; i < board.getSize(); i++) {
            for(int j = 0; j < board.getSize(); j++) {
                if(board.getField(i, j) < -1){
                    for(int k = 0; k < 4; k++) {
                        if(board.getField(i+dir[k][0],j+dir[k][1]) >= 0){
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
    private void getMove(int seed) {
        if(player == 1) getComMove();
        else
        while(true) {
            System.out.println("which Piece >");
            String cPoint = in.point();
            if(cPoint.length() == 2) {
                int cColumn = cPoint.charAt(0)-'a'+1;
                int cRow = cPoint.charAt(1)-'1'+1;
                if(0 <= cColumn && cColumn < 8 && 0 <= cRow && cRow < 8) {
                    if((board.getField(cRow, cColumn) != -1) && (board.getField(cRow, cColumn)*seed > 0)) {
                        System.out.println("where >");
                        String nPoint = in.point();
                        if(nPoint.length() == 2) {
                            int nColumn = nPoint.charAt(0)-'a'+1;
                            int nRow = nPoint.charAt(1)-'1'+1;
                            if(0 <= nColumn && nColumn < 8 && 0 <= nRow && nRow < 8) {
                                if((board.getField(nRow, nColumn) != -1) && (board.getField(nRow, nColumn)*seed <= 0)
                                && (Math.abs(cRow-nRow)+Math.abs(cColumn-nColumn) == 1)) {
                                    if((board.getField(nRow, nColumn) != 0)) {
                                        players[player].addTakenPiece(board.getField(nRow, nColumn));
                                        board.takePiece(nRow, nColumn);
                                    }
                                    board.move(cRow, cColumn, nRow, nColumn);
                                    break;
                                }
                            }
                        }
                    }
                }
                
            }
        }
    }
}