package main.chessutil;

public enum Piece {
    WHITE_PAWN,
    WHITE_KNIGHT,
    WHITE_BISHOP,
    WHITE_ROOK,
    WHITE_QUEEN,
    WHITE_KING,

    BLACK_PAWN,
    BLACK_KNIGHT,
    BLACK_BISHOP,
    BLACK_ROOK,
    BLACK_QUEEN,
    BLACK_KING,

    EMPTY;

    @Override
    public String toString(){
        return this.name();
    }

    public String getLetter(){
        switch(this){
            case BLACK_BISHOP:
                return "b";
            case BLACK_KING:
                return "k";
            case BLACK_KNIGHT:
                return "n";
            case BLACK_PAWN:
                return "p";
            case BLACK_QUEEN:
                return "q";
            case BLACK_ROOK:
                return "r";
            case EMPTY:
                return ".";
            case WHITE_BISHOP:
                return "b";
            case WHITE_KING:
                return "k";
            case WHITE_KNIGHT:
                return "n";
            case WHITE_PAWN:
                return "p";
            case WHITE_QUEEN:
                return "q";
            case WHITE_ROOK:
                return "r";
            default:
                return ".";
            
        }
    }

    public boolean canSlide(){
        return this == WHITE_BISHOP || 
        this == WHITE_ROOK || 
        this == WHITE_QUEEN || 
        this == BLACK_BISHOP || 
        this == BLACK_ROOK ||
        this == BLACK_QUEEN;
    }
}
