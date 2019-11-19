package Othello;
import java.util.Random;

class Player{
    private String name;  //プレイヤー名
    private int num;  //プレイヤーの石の割り当て
    private int placedStone;  //すでに置かれた石の数
    private int candMove[] = new int[100];  //プレイヤーの候補手
    private Board board = new Board();  //現局面
    private int dir[][] = {{-1,-1},{-1,0},{-1,1},{0,-1},{0,1},{1,-1},{1,0},{1,1}};  //方向
    private static final int MMDEPTH = 4;  //mini-max法の読む深さ(5までなら割とスムーズ)
    private static final int ABDEPTH = 7;  //αβ法の読む深さ(8までなら割とスムーズ)
    private static final int MONTIME = 3000;  //モンテカルロシミュレーションの秒数(ms)

    Player(String inputName,int inputNum){
        name = inputName;
        num = inputNum;
        placedStone = 2;
    }

    String getName(){
        return name;
    }
    int getNum(){
        return num;
    }

    int getPlacedStone() {
        return placedStone;
    }
    void setPlacedStone(int num){
        placedStone = num;
    }

    //ランダムムーブ
    int randGetMove(Board nowBoard){
        int count;  //候補手の数
        int move;  //着手
        int[] nextCand; 
        Random r = new Random();

        nextCand = getLegalMove(nowBoard,num);
        count = countCand(nextCand);  //候補手の数
        move = nextCand[r.nextInt(count)];  //着手をランダムに決定

        return move;
    }

    private int randGetMove(Board nowBoard,int number){
        int count;  //候補手の数
        int move;  //着手
        int[] nextCand; 
        Random r = new Random();

        nextCand = getLegalMove(nowBoard,number);
        count = countCand(nextCand);  //候補手の数
        move = nextCand[r.nextInt(count)];  //着手をランダムに決定

        return move;
    }

    //ネガマックス法
    int negaGetMove(Board realBoard,int[] nowCandMove){
        int count;  //候補手の数
        int depth = 0;  //深さ
        int move = 0;  //着手
        int x;  //返ってきた評価値
        int max = -999999;  //評価値の最大
        Board nowBoard = new Board();  //現局面

        realBoard.copy(nowBoard);  
        candMove = nowCandMove;
        count = countCand();

        //cpuの挙動を見たくなければ、ここと、8行下と、17行下の出力をコメントアウト
        System.out.println("I'm thinking...");
        for(int i=0;i<count;i++){

            Board nextBoard = new Board();  //次の局面

            //着手の座標計算
            int column = candMove[i]/10;
            int row = candMove[i]%10;
            System.out.println("("+(char)(column+'a'-1)+","+row+")");

            nextBoard = reverse(row,column,nowBoard,num);  //ひっくり返し処理

            Board mirBoard = new Board();
            nextBoard.copy(mirBoard);  //現局面をコピー

            x = -negaMax(mirBoard,opposite(num),depth);  //次の深さへ

            System.out.println("Value: "+x);
            
            if(x>max){
                max = x;
                move = candMove[i];  //指し手を更新
            }
        }
        
        return move;
    }

    //ネガマックス法本体
    private int negaMax(Board nowBoard,int number,int depth){
        if(!existLegalMove(nowBoard,number) || depth==MMDEPTH){  //次の手がないか深さが一定に達した
            
            int ev;
            ev = (evaluation(nowBoard,num)-evaluation(nowBoard,opposite(num)));  //評価値取得
            //System.out.println("EV: "+ev);
            if(number!=num) return -ev;
            return ev;
        }
        int count = 0;
        int max = -999999;
        int[] nextCand = new int[100];
        int x;
        Board newBoard = new Board();

        nowBoard.copy(newBoard);
        nextCand = getLegalMove(nowBoard,number);
        count = countCand(nextCand);

        for(int i=0;i<count;i++){
            Board nextBoard = new Board();
            int column = nextCand[i]/10;
            int row = nextCand[i]%10;

            nextBoard = reverse(row,column,nowBoard,number);
            
            Board mirBoard = new Board();
            nextBoard.copy(mirBoard);

            x = -negaMax(mirBoard,opposite(number),depth+1);
            if(x>max) max = x;
        }
        return max;
    }

    private int[][] sortCandMove(int[][] nextCand,int count){
        int tmp;

        for(int i=0;i<count-1;i++){
            for(int j=count-1;j>i;j--){
                if(nextCand[j-1][1]<nextCand[j][1]){
                    //System.out.println(i+","+j+"swaped");
                    tmp = nextCand[j][0];
                    nextCand[j][0] = nextCand[j-1][0];
                    nextCand[j-1][0] = tmp;
                    tmp = nextCand[j][1];
                    nextCand[j][1] = nextCand[j-1][1];
                    nextCand[j-1][1] = tmp;
                }
            }
        }
        return nextCand;
    }

    //αβ法
    int ABGetMove(Board realBoard){
        int count;
        int depth = 0;
        int move = 0;
        int x;
        int max = -999999;
        Board nowBoard = new Board();
        int[][] nextCand = new int[100][2];

        realBoard.copy(nowBoard);
        nextCand = getLegalMove(nowBoard,num,nextCand);
        count = countCand(nextCand);
        
        for(int i=0;i<count;i++){
            Board tmpBoard = new Board();
            int column = nextCand[i][0]/10;
            int row = nextCand[i][0]%10;
            System.out.print("("+(char)(column+'a'-1)+","+row+")");
            
            tmpBoard = reverse(row,column,nowBoard,num);
            nextCand[i][1] = (evaluation(tmpBoard,num)-evaluation(tmpBoard,opposite(num)));
            System.out.println(nextCand[i][1]);
        }
        nextCand = sortCandMove(nextCand,count);
        
        System.out.println("sortedCandMove");
        for(int i=0;i<count;i++){
            int column = nextCand[i][0]/10;
            int row = nextCand[i][0]%10;
            System.out.println("("+(char)(column+'a'-1)+","+row+")"+nextCand[i][1]);
        }
        


        //cpuの挙動を見たくなければ、ここと、6行下と、13行下の出力をコメントアウト
        System.out.println("I'm thinking...");
        for(int i=0;i<count;i++){
            Board nextBoard = new Board();
            int column = nextCand[i][0]/10;
            int row = nextCand[i][0]%10;
            System.out.println("("+(char)(column+'a'-1)+","+row+")");
            
            nextBoard = reverse(row,column,nowBoard,num);
            Board mirBoard = new Board();
            nextBoard.copy(mirBoard);
            
            x = -ABnegaMax(mirBoard,opposite(num),depth,-999999,-max);
            System.out.println("Value: "+x);
            
            if(x>max){
                //System.out.println("最善手更新"+move+"->"+candMove[i]);
                max = x;
                move = nextCand[i][0];
            }
        }
        
        return move;
    }
    

    //αβ法本体
    private int ABnegaMax(Board nowBoard,int number,int depth,int alpha,int beta){
        int[][] nextCand = new int[100][2];
        int count;
        if((!existLegalMove(nowBoard,number) || depth==ABDEPTH)){
            /*
            if(existLegalMove(nowBoard,number) && (number==num)){
                nextCand = getLegalMove(nowBoard,number);
                count = countCand(nextCand);
                return -((evaluation(nowBoard,number)-evaluation(nowBoard,opposite(number)))+count*5);
            }
            */
            int ev;
            ev = (evaluation(nowBoard,num)-evaluation(nowBoard,opposite(num)));
            //System.out.println("EV: "+ev);
            if(number!=num) return -ev;
            return ev;
        }
        
        int x;
        int max = alpha;
        Board newBoard = new Board();

        nowBoard.copy(newBoard);
        nextCand = getLegalMove(nowBoard,number,nextCand);
        count = countCand(nextCand);
        //System.out.println("depth: "+depth+" alpha: "+alpha+" beta: "+beta);

        
        for(int i=0;i<count;i++){
            Board tmpBoard = new Board();
            int column = nextCand[i][0]/10;
            int row = nextCand[i][0]%10;
            //System.out.print("("+(char)(column+'a'-1)+","+row+")");
            
            tmpBoard = reverse(row,column,nowBoard,num);
            nextCand[i][1] = (evaluation(tmpBoard,num)-evaluation(tmpBoard,opposite(num)));
            //System.out.println(nextCand[i][1]);
        }
        nextCand = sortCandMove(nextCand,count);
        
        for(int i=0;(i<count);i++){
            Board nextBoard = new Board();
            int column = nextCand[i][0]/10;
            int row = nextCand[i][0]%10;

            nextBoard = reverse(row,column,nowBoard,number);
            
            Board mirBoard = new Board();
            nextBoard.copy(mirBoard);

            //System.out.println("("+(char)(column+'a'-1)+","+row+")");

            x = -ABnegaMax(mirBoard,opposite(number),depth+1,-beta,-max);
            if(x>=beta){
                //System.out.println("beta切り上げ "+x);
                return x;
            }
            if(x>max){
                //System.out.println("max更新"+x);
                max = x;
            }
        }
        return max;
    }

    int monGetMove(Board realBoard){
        int[] nextCand = getLegalMove(realBoard,num);
        int count = countCand(nextCand);
        int countSim = 0;
        double[] winPer = new double[count];
        long start,end;
        start = end = System.currentTimeMillis();
        //時間で行う
        while(end-start<=MONTIME){
            countSim++;
            for(int i=0;i<count;i++){
                int number = num;
                int column = nextCand[i]/10;
                int row = nextCand[i]%10;
                Board monBoard = new Board();
                realBoard.copy(monBoard);
                monBoard = reverse(row,column,monBoard,number);
                number = opposite(number);
                while(true){
                    if(!existLegalMove(monBoard,number)){
                        number = opposite(number);
                        if(!existLegalMove(monBoard,number)) break;
                    }
                    int move = randGetMove(monBoard,number);
                    int c = move/10;
                    int r = move%10;
                    monBoard = reverse(r,c,monBoard,number);
                    number = opposite(number);
                }
                if(monBoard.countStone(num) > monBoard.countStone(opposite(number))){
                    winPer[i] = winPer[i] + 1.0;
                }
            }
            end = System.currentTimeMillis();
        }
        for(int i=0;i<count;i++) winPer[i] = winPer[i]/(double)countSim;
        double maxPer = winPer[0];
        int maxMove = 0;
        for(int i=0;i<count;i++){
            if(maxPer<winPer[i]){
                maxPer = winPer[i];
                maxMove = i;
            }
        }
        for(int i=0;i<count;i++){
            System.out.print("candMove: ("+(char)(nextCand[i]/10+'a'-1)+","+nextCand[i]%10+")");
            System.out.println(", WinPer: "+winPer[i]);
        }
        System.out.println("done "+(countSim*count)+" Playout");
        return nextCand[maxMove];

    }

    //評価関数
    private int evaluation(Board nowBoard,int number){
        int value;
        value = nowBoard.getValue(number);

        return value;
    }

    //ひっくり返しシミュレーション
    private Board reverse(int row,int column,Board nowBoard,int number){
        Board nextBoard = new Board();
        nowBoard.copy(nextBoard);
        //方向をまわすfor文
        for(int i=0;i<8;i++){
            int count = 0;  //ひっくり返せる枚数
            count = countReversibleStone(row,column,dir[i][0],dir[i][1],number);
            if(count>0){
                for(int j=1;j<=count;j++){  //反対の自分の駒までひっくり返し
                    nextBoard.update(row+dir[i][0]*j,column+dir[i][1]*j,number);
                }
            }
        }
        nextBoard.update(row,column,number);  //置いた駒

        return nextBoard;
    }

    //候補手の数を数える
    private int countCand(){
        int count = 0;
        while(candMove[count]!=0) count++;
        return count;
    }
    private int countCand(int[] nextCand){
        int count = 0;
        while(nextCand[count]!=0) count++;
        return count;
    }
    private int countCand(int[][] nextCand){
        int count = 0;
        while(nextCand[count][0]!=0) count++;
        return count;
    }

    //着手できる合法手があるか
    private boolean existLegalMove(Board nowBoard,int number){
        board = nowBoard;
        //盤面の大きさ
        int field = board.getSize()-2;

        for(int i=1;i<=field;i++){
            for(int j=1;j<=field;j++){
                if(isLegalMove(i,j,number)) return true;
            }
        }
        return false;
    }

    boolean existLegalMove(Board nowBoard){
        board = nowBoard;
        //盤面の大きさ
        int field = board.getSize()-2;

        for(int i=1;i<=field;i++){
            for(int j=1;j<=field;j++){
                if(isLegalMove(i,j)) return true;
            }
        }
        return false;
    }

    //合法手かどうか
    private boolean isLegalMove(int row,int column){
        //盤面の大きさ
        int field = board.getSize()-2;

        //盤外かどうか
        if(row<1 || field<row || column<1 || field<column) return false;
        //そこに駒を置けるか
        if(board.getField(row,column)!=0) return false;

        //方向をまわすfor文
        for(int i=0;i<8;i++){
            int count = 0;  //ひっくり返せる枚数

            count = countReversibleStone(row,column,dir[i][0],dir[i][1]);

            if(count>0) return true;
        }
        return false;
    }

    //合法手かどうか
    private boolean isLegalMove(int row,int column,int number){
        //盤面の大きさ
        int field = board.getSize()-2;

        //盤外かどうか
        if(row<1 || field<row || column<1 || field<column) return false;
        //そこに駒を置けるか
        if(board.getField(row,column)!=0) return false;

        //方向をまわすfor文
        for(int i=0;i<8;i++){
            int count = 0;  //ひっくり返せる枚数

            count = countReversibleStone(row,column,dir[i][0],dir[i][1],number);

            if(count>0) return true;
        }
        return false;
    }

    //ひっくり返せる駒の数え上げ
    private int countReversibleStone(int row,int column, int s, int t,int number){
        //盤面の大きさ
        int field = board.getSize()-2;
        //自分の駒と駒の間の数
        int contents;
        //置けるかどうか
        boolean judge;

        //初期化
        contents = 0;
        judge = false;

        for(int j=1;j<=field;j++){  //直線の調査
            if(board.getField(row+s*j,column+t*j)==-1) break;  //壁判定
            if(board.getField(row+s*j,column+t*j)==number){  //反対の自分の駒発見
                contents = j-1;
                break;
            }
        }
        if(contents>0){  //駒と駒の間にマスがある
            judge = true;
            for(int j=1;j<=contents;j++){  //間のマスの調査
                if(board.getField(row+s*j,column+t*j)!=opposite(number)){  //相手の駒以外だったらfalse
                    judge = false;
                    break;
                }
            }
        }
        if(judge){
            return contents;
        }
        return 0;
    }

    //ひっくり返せる駒の数え上げ
    private int countReversibleStone(int row,int column, int s, int t){
        //盤面の大きさ
        int field = board.getSize()-2;
        //自分の駒と駒の間の数
        int contents;
        //置けるかどうか
        boolean judge;

        //初期化
        contents = 0;
        judge = false;

        for(int j=1;j<=field;j++){  //直線の調査
            if(board.getField(row+s*j,column+t*j)==-1) break;  //壁判定
            if(board.getField(row+s*j,column+t*j)==num){  //反対の自分の駒発見
                contents = j-1;
                break;
            }
        }
        if(contents>0){  //駒と駒の間にマスがある
            judge = true;
            for(int j=1;j<=contents;j++){  //間のマスの調査
                if(board.getField(row+s*j,column+t*j)!=opposite(num)){  //相手の駒以外だったらfalse
                    judge = false;
                    break;
                }
            }
        }
        if(judge){
            return contents;
        }
        return 0;
    }

    //候補手の列挙
    int[] getLegalMove(){
        int count = 0;
        int field = board.getSize()-2;
        int dimention = board.getSize()*board.getSize();
        int legalMove[] = new int[dimention];
        for(int i=0;i<dimention;i++){
            legalMove[i] = 0;
        }
        /*
        for(int j=1;j<=field;j++){
            if(isLegalMove(1,j)){
                legalMove[count] = j*10+1;
                count++;
            }
            if(isLegalMove(8,j)){
                legalMove[count] = j*10+8;
                count++;
            }
        }
        for(int i=2;i<field;i++){
            for(int j=1;j<=field;j++){
                if(isLegalMove(i,j)){
                    legalMove[count] = j*10+i;
                    count++;
                }
            }
        }
        */
        
        for(int i=1;i<=field;i++){
            for(int j=1;j<=field;j++){
                if(isLegalMove(i,j)){
                    legalMove[count] = j*10+i;
                    count++;
                }
            }
        }
        

        return legalMove;
    }

    private int[] getLegalMove(Board nowBoard,int number){
        int count = 0;
        int field = nowBoard.getSize()-2;
        int dimention = nowBoard.getSize()*board.getSize();
        int legalMove[] = new int[dimention];
        for(int i=0;i<dimention;i++){
            legalMove[i] = 0;
        }

        /*
        for(int j=1;j<=field;j++){
            if(isLegalMove(1,j,number)){
                legalMove[count] = j*10+1;
                count++;
            }
            if(isLegalMove(8,j,number)){
                legalMove[count] = j*10+8;
                count++;
            }
        }
        for(int i=2;i<field;i++){
            for(int j=1;j<=field;j++){
                if(isLegalMove(i,j,number)){
                    legalMove[count] = j*10+i;
                    count++;
                }
            }
        }
        */
        for(int i=1;i<=field;i++){
            for(int j=1;j<=field;j++){
                if(isLegalMove(i,j,number)){
                    legalMove[count] = j*10+i;
                    count++;
                }
            }
        }
        

        return legalMove;
    }

    private int[][] getLegalMove(Board nowBoard,int number,int[][] legalMove){
        int count = 0;
        int field = nowBoard.getSize()-2;
        int dimention = nowBoard.getSize()*board.getSize();
        for(int i=0;i<dimention;i++){
            legalMove[i][0] = 0;
            legalMove[i][1] = 0;
        }

        /*
        for(int j=1;j<=field;j++){
            if(isLegalMove(1,j,number)){
                legalMove[count] = j*10+1;
                count++;
            }
            if(isLegalMove(8,j,number)){
                legalMove[count] = j*10+8;
                count++;
            }
        }
        for(int i=2;i<field;i++){
            for(int j=1;j<=field;j++){
                if(isLegalMove(i,j,number)){
                    legalMove[count] = j*10+i;
                    count++;
                }
            }
        }
        */
        for(int i=1;i<=field;i++){
            for(int j=1;j<=field;j++){
                if(isLegalMove(i,j,number)){
                    legalMove[count][0] = j*10+i;
                    count++;
                }
            }
        }
        

        return legalMove;
    }

    //候補手の表示
    void displayLegalMove(){
        //盤面の大きさ
        int field = board.getSize()-2;

        for(int i=1;i<=field;i++){
            for(int j=1;j<=field;j++){
                if(isLegalMove(i,j)) System.out.print("("+(char)(j+'a'-1)+","+i+") ");
            }
        }
        System.out.println();
    }

    //相手の駒
    private int opposite(int player){
        return 3-player;
    }

}