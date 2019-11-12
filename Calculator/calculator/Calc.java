package calculator;

import java.io.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;


public class Calc{
    String cal(String str){
        try{
            Deque<Integer> stack = new ArrayDeque<Integer>();
            int tmpi;
            String oder1;
            StringTokenizer token;

            token = new StringTokenizer(str," ");

            while(token.hasMoreTokens()){
                oder1 = token.nextToken();

                if(oder1.equals("push")){
                    oder1 = token.nextToken();
                    tmpi = Integer.parseInt(oder1);
                    stack.push(tmpi);
                }else{
                    if(oder1.equals("add")){
                        int b = stack.pop();
                        int a = stack.pop();
                        tmpi = a + b;
                        stack.push(tmpi);
                    }else if(oder1.equals("sub")){
                        int b = stack.pop();
                        int a = stack.pop();
                        tmpi = a - b;
                        stack.push(tmpi);
                    }else if(oder1.equals("mul")){
                        int b = stack.pop();
                        int a = stack.pop();
                        tmpi = a * b;
                        stack.push(tmpi);
                    }else if(oder1.equals("div")){
                        int b = stack.pop();
                        int a = stack.pop();
                        tmpi = a / b;
                        stack.push(tmpi);
                    }else if(oder1.equals("wrt")){
                        return String.valueOf(stack.pop());
                    }else if(oder1.equals("halt")){
                        System.out.println("終了します");
                        break;
                    }else{
                        return str;
                    }
                }
            }
        }catch(ArithmeticException e){
            System.out.println("0で割っています");
            System.out.println("終了します");
            //System.exit(0);
            return "0で割ってしまいます";
        }catch(NumberFormatException ex){
            ex.printStackTrace();
        }catch(NoSuchElementException e){
            System.out.println("式を正しく入力してください");
        }
        return "";
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
