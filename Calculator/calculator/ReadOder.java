package calculator;

import java.util.StringTokenizer;


public class ReadOder{
    private String oder;
    private String input;

    ReadOder(String in){
        this.oder = "";
        this.input = in;
    }

    String ReadFormula(){
        StringTokenizer token;
        token = new StringTokenizer(input," ");

        int tmpi;
        String tmps;

        while(token.hasMoreTokens()){
            tmps = token.nextToken();
            System.out.println(tmps);
            if(isNum(tmps)){
                tmpi = Integer.parseInt(tmps);
                tmps = "push";

                oder += tmps+" ";
                oder += tmpi+" ";
            }else if(tmps.equals("+")){
                oder += "add ";
            }else if(tmps.equals("-")){
                oder += "sub ";
            }else if(tmps.equals("*")){
                oder += "mul ";
            }else if(tmps.equals("/")){
                oder += "div ";
            }else{
                return input;
            }
        }
        return oder;
    }

    static boolean isNum(String s){
        try{
            Integer.parseInt(s);
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }
}
