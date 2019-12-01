package Geister;

import java.util.Scanner;
import java.lang.NumberFormatException;

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

    int inputInt(String str) {
        int mode = 0;
        try {
            while(true) {
                System.out.println("mode select >");
                System.out.println("0: Player vs Random Com");
                System.out.println("1: Player vs MonChan Com");
                System.out.println("2: Player vs TrustMonChan");
                System.out.println("3: Random Com vs Random Com");
                System.out.println("4: Rnadom Com vs MonChan");
                System.out.println("5: Random Com vs TrustMonChan");
                System.out.println("6: MonChan vs MonChan");
                System.out.println("7: MonChan vs TrustMonChan");
                System.out.println("8: TrustMonChan vs TrustMonChan");
                mode = Integer.parseInt(scan.next());
                if(0 <= mode && mode < 9) break;
                else System.out.println("mode number: 0-8");
            }
        }catch(NumberFormatException e) {
            System.out.println(e);
            System.out.println("please input number");
            System.exit(-1);
        }
        return mode;
    }
}