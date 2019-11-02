package Othello;

import java.util.Scanner;

class Input{
    private Scanner scan = new Scanner(System.in);
    
    //名前入力
    String setName(int num){
        String name = scan.nextLine();
        if(name.equals("com")) name = name + num;
        return name;
    }

    //続けるかどうか
    String keepOn(){
        String cont="";
        while(true){
            cont = scan.nextLine();
            if(cont.equals("yes") || cont.equals("no")) break;
        }
        return cont;
    }

    //座標の入力
    String point(){
        String point = scan.next();
        return point;
    }

}