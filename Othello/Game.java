package Othello;

class Game{
    private int player;  //現在のプレイヤー
    private int victory;  //勝者
    private Input in = new Input();  //入力
    private Player players[] = new Player[2];  //ゲームプレイヤー
    private Board board = new Board();  //盤面
    private int row,column;  //着手
    private int mode = -1;
    private int dir[][] = {{-1,-1},{-1,0},{-1,1},{0,-1},{0,1},{1,-1},{1,0},{1,1}};  //方向
    
    
    
    //ゲーム本体
    void play(){
        System.out.println("play the game!!");
        System.out.println("If you want to play \"VS cpu\", input (randcom/negacom/abcom).");

        //プレイヤー情報入力
        for(int i=0;i<2;i++){
            System.out.println("Input player"+(i+1)+" name:");
            players[i] = new Player(in.setName(i+1),i+1);
        }
        for(int i=0;i<2;i++){
            String name = players[i].getName();
            System.out.println("welcome "+name);
            if(name.equals("randcom") || name.equals("negacom") || name.equals("abcom")){
                if(mode+i==1) mode = 2;
                else mode = i;
            }
        }
        if(mode==-1) System.out.println("PvP");
        else if(mode==0) System.out.println("CvP");
        else if(mode==1) System.out.println("PvC");
        else if(mode==2) System.out.println("CvC");


        //初期化
        player = 0;
        board.init();

        //ゲーム
        while(true){
            int flag = 0;  //パスのフラグ
            board.display();

            //二回連続でpassが続いたらゲーム終了
            while(flag!=2){
                if(players[player].existLegalMove(board)) break;
                System.out.println("Player: "+players[player].getName()+ " pass");
                player = opposite(player);
                flag++;
            }
            if(flag==2){  //両者合法手がなければゲーム終了
                System.out.println("Game Over!");
                break;
            }

            if(getMove()==-1){  //着手
                System.out.println("Game Over!");
                System.out.println("Winner: "+players[opposite(player)].getName());
                return;
            }
            reverse();  //駒設置とひっくり返し
            player = opposite(player);
        }

        players[0].setPlacedStone(board.countStone(players[0].getNum()));  //先手の石の数
        players[1].setPlacedStone(board.countStone(players[1].getNum()));  //後手の石の数
        for(int i=0;i<2;i++){
            System.out.println(players[i].getName()+": "+players[i].getPlacedStone());
        }
        victory = winner(players[0].getPlacedStone(), players[1].getPlacedStone());  //勝者の計算
        System.out.println("Winner: "+((victory==-1)?"Draw":players[victory].getName()));

    }

    

    //合法手かどうか
    private boolean isLegalMove(){
        //盤面の大きさ
        int field = board.getSize()-2;

        //盤外かどうか
        if(row<1 || field<row || column<1 || field<column) return false;
        //そこに駒を置けるか
        if(board.getField(row,column)!=0) return false;
        
        //方向をまわすfor文
        for(int i=0;i<8;i++){
            int count = 0;  //ひっくり返せる枚数

            count = countReversibleStone(dir[i][0],dir[i][1]);

            if(count>0) return true;
        }
        return false;
    }

    //ひっくり返せる駒の数えあげ
    private int countReversibleStone(int s, int t){
        //盤面の大きさ
        int field = board.getSize()-2;
        //間のマスの数
        int contents;
        //置けるかどうか
        boolean judge;

        //初期化
        contents = 0;
        judge = false;

        for(int j=1;j<=field;j++){  //直線の調査
            if(board.getField(row+s*j,column+t*j)==-1) break;  //壁判定
            if(board.getField(row+s*j,column+t*j)==players[player].getNum()){  //反対の自分の駒発見
                contents = j-1;
                break;
            }
        }
        if(contents>0){  //駒と駒の間にマスがある
            judge = true;
            for(int j=1;j<=contents;j++){  //間の駒の調査
                if(board.getField(row+s*j,column+t*j)!=players[opposite(player)].getNum()){  //相手の駒意外はfalse
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

    //着手を取得
    private int getMove(){
        //目標マス
        String point;

        while(true){
            System.out.println("Your Turn: "+players[player].getName());
            
            if(player==mode || mode==2){  //playerがcomならば
                int move;

                move = comGetMove();  //comの手の取得
                
                //着手の座標を計算
                column = move/10;
                row = move%10;
                System.out.println("I select ("+(char)(column+'a'-1)+","+row+")");
                return 1;
            }

            System.out.print("Input point > ");
            point = in.point();  //着手の座標を取得

            if(point.equals("resign")) return -1;  //投了

            if(point.length()!=2) System.out.println("ex.(a1,f3)");  //入力方法
            else{
                column = point.charAt(0)-'a'+1;
                row = point.charAt(1)-'1'+1;
                if(isLegalMove()) return 1;  //合法手ならreturn
                else {
                    System.out.println("You can't set a storn this place");
                    System.out.println("Candidate: ");
                    players[player].displayLegalMove();  //候補手の表示
                }
            }
        }
    }

    //cpuの手を取得する
    private int comGetMove(){
        int move;  //取得された手
        Board nowBoard = new Board();  //今の盤面の状態
        nowBoard.init();
        int CandMove[] = new int[board.getSize()*board.getSize()];  //候補手

        CandMove = players[player].getLegalMove();  //候補手の取得
        board.copy(nowBoard);  //今の盤面をコピー

        
        //各種cpuによって次の手を取得
        if("negacom".equals(players[player].getName())) move = players[player].negaGetMove(nowBoard,CandMove);
        else if("abcom".equals(players[player].getName())) move = players[player].ABGetMove(nowBoard);
        else move = players[player].randGetMove(nowBoard,CandMove);

        return move;
    }

    //ひっくり返し処理
    private void reverse(){
        //方向をまわすfor文
        for(int i=0;i<8;i++){
            int count = 0;  //ひっくり返せる枚数
            count = countReversibleStone(dir[i][0],dir[i][1]);
            if(count>0){
                for(int j=1;j<=count;j++){  //反対の自分の駒までひっくり返し
                    board.update(row+dir[i][0]*j,column+dir[i][1]*j,players[player].getNum());
                }
            }
        }
        board.update(row,column,players[player].getNum());  //置いた駒
    }
    

    //手番を移す
    private int opposite(int turn){
        return (turn+1)%2;
    }
    
    //勝利者決定
    private int winner(int a, int b){
        if(a > b) return 0;
        else if(a < b) return 1;
        return -1;
    }
}