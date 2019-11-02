package Othello;

public class Main{
    public static void main(String arg[]){
        Input in = new Input();
        while(true){
            Game game = new Game();
            game.play();

            System.out.println("play again?(yes/no)>");
            if(in.keepOn().equals("no")) break;
        }
        System.out.println("See you");
    }
}