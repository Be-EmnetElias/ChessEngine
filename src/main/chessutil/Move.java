package main.chessutil;

import java.util.*;

public class Move {

    public int fromSquare; // The starting square (0x77 for the bottom right)
    public int toSquare;   // The target square
    public Piece piece;      // The piece that is moving
    public Piece captured;   // The piece that is captured, if any
    public Piece promotionName; //What the piece will be promoted to
    public List<MoveType> flags = new ArrayList<>(); // Special flags for castling, en passant, promotion, etc.

    public int previousCastlingRights;
    public int previousEnpassantSquare;

    public Move(int fromSquare, int toSquare, Piece piece, Piece captured, MoveType flag) {
        this.fromSquare = fromSquare;
        this.toSquare = toSquare;
        this.piece = piece;
        this.captured = captured;
        this.flags.add(flag);
    }

    public Move(Move other) {
        this.fromSquare = other.fromSquare;
        this.toSquare = other.toSquare;
        this.piece = other.piece;
        this.captured = other.captured;
        this.promotionName = other.promotionName;
        this.flags = other.flags;
        this.previousCastlingRights = other.previousCastlingRights;
        this.previousEnpassantSquare = other.previousEnpassantSquare;
    }

    private String square(int square) {
        int row = 8 - (square / 8);
        char col = (char) ((square % 8) + 'a');
        return col + String.valueOf(row);
    }

    public String getStr(){
        // row = 8 - Integer.parseInt(enpassantSquare.charAt(1)+"");
        // col = enpassantSquare.charAt(0) - 97;

        int prevCol = fromSquare % 8;
        int prevRow = fromSquare / 8;

        int targCol = toSquare % 8;
        int targRow = toSquare / 8;

        return (char)(prevCol+97)+""+(8-prevRow)+""+(char)(targCol+97)+""+(8-targRow)+((this.promotionName!=null)?promotionName.getLetter():"");
    }
    

    @Override
    public String toString(){
        String result = "MoveTypes ";
        for(MoveType movetype: this.flags){
            result += movetype + " ";
        }
        String fromSquareString = square(fromSquare);
        String toSquareString = square(toSquare);

        result += ": " + piece + " from " + fromSquareString + " to " + toSquareString + " " + (captured != Piece.EMPTY ? "capturing " + captured: "") + (promotionName != null ? " promoting to " + promotionName: "");
        return result;
    }
}
