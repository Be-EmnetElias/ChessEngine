import java.util.*;

import util.*;

public class Board {

    /**
     * Represents the 8x8 chess board. Each cell can contain a Piece or be null if there is no piece on that cell.
     */
    public Piece[][] BOARD;

    /**
     * A mapping from each Piece type to a list of its pseudo-legal displacements. 
     * Each move is represented as a displacement vector (dx, dy).
     */
    public EnumMap<Name, HashSet<Square>> PSEUDO_LEGAL_MOVEMENT;

    /**
     * The current state of the game. This can be ACTIVE, STALEMATE, BLACKWON, WHITEWON, or INSUFFICIENT_MATERIAL.
     */
    public GameState GAME_STATE;

    /**
     * The number of moves that have been made in the game so far.
     */
    public int MOVE_NUMBER;

    /**
     * A history of all moves made in the game.
     */
    public HashMap<Integer, Move> MOVE_HISTORY;

    /**
     * A mapping from each Piece type to a list of all legal moves for that type for the white player in the current game state.
     */
    public HashMap<Piece, HashSet<Move>> CURRENT_WHITE_LEGAL_MOVES;

    /**
     * A mapping from each Piece type to a list of all legal moves for that type for the black player in the current game state.
     */
    public HashMap<Piece, HashSet<Move>> CURRENT_BLACK_LEGAL_MOVES;

    /**
     * A boolean that is true if it's white's turn to move and false if it's black's turn.
     */
    public Boolean WHITE_TURN;

    /**
     * An integer that represents the orientation of the board. 1 represents the normal orientation and -1 represents the flipped orientation.
     */
    public int BOARD_ORIENTATION;

    /**
     * Initializes board class. Use {#link }
     * @param initial_position
     */
    public Board(){

        this.PSEUDO_LEGAL_MOVEMENT = new EnumMap<>(Name.class);
        initPSEUDO_LEGAL_MOVEMENT();

        this.MOVE_HISTORY = new HashMap<>();

        this.CURRENT_WHITE_LEGAL_MOVES = new HashMap<>();

        this.CURRENT_BLACK_LEGAL_MOVES = new HashMap<>();


    }

    /**
     * Initializes PSEUDO_LEGAL_MOVEMENT with piece names and their displacements
     */
    private void initPSEUDO_LEGAL_MOVEMENT(){
        HashSet<Square> PAWNW = new HashSet<>(Arrays.asList(
            new Square(0,-1), new Square(-1,-1), new Square(1,-1)
        ));
        
        HashSet<Square> PAWNB = new HashSet<>(Arrays.asList(
            new Square(0,1), new Square(-1,1), new Square(1,1)
        ));

        HashSet<Square> KNIGHT = new HashSet<>(Arrays.asList(
            new Square(1,2), new Square(-1,2), new Square(1,-2), new Square(-1,-2),
            new Square(2,1), new Square(-2,1), new Square(2,-1), new Square(-2,-1)
        ));

        HashSet<Square> BISHOP = new HashSet<>(Arrays.asList(
            new Square(1,1), new Square(-1,1), new Square(1,-1), new Square(-1,-1)
        ));

        HashSet<Square> ROOK = new HashSet<>(Arrays.asList(
            new Square(1,0), new Square(-1,0), new Square(0,1), new Square(0,-1)
        ));

        HashSet<Square> QUEEN = new HashSet<>();
        QUEEN.addAll(BISHOP);
        QUEEN.addAll(ROOK);

        HashSet<Square> KING = new HashSet<>();
        KING.addAll(BISHOP);
        KING.addAll(ROOK);

        this.PSEUDO_LEGAL_MOVEMENT.put(Name.PAWNW,PAWNW);
        this.PSEUDO_LEGAL_MOVEMENT.put(Name.PAWNB,PAWNB);
        this.PSEUDO_LEGAL_MOVEMENT.put(Name.KNIGHT,KNIGHT);
        this.PSEUDO_LEGAL_MOVEMENT.put(Name.BISHOP,BISHOP);
        this.PSEUDO_LEGAL_MOVEMENT.put(Name.ROOK,ROOK);
        this.PSEUDO_LEGAL_MOVEMENT.put(Name.QUEEN,QUEEN);
        this.PSEUDO_LEGAL_MOVEMENT.put(Name.KING,KING);

    }

    /**
     * Creates a board with pieces according to this fenposition.
     * Updates GameState, Move_Number etc....
     * @param fenposition
     */
    public void setBoard(String fenposition){

        this.BOARD = new Piece [8][8];

        this.MOVE_NUMBER = 1;

        switch (fenposition){

            case "test_castle":
                fenposition = "r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 0";
                break;
            case "random":
                fenposition = "r3k2r/ppqbpp1p/n1pp2pb/8/8/2PP1NP1/PPQ1PPBP/2KR3R b kq - random";
                break;
            default:
                fenposition = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
                break;
        }

        String[] boardInformation = fenposition.split("\\s+");

        String position = boardInformation[0];
        String turn = boardInformation[1];
        String castleRights = boardInformation[2];
        String enpassantSquare = boardInformation[3];
        String halfmoves = boardInformation[4];

        // Set turn
        this.WHITE_TURN = turn.equals("w");

        int row = 0;
        int col = 0;

        // Set position
        for(int i=0;i<position.length();i++){
            char c = position.charAt(i);
            Name name = null;
            int image_index = -1;
            boolean white = true;

            switch(c){
                case 'r':
                    name = Name.ROOK;
                    image_index = 10;
                    white = false;
                    break;
                case 'n':
                    name = Name.KNIGHT;
                    image_index = 9;
                    white = false;
                    break;
                case 'b':
                    name = Name.BISHOP;
                    image_index = 8;
                    white = false;
                    break;
                case 'q':
                    name = Name.QUEEN;
                    image_index = 7;
                    white = false;
                    break;
                case 'k':
                    name = Name.KING;
                    image_index = 6;
                    white = false;
                    break;
                case 'p':
                    name = Name.PAWNB;
                    image_index = 11;
                    white = false;
                    break;
                case 'R':
                    name = Name.ROOK;
                    image_index = 4;
                    white = true;
                    break;
                case 'N':
                    name = Name.KNIGHT;
                    image_index = 3;
                    white = true;
                    break;
                case 'B':
                    name = Name.BISHOP;
                    image_index = 2;
                    white = true;
                    break;
                case 'Q':
                    name = Name.QUEEN;
                    image_index = 1;
                    white = true;
                    break;
                case 'K':
                    name = Name.KING;
                    image_index = 0;
                    white = true;
                    break;
                case 'P':
                    name = Name.PAWNW;
                    image_index = 5;
                    white = true;
                    break;
                case '/':
                    name = null;
                    col = -1;
                    row+=1;
                    break;
                default:
                    name = null;
                    col += Integer.parseInt(c+"")-1;
                    break;
            }

            // TODO: When making a new piece, its firstMove is auto set to true.
            if (name != null) setPiece(new Piece(name,white,col,row,image_index),new Square(col,row));
            

            col = (col >= 8) ? 0:col+1;

        }

        Piece whiteRookQueenSide =  (getPiece(new Square(0,7)) != null && getPiece(new Square(0,7)).name == Name.ROOK) ?  getPiece(new Square(0,7)) : null;
        Piece whiteRookKingSide =  (getPiece(new Square(7,7)) != null && getPiece(new Square(7,7)).name == Name.ROOK) ?  getPiece(new Square(7,7)) : null;
        Piece blackRookQueenSide =  (getPiece(new Square(0,0)) != null && getPiece(new Square(0,0)).name == Name.ROOK) ?  getPiece(new Square(0,0)) : null;
        Piece blackRookKingSide =  (getPiece(new Square(7,0)) != null && getPiece(new Square(7,0)).name == Name.ROOK) ?  getPiece(new Square(7,0)) : null;

        if (whiteRookKingSide != null) whiteRookKingSide.firstMove = false;
        if (whiteRookQueenSide != null) whiteRookQueenSide.firstMove = false;
        if (blackRookKingSide != null) blackRookKingSide.firstMove = false;
        if (blackRookQueenSide != null) blackRookQueenSide.firstMove = false;

        if (!castleRights.equals("-")){
            for(char c: castleRights.toCharArray()){
                if (c == '-') continue;

                if (c == 'K' && whiteRookKingSide != null) whiteRookKingSide.firstMove = true;
                if (c == 'Q' && whiteRookQueenSide != null) whiteRookQueenSide.firstMove = true;
                if (c == 'k' && blackRookKingSide != null) blackRookKingSide.firstMove = true;
                if (c == 'q' && blackRookQueenSide != null) blackRookQueenSide.firstMove = true;

            }
        }

        if (!enpassantSquare.equals("-")){
            // Square conversion: ASCII code - 97
            row = 8 - Integer.parseInt(enpassantSquare.charAt(1)+"");
            col = enpassantSquare.charAt(0) - 97;
            int dy = (row > 6) ? -1:1;
            Piece enPassantPiece = getPiece(new Square(col, row+dy));
            if (enPassantPiece != null) enPassantPiece.enPassant = true;
        }

        updateCurrentLegalMoves();
    }

    public void updateCurrentLegalMoves(){

        for(Piece piece: getPieces(true)){
            HashSet<Move> moves = legalMoves(piece, true);
            System.out.print(piece.name + " | ");
            System.out.println();
            this.CURRENT_WHITE_LEGAL_MOVES.put(piece, moves);
        }

        for(Piece piece: getPieces(false)){
            HashSet<Move> moves = legalMoves(piece, true);
            this.CURRENT_BLACK_LEGAL_MOVES.put(piece, moves);
        }

    }

    public String getPositionFenstring(){

        throw new UnsupportedOperationException();

    }

    public void printBoard(Move highlight){
        
        for(int row=0; row<8; row++){
            for(int col=0; col<8; col++){
                Piece piece = getPiece(new Square(col,row));
                if (piece != null) System.out.print(piece.getLetter());
                else System.out.print(". ");
            }
            System.out.println("\n");
        }

    }

    /**
     * Checks whether this piece can move to this position. This function is called by the visuals to see
     * if a user-selected piece can move to a user-selected position
     * @param piece
     * @param position
     * @return 
     */
    public Move userValidMove(Piece piece, Square position){

        Move potentialMove = null;

        HashSet<Move> legalMovesForPiece = (piece.white) ? this.CURRENT_WHITE_LEGAL_MOVES.get(piece):this.CURRENT_BLACK_LEGAL_MOVES.get(piece);
        
        for(Move move: legalMovesForPiece){
            if(position.equals(move.targetPosition) /* TODO: && piece.white == this.WHITE_TURN*/){
                potentialMove = new Move(move.piece, move.capturedPiece, move.previousPosition, move.targetPosition, move.moveType);
                if (areEnemies(piece, move.capturedPiece)) potentialMove.moveType = MoveType.CAPTURE;

                return potentialMove;
            }
        }

        return null;

    }

    /**
     * Applies this move to the board. Assumes that this move is already legal
     * @param piece
     * @param move
     * @param simulation
     * @return Returns this move with the updated castling, enpassant or promotion
     */
    public Move movePiece(Move move){

        Move result = null;

        Piece piece = move.piece;
        Piece capturedPiece = move.capturedPiece;
        Square previousPosition = move.previousPosition;
        Square targetPosition = move.targetPosition;
        MoveType moveType = move.moveType;
        Boolean enPassantSave = piece.enPassant;

        if ((moveType == MoveType.DEFAULT) || ( moveType == MoveType.CASTLE_KING_SIDE) || (moveType == MoveType.CASTLE_QUEEN_SIDE)){
            setPiece(piece,targetPosition);
            setPiece(null,previousPosition);

            piece.enPassant = (EnumSet.of(Name.PAWNW,Name.PAWNB).contains(piece.name) && piece.firstMove && Math.abs(targetPosition.row-previousPosition.row)==2);
            result = new Move(piece,capturedPiece, previousPosition, targetPosition, moveType);
        }

        if ((moveType == MoveType.CASTLE_KING_SIDE) || (moveType == MoveType.CASTLE_QUEEN_SIDE)){
            Piece rook = move.rookToCastle;
            previousPosition = rook.getPosition();
            Square targetPositionRook = move.rookTargetPosition;
            capturedPiece = getPiece(targetPositionRook);
            setPiece(rook,targetPositionRook);
            setPiece(null,previousPosition);

            rook.firstMove = false;
            result = move;
            
        }

        if (moveType == MoveType.PROMOTION){
            setPiece(piece, targetPosition);
            setPiece(null, previousPosition);

            piece.name = Name.QUEEN; //TODO: LET USER PICK
            //piece.imgIndex = piece.white ? 1:7; //TODO: enable when visuals work

            piece.canSlide = true;

            result = new Move(piece, capturedPiece, previousPosition, targetPosition, moveType);
        }

        if (moveType == MoveType.ENPASSANT){
            Square enPassantSquare = new Square(targetPosition.col,previousPosition.row);
            capturedPiece = getPiece(enPassantSquare);
            setPiece(piece, targetPosition);
            setPiece(null, previousPosition);
            setPiece(null, enPassantSquare);

            result = new Move(piece, capturedPiece, previousPosition, targetPosition, moveType);

        }
        
        this.MOVE_HISTORY.put(this.MOVE_NUMBER, result);
        this.MOVE_NUMBER += 1;
        this.WHITE_TURN = !this.WHITE_TURN;
        this.updateCurrentLegalMoves();
        //TODO: this.updateGameStatus();

        return result;
    }

    public void undoMove(Move move){
        Piece piece = move.piece;
        Piece capturedPiece = move.capturedPiece;
        Square previousPosition = move.previousPosition;
        Square targetPosition = move.targetPosition;
        MoveType moveType = move.moveType;

        if(piece.name != Name.PAWNW && piece.name != Name.PAWNB){
            piece.firstMove = move.firstMove;
        }

        if(moveType == MoveType.DEFAULT || moveType == MoveType.CASTLE_KING_SIDE || moveType == MoveType.CASTLE_QUEEN_SIDE){
            setPiece(piece, previousPosition);
            setPiece(capturedPiece, targetPosition);

            piece.col = previousPosition.col;
            piece.row = previousPosition.row;

            if(piece.name == Name.PAWNW){
                piece.firstMove = previousPosition.row == 6;
            }

            if(piece.name == Name.PAWNB){
                piece.firstMove = previousPosition.row == 1;
            }

            if(moveType == MoveType.CASTLE_KING_SIDE || moveType == MoveType.CASTLE_QUEEN_SIDE){
                Piece rook = move.rookToCastle;
                previousPosition = move.rookPreviousPosition;
                targetPosition = move.rookTargetPosition;

                piece.firstMove = true;
                rook.firstMove = true;

                setPiece(rook,previousPosition);
                setPiece(capturedPiece,targetPosition);

                rook.col = previousPosition.col;
                rook.row = previousPosition.row;
            }
        }

        if(moveType == MoveType.ENPASSANT){
            Piece enPassantPiece = move.enPassantPiece;
            Square enPassantPosition = move.enPassantPosition;
            setPiece(piece,previousPosition);
            setPiece(null,targetPosition);
            setPiece(enPassantPiece,enPassantPosition);

            piece.col = previousPosition.col;
            piece.row = previousPosition.row;
        }

        if(moveType == MoveType.PROMOTION){
            setPiece(capturedPiece,targetPosition);
            setPiece(piece,previousPosition);

            piece.col = previousPosition.col;
            piece.row = previousPosition.row;

            piece.name = (targetPosition.row == 0) ? Name.PAWNW : Name.PAWNB;
            piece.canSlide = false;
        }
    }

    public GameState updateGameStatus(){

        throw new UnsupportedOperationException();

    }

    public HashSet<Move> legalMoves(Piece piece, Boolean checkKingSafety){

        HashMap<Piece, HashSet<Square>> pinnedPieces = updatePinnedPieces(piece.white);
        
        HashSet<Move> legalMoves = new HashSet<>();

        // Pawns and Kings are calculated separately
        if(piece.name == Name.PAWNW || piece.name == Name.PAWNB) return legalMoves; //return legalMovesPawn(piece, checkKingSafety, pinnedPieces);
        if (piece.name == Name.KING) return legalMoves; //return legalMovesKing(piece, checkKingSafety, pinnedPieces);
        
        HashSet<Square> displacements = this.PSEUDO_LEGAL_MOVEMENT.get(piece.name);

        for(Square displacement: displacements){

            if(pinnedPieces.keySet().contains(piece) && !pinnedPieces.get(piece).contains(displacement)) continue;

            Square previousPosition = piece.getPosition();
            Square targetPosition = piece.getPosition();
            targetPosition.displace(displacement);

            while(validPosition(targetPosition)){

                Piece targetPiece = getPiece(targetPosition);

                Move potentialMove = new Move(piece,targetPiece,previousPosition,targetPosition,MoveType.DEFAULT);

                // Empty spaces or enemies are added as long as the king is safe after this move
                if((targetPosition == null || areEnemies(targetPiece, piece)) && kingSafeAfterMove(potentialMove, checkKingSafety)){
                    legalMoves.add(potentialMove);
                }

                // If a piece is in the way, or this piece cannot slide, break and try new direction
                if(targetPiece != null || !piece.canSlide) break;
                
                targetPosition.displace(displacement);

            }

        }


        return legalMoves;
    }

    public HashSet<Move> legalMovesKing(Piece king, Boolean checkKingSafety, HashMap<Piece, HashSet<Square>> pinnedPieces){

        HashSet<Move> kingMoves = new HashSet<>();
        HashSet<Square> displacements = this.PSEUDO_LEGAL_MOVEMENT.get(king.name);

        for(Square displacement: displacements){
            if(pinnedPieces.keySet().contains(king) && !pinnedPieces.get(king).contains(displacement)) continue;
            
            Square target = king.getPosition();
            target.displace(displacement);

            if(validPosition(target)){
                Piece targetPiece = getPiece(target);
                Move potentialMove = new Move(king, targetPiece, king.getPosition(), target, MoveType.DEFAULT);

                if((targetPiece == null || areEnemies(targetPiece, king)) && kingSafeAfterMove(potentialMove, checkKingSafety)){
                    kingMoves.add(potentialMove);

                    target.displace(new Square(displacement.col,0));
                    if(king.firstMove && validPosition(target)){
                        targetPiece = getPiece(target);
                        int dx = displacement.col;
                        MoveType moveType = (dx == 1) ? MoveType.CASTLE_KING_SIDE: MoveType.CASTLE_QUEEN_SIDE;
                        potentialMove = new Move(king,targetPiece,king.getPosition(),target,moveType);
                        Piece rook = canRookCastle(king, dx);
                        
                        if((dx == 1 || dx == -1) && displacement.row == 0 && getPiece(target) == null && rook!=null){
                            potentialMove.rookPreviousPosition = rook.getPosition();
                            potentialMove.rookToCastle = rook;
                            potentialMove.rookTargetPosition = new Square(potentialMove.targetPosition.col-dx,potentialMove.targetPosition.row);
                            kingMoves.add(potentialMove);
                            
                        }
                    }
                }


            }
        }

        return kingMoves;

    }

    public HashSet<Move> legalMovesPawn(Piece pawn, Boolean checkKingSafety, HashMap<Piece, HashSet<Square>> pinnedPieces){

        HashSet<Move> pawnMoves = new HashSet<>();

        HashSet<Square> displacements = this.PSEUDO_LEGAL_MOVEMENT.get(pawn.name);

        for(Square displacement: displacements){
            if(pinnedPieces.keySet().contains(pawn) && !pinnedPieces.get(pawn).contains(displacement)) continue;

            Square previousPosition = pawn.getPosition();
            Square targetPosition = pawn.getPosition();
            targetPosition.displace(displacement);

            if(!validPosition(targetPosition)) continue;

            MoveType moveType = ((pawn.white && pawn.row == 0) || (!pawn.white && pawn.row == 7)) ? MoveType.PROMOTION:MoveType.DEFAULT;
            Piece targetPiece = getPiece(targetPosition);

            Move potentialMove = new Move(pawn,targetPiece,previousPosition, targetPosition, moveType);
            
            // Forward move
            if(targetPosition.col == 0 && targetPiece == null && kingSafeAfterMove(potentialMove, checkKingSafety)){
                pawnMoves.add(potentialMove);

                // // Double Forward Move
                // if(pawn.firstMove){

                //     targetPosition.displace(new Square(0,targetPosition.row));
                //     targetPiece = getPiece(targetPosition);
                //     potentialMove = new Move(pawn,targetPiece,previousPosition,targetPosition,moveType);

                //     if(targetPosition == null && kingSafeAfterMove(potentialMove, checkKingSafety)){
                //         pawnMoves.add(potentialMove);
                //     }
                // }
            }

            // // Diagonal Capture
            // if (targetPosition.col != 0 && areEnemies(targetPiece, pawn) && kingSafeAfterMove(potentialMove, checkKingSafety)){
            //     pawnMoves.add(potentialMove);
            // }

            // Square enPassantPosition = new Square(previousPosition.col, targetPosition.row);
            // Piece enPassantPiece = getPiece(enPassantPosition);
            // potentialMove.moveType = MoveType.ENPASSANT;
            // potentialMove.enPassantPiece = enPassantPiece;

            // // Enpassant capture
            // if(targetPosition.col != 0 && targetPiece == null && areEnemies(enPassantPiece, pawn) && enPassantPiece.enPassant && kingSafeAfterMove(potentialMove, checkKingSafety)){
            //     pawnMoves.add(potentialMove);
            // }else{
            //     potentialMove.moveType = moveType;
            //     potentialMove.enPassantPiece = null;
            // }
        }

        return pawnMoves;

    }

    public Boolean kingSafeAfterMove(Move move, Boolean checkKingSafety){

        if(!checkKingSafety) return true;

        // If this piece's king is captured, return false
        if(move.capturedPiece != null && move.capturedPiece.white != move.piece.white && move.capturedPiece.name == Name.KING) return false;
    
        Piece piece = move.piece;
        boolean kingSafeAfterMove = true;
        HashSet<Square> spacesInCheck = new HashSet<>();

        Move simulatedMove = movePiece(move);

        // Check if king is in the attacking squares of enemy pieces
        Square king = getKing(piece.white).getPosition();

        // Find all squares that enemies are attacking, no need to check for their king safety
        for(Piece enemy: getPieces(!piece.white)){
            HashSet<Move> enemyLegalMoves = legalMoves(enemy,false);

            for(Move enemyMove:enemyLegalMoves){
                spacesInCheck.add(enemyMove.targetPosition);

                if(spacesInCheck.contains(king)){
                    kingSafeAfterMove = false;
                    break;
                }
            }
        }

        undoMove(simulatedMove);

        return kingSafeAfterMove;

    }

    public HashMap<Piece, HashSet<Square>> updatePinnedPieces(boolean white){

        HashMap<Piece, HashSet<Square>> pinnedPieces = new HashMap<>();
        Piece king = getKing(white);
        HashSet<Square> displacements = this.PSEUDO_LEGAL_MOVEMENT.get(Name.QUEEN);

        Square target = king.getPosition();
        // For this king expand outwards in all 8 directions (queen movement)

        for(Square displacement: displacements){
            Piece potentialPin = null;
            target.displace(displacement);

            while(validPosition(target)){
                Piece targetPiece = getPiece(target);

                if(targetPiece == null){
                    target.displace(displacement);
                    continue;
                }

                if(areEnemies(targetPiece, king)){
                    if(potentialPin != null && targetPiece.canSlide && this.PSEUDO_LEGAL_MOVEMENT.get(targetPiece.name).contains(displacement)){
                        pinnedPieces.put(potentialPin,new HashSet<>(Arrays.asList(new Square(displacement.col,displacement.row), new Square(displacement.col * -1, displacement.row * -1))));
                    }
                    break;
                }else{
                    if(potentialPin != null) break;
                    else potentialPin = targetPiece;
                }

                target.displace(displacement);
            }

        }

        return pinnedPieces;
    }

    public Piece canRookCastle(Piece king, int dx){
        Piece rook = null;
        if (king.white) rook = (dx==1) ? getPiece(new Square(7,7)): getPiece(new Square(0,7));
        else rook = (dx == 1) ? getPiece(new Square(7,0)): getPiece(new Square(0,0));

        return rook;

    }

    public Piece getKing(Boolean white){

        for(Piece piece: getPieces(white)){
            if(piece.white == white && piece.name == Name.KING){
                return piece;
            }
        }

        throw new IllegalStateException("KING IS NOT FOUND");

    }

    public Boolean areEnemies(Piece a, Piece b){

        if (a==null || b==null) return false;
        return a.white != b.white;

    }

    public Piece getPiece(Square position){

        return this.BOARD[position.row][position.col];

    }

    /**
     * Sets this piece at this col and row on the board, overriding anything at this position
     * Updates this piece's col and row
     * @param newPiece
     * @param col
     * @param row
     */
    public void setPiece(Piece newPiece, Square position){

        this.BOARD[position.row][position.col] = newPiece;

        if(newPiece != null){
            newPiece.col = position.col;
            newPiece.row = position.row;
        }

    }

    public Boolean validPosition(Square position){

        return position.col <= 7 && position.col >= 0 && position.row <= 7 && position.row >=0;

    }

    public HashSet<Piece> getPieces(Boolean white){

        HashSet<Piece> pieces = new HashSet<>();

        for(int row=0;row<=7;row++){
            for(int col=0;col<=7;col++){
                Piece current = getPiece(new Square(col,row));
                if(current==null) continue;

                if(current.white == white){
                    pieces.add(current);
                }
            }
        }
        return pieces;

    }
}