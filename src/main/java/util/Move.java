package main.java.util;

public class Move {

    public Piece piece;
    public Piece capturedPiece;
    public Square previousPosition;
    public Square targetPosition;

    public MoveType moveType;

    public Piece rookToCastle;
    public Square rookTargetPosition;
    public Square rookPreviousPosition;

    public Name promotionName;

    public boolean whiteTurn;
    public boolean firstMove;
    public Piece enPassantPiece;
    public Square enPassantPosition;
    public boolean enPassant;

    public Move(Piece piece, Piece capturedPiece, Square previousPosition, Square targetPosition, MoveType moveType, Name promotionName) {
        this.piece = piece;
        this.capturedPiece = capturedPiece;
        this.previousPosition = previousPosition;
        this.targetPosition = targetPosition;
        this.moveType = moveType;
        this.firstMove = true;
        this.enPassant = false;
        this.promotionName = promotionName;
    }

    public Move(Piece piece, Piece capturedPiece, Square previousPosition, Square targetPosition, MoveType moveType) {
        this.piece = piece;
        this.capturedPiece = capturedPiece;
        this.previousPosition = previousPosition;
        this.targetPosition = targetPosition;
        this.moveType = moveType;
        this.firstMove = true;
        this.enPassant = false;
    }

    public String getStr(){
        // row = 8 - Integer.parseInt(enpassantSquare.charAt(1)+"");
        // col = enpassantSquare.charAt(0) - 97;

        int prevCol = previousPosition.col;
        int prevRow = previousPosition.row;

        int targCol = targetPosition.col;
        int targRow = targetPosition.row;

        return (char)(prevCol+97)+""+(8-prevRow)+""+(char)(targCol+97)+""+(8-targRow)+((this.promotionName!=null)?promotionName.letter():"");
    }
    @Override
    public String toString() {
        return this.moveType + ": " + this.piece.name + " to " + this.targetPosition + 
        (this.capturedPiece != null ? " capturing " + this.capturedPiece.name : "") + 
        (this.promotionName != null ? " promoting to " + this.promotionName: "") + 
        (this.rookToCastle != null ? " rook castling to " + this.rookTargetPosition: "");
    }
}

