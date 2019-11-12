package calculator;

import java.io.*;
import java.text.*;
import java.util.StringTokenizer;
import java.lang.NumberFormatException;

class Node{
    private String node;
    private String formula = "";
    private Node right = null;
    private Node left = null;

    Node(String str){
        this.node = str;
    }


    //前処理
    void mae(){
        String tmp = removeSpace();  //スペース除去

        for(int i=0;i<tmp.length();i++){
            char tnp;
            tnp = tmp.charAt(i);

            if(tnp=='-' && (i==0 
            || tmp.charAt(i-1)=='('
            || tmp.charAt(i-1)=='+'
            || tmp.charAt(i-1)=='-'
            || tmp.charAt(i-1)=='*'
            || tmp.charAt(i-1)=='/')){  //符号付き計算のマイナスの値があった時の処理
                formula += "0";
            }
            else if(tnp=='-' && i!=0){  //符号付き計算のマイナスの値があった時の処理
                formula += "+0";
            }

            formula += tnp;
        }
        node = formula;  //スペースを除去した式に更新
        return;
    }

    //中置記法から二分木を作成
    void createTree(){
        try{
            int opPlace = 0;  //演算子の位置
            char start;  //式のはじめ
            char finish;  //式の終わり
            int bflag = 1;  //()が外れているかどうかのフラグ

            System.out.println(formula);
            if(node.length()>0){
                if(!correct()){  //式が正しいかどうかの判定
                    System.out.println("式が正しくありません");
                    node = "";
                    return;
                    //System.exit(0);
                }
            }
            
            start = node.charAt(0);
            finish = node.charAt(node.length()-1);

            while(bflag==1){  //()を外す
                if(start=='(' && finish==')'){
                     bflag = removeOuterBracket();
                }
                else bflag = 0;
            }

            opPlace = getOpPlace();  //演算子の位置を取得
            
            if(opPlace==0){  //演算子がなければ数値
                left = null;
                right = null;
                return;
            }

            left = new Node(node.substring(0,opPlace));  //演算子の前半を再帰的に二分木作成
            left.createTree();
            
            right = new Node(node.substring(opPlace+1));  //演算子の後半を再帰的に二分木作成
            right.createTree();
            
            node = node.substring(opPlace,opPlace+1);  //このノードに演算子を挿入
        }catch(StringIndexOutOfBoundsException e){
            System.out.println("式を入力してください");
            //e.printStackTrace();
        }
    }
    
    //走査
    String traceTree(){
        String l = "";
        String r = "";
        if(left != null) l += left.traceTree()+" ";
        if(right != null) r += right.traceTree()+" ";
        return l + r + node;
    }

    //式が正しいかどうか
    private boolean correct(){
        char tmp1;
        int numCount = 0;
        int charCount = 0;
        int height = 0;
        char tmp;

        for(int i=0;i<node.length();i++){
            tmp = node.charAt(i);
            if(!isNum(tmp) 
                && tmp!='+'
                && tmp!='-'
                && tmp!='*'
                && tmp!='/'
                && tmp!='('
                && tmp!=')'){  //数値でなければこれらの記号のはず
                    return false;
            }
        }
        for(int i=0;i<node.length();i++){  //()の深さが正しいかどうか
            tmp = node.charAt(i);
            if(tmp=='(') height++;
            else if(tmp==')') height--;
        }
        if(height!=0) return false;

        int j;
        for(int i=0;i<node.length();i++){  //数値と演算子の数を取得
            tmp1 = node.charAt(i);
            if(isNum(tmp1)){
                numCount++;
                for(j=i+1;j<node.length();j++){  //i番目数値ならば記号まで読み飛ばし
                    tmp = node.charAt(j);
                    if(!isNum(tmp)){
                        tmp1 = tmp;
                        break;
                    }
                }
                i = j;
                
            }
            if(tmp1=='+'
            || tmp1=='-'
            || tmp1=='*'
            || tmp1=='/') charCount++;  //記号のうち演算子をカウント
        }
        if(numCount==charCount+1) return true;  //数値は演算子+1と同じ個数のはず
        return false;
    }

    //空白を除去する
    private String removeSpace(){
        String retmp="";
        String tmp;
        StringTokenizer token;
        token = new StringTokenizer(node," ");
        while(token.hasMoreTokens()){  //空白読み飛ばし
            tmp = token.nextToken();
            retmp += tmp;
        }
        return retmp;
    }

    //一番外側の()をはずす
    private int removeOuterBracket(){
        int count = 0;
        int height = 0;
        char tmp;
        for(int i=0;i<node.length();i++){
            tmp = node.charAt(i);
            if(tmp=='(') height++;
            else if(tmp==')') height--;
            if(height==0) count++;  //何回()が閉じられるか
        }

		if(count==1){  //一番外側同士で閉じられている
            node = node.substring(1,node.length()-1);
            return 1;
        }
        return 0;
    }

    //演算子の場所を取得
    private int getOpPlace(){
        char tmp;
        int place = 0;
        int height = 0;
        for(int i=node.length()-1;i>-1;i--){
            tmp = node.charAt(i);
            if(tmp==')') height++;
            else if(tmp=='(') height--;
            else if((tmp=='+' || tmp=='-' || tmp=='*' || tmp=='/') && height==0){  //深さが0の演算子の場所を後ろから取得
                place = i;
                break;
            }
        }
        return place;
    }

    //数値かどうか
    private boolean isNum(char num){
        String tmp = String.valueOf(num);
        try{
            Integer.parseInt(tmp);
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }
}