package Geister;

import java.util.*;
import java.lang.Math;
 

class Game {
    Board board = new Board();
    Board[] simuBoard = new Board[70];
    private Input in = new Input();
    private Player[] players = new Player[2];
    private int player;

    Game() {
        players[0] = new Player("you", 1);
        players[1] = new Player("com", -1);
    }
    //ゲーム本体
    void play() {

        //盤面初期化と初期配置決定
        init();

        //先行決め
        Random r = new Random();
        player = r.nextInt(2);

        int move;
        while(true) {
            move = 0;
            if(isGameOver()) break;
            
            System.out.println("your turn: "+players[player].getName());
            System.out.println("you have taken blue piece: "+players[player].getTakenPieceB());
            System.out.println("you have taken red piece: "+players[player].getTakenPieceR());

            if(player == 1) move = players[player].getComMove(board);
            else move = getPointAndMove();
            takeAndMove(move);

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
        board.setInitPiece();

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
            String point = in.point("Where to place blue piece?(ex.(a1, f3)) >", count+1);
            if(point.length() != 2) System.out.println("ex.(a1, f3)");  //入力方法
            else{
                int column = point.charAt(0)-'a'+1;
                int row = point.charAt(1)-'1'+1;
                if(row < 5 || 6 < row || column < 2 || 5 < column) {
                    System.out.println("Column of 'b-e'　And　Row of '5-6'");
                }
                else if(board.getField(row, column) == 2) {
                    System.out.println("There is already a blue piece");
                }
                else {
                    board.setInitBlue(row,column);
                    count++;
                }
                board.display();
            }
        }
    }


    private boolean isGameOver() {
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

    private int getPointAndMove() {
        int move = 0;
        String point;
        int row;
        int column;

        while(true){
            move = 0;
            row = 0;
            column = 0;

            point = in.point("Which piece?(ex: d4, e2) >");
            if(point.length() == 2) {
                column = point.charAt(0)-'a'+1;
                row = point.charAt(1)-'1'+1;
            }

            if(isLegalPoint(row, column)) {
                move += column*10 + row;
                row = 0;
                column = 0;

                point = in.point("Whare to place?(ex: d4, e2) >");
                if(point.length() == 2) {
                    column = point.charAt(0)-'a'+1;
                    row = point.charAt(1)-'1'+1;
                }

                if(isLegalMove(move%10, move/10, row, column)) {
                    move *= 100;
                    move += column*10 + row;

                    return move;
                }

            }
        }
    }

    private boolean isLegalPoint(int row, int column) {
        if(0 <= column && column < 8 && 0 <= row && row < 8)
            if((board.getField(row, column) != -1) && (board.getField(row, column)*players[player].getNum() > 0))
                return true;
        
        return false;
    }

    private boolean isLegalMove(int beforeRow, int beforeColumn, int afterRow, int afterColumn) {
        if(0 <= afterColumn && afterColumn < 8 && 0 <= afterRow && afterRow < 8)
            if((board.getField(afterRow, afterColumn) != -1) && (board.getField(afterRow, afterColumn)*players[player].getNum() <= 0)
            && (Math.abs(beforeRow-afterRow)+Math.abs(beforeColumn-afterColumn) == 1))
                return true;
        
        return false;
    }

    private void takeAndMove(int move) {
        int beforeRow;
        int beforeColumn;
        int afterRow;
        int afterColumn;

        afterRow = move%10;
        move /= 10;
        afterColumn = move%10;
        move /= 10;
        beforeRow = move%10;
        move /= 10;
        beforeColumn = move;
        
        if((board.getField(afterRow, afterColumn) != 0)) {
            players[player].addTakenPiece(board.getField(afterRow, afterColumn));
            board.takePiece(afterRow, afterColumn);
        }

        board.move(beforeRow, beforeColumn, afterRow, afterColumn);
    }
}