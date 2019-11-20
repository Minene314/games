package Geister;

class Player {
    String name;
    int takenPieceB;
    int takenPieceR;


    Player(String str) {
        name = str;
        takenPieceB = 0;
        takenPieceR = 0;
    }

    void setName(String str) {
        name = str;
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

}