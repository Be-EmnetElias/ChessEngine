package helper;

public class Move {

    public Piece piece;
    public Piece capturedPiece;
    public int[] previousPosition;
    public int[] targetPosition;
    public MoveType moveType;
    public Piece rookToCastle;
    public boolean firstMove;
    public boolean enPassant;

    public Move(Piece piece, Piece capturedPiece, int[] previousPosition, int[] targetPosition, MoveType moveType) {
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

