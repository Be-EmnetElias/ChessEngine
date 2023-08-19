package main.java.util;

public enum Name {

    PAWNW, PAWNB, KNIGHT, BISHOP, ROOK, QUEEN, KING;
    

    public String letter(){

        if(this == KNIGHT){
            return "n";
        }else{
            return this.toString().toLowerCase().charAt(0)+"";

        }
        
    }
}

