package main.chessutil;

public enum MoveType {
    DEFAULT, 
    CAPTURE,
    DOUBLE,
    CASTLE,
    PROMOTION,
    ENPASSANT;

    @Override
    public String toString(){
        return this.name();
    }
}
