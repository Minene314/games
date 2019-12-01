package Geister;

import java.util.*;
import java.lang.Math;
import java.text.NumberFormat;

class Player {
    private String name;
    private int num;
    private int takenPieceB;
    private int takenPieceR;
    private static final int MONTIME = 4000;
    private static final int MAXWORLD = 70;
    private static final int MAXMOVES = 100;
    private int dir[][] = {{-1,0},{0,-1},{0,1},{1,0}};  //方向
    private Board[] simuBoard = new Board[MAXWORLD];
    private int[] world = new int[MAXWORLD];


    Player(String str, int n) {
        name = str;
        num = n;
        takenPieceB = 0;
        takenPieceR = 0;
        for(int i = 0; i < MAXWORLD; i++){
            simuBoard[i] = new Board();
            world[i] = 1;
        }
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

    private int[] getNextPoint(int row, int column, Board nowBoard) {
        int[] nextPoint = new int[4];
        int count = 0;
        for(int i = 0; i < 4; i++) {
            if(nowBoard.getField(row+dir[i][0],column+dir[i][1])*num < -1 || nowBoard.getField(row+dir[i][0],column+dir[i][1]) == 0){
                nextPoint[count] = 10*(column+dir[i][1]) + row+dir[i][0];
                count++;
            }
        }
        return nextPoint;
    }

    private int[] getNextPoint(int row, int column, Board nowBoard, int number) {
        int[] nextPoint = new int[4];
        int count = 0;
        for(int i = 0; i < 4; i++) {
            if(nowBoard.getField(row+dir[i][0],column+dir[i][1])*number < -1 || nowBoard.getField(row+dir[i][0],column+dir[i][1]) == 0){
                nextPoint[count] = 10*(column+dir[i][1]) + row+dir[i][0];
                count++;
            }
        }
        return nextPoint;
    }

    private int[] getCandPoint(Board nowBoard) {
        int count = 0;
        int[] candPoint = new int[8];
        for(int i = 0; i < nowBoard.getSize(); i++) {
            for(int j = 0; j < nowBoard.getSize(); j++) {
                if(nowBoard.getField(i, j)*num > 1){
                    for(int k = 0; k < 4; k++) {
                        if(nowBoard.getField(i+dir[k][0],j+dir[k][1])*num < -1 || nowBoard.getField(i+dir[k][0],j+dir[k][1]) == 0){
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

    private int[] getCandPoint(Board nowBoard, int number) {
        int count = 0;
        int[] candPoint = new int[8];
        for(int i = 0; i < nowBoard.getSize(); i++) {
            for(int j = 0; j < nowBoard.getSize(); j++) {
                if(nowBoard.getField(i, j)*number > 1){
                    for(int k = 0; k < 4; k++) {
                        if(nowBoard.getField(i+dir[k][0],j+dir[k][1])*number < -1 || nowBoard.getField(i+dir[k][0],j+dir[k][1]) == 0){
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
        Board nowBoard = new Board();
        realBoard.copy(nowBoard);
        int move = 0;
        int point;
        int row;
        int column;

        int count;
        Random r = new Random();
        int[] candPoint = new int[8];
        int [] nextPoint = new int[4];

        candPoint = getCandPoint(nowBoard);
        count = countCandPoint(candPoint, 8);
        System.out.println("count: "+count);
        point = candPoint[r.nextInt(count)];
        column = point/10;
        row = point%10;
        move = point*100;

        nextPoint = getNextPoint(row, column, nowBoard);
        System.out.println((char)(column+'a'-1)+","+row);
        System.out.println();
        for(int i=0;i<4;i++) System.out.println((char)(nextPoint[i]/10+'a'-1)+","+nextPoint[i]%10);
        count = countCandPoint(nextPoint, 4);
        point = nextPoint[r.nextInt(count)];
        move += point;

        System.out.println("move: "+move);

        return move;
    }

    int[] getAllMove(Board nowBoard, int number) {
        int row;
        int column;
        int count = 0;

        int candCount;
        int nextCount;
        int[] candPoint = new int[8];
        int[] candMove = new int[32];

        candPoint = getCandPoint(nowBoard, number);
        candCount = countCandPoint(candPoint, 8);

        for(int i = 0; i < candCount; i++) {
            column = candPoint[i]/10;
            row = candPoint[i]%10;

            int[] nextPoint = new int[4];
            nextPoint = getNextPoint(row, column, nowBoard, number);
            nextCount = countCandPoint(nextPoint, 4);

            for(int j = 0; j < nextCount; j++) {
                candMove[count] = candPoint[i]*100 + nextPoint[j];
                count++;
            }
        }
        return candMove;
    }

    int getMonCom(Board realBoard) {
        long start,end;

        int[] candMove = new int[32];
        long countSimu = 0;
        int count;
        int vic = 0;

        candMove = getAllMove(realBoard, num);
        count = countCandPoint(candMove, 32);

        double[] winPer = new double[count];
        long[] winCnt = new long[count];
        long[] garb = new long[count];

        start = end = System.currentTimeMillis();
        while(end-start <= MONTIME) {
            countSimu++;

            //手をまわす
            for(int i = 0; i < count; i++) {
                //世界をまわす
                for(int j = 0; j < MAXWORLD; j++) {
                    if(world[j] == 1) {
                        Board nowBoard = new Board();
                        simuBoard[j].copy(nowBoard);
                        takeAndMove(candMove[i], nowBoard);

                        vic = playSimu(nowBoard);

                        if(vic == num) winCnt[i]++;
                        else if(vic == 0) garb[i]++;
                    }
                }
            }
            end = System.currentTimeMillis();
        }

        System.out.println("world: "+countCandPoint(world, MAXWORLD));
        for(int i = 0; i < count; i++) winPer[i] = (double)winCnt[i]/(double)((countSimu*countCandPoint(world, MAXWORLD))-garb[i]);

        double maxPer = winPer[0];
        int maxMove = 0;

        for(int i = 0; i < count; i++){
            if(maxPer < winPer[i]){
                maxPer = winPer[i];
                maxMove = i;
            }
        }

        for(int i=0;i<count;i++){
            System.out.print("candMove: ("+(char)(candMove[i]/1000+'a'-1)+","+((candMove[i]%1000)/100)+")->("+(char)((candMove[i]%100)/10+'a'-1)+","+(candMove[i]%10)+")");
            System.out.print(", WinPer: "+String.format("%.4f", winPer[i]));
            System.out.print(", Value: "+String.format("%.4f", winPer[i]));
            System.out.println(", garbage simulation: "+garb[i]);
        }
        System.out.println("done "+(countSimu*countCandPoint(world, MAXWORLD))+" 全世界のPlayout");
        System.out.println("done "+(countSimu*count*countCandPoint(world, MAXWORLD))+" 全Playout");
        return candMove[maxMove];
    }

    int getTrustMon(Board realBoard) {
        long start,end;

        int[] candMove = new int[32];
        long countSimu = 0;
        int count;
        int vic = 0;

        candMove = getAllMove(realBoard, num);
        count = countCandPoint(candMove, 32);

        double[] winPer = new double[count];
        double[] trustPer = new double[count];
        long[] winCnt = new long[count];
        long[] garb = new long[count];

        start = end = System.currentTimeMillis();
        while(end-start <= MONTIME) {
            countSimu++;

            //手をまわす
            for(int i = 0; i < count; i++) {
                //世界をまわす
                for(int j = 0; j < MAXWORLD; j++) {
                    if(world[j] == 1) {
                        Board nowBoard = new Board();
                        simuBoard[j].copy(nowBoard);
                        takeAndMove(candMove[i], nowBoard);

                        vic = playSimu(nowBoard);

                        if(vic == num) winCnt[i]++;
                        else if(vic == 0) garb[i]++;
                    }
                }
            }
            end = System.currentTimeMillis();
        }

        System.out.println("world: "+countCandPoint(world, MAXWORLD));
        for(int i = 0; i < count; i++) winPer[i] = (double)winCnt[i]/(double)((countSimu*countCandPoint(world, MAXWORLD))-garb[i]);
        for(int i = 0; i < count; i++) trustPer[i] = winPer[i] * (1.0 - ((double)garb[i])/(double)(countSimu*countCandPoint(world, MAXWORLD)));

        double maxPer = trustPer[0];
        int maxMove = 0;

        for(int i = 0; i < count; i++){
            if(maxPer < trustPer[i]){
                maxPer = trustPer[i];
                maxMove = i;
            }
        }

        for(int i=0;i<count;i++){
            System.out.print("candMove: ("+(char)(candMove[i]/1000+'a'-1)+","+((candMove[i]%1000)/100)+")->("+(char)((candMove[i]%100)/10+'a'-1)+","+(candMove[i]%10)+")");
            System.out.print(", WinPer: "+String.format("%.4f", winPer[i]));
            System.out.print(", Value: "+String.format("%.4f", trustPer[i]));
            System.out.println(", garbage simulation: "+garb[i]);
        }
        System.out.println("done "+(countSimu*countCandPoint(world, MAXWORLD))+" 全世界のPlayout");
        System.out.println("done "+(countSimu*count*countCandPoint(world, MAXWORLD))+" 全Playout");
        return candMove[maxMove];
    }

    private int playSimu(Board board) {
        int vic = 0;
        int move;
        int player = opposite(num);
        int count = 0;
        while(true){
            count++;
            vic = isGameOver(board, player);
            if(vic != 0) break;
            move = getRandMove(board, player);
            takeAndMove(move, board);
            player = opposite(player);
            if(count > MAXMOVES) break;
        }
        return vic;
    }

    private int getRandMove(Board board, int number) {
        int[] candMove = new int[32];
        int count;
        int move;
        Random r = new Random();
        candMove = getAllMove(board, number);
        count = countCandPoint(candMove, 32);
        move = candMove[r.nextInt(count)];

        return move;
    }

    private int opposite(int number) {
        return number*(-1);
    }

    void updateSimuBoard(int move) {
        for(int i = 0; i < MAXWORLD; i++) {
            takeAndMove(move, simuBoard[i]);
        }
        //System.out.println("Sumulation Board!");
        //simuBoard[0].display();
    }

    void setSimuBoard(Board board) {
        for(int i = 0; i < MAXWORLD; i++) {
            board.copy(simuBoard[i]);
        }
    }

    void worldDelete(int row, int column, int color) {
        for(int i = 0; i < MAXWORLD; i++) {
            if((world[i] == 1) && (simuBoard[i].getField(row, column) != color)) world[i] = 0;
        }
        System.out.println("("+(char)(column+'a'-1)+","+row+"): color: "+color+" deleted!");
    }

    private int isGameOver(Board board, int player) {
        int count = 0;
        //自分ターンのはじめに脱出マスに青駒がある場合勝ち
        if(player == 1 && (board.getField(1, 1) == 2 || board.getField(1, 6) == 2)) return player;
        else if(opposite(player) == 1 && (board.getField(6, 1) == -2 || board.getField(6, 6) == -2)) return player;

        //自分のターンのはじめに赤駒がすべてなくなっていれば勝ち
        if(player == 1) {
            count = 0;
            for(int i = 0; i < board.getSize(); i++) {
                for(int j = 0; j < board.getSize(); j++) {
                    if(board.getField(i, j) == 3) count++;
                }
            }
            if(count == 0) return player;
        }
        else if(opposite(player) == 1) {
            count = 0;
            for(int i = 0; i < board.getSize(); i++) {
                for(int j = 0; j < board.getSize(); j++) {
                    if(board.getField(i, j) == -3) count++;
                }
            }
            if(count == 0) return player;
        }

        //自分のターンのはじめに青駒がすべてなくなっていれば負け
        if(player == 1) {
            count = 0;
            for(int i = 0; i < board.getSize(); i++) {
                for(int j = 0; j < board.getSize(); j++) {
                    if(board.getField(i, j) == 2) count++;
                }
            }
            if(count == 0){
                return opposite(player);
            }
        }
        else if(opposite(player) == 1) {
            count = 0;
            for(int i = 0; i < board.getSize(); i++) {
                for(int j = 0; j < board.getSize(); j++) {
                    if(board.getField(i, j) == -2) count++;
                }
            }
            if(count == 0) {
                return opposite(player);
            }
        }
        return 0;
    }

    private void takeAndMove(int move, Board board) {
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
            //addTakenPiece(board.getField(afterRow, afterColumn));
            board.takePiece(afterRow, afterColumn);
        }

        board.move(beforeRow, beforeColumn, afterRow, afterColumn);
    }

    void setSimuBoard(Board[] sBoard) {
        for(int i = 0; i < MAXWORLD; i++){
            sBoard[i].copy(simuBoard[i]);
        }
    }

    void createSimuBoard() {
        ArrayList<ArrayList<Integer>> lists = new ArrayList<ArrayList<Integer>>();
        if(num == -1) {
            for(int i = 2; i < 6; i++) {
                ArrayList<Integer> sub1 = new ArrayList<Integer>();
                sub1.add(5);
                sub1.add(i);
                lists.add(sub1);
                ArrayList<Integer> sub2 = new ArrayList<Integer>();
                sub2.add(6);
                sub2.add(i);
                lists.add(sub2);
            }
        }
        else if(num == 1) {
            for(int i = 2; i < 6; i++) {
                ArrayList<Integer> sub1 = new ArrayList<Integer>();
                sub1.add(1);
                sub1.add(i);
                lists.add(sub1);
                ArrayList<Integer> sub2 = new ArrayList<Integer>();
                sub2.add(2);
                sub2.add(i);
                lists.add(sub2);
            }
        }

        calc8C4(lists);
    }

    private void calc8C4(final ArrayList<ArrayList<Integer>> list) {
        int count = 0;
        int row;
        int column;
        for (int i = 0; i < list.size()-3; i++) {
            for (int j = i+1; j < list.size()-2; j++) {
                for (int k = j+1; k < list.size()-1; k++) {
                    for(int l = k+1; l < list.size(); l++) {
                        row = list.get(i).get(0);
                        column = list.get(i).get(1);
                        simuBoard[count].setInitBlue(row, column);
                        row = list.get(j).get(0);
                        column = list.get(j).get(1);
                        simuBoard[count].setInitBlue(row, column);
                        row = list.get(k).get(0);
                        column = list.get(k).get(1);
                        simuBoard[count].setInitBlue(row, column);
                        row = list.get(l).get(0);
                        column = list.get(l).get(1);
                        simuBoard[count].setInitBlue(row, column);
                        count++;
                    }
                }
            }
        }
    }
}