package util;

public class Move {

    public Piece piece;
    public Piece capturedPiece;
    public Square previousPosition;
    public Square targetPosition;

    public MoveType moveType;

    public Piece rookToCastle;
    public Square rookTargetPosition;
    public Square rookPreviousPosition;

    public boolean whiteTurn;
    public boolean firstMove;
    public Piece enPassantPiece;
    public Square enPassantPosition;
    public boolean enPassant;

    public Move(Piece piece, Piece capturedPiece, Square previousPosition, Square targetPosition, MoveType moveType) {
        this.piece = piece;
        this.capturedPiece = capturedPiece;
        this.previousPosition = previousPosition;
        this.targetPosition = targetPosition;
        this.moveType = moveType;
        this.firstMove = true;
        this.enPassant = false;
    }

    @Override
    public String toString() {
        return this.moveType + ": " + this.piece.name + " to " + this.targetPosition + (this.capturedPiece != null ? " capturing " + this.capturedPiece.name : "");
    }
}

