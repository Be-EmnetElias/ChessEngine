/**
 * * This Board class handles all logic. Code is down bad terrible but faster than before
 * 
 *  Legal moves for a position can be found like this:
 * 
 *  (1) Remove the king and calculate all squares being attacked, the king cannot move to these squares
 *      If the danger squares contain the king squares, the king is in check. If the king is being attacked
 *      by two pieces, it is in double-check and the only legal moves are king moves
 * 
 *  (2) Calculate the capture mask and push masks to represent which squares can be captured, and which
 *      squares can be moved to. When in check these should be updated accordingly
 * 
 *  (3) Calculate pinned pieces. Start from the kings location and expand in all 8 directions to find
 *      which pieces are pinned to the king. Pinned pieces can only travel in the pinned direction.
 *      Pinned horses cannot move at all.
 * 
 *  (4) Special Enpassant Check: Each enpassant move should be checked for a horizontal attack. Remove
 *      the pawns and check if the king is being attacked horizontally.
 */

//TODO: Check tests
//TODO: Implement board evaluator

package main;

import main.chessutil.*;
import java.util.*;

public class Board{

    /**
     * Bitboard for each type of white piece
     */
    public Bitboard WHITE_PAWN_BITBOARD, WHITE_KNIGHT_BITBOARD, WHITE_BISHOP_BITBOARD, WHITE_ROOK_BITBOARD, WHITE_QUEEN_BITBOARD, WHITE_KING_BITBOARD;

    /**
     * Bitboard for each type of black piece
     */
    public Bitboard BLACK_PAWN_BITBOARD, BLACK_KNIGHT_BITBOARD, BLACK_BISHOP_BITBOARD, BLACK_ROOK_BITBOARD, BLACK_QUEEN_BITBOARD, BLACK_KING_BITBOARD;

    /**
     * Arrays containing the bitboards of each team
     */
    public long[] BOARD;

    /**
     * Bitboard for the current enpassant square
     */
    public int ENPASSANT_SQUARE;

    /**
     * Current castling rights.
     */
    public int CASLTING_RIGHTS;

    /**
     * True when white's turn.
     */
    public boolean IS_WHITE_TURN;

    public boolean WHITE_CASTLED;

    public boolean BLACK_CASTLED;

    /**
     * Evaluates a board position and finds the 'best' move
     */
    public Evaluator HIVE;


    public Board(){
        String position = "6k1/8/8/8/8/3n4/6PR/5R1K w - - 0 1";

        setBoard("1r5k/1r6/8/8/K7/8/8/8 w - - 0 1");
        this.BOARD = getAllBitboards();
        HIVE = new Evaluator();
        HIVE.BOARD = this;
    }

    public Board(String fenposition){
        setBoard(fenposition);
    }

    public long[] getBoard(){
        return this.BOARD;
    }

    public void movePiece(Move move, boolean simulation){
        int fromSquare = move.fromSquare;
        int toSquare = move.toSquare;
        Piece pieceType = move.piece;
        move.previousCastlingRights = this.CASLTING_RIGHTS;
        move.previousEnpassantSquare = this.ENPASSANT_SQUARE;
        this.ENPASSANT_SQUARE = -1;
        switch (pieceType) {
            case WHITE_PAWN:
                WHITE_PAWN_BITBOARD.clearBit(fromSquare);  
                if(!move.flags.contains(MoveType.PROMOTION)){
                    WHITE_PAWN_BITBOARD.setBit(toSquare);    
                }else{
                    if(move.promotionName == Piece.WHITE_QUEEN){
                        WHITE_QUEEN_BITBOARD.setBit(toSquare);
                    }
                    if(move.promotionName == Piece.WHITE_ROOK){
                        WHITE_ROOK_BITBOARD.setBit(toSquare);
                    }
                    if(move.promotionName == Piece.WHITE_BISHOP){
                        WHITE_BISHOP_BITBOARD.setBit(toSquare);
                    }
                    if(move.promotionName == Piece.WHITE_KNIGHT){
                        WHITE_KNIGHT_BITBOARD.setBit(toSquare);
                    }   
                }
                break;
            case BLACK_PAWN:
                BLACK_PAWN_BITBOARD.clearBit(fromSquare);
                if(!move.flags.contains(MoveType.PROMOTION)){
                    BLACK_PAWN_BITBOARD.setBit(toSquare);    
                }else{
                    if(move.promotionName == Piece.BLACK_QUEEN){
                        BLACK_QUEEN_BITBOARD.setBit(toSquare);
                    }
                    if(move.promotionName == Piece.BLACK_ROOK){
                        BLACK_ROOK_BITBOARD.setBit(toSquare);
                    }
                    if(move.promotionName == Piece.BLACK_BISHOP){
                        BLACK_BISHOP_BITBOARD.setBit(toSquare);
                    }
                    if(move.promotionName == Piece.BLACK_KNIGHT){
                        BLACK_KNIGHT_BITBOARD.setBit(toSquare);
                    }   
                }
                break;
            case BLACK_BISHOP:
                BLACK_BISHOP_BITBOARD.clearBit(fromSquare);  
                BLACK_BISHOP_BITBOARD.setBit(toSquare);  
                break;
            case BLACK_KING:
                BLACK_KING_BITBOARD.clearBit(fromSquare);  
                BLACK_KING_BITBOARD.setBit(toSquare);  
                break;
            case BLACK_KNIGHT:
                BLACK_KNIGHT_BITBOARD.clearBit(fromSquare);  
                BLACK_KNIGHT_BITBOARD.setBit(toSquare);  
                break;
            case BLACK_QUEEN:
                BLACK_QUEEN_BITBOARD.clearBit(fromSquare);  
                BLACK_QUEEN_BITBOARD.setBit(toSquare);  
                break;
            case BLACK_ROOK:
                BLACK_ROOK_BITBOARD.clearBit(fromSquare);  
                BLACK_ROOK_BITBOARD.setBit(toSquare);  
                break;
            case EMPTY:
                break;
            case WHITE_BISHOP:
                WHITE_BISHOP_BITBOARD.clearBit(fromSquare);  
                WHITE_BISHOP_BITBOARD.setBit(toSquare);  
                break;
            case WHITE_KING:
                WHITE_KING_BITBOARD.clearBit(fromSquare);  
                WHITE_KING_BITBOARD.setBit(toSquare);  
                break;
            case WHITE_KNIGHT:
                WHITE_KNIGHT_BITBOARD.clearBit(fromSquare);  
                WHITE_KNIGHT_BITBOARD.setBit(toSquare);     
                break;
            case WHITE_QUEEN:
                WHITE_QUEEN_BITBOARD.clearBit(fromSquare);  
                WHITE_QUEEN_BITBOARD.setBit(toSquare);  
                break;
            case WHITE_ROOK:
                WHITE_ROOK_BITBOARD.clearBit(fromSquare);  
                WHITE_ROOK_BITBOARD.setBit(toSquare);  
                break;
            default:
                break;
        }

        if(move.flags.contains(MoveType.ENPASSANT)){
            toSquare += (pieceType == Piece.WHITE_PAWN ? 8:-8);
        }
        
        switch (move.captured) {
            case WHITE_PAWN:
                WHITE_PAWN_BITBOARD.clearBit(toSquare);  
                break;
            case BLACK_BISHOP:
                BLACK_BISHOP_BITBOARD.clearBit(toSquare);  
                break;
            case BLACK_KING:
                BLACK_KING_BITBOARD.clearBit(toSquare);  
                break;
            case BLACK_KNIGHT:
                BLACK_KNIGHT_BITBOARD.clearBit(toSquare);  
                break;
            case BLACK_PAWN:
                BLACK_PAWN_BITBOARD.clearBit(toSquare);  
                break;
            case BLACK_QUEEN:
                BLACK_QUEEN_BITBOARD.clearBit(toSquare);  
                break;
            case BLACK_ROOK:
                BLACK_ROOK_BITBOARD.clearBit(toSquare);  
                break;
            case EMPTY:
                break;
            case WHITE_BISHOP:
                WHITE_BISHOP_BITBOARD.clearBit(toSquare);  
                break;
            case WHITE_KING:
                WHITE_KING_BITBOARD.clearBit(toSquare);  
                break;
            case WHITE_KNIGHT:
                WHITE_KNIGHT_BITBOARD.clearBit(toSquare);  
                break;
            case WHITE_QUEEN:
                WHITE_QUEEN_BITBOARD.clearBit(toSquare);  
                break;
            case WHITE_ROOK:
                WHITE_ROOK_BITBOARD.clearBit(toSquare);   
                break;
            default:
                break;
        }
        
        if(move.flags.contains(MoveType.DOUBLE)){
            int enPassantSquare = move.toSquare + (move.piece == Piece.WHITE_PAWN ? 8:-8);
            this.ENPASSANT_SQUARE = enPassantSquare;
        }
        
        if(move.flags.contains(MoveType.CASTLE)){
            if(toSquare > 50) WHITE_CASTLED = true;
            if(toSquare < 10) BLACK_CASTLED = true;

            //move rook
            if(toSquare == 62){
                this.WHITE_ROOK_BITBOARD.clearBit(63);
                this.WHITE_ROOK_BITBOARD.setBit(61);
            }
            if(toSquare == 58){
                this.WHITE_ROOK_BITBOARD.clearBit(56);
                this.WHITE_ROOK_BITBOARD.setBit(59);
            }
            if(toSquare == 6){
                this.BLACK_ROOK_BITBOARD.clearBit(7);
                this.BLACK_ROOK_BITBOARD.setBit(5);
            }
            if(toSquare == 2){
                this.BLACK_ROOK_BITBOARD.clearBit(0);
                this.BLACK_ROOK_BITBOARD.setBit(3);
            }
            
        }

        if(pieceType == Piece.WHITE_KING){
            this.CASLTING_RIGHTS &= ~(1 | 2);
        }
        if(pieceType == Piece.BLACK_KING){
            this.CASLTING_RIGHTS &= ~(4 | 8);
        }
        if((pieceType == Piece.WHITE_ROOK && fromSquare == 63) || (move.captured == Piece.WHITE_ROOK && toSquare == 63)){
            this.CASLTING_RIGHTS &= 14;
        }
        if((pieceType == Piece.WHITE_ROOK && fromSquare == 56) || (move.captured == Piece.WHITE_ROOK && toSquare == 56)){
            this.CASLTING_RIGHTS &= 13;
        }
        if((pieceType == Piece.BLACK_ROOK && fromSquare == 7) || (move.captured == Piece.BLACK_ROOK && toSquare == 7)){
            this.CASLTING_RIGHTS &= 11;
        }
        if((pieceType == Piece.BLACK_ROOK && fromSquare == 0) || (move.captured == Piece.BLACK_ROOK && toSquare == 0)){
            this.CASLTING_RIGHTS &= 7;
        }


        // System.out.println("MOVING " + move.piece + " FROM: " + fromSquare + " TO: " + toSquare + (move.captured != null ? " CAPTURING: " + move.captured: ""));
        // printBoard();
        // System.out.println();
        
        
        this.IS_WHITE_TURN = !this.IS_WHITE_TURN;
        if(!simulation){
            this.BOARD = getAllBitboards();
        }
        
    }

    public void undoMove(Move move, boolean simulation){
        int fromSquare = move.fromSquare;
        int toSquare = move.toSquare;
        Piece pieceType = move.piece;
        this.CASLTING_RIGHTS = move.previousCastlingRights;
        this.ENPASSANT_SQUARE = move.previousEnpassantSquare;
        this.IS_WHITE_TURN = !this.IS_WHITE_TURN;

        switch (pieceType) {
            case WHITE_PAWN:
                if(!move.flags.contains(MoveType.PROMOTION)) WHITE_PAWN_BITBOARD.clearBit(toSquare);
                else{
                    switch(move.promotionName){
                        case WHITE_QUEEN:
                            WHITE_QUEEN_BITBOARD.clearBit(toSquare);
                            break;
                        case WHITE_ROOK:
                            WHITE_ROOK_BITBOARD.clearBit(toSquare);
                            break;
                        case WHITE_BISHOP:
                            WHITE_BISHOP_BITBOARD.clearBit(toSquare);
                            break;
                        case WHITE_KNIGHT:
                            WHITE_KNIGHT_BITBOARD.clearBit(toSquare);
                            break;
                        default: break;
                    }
                }
                WHITE_PAWN_BITBOARD.setBit(fromSquare);    
                break;
            case BLACK_BISHOP:
                BLACK_BISHOP_BITBOARD.clearBit(toSquare);
                BLACK_BISHOP_BITBOARD.setBit(fromSquare);  
                
                break;
            case BLACK_KING:
                BLACK_KING_BITBOARD.clearBit(toSquare);  
                BLACK_KING_BITBOARD.setBit(fromSquare);  
                break;
            case BLACK_KNIGHT:
                BLACK_KNIGHT_BITBOARD.clearBit(toSquare);  
                BLACK_KNIGHT_BITBOARD.setBit(fromSquare);  

                break;
            case BLACK_PAWN:
                if(!move.flags.contains(MoveType.PROMOTION)) BLACK_PAWN_BITBOARD.clearBit(toSquare);
                else{
                    switch(move.promotionName){
                        case BLACK_QUEEN:
                            BLACK_QUEEN_BITBOARD.clearBit(toSquare);
                            break;
                        case BLACK_ROOK:
                            BLACK_ROOK_BITBOARD.clearBit(toSquare);
                            break;
                        case BLACK_BISHOP:
                            BLACK_BISHOP_BITBOARD.clearBit(toSquare);
                            break;
                        case BLACK_KNIGHT:
                            BLACK_KNIGHT_BITBOARD.clearBit(toSquare);
                            break;
                        default: break;
                    }
                } 
                BLACK_PAWN_BITBOARD.setBit(fromSquare);  
                break;
            case BLACK_QUEEN:
                BLACK_QUEEN_BITBOARD.clearBit(toSquare);  
                BLACK_QUEEN_BITBOARD.setBit(fromSquare);  
                
                break;
            case BLACK_ROOK:
                BLACK_ROOK_BITBOARD.clearBit(toSquare);  
                BLACK_ROOK_BITBOARD.setBit(fromSquare);  
                 
                break;
            case WHITE_BISHOP:
                WHITE_BISHOP_BITBOARD.clearBit(toSquare);  
                WHITE_BISHOP_BITBOARD.setBit(fromSquare);  
                
                break;
            case WHITE_KING:
                WHITE_KING_BITBOARD.clearBit(toSquare);  
                WHITE_KING_BITBOARD.setBit(fromSquare);  
                break;
            case WHITE_KNIGHT:
                WHITE_KNIGHT_BITBOARD.clearBit(toSquare);  
                WHITE_KNIGHT_BITBOARD.setBit(fromSquare);  
                  
                break;
            case WHITE_QUEEN:
                WHITE_QUEEN_BITBOARD.clearBit(toSquare);  
                WHITE_QUEEN_BITBOARD.setBit(fromSquare);  
                
                break;
            case WHITE_ROOK:
                WHITE_ROOK_BITBOARD.clearBit(toSquare);  
                WHITE_ROOK_BITBOARD.setBit(fromSquare);  
                break;
            default:
                break;
        }

        if(move.flags.contains(MoveType.ENPASSANT)){
            toSquare += (pieceType == Piece.WHITE_PAWN ? 8:-8);
        }
        
        switch (move.captured) {
            case WHITE_PAWN:
                WHITE_PAWN_BITBOARD.setBit(toSquare);  
                break;
            case BLACK_BISHOP:
                BLACK_BISHOP_BITBOARD.setBit(toSquare);  
                break;
            case BLACK_KING:
                BLACK_KING_BITBOARD.setBit(toSquare);  
                break;
            case BLACK_KNIGHT:
                BLACK_KNIGHT_BITBOARD.setBit(toSquare);  
                break;
            case BLACK_PAWN:
                BLACK_PAWN_BITBOARD.setBit(toSquare);  
                break;
            case BLACK_QUEEN:
                BLACK_QUEEN_BITBOARD.setBit(toSquare);  
                break;
            case BLACK_ROOK:
                BLACK_ROOK_BITBOARD.setBit(toSquare);  
                break;
            case WHITE_BISHOP:
                WHITE_BISHOP_BITBOARD.setBit(toSquare);  
                break;
            case WHITE_KING:
                WHITE_KING_BITBOARD.setBit(toSquare);  
                break;
            case WHITE_KNIGHT:
                WHITE_KNIGHT_BITBOARD.setBit(toSquare);  
                break;
            case WHITE_QUEEN:
                WHITE_QUEEN_BITBOARD.setBit(toSquare);  
                break;
            case WHITE_ROOK:
                WHITE_ROOK_BITBOARD.setBit(toSquare);   
                break;
            case EMPTY:
                break;
            default:
                break;
        }
        
        
        
        if(move.flags.contains(MoveType.CASTLE)){
            if(toSquare > 50) WHITE_CASTLED = false;
            if(toSquare < 10) BLACK_CASTLED = false;

            if(toSquare == 62){
                this.WHITE_ROOK_BITBOARD.clearBit(61);
                this.WHITE_ROOK_BITBOARD.setBit(63);
            }
            if(toSquare == 58){
                this.WHITE_ROOK_BITBOARD.clearBit(59);
                this.WHITE_ROOK_BITBOARD.setBit(56);
            }
            if(toSquare == 6){
                this.BLACK_ROOK_BITBOARD.clearBit(5);
                this.BLACK_ROOK_BITBOARD.setBit(7);
            }
            if(toSquare == 2){
                this.BLACK_ROOK_BITBOARD.clearBit(3);
                this.BLACK_ROOK_BITBOARD.setBit(0);
            }
        }


        // System.out.println("UNDOING " + pieceType + " FROM: " + toSquare + " BACK TO: " + fromSquare);
        // printBoard();
        // System.out.println();

        
    }

    public List<Move> getCurrentLegalMoves(boolean isWhite){
        int kingPosition = getKingPosition(isWhite);
        int enemyKingPosition = getKingPosition(!isWhite);
        long[] team = getTeamBitBoards(isWhite);
        long[] enemies = getTeamBitBoards(!isWhite);
        long[] teamWithoutKing = getTeamBitBoardsWithoutKing(isWhite);
        long[] board = getAllBitboards();
        Bitboard pushMask = null;
        int captureMask = -1;
        List<Move> enemyMoves = getPsuedoLegalMoves(enemies,teamWithoutKing,new ArrayList<Integer>(),board,!isWhite, true,false,captureMask,pushMask, enemyKingPosition,kingPosition);
        List<Integer> dangerSquares = new ArrayList<>();

        for(int displacement: new int[]{-9,-8,-7,-1,1,7,8,9}){
            int toSquare = enemyKingPosition + displacement;
            if(validSquare(toSquare)){
                dangerSquares.add(toSquare);
            }
        }
        Move checker = null;
        boolean doubleCheck = false;
        for(Move move: enemyMoves){
            dangerSquares.add(move.toSquare);
            if(checker!=null && move.toSquare == kingPosition && move.fromSquare != checker.fromSquare){
                doubleCheck = true;
            }
            if(move.toSquare == kingPosition){
                checker = move;
            }
        }

        boolean singleCheck = dangerSquares.contains(kingPosition);
        
        //calculate push and capture masks
        if(singleCheck && !doubleCheck){
            
            for(Move move: enemyMoves){
                if(move.toSquare == kingPosition){
                    captureMask = move.fromSquare;
                    checker =  move;
                }
            }
            if(checker.piece.canSlide()){ //all squares in between
                pushMask = new Bitboard(0L);
                int dy = (kingPosition / 8) - (checker.fromSquare / 8);
                int dx = (kingPosition % 8) - (checker.fromSquare % 8);
                if (dy > 1) dy = 1;
                if (dy < 0) dy = -1;
                if(dx > 1) dx = 1;
                if (dx < 0) dx = -1;
                int displacement = dx + (dy == 1 ? 8:0) + (dy == -1 ? -8:0);
                int current = checker.fromSquare + displacement;
                while(current != kingPosition){
                    pushMask.setBit(current);
                    current += displacement;
                }
            }
        }

        List<Move> legalMoves = getPsuedoLegalMoves(team,enemies,dangerSquares,board,isWhite,false,doubleCheck,captureMask,pushMask, kingPosition,enemyKingPosition);
        return legalMoves;
    }

    public List<Move> getPsuedoLegalMoves(long[] team, long[] enemies, List<Integer> dangerSquares, long[] board, boolean isWhite, boolean removeKing, boolean doubleCheck, int captureMask, Bitboard pushMask, int kingPosition, int enemyKingPosition){
        List<Move> moves = new ArrayList<>();
        long teamPieces = 0L;
        long enemyPieces = 0L;
        HashMap<Integer, Integer> pinnedPieces = new HashMap<>();
        for(long bitboard:team){
            teamPieces |= bitboard;
        }
        for(long bitboard:enemies){
            enemyPieces |= bitboard;
        }
        if(!removeKing){
            moves.addAll(kingMoves(team[5],teamPieces,dangerSquares,isWhite));
            if(doubleCheck) return moves;

            pinnedPieces = calculatePinnedPieces(teamPieces, enemies,enemyPieces,kingPosition,isWhite);

        }
        
        moves.addAll(pawnMoves(team[0],teamPieces,enemyPieces,isWhite,removeKing, captureMask,pushMask, pinnedPieces));
        moves.addAll(knightMoves(team[1],teamPieces,isWhite,captureMask,pushMask, pinnedPieces,removeKing));
        moves.addAll(slidingPieceMoves(team[2],teamPieces,enemyPieces, isWhite, "BISHOP", captureMask,pushMask, pinnedPieces,removeKing));
        moves.addAll(slidingPieceMoves(team[3],teamPieces,enemyPieces, isWhite, "ROOK", captureMask,pushMask, pinnedPieces,removeKing));
        moves.addAll(slidingPieceMoves(team[4],teamPieces,enemyPieces, isWhite, "QUEEN", captureMask,pushMask, pinnedPieces,removeKing));

        if(!removeKing){
            for(Move move: moves){
                if(move.toSquare == enemyKingPosition){
                    move.flags.add(MoveType.CHECK);
                }
            }
        }
        

        return moves;
    }

    public HashMap<Integer,Integer> calculatePinnedPieces(long team, long[] enemies, long enemyPieces, int kingPosition, boolean isWhite){
        HashMap<Integer, Integer> pinnedPieces = new HashMap<>();
        // Directions for rook and bishop (and queen)
        int[] rookDirections = {-8, 8, -1, 1};
        int[] bishopDirections = isWhite ? new int[]{-9, -7}: new int[]{9, 7};
        int[] queenDirections = {-8, 8, -1, 1, -9, 9, -7, 7};

        // Loop through each enemy slider type (bishops, rooks, queens)
        for (int i = 2; i <= 4; i++) {
            long enemySliders = enemies[i];
            int[] directions = (i == 2) ? bishopDirections : (i == 3) ? rookDirections : queenDirections;

            while (enemySliders != 0) {
                int enemySquare = Long.numberOfTrailingZeros(enemySliders);

                for (int dir : directions) {
                    int firstFriendlySquare = -1;
                    int square = kingPosition;

                    // Trace a line from the king to the enemy piece
                    while (true) {
                        int currentFile = square % 8;
                        square += dir;
                        int nextFile = square % 8;

                        // Check for board boundaries
                        if (square < 0 || square >= 64 || Math.abs(nextFile - currentFile) > 1) {
                            firstFriendlySquare = -1;
                            break;
                        }

                        long bit = 1L << square;

                        if ((team & bit) != 0) {
                            if (firstFriendlySquare == -1) {
                                firstFriendlySquare = square;
                            } else {
                                // Found a second friendly piece, so the first is not pinned
                                firstFriendlySquare = -1;
                                break;
                            }
                        }

                        //enemy piece found
                        if((enemyPieces & bit) != 0 && square != enemySquare){
                            firstFriendlySquare = -1;
                            break;
                        }

                        if (square == enemySquare) {
                            break;
                        }
                    }

                    if (firstFriendlySquare != -1) {
                        // The piece at firstFriendlySquare is pinned in direction `dir`
                        pinnedPieces.put(firstFriendlySquare, dir);
                    }
                }
                enemySliders &= enemySliders - 1; // Clear the least significant bit
            }
        }
        return pinnedPieces;
    }
    
    public List<Move> pawnMoves(long pawns, long team, long enemy, boolean isWhite, boolean attacksOnly, int captureMask, Bitboard pushMask, HashMap<Integer, Integer> pinnedPieces){
        List<Move> result = new ArrayList<>();

        // Displacements for a knight
        int[] pawnDisplacements = (isWhite) ? new int[]{-16,-8,-7,-9}: new int[]{16,8,7,9};
        Piece name = isWhite ? Piece.WHITE_PAWN : Piece.BLACK_PAWN;

        while (pawns != 0) {
            int fromSquare = Long.numberOfTrailingZeros(pawns);  // Find the square number of one of the knights
            int fromFile = fromSquare % 8;
            int fromRank = fromSquare / 8;

            for (int displacement : pawnDisplacements) {
                if(pinnedPieces.containsKey(fromSquare) && displacement != pinnedPieces.get(fromSquare) && !(Math.abs(displacement) == 16 && Math.abs(pinnedPieces.get(fromSquare)) == 8)) continue;
                int toSquare = fromSquare + displacement;
                boolean canJump = (isWhite && fromSquare/8 == 6) || (!isWhite && fromSquare/8 == 1);
                // Check that the destination square is on the board
                if (validSquare(toSquare)) {
                    boolean isPromoting = (isWhite && toSquare < 8) || (!isWhite && toSquare > 56);
                    Move move = null;
                    
                    int toFile = toSquare % 8;
                    int toRank = toSquare / 8;
                    if (Math.abs(fromFile - toFile) > 2 || Math.abs(fromRank - toRank) > 2){
                        continue;
                    }

                    // forward move, attacksOnly must be false, since this is not an attack
                    if (!attacksOnly && Math.abs(displacement) == 8 && getPieceAtSquare(toSquare) == Piece.EMPTY) {
                        move = new Move(fromSquare, toSquare, name, getPieceAtSquare(toSquare), MoveType.DEFAULT);
                    }
                    
                    //double atack
                    if(!attacksOnly && (Math.abs(displacement) == 16) && canJump && (getPieceAtSquare(toSquare) == Piece.EMPTY) && (getPieceAtSquare(fromSquare+displacement/2) == Piece.EMPTY)){
                        move = new Move(fromSquare, toSquare, name, getPieceAtSquare(toSquare), MoveType.DOUBLE);
                    }
                    
                    //When attacksOnly is true, we are looking for all attacks
                    if(Math.abs(displacement) % 2 != 0 && (attacksOnly || isOccupiedByOwnTeam(toSquare, enemy))){ //diagonal
                        move = new Move(fromSquare, toSquare, name, getPieceAtSquare(toSquare), MoveType.CAPTURE);
                    }
                    
                    if(Math.abs(displacement) % 2 != 0 && (this.ENPASSANT_SQUARE == toSquare)){
                        move = new Move(fromSquare, toSquare, name, getPieceAtSquare(toSquare + (isWhite?8:-8)), MoveType.ENPASSANT);
                    }

                    if(captureMask != -1 || pushMask != null){
                        if(toSquare != captureMask && (pushMask == null || !((pushMask.getLong() & (1L << toSquare)) != 0))){
                            continue;
                        }
                    }
                    
                    if(move != null && isPromoting){
                        Move promoteQueen = new Move(move);
                        promoteQueen.flags.add(MoveType.PROMOTION);
                        promoteQueen.promotionName = isWhite ? Piece.WHITE_QUEEN : Piece.BLACK_QUEEN;
                        Move promoteRook = new Move(move);
                        promoteRook.promotionName = isWhite ? Piece.WHITE_ROOK : Piece.BLACK_ROOK;
                        Move promoteBishop = new Move(move);
                        promoteBishop.promotionName = isWhite ? Piece.WHITE_BISHOP : Piece.BLACK_BISHOP;
                        Move promoteKnight = new Move(move);
                        promoteKnight.promotionName = isWhite ? Piece.WHITE_KNIGHT : Piece.BLACK_KNIGHT;

                        result.add(promoteQueen);
                        result.add(promoteBishop);
                        result.add(promoteRook);
                        result.add(promoteKnight);

                    }else if(move != null){
                        result.add(move);
                    }
                }

            }
        

            pawns &= pawns - 1;  // Clear the least significant bit to move on to the next knight
        }

        return result;
    }

    public List<Move> kingMoves(long king, long team, List<Integer> dangerSquares, boolean isWhite){
        List<Move> result = new ArrayList<>();
        int kingPosition = getKingPosition(isWhite);
        // Displacements for a knight
        int[] kingDisplacements = {-9,-8,-7,-1,1,7,8,9};

        while (king != 0) {
            int fromSquare = Long.numberOfTrailingZeros(king);  // Find the square number of one of the knights
            int fromFile = fromSquare % 8;
            int fromRank = fromSquare / 8;
            for (int displacement : kingDisplacements) {
                int toSquare = fromSquare + displacement;

                // Check that the destination square is on the board
                if (toSquare >= 0 && toSquare < 64) {
                    int toFile = toSquare % 8;
                    int toRank = toSquare / 8;

                    if (Math.abs(fromFile - toFile) > 2 || Math.abs(fromRank - toRank) > 2) continue;
                    // Check that the destination square is not occupied by one of our own pieces
                    if (!isOccupiedByOwnTeam(toSquare, team) && !dangerSquares.contains(toSquare)) {
                        result.add(new Move(fromSquare, toSquare, getPieceAtSquare(fromSquare), getPieceAtSquare(toSquare), MoveType.DEFAULT));

                        if(((displacement == 1 && isWhite && (CASLTING_RIGHTS & 1) != 0) && getPieceAtSquare(61) == Piece.EMPTY) ||
                            ((displacement == -1 && isWhite && (CASLTING_RIGHTS & 2) != 0) && getPieceAtSquare(57) == Piece.EMPTY && getPieceAtSquare(59) == Piece.EMPTY)  ||
                            ((displacement == 1 && !isWhite && (CASLTING_RIGHTS & 4) != 0) && getPieceAtSquare(5) == Piece.EMPTY) || 
                            ((displacement == -1 && !isWhite && (CASLTING_RIGHTS & 8) != 0) && getPieceAtSquare(1) == Piece.EMPTY && getPieceAtSquare(3) == Piece.EMPTY) ){
                            
                            if(!dangerSquares.contains(kingPosition) && getPieceAtSquare(toSquare +displacement) == Piece.EMPTY && !dangerSquares.contains(toSquare + displacement)){
                                result.add(new Move(fromSquare, toSquare + displacement, getPieceAtSquare(fromSquare), getPieceAtSquare(toSquare+displacement), MoveType.DEFAULT));
                                result.get(result.size()-1).flags.add(MoveType.CASTLE);
                            }
                            
                        }
                    }
                }
            }

            king &= king - 1;  // Clear the least significant bit to move on to the next knight
        }

        return result;
    }

    public List<Move> slidingPieceMoves(long pieces, long team, long enemies, boolean isWhite, String piece, int captureMask, Bitboard pushMask, HashMap<Integer, Integer> pinnedPieces, boolean removeKing){
        List<Move> result = new ArrayList<>();

        // Diagonal displacements for a bishop
        int[] displacements;
        if(piece.equals("BISHOP")) displacements = new int[]{-9, -7, 7, 9};
        else if(piece.equals("ROOK")) displacements = new int[]{-8, -1, 1, 8};
        else displacements = new int[]{-9,-8,-7,-1,1,7,8,9};


        while (pieces != 0) {
            int fromSquare = Long.numberOfTrailingZeros(pieces);  // Find the square number of one of the bishops

            for (int displacement : displacements) {
                if(pinnedPieces.containsKey(fromSquare) && !(displacement == pinnedPieces.get(fromSquare) || displacement == -1 * pinnedPieces.get(fromSquare))) continue;
                int toSquare = fromSquare;
                while (true) {
                    boolean addMove = true; 
                    int toFile = toSquare % 8;
                    toSquare += displacement;

                    
                    
                    // Check for crossing file boundaries
                    if (Math.abs(toFile - (toSquare % 8)) > 1) break;

                    // Check that the destination square is on the board
                    if (!validSquare(toSquare)) break;

                    // Check if the destination square is occupied by one of our own pieces
                    if (isOccupiedByOwnTeam(toSquare, team) && !removeKing) break;

                    if(captureMask != -1 || pushMask != null){
                        if(toSquare != captureMask && (pushMask == null || !((pushMask.getLong() & (1L << toSquare)) != 0))){
                            addMove = false;
                        }
                    }

                    if(addMove){
                        Move move = new Move(fromSquare, toSquare, getPieceAtSquare(fromSquare), getPieceAtSquare(toSquare), MoveType.DEFAULT);
                        result.add(move);
                        if(((1 << toSquare) & enemies) != 0) move.flags.add(MoveType.CAPTURE);
                    }

                    

                    // Check if the destination square is occupied by an enemy piece
                    if (isOccupiedByOwnTeam(toSquare, enemies) || isOccupiedByOwnTeam(toSquare, team)) break;
                    
                    
                }
            }

            pieces &= pieces - 1;  // Clear the least significant bit to move on to the next bishop
        }

        return result;
    }

    public List<Move> knightMoves(long knights, long team, boolean isWhite, int captureMask, Bitboard pushMask, HashMap<Integer, Integer> pinnedPieces, boolean removeKing){
        List<Move> result = new ArrayList<>();

        // Displacements for a knight
        int[] knightDisplacements = {-17, -15, -10, -6, 6, 10, 15, 17};

        while (knights != 0) {
            int fromSquare = Long.numberOfTrailingZeros(knights);  // Find the square number of one of the knights
            int fromFile = fromSquare % 8;
            int fromRank = fromSquare / 8;

            for (int displacement : knightDisplacements) {
                if(pinnedPieces.containsKey(fromSquare) && displacement != pinnedPieces.get(fromSquare)) continue;
                int toSquare = fromSquare + displacement;

                // Check that the destination square is on the board
                if (toSquare >= 0 && toSquare < 64) {

                    if(captureMask != -1 || pushMask != null){
                        if(toSquare != captureMask && (pushMask == null || !((pushMask.getLong() & (1L << toSquare)) != 0))){
                            continue;
                        }
                    }
                    int toFile = toSquare % 8;
                    int toRank = toSquare / 8;

                    if (Math.abs(fromFile - toFile) > 2 || Math.abs(fromRank - toRank) > 2) continue;

                    // Check that the destination square is not occupied by one of our own pieces
                    if (removeKing || !isOccupiedByOwnTeam(toSquare, team)) {
                        result.add(new Move(fromSquare, toSquare, getPieceAtSquare(fromSquare), getPieceAtSquare(toSquare), MoveType.DEFAULT));
                    }
                }
            }

            knights &= knights - 1;  // Clear the least significant bit to move on to the next knight
        }

        return result;
    }

    /**
     * Returns an array of bitboards of this colors team pieces
     * @param is_white
     * @return Returns an array of bitboards of this colors team pieces
     */
    public long[] getTeamBitBoards(boolean is_white){
        if(is_white){
            return new long[]{WHITE_PAWN_BITBOARD.getLong(), WHITE_KNIGHT_BITBOARD.getLong(), WHITE_BISHOP_BITBOARD.getLong(), WHITE_ROOK_BITBOARD.getLong(), WHITE_QUEEN_BITBOARD.getLong(), WHITE_KING_BITBOARD.getLong()};
        }else{
            return new long[]{BLACK_PAWN_BITBOARD.getLong(), BLACK_KNIGHT_BITBOARD.getLong(), BLACK_BISHOP_BITBOARD.getLong(), BLACK_ROOK_BITBOARD.getLong(), BLACK_QUEEN_BITBOARD.getLong(), BLACK_KING_BITBOARD.getLong()};
        }
    }

    public long[] getTeamBitBoardsWithoutKing(boolean is_white){
        if(is_white){
            return new long[]{WHITE_PAWN_BITBOARD.getLong(), WHITE_KNIGHT_BITBOARD.getLong(), WHITE_BISHOP_BITBOARD.getLong(), WHITE_ROOK_BITBOARD.getLong(), WHITE_QUEEN_BITBOARD.getLong()};
        }else{
            return new long[]{BLACK_PAWN_BITBOARD.getLong(), BLACK_KNIGHT_BITBOARD.getLong(), BLACK_BISHOP_BITBOARD.getLong(), BLACK_ROOK_BITBOARD.getLong(), BLACK_QUEEN_BITBOARD.getLong()};
        }
    }

    /**
     * Returns an array of every pieces bitboard
     * @return Returns an array of every pieces bitboard
     */
    public long[] getAllBitboards(){
        return new long[]{
            WHITE_PAWN_BITBOARD.getLong(), WHITE_KNIGHT_BITBOARD.getLong(), WHITE_BISHOP_BITBOARD.getLong(), WHITE_ROOK_BITBOARD.getLong(), WHITE_QUEEN_BITBOARD.getLong(), WHITE_KING_BITBOARD.getLong(),
            BLACK_PAWN_BITBOARD.getLong(), BLACK_KNIGHT_BITBOARD.getLong(), BLACK_BISHOP_BITBOARD.getLong(), BLACK_ROOK_BITBOARD.getLong(), BLACK_QUEEN_BITBOARD.getLong(), BLACK_KING_BITBOARD.getLong()
        };

    }
    /**
     * Returns if this square is a valid square. Valid squares are between 0 and 64
     * @param square
     * @return Returns if this square is a valid one.
     */
    public boolean validSquare(int square){
        return square >= 0 && square < 64;
    }

    /**
     * @param isWhite
     * @return Returns the int position of this color's king
     */
    public int getKingPosition(boolean isWhite){
        Bitboard kingBitboard = isWhite ? WHITE_KING_BITBOARD: BLACK_KING_BITBOARD;
        return Long.numberOfTrailingZeros(kingBitboard.getLong());
    }

    /**
     * 
     * @param square
     * @param ownTeamPieces
     * @return
     */
    public boolean isOccupiedByOwnTeam(int square, long ownTeamPieces) {
        long bit = 1L << square;
        return (ownTeamPieces & bit) != 0;
    }

    /**
     * Returns the {@code Piece} at this square
     * @param square
     * @return Returns the {@code Piece} at this square
     */
    public Piece getPieceAtSquare(int square){
        return getPieceAtSquare(square,getAllBitboards());
    }

    public Piece getPieceAtSquare(int square, long[] board){
        long bit = 1L << square;
        Piece[] pieces = Piece.values();
        for(int i=0; i < board.length; i++){
            if((board[i] & bit) != 0){
                return pieces[i];
            }
        }
        
        return Piece.EMPTY;
    }

    public Move validateUserMove(int previousSquare, int targetSquare, Piece piece){
        List<Move> legalMoves = getCurrentLegalMoves(this.IS_WHITE_TURN);
        boolean promotion = (piece == Piece.WHITE_PAWN && targetSquare < 8) || (piece == Piece.BLACK_PAWN && targetSquare > 55);
        for(Move move: legalMoves){
            if(move.fromSquare == previousSquare && move.toSquare == targetSquare && move.piece == piece && !promotion){
                return move;
            }
            if(promotion && (move.promotionName == Piece.WHITE_QUEEN || move.promotionName == Piece.BLACK_QUEEN) && move.toSquare == targetSquare){
                return move;
            }
        }
        return null;
    }

    /**
     * Sets each bitboard, enpassant position, castling rights, half moves and current turn according to fen position. Must be
     * a valid fen string
     * @param fenposition
     */
    public void setBoard(String fenposition){
        
        //Saved fenpositions
        switch (fenposition){
            case "castle_test": fenposition = "r3k2r/pppppppp/8/8/8/8/PPPPPPPP/R3K2R w KQkq - 0 0"; break;
            case "castle_white_king_side": fenposition = "4k3/8/8/8/8/8/8/4K2R w KQkq - 0 0"; break;
            case "start": fenposition = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - random"; break;
            case "random": fenposition = "r3k2r/ppqbpp1p/n1pp2pb/8/8/2PP1NP1/PPQ1PPBP/2KR3R w kq - random"; break;
            case "knight_test": fenposition = "2k5/8/8/8/4N3/8/8/2K5 w - - 0 59"; break;
            case "bishop_test": fenposition = "2k5/8/8/8/4B3/8/8/2K5 w - - 0 59"; break;
            case "rook_test": fenposition = "2k5/8/8/8/4R3/8/8/2K5 w - - 0 59"; break;
            case "queen_test": fenposition = "2k5/8/8/8/4Q3/8/8/2K5 w - - 0 59"; break;
            case "pawn_test": fenposition = "3k4/1P6/8/5pP1/8/3p4/4P3/2K5 w - f6 0 59"; break;
            default: break;
        }

        String[] boardInformation = fenposition.split("\\s+");

        String position = boardInformation[0];
        String turn = boardInformation[1];
        String castleRights = boardInformation[2];
        String enpassantSquare = boardInformation[3];
        String halfmoves = boardInformation[4];

        // Set turn
        this.IS_WHITE_TURN = turn.equals("w");

        //Init all bitboards to empty
        WHITE_PAWN_BITBOARD = new Bitboard(0L); 
        WHITE_BISHOP_BITBOARD = new Bitboard(0L); 
        WHITE_KNIGHT_BITBOARD = new Bitboard(0L);
        WHITE_ROOK_BITBOARD = new Bitboard(0L); 
        WHITE_QUEEN_BITBOARD = new Bitboard(0L); 
        WHITE_KING_BITBOARD = new Bitboard(0L); 
        BLACK_PAWN_BITBOARD = new Bitboard(0L); 
        BLACK_BISHOP_BITBOARD = new Bitboard(0L); 
        BLACK_KNIGHT_BITBOARD = new Bitboard(0L); 
        BLACK_ROOK_BITBOARD = new Bitboard(0L); 
        BLACK_QUEEN_BITBOARD = new Bitboard(0L);
        BLACK_KING_BITBOARD = new Bitboard(0L);

        ENPASSANT_SQUARE = -1;
        CASLTING_RIGHTS = 0;

        int row = 0;  // Start at the top row
        int col = 0;  // Start at the first column

        //Set piece position
        for (char c : position.toCharArray()) {
            switch (c) {
                case 'P': WHITE_PAWN_BITBOARD.setBit(row * 8 + col); break;
                case 'N': WHITE_KNIGHT_BITBOARD.setBit(row * 8 + col);break;
                case 'B': WHITE_BISHOP_BITBOARD.setBit(row * 8 + col); break;
                case 'R': WHITE_ROOK_BITBOARD.setBit(row * 8 + col); break;
                case 'Q': WHITE_QUEEN_BITBOARD.setBit(row * 8 + col); break;
                case 'K': WHITE_KING_BITBOARD.setBit(row * 8 + col); break;
                case 'p': BLACK_PAWN_BITBOARD.setBit(row * 8 + col); break;
                case 'n': BLACK_KNIGHT_BITBOARD.setBit(row * 8 + col); break;
                case 'b': BLACK_BISHOP_BITBOARD.setBit(row * 8 + col); break;
                case 'r': BLACK_ROOK_BITBOARD.setBit(row * 8 + col); break;
                case 'q': BLACK_QUEEN_BITBOARD.setBit(row * 8 + col); break;
                case 'k': BLACK_KING_BITBOARD.setBit(row * 8 + col); break;
                case '/': row++; col = -1; break;  // Move to the next row
                default: col += Character.getNumericValue(c) - 1; break;  // Skip empty squares
            }

            col++;
        }

        // Set enpassant square
        if (!enpassantSquare.equals("-")) {
            int epCol = enpassantSquare.charAt(0) - 'a';
            int epRow = 7 - (enpassantSquare.charAt(1) - '1');
            this.ENPASSANT_SQUARE = epRow * 8 + epCol;
        }

        // Set castling rights
        if (castleRights.contains("K")) CASLTING_RIGHTS |= 1;  // White King-side
        if (castleRights.contains("Q")) CASLTING_RIGHTS |= 2;  // White Queen-side
        if (castleRights.contains("k")) CASLTING_RIGHTS |= 4;  // Black King-side
        if (castleRights.contains("q")) CASLTING_RIGHTS |= 8;  // Black Queen-side

        getCurrentLegalMoves(this.IS_WHITE_TURN);
    }

    /**
     * Prints all board information in ASCII format
     */
    public void printBoard() {
        int enpassantPosition = this.ENPASSANT_SQUARE;
        String enpasantSquare = (enpassantPosition == -1) ? "NONE":"" + (char)('a' + enpassantPosition%8) + (8-enpassantPosition/8);
        for (int row = 0; row < 8; row++) {
            System.out.print(8-row + " | ");
            for (int col = 0; col < 8; col++) {
                long pos = 1L << (row * 8 + col);
    
                if ((WHITE_PAWN_BITBOARD.getLong() & pos) != 0) { System.out.print("P  ");}
                else if ((WHITE_KNIGHT_BITBOARD.getLong() & pos) != 0) { System.out.print("N  ");}
                else if ((WHITE_BISHOP_BITBOARD.getLong() & pos) != 0) { System.out.print("B  ");}
                else if ((WHITE_ROOK_BITBOARD.getLong() & pos) != 0) { System.out.print("R  ");}
                else if ((WHITE_QUEEN_BITBOARD.getLong() & pos) != 0) { System.out.print("Q  ");}
                else if ((WHITE_KING_BITBOARD.getLong() & pos) != 0) { System.out.print("K  ");}
                
                else if ((BLACK_PAWN_BITBOARD.getLong() & pos) != 0) { System.out.print("p  ");}
                else if ((BLACK_KNIGHT_BITBOARD.getLong() & pos) != 0) { System.out.print("n  ");}
                else if ((BLACK_BISHOP_BITBOARD.getLong() & pos) != 0) { System.out.print("b  ");}
                else if ((BLACK_ROOK_BITBOARD.getLong() & pos) != 0) { System.out.print("r  ");}
                else if ((BLACK_QUEEN_BITBOARD.getLong() & pos) != 0) { System.out.print("q  ");}
                else if ((BLACK_KING_BITBOARD.getLong() & pos) != 0) { System.out.print("k  ");}

                else{
                    System.out.print(".  ");
                }
            }
            if(row == 0) System.out.print("\t TURN: " + (this.IS_WHITE_TURN ? "WHITE":"BLACK"));
            if(row == 1) System.out.print("\t CASTLE RIGHTS: " + 
            ((CASLTING_RIGHTS & 1) != 0 ? "K":"") +
            ((CASLTING_RIGHTS & 2) != 0 ? "Q":"") +
            ((CASLTING_RIGHTS & 3) != 0 ? "k":"") + 
            ((CASLTING_RIGHTS & 4) != 0 ? "q":""));
            if(row == 2) System.out.print("\t ENPASSANT CAPTURE: " + (enpasantSquare.equals("a0")?"NONE":enpasantSquare));

            if(row != 7) System.out.println("\n  | ");

        }

        System.out.println("\n   ------------------------");
        System.out.print("    ");
        for(int i=0; i<8; i++){
            System.out.print((char)('a' + i) + "  ");
        }

        System.out.println("\n");
    }
    










    
}