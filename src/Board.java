import helper.*;
import java.util.*;

public class Board {

    
    public Board(String initial_position){

        throw new UnsupportedOperationException();

    }

    public void setBoard(String fenposition){

        throw new UnsupportedOperationException();

    }

    public void updateCurrentLegalMoves(){

        throw new UnsupportedOperationException();

    }

    public String getPositionFenstring(){

        throw new UnsupportedOperationException();

    }

    public void printBoard(Move highlight){
        
        throw new UnsupportedOperationException();

    }

    public int[] userValidMove(Piece piece, int[] position){

        throw new UnsupportedOperationException();

    }

    public Move movePiece(Piece piece, Move move, Boolean simulation){

        throw new UnsupportedOperationException();

    }

    public void undoMove(Move move){

        throw new UnsupportedOperationException();

    }

    public GameState updateGameStatus(){

        throw new UnsupportedOperationException();

    }

    public HashSet<Move> legalMoves(Piece piece, Boolean checkKingSafety){

        throw new UnsupportedOperationException();

    }

    public HashSet<Move> legalMovesKing(Piece piece, Boolean checkKingSafety, HashMap<Piece, HashSet<Move>> pinnedPieces){

        throw new UnsupportedOperationException();

    }

    public HashSet<Move> legalMovesPawn(Piece piece, Boolean checkKingSafety, HashMap<Piece, HashSet<Move>> pinnedPieces){

        throw new UnsupportedOperationException();

    }

    public Boolean kingSafeAfterMove(Move move, Boolean checkKingSafety){

        throw new UnsupportedOperationException();

    }

    public HashMap<Piece, HashSet<Move>> updatePinnedPieces(){

        throw new UnsupportedOperationException();

    }

    public Boolean canRookCastle(Piece king, int dx){
        
        throw new UnsupportedOperationException();

    }

    public Piece getKing(Boolean isWhite){

        throw new UnsupportedOperationException();

    }

    public Boolean areEnemies(Piece x, Piece y){

        throw new UnsupportedOperationException();

    }

    public Piece getPiece(int col, int row){

        throw new UnsupportedOperationException();

    }

    public Boolean validPosition(int col, int row){

        throw new UnsupportedOperationException();

    }

    public HashSet<Piece> getPieces(Boolean isWhite){

        throw new UnsupportedOperationException();

    }
}