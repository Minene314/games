package Geister;

import java.util.Scanner;

class Input{
    private Scanner scan = new Scanner(System.in);
    

    //座標の入力
    String point(String str, int n){
        System.out.println(n+" of 4: "+str);
        String point = scan.next();
        return point;
    }

    String point(String str){
        System.out.println(str);
        String point = scan.next();
        return point;
    }

}