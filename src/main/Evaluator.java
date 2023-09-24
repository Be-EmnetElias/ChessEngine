package main;
/**
 * * The board evaluator has 2 different evaluators. Both evaluators use the nega-max algorithm to traverse all moves to a certain depth.
 * * Each board position is then evaluated and given a score. The move that results in the highest score is then chosen. The first evaluator
 * * uses a static hand-crafted evaluator with chosen weights.
 */

import main.chessutil.*;

import java.util.*;



public class Evaluator {
    
    public Board BOARD;

    /**
     * Move priorities
     */
    public double CHECKMATE = 100, PROMOTION = 50, CASTLE = 30, CAPTURE = 25, CHECK = 10, DEFAULT = 0;

    private double getMovePriority(Move move){
        double result = 0.0;
        for(MoveType flag: move.flags){
            switch(flag){
                case CAPTURE:
                    result += CAPTURE;
                    break;
                case CASTLE:
                    result += CASTLE;
                    break;
                case CHECK:
                    result += CHECK;
                    break;
                case ENPASSANT:
                    result += CAPTURE;
                    break;
                case PROMOTION:
                    result += PROMOTION;
                    break;
                default:
                    break;
                
            }
        }
        return result;
    }

    public List<Move> orderMoves(List<Move> moves){
        moves.sort((move1,move2) -> Double.compare(getMovePriority(move2), getMovePriority(move1)));
        return moves;
    }

    /**
     * Returns the best move out of these currentLegalMoves of this depth
     * @param currentLegalMoves
     * @param depth
     * @param COLOR
     * @return
     */
    public Move bestMove(List<Move> currentLegalMoves, int depth, boolean COLOR) {
        double maxScore = Double.NEGATIVE_INFINITY;
        Move bestMove = !currentLegalMoves.isEmpty() ? currentLegalMoves.get(0):null;
        double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;
        for (Move move : currentLegalMoves) {
            BOARD.movePiece(move, true);
            List<Move> nextLegalMoves = orderMoves(BOARD.getCurrentLegalMoves(!COLOR));
            List<Move> nextEnemyLegalMoves = orderMoves(BOARD.getCurrentLegalMoves(COLOR));
            double score = -negamax(nextLegalMoves,nextEnemyLegalMoves, depth - 1, !COLOR,-beta,-alpha);  // Negate the score
            BOARD.undoMove(move, true);
    
            if (score > maxScore) {
                maxScore = score;
                bestMove = move;
            }

            alpha = Math.max(alpha, score);
            if(alpha >= beta) break;
        }
    
        return bestMove;
    }
    
    private double negamax(List<Move> currentLegalMoves,List<Move> enemyLegalMoves, int depth, boolean COLOR, double alpha, double beta) {
        if (depth <= 0) return staticEvaluation(currentLegalMoves,COLOR) - staticEvaluation(enemyLegalMoves, !COLOR);
    
        double maxScore = Double.NEGATIVE_INFINITY;
        
    
        for (Move move : currentLegalMoves) {
            BOARD.movePiece(move, true);
            List<Move> nextLegalMoves = orderMoves(BOARD.getCurrentLegalMoves(!COLOR));
            List<Move> nextEnemyLegalMoves = orderMoves(BOARD.getCurrentLegalMoves(COLOR));
            double score = -negamax(nextLegalMoves, nextEnemyLegalMoves, depth - 1, !COLOR, -beta, -alpha);  // Negate the score
            BOARD.undoMove(move, true);
    
            if (score > maxScore) {
                maxScore = score;
            }

            alpha = Math.max(alpha,score);
            if(alpha >= beta) break;
        }
    
        return maxScore;
    }

    private double basicEval(List<Move> currentLegalMoves, boolean isWhite){
        long[] team = BOARD.getTeamBitBoards(isWhite);
        double score = 0.0;
        score += Long.bitCount(team[0]) * 100;
        score += Long.bitCount(team[1]) * 200;
        score += Long.bitCount(team[2]) * 300;
        score += Long.bitCount(team[3]) * 500;
        score += Long.bitCount(team[4]) * 900;
        
        return score;
        
    }


// = = == == == === === WEIGHTS === === == == == = =

    public double 
    
    WEAK_COUNT_WEIGHT = 1.0,

    
    MATERIAL_COUNT_PAWN_WEIGHT = 100.0, 
    
    CENTER_PAWN_COUNT_WEIGHT = 150.0,
    
    KING_PAWN_SHIELD_WEIGHT = 120.0,
    
    ISOLATED_PAWN_WEIGHT = -100.0,
    
    DOUBLE_PAWN_WEIGHT = -200.0,
    
    RANK_PASSED_PAWN_WEIGHT = 1.0,
    
    PASS_PAWN_WEIGHT = 1.0,
    
    RANK_PASS_PAWN_WEIGHT = 1.0,

    BLOCKED_PAWN_WEIGHT = -1.0,
    
    BLOCKED_PASSED_PAWN_WEIGHT = -1.0,
    
    BACKWARD_PAWN_WEIGHT = -1.0,


    
    MATERIAL_COUNT_KNIGHT_WEIGHT = 200.0,
    
    KNIGHT_MOBILITY_WEIGHT = 2.0,
    
    KNIGHT_ON_OUTPOST_WEIGHT = 2.0,
    
    KNIGHT_ON_CENTER_WEIGHT = 2.0,
    
    KNIGHT_ON_OUTER_EDGE_1_WEIGHT = 1.9,
    
    KNIGHT_ON_OUTER_EDGE_2_WEIGHT = 1.5,
    
    KNIGHT_ON_OUTER_EDGE_3_WEIGHT = 1.2,
    
    KNIGHT_SUPPORTED_BY_PAWN_WEIGHT = 2.5,
    

    MATERIAL_COUNT_BISHOP_WEIGHT = 250.0,
    
    BISHOP_MOBILITY_WEIGHT = 2.1,

    BISHOP_ON_LARGE_DIAGONAL_WEIGHT = 2.5,

    BISHOP_PAIR_WEIGHT = 5.0,

    
    MATERIAL_COUNT_ROOK_WEIGHT = 500.0,
    
    ROOK_MOBILITY_WEIGHT = 5.0,

    ROOK_BEHIND_PASS_PAWN = 1.0,

    ROOK_ON_CLOSED_FILE = -5.0,

    ROOK_ON_OPEN_FILE = 5.0,

    ROOK_ON_SEMI_OPEN_FILE = 2.0,

    ROOKS_CONNECTED_WEIGHT = 3.0,


    
    MATERIAL_COUNT_QUEEN_WEIGHT = 900.0,
    
    QUEEN_MOBILITY_WEIGHT = 1.0,

    
    KING_CASTLED_WEIGHT = 10.0,

    KING_ATTACKED_VALUE_WEIGHT = -1.0,

    KING_DEFENDED_VALUE_WEIGHT = 1.0;


// * = = == == == === === === === === == == == = =




    /**
     * <p>
     * * A static evaluation function. Uses hand crafted functions multiplied by chosen weights to evaluate a board 
     * <p>
     * 
     * @param currentLegalMoves The current legal moves for the given board position
     * @see https://www.cmpe.boun.edu.tr/~gungort/undergraduateprojects/Tuning%20of%20Chess%20Evaluation%20Function%20by%20Using%20Genetic%20Algorithms.pdf
     * @return An double evaluating the position of this board
     */
    public double staticEvaluation(List<Move> currentLegalMoves, boolean isWhite){
        if(currentLegalMoves.isEmpty()){
            return Double.NEGATIVE_INFINITY;
        }
        double score = 0;
        long[] team = this.BOARD.getTeamBitBoards(isWhite);
        long[] enemies = this.BOARD.getTeamBitBoards(!isWhite);

        long board = 0L;

        for(long pieces: team){
            board |= pieces;
        }

        for(long pieces: enemies){
            board |= pieces;
        }

        long pawns = team[0];
        long knights = team[1];
        long bishops = team[2];
        long rooks = team[3];
        long queens = team[4];
        long king = team[5];

        long enemyPawns = enemies[0];

        EnumMap<Piece,List<Move>> pieceMovesMap = new EnumMap<>(Piece.class);

        for(Move move: currentLegalMoves){
            if(!pieceMovesMap.containsKey(move.piece)){
                pieceMovesMap.put(move.piece,new ArrayList<>(){{add(move);}});
            }else{
                List<Move> prevMoves = pieceMovesMap.get(move.piece);
                prevMoves.add(move);
                pieceMovesMap.put(move.piece, prevMoves);
            }
        }


        //GENERAL INFORMATION
        score += weakCount(currentLegalMoves, enemies)         *WEAK_COUNT_WEIGHT;

        //PAWN INFORMATION
        score += materialCount(pawns)  *MATERIAL_COUNT_PAWN_WEIGHT;
        score += centerPawnCount(pawns)                             *CENTER_PAWN_COUNT_WEIGHT;
        score += kingPawnShield(pawns,king)                              *KING_PAWN_SHIELD_WEIGHT;
        score += isolatedPawns(pawns)                          *ISOLATED_PAWN_WEIGHT;
        score += doubledPawns(pawns)                            *DOUBLE_PAWN_WEIGHT;
        score += passPawns(pawns, enemyPawns, isWhite)                              *PASS_PAWN_WEIGHT;
        score += rankPassPawn()                          *RANK_PASS_PAWN_WEIGHT;
        score += backwardPawn()                                 *BACKWARD_PAWN_WEIGHT;
        score += blockedPawn()                                  *BLOCKED_PAWN_WEIGHT;
        score += blockedPassPawn()                              *BLOCKED_PASSED_PAWN_WEIGHT;

        //KNIGHT INFORMATION
        score += materialCount(knights)                    *MATERIAL_COUNT_KNIGHT_WEIGHT;
        score += mobility(isWhite ? Piece.WHITE_KNIGHT: Piece.BLACK_KNIGHT, pieceMovesMap)            *KNIGHT_MOBILITY_WEIGHT;
        score += knightOnOutpost(knights, enemyPawns)                       *KNIGHT_ON_OUTPOST_WEIGHT;
        score += knightOnCenter(knights)                               *KNIGHT_ON_CENTER_WEIGHT;
        score += knightOnOuterEdge1(knights)                           *KNIGHT_ON_OUTER_EDGE_1_WEIGHT;
        score += knightOnOuterEdge2(knights)                           *KNIGHT_ON_OUTER_EDGE_2_WEIGHT;
        score += knightOnOuterEdge3(knights)                           *KNIGHT_ON_OUTER_EDGE_3_WEIGHT;
        score += knightSupportedByPawn(knights, pawns)                        *KNIGHT_SUPPORTED_BY_PAWN_WEIGHT;

        //BISHOP INFORMATION
        score += materialCount(bishops)                    *MATERIAL_COUNT_BISHOP_WEIGHT;
        score += mobility(isWhite ? Piece.WHITE_BISHOP: Piece.BLACK_BISHOP, pieceMovesMap)           *BISHOP_MOBILITY_WEIGHT;
        score += bishopOnLargeDiagonal(bishops)                        *BISHOP_ON_LARGE_DIAGONAL_WEIGHT;
        score += bishopPair(bishops)                                   *BISHOP_PAIR_WEIGHT;

        //ROOK INFORMATION
        score += materialCount(rooks)                      *MATERIAL_COUNT_ROOK_WEIGHT;
        score += mobility(isWhite ? Piece.WHITE_ROOK: Piece.BLACK_ROOK, pieceMovesMap)              *ROOK_MOBILITY_WEIGHT;
        score += rookBehindPassPawn()                           *ROOK_BEHIND_PASS_PAWN;
        score += rookOnClosedFile(rooks, pawns, enemyPawns)                             *ROOK_ON_CLOSED_FILE;
        score += rookOnOpenFile(rooks,board)                               *ROOK_ON_OPEN_FILE;
        score += rookOnSemiOpenFile(rooks,pawns)                           *ROOK_ON_SEMI_OPEN_FILE;
        score += rooksConnected(rooks,board)                               *ROOKS_CONNECTED_WEIGHT;

        //QUEEN INFORMATION
        score += materialCount(queens)                     *MATERIAL_COUNT_QUEEN_WEIGHT;
        score += mobility(isWhite ? Piece.WHITE_QUEEN: Piece.BLACK_QUEEN, pieceMovesMap)            *QUEEN_MOBILITY_WEIGHT;

        //KING INFORMATION
        score += kingCastled()                                 *KING_CASTLED_WEIGHT;
        score += kingAttackedValue()                           *KING_ATTACKED_VALUE_WEIGHT;
        score += kingDefendedValue()                           *KING_DEFENDED_VALUE_WEIGHT;

        return score;
    }


    /**
     * Returns the number of squares that cannot be protected by this team
     * @return
     */
    public int weakCount(List<Move> currentLegalMoves, long[] enemies){
        HashSet<Integer> protectedSquares = new HashSet<>();
        HashSet<Integer> currentSquares = new HashSet<>();
        int enemySquares = 0;
        for(Move move: currentLegalMoves){
            protectedSquares.add(move.toSquare);
            currentSquares.add(move.fromSquare);
        }
        for(long enemy: enemies){
            enemySquares += Long.bitCount(enemy);
        }
        return 64 - currentSquares.size() - enemySquares - protectedSquares.size();
    }

    /**
     * Returns the count of this type of piece
     * @param bitboard 
     * @return
     */
    public int materialCount(long bitboard){
        return Long.bitCount(bitboard);
    }

    /**
     * Returns the amount of squares this piece can move to
     * @param currentLegalMoves
     * @param name
     * @return
     */
    public int mobility(Piece name, EnumMap<Piece,List<Move>> moves){
        return moves.get(name) != null ? moves.get(name).size() : 0;
        
    }

    /**
     * Returns the count of pawns on the center squares: E4 D4 E5 D5
     * @return
     */
    public int centerPawnCount(long pawns){
        long centerMask = (1L << 27) | (1L << 28) | (1L << 35) | (1L << 36);
        long centerPawns = pawns & centerMask;
        return Long.bitCount(centerPawns);
    }

    /**
     * Returns tge count of pawns that are on the adjacent squares of the king
     * @return
     */
    public int kingPawnShield(long pawns, long king){
        int kingSquare = Long.numberOfTrailingZeros(king); // Find the position of the king
        int count = 0;

        // Define the relative positions of the squares adjacent to the king
        int[] adjacentOffsets = {-9, -8, -7, -1, 1, 7, 8, 9};

        for (int offset : adjacentOffsets) {
            int adjacentSquare = kingSquare + offset;

            // Check if the adjacent square is a valid square on the board
            if (adjacentSquare >= 0 && adjacentSquare < 64) {
                long adjacentMask = 1L << adjacentSquare;

                // Check if the adjacent square is occupied by a pawn
                if ((pawns & adjacentMask) != 0) {
                    count++;
                }
            }
        }

        return count;
    }

    /**
     * Returns the count of isolated pawns. Isolated pawns have no neighboring pawns
     * @return
     */
    public int isolatedPawns(long pawns){
        int count = 0;
        for (int i = 0; i < 64; i++) {
            if ((pawns & (1L << i)) != 0) { // If there is a pawn at this square
                int file = i % 8;
                long adjacentFiles = 0L;
                if (file > 0) adjacentFiles |= (pawns >> 1) & 0x7F7F7F7F7F7F7F7FL;
                if (file < 7) adjacentFiles |= (pawns << 1) & 0xFEFEFEFEFEFEFEFEL;
                if (adjacentFiles == 0) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Returns the count of doubled pawns. A pawn is doubled if it is on the same column as a friendly pawn
     * @return
     */
    public int doubledPawns(long pawns){
        int count = 0;
        for (int file = 0; file < 8; file++) {
            long fileMask = 0x0101010101010101L << file;
            long pawnsOnFile = pawns & fileMask;
            int numPawnsOnFile = Long.bitCount(pawnsOnFile);
            if (numPawnsOnFile > 1) {
                count += (numPawnsOnFile - 1);
            }
        }
        return count;
    }

    /**
     * Returns the count of pass pawns. A pass pawn has no enemy pawns on its column and the columns to its left and right
     * @return
     */
    public int passPawns(long pawns, long enemyPawns, boolean isWhite){
        int count = 0;
        for (int i = 0; i < 64; i++) {
            if ((pawns & (1L << i)) != 0) { // If there is a pawn at this square
                int file = i % 8;
                long fileMask = 0x0101010101010101L << file;
                long adjacentFiles = 0L;
                if (file > 0) adjacentFiles |= fileMask >> 1;
                if (file < 7) adjacentFiles |= fileMask << 1;
                long blockingFiles = fileMask | adjacentFiles;

                // Only consider squares in front of the pawn
                if (isWhite) {
                    blockingFiles &= ~((1L << i) - 1);
                } else {
                    blockingFiles &= -1L << i;
                }

                if ((enemyPawns & blockingFiles) == 0) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Returns the total ranks of all pass pawns. Measured by how far up a pawn has moved, not its current rank
     * @param TEAM
     * @return
     */
    public int rankPassPawn(){
        int score = 0;
        //TODO:
        return score;
    }
    
    /**
     * Returns the number of backward pawns. A pawn is backwards if its neighboring pawns are ahead of it
     * @param TEAM
     * @return
     */
    public int backwardPawn(){
        int score = 0;
        //TODO:
        return score;
    }


    /**
     * Returns the number of central pawns that are blocked by a team piece
     * @param TEAM
     * @return
     */
    public int blockedPawn(){
        int score = 0;
        //TODO:
        return score;
    }

    /**
     * Returns the number of pass pawns blocked by an enemy piece
     * @param TEAM
     * @return
     */
    public int blockedPassPawn(){
        int score = 0;
        //TODO:
        return score;
    }

    /**
     * Returns the amount of knights on an outpost. An outpost is a square that is not being attacked by enemy pawns
     * @return Returns the amount of knights on an outpost
     */
    public int knightOnOutpost(long knights, long enemyPawns){
        int score = 0;
        //TODO:
        return score;
    }


    /**
     * Returns how many knights are supported by ones pawns
     * @return
     */
    public int knightSupportedByPawn(long knights, long pawns){
        return 0;
    }

    /**
     * Returns how many knights are on the center of the board
     * @return
     */
    public int knightOnCenter(long knights){
        long centerSquares = (1L << 27) | (1L << 28) | (1L << 35) | (1L << 36);
        return Long.bitCount(knights & centerSquares);
    }

    /**
     * Returns how many knights are on the outer edge of the board
     * @param TEAM
     * @return
     */
    public int knightOnOuterEdge3(long knights){
        long outerEdge3 = 0xFF818181818181FFL;
        return Long.bitCount(knights & outerEdge3);
    }

    /**
     * Returns how many knights are on the inner outer edge of the board
     * @param TEAM
     * @return
     */
    public int knightOnOuterEdge2(long knights){
        long outerEdge2 = 0x00003C24243C0000L;
        return Long.bitCount(knights & outerEdge2);
    }

    /**
     * Returns how many knights are on the inner inner outer edge of the board
     * @param TEAM
     * @return
     */
    public int knightOnOuterEdge1(long knights){
        long outerEdge1 = 0x0000001818000000L;
        return Long.bitCount(knights & outerEdge1);
    }

    /**
     * Returns the amount of ones bishops on the large diagonals of the board
     * @return
     */
    public int bishopOnLargeDiagonal(long bishops){
        int count = 0;
        long largeDiagonal1 = 0x8040201008040201L; // From a1 to h8
        long largeDiagonal2 = 0x0102040810204080L; // From a8 to h1

        long bishopsOnLargeDiagonal = bishops & (largeDiagonal1 | largeDiagonal2);
        count = Long.bitCount(bishopsOnLargeDiagonal);

        return count;
    }

    /**
     * Returns if this team has 2 bishops
     * @return
     */
    public int bishopPair(long bishops){
        long lightSquares = 0x55AA55AA55AA55AAL;
        long darkSquares = 0xAA55AA55AA55AA55L;

        long bishopsOnLightSquares = bishops & lightSquares;
        long bishopsOnDarkSquares = bishops & darkSquares;

        if (Long.bitCount(bishopsOnLightSquares) >= 1 && Long.bitCount(bishopsOnDarkSquares) >= 1) {
            return 1;
        }
        return 0;
    }

    /**
     * Returns how many rooks are behind a pass pawn
     * @return
     */
    public int rookBehindPassPawn(){
        int score = 0;
        //TODO:
        return score;
    }

    /**
     * Returns the number of rooks on an open file. An open file is a column with no pieces
     * @param TEAM
     * @return
     */
    public int rookOnOpenFile(long rooks, long board){
        int count = 0;
        for (int file = 0; file < 8; file++) {
            long fileMask = 0x0101010101010101L << file;
            if ((fileMask & board) == 0 && (fileMask & rooks) != 0) {
                count++;
            }
        }
        return count;
    }

    /**
     * Returns the number of rooks on a semi open file. A semi open file is a column with no friendly pawns
     * @return
     */
    public int rookOnSemiOpenFile(long rooks, long pawns){
        int count = 0;
        for (int file = 0; file < 8; file++) {
            long fileMask = 0x0101010101010101L << file;
            if ((fileMask & pawns) == 0 && (fileMask & rooks) != 0) {
                count++;
            }
        }
        return count;
    }


    /**
     * Returns the number of rooks on a closed file. A closed file is a column with both friendly and enemy pawns
     * @return
     */
    public int rookOnClosedFile(long rooks, long pawns, long enemyPawns){
        int count = 0;
        for (int file = 0; file < 8; file++) {
            long fileMask = 0x0101010101010101L << file;
            if ((fileMask & enemyPawns) != 0 && (fileMask & pawns) != 0 && (fileMask & rooks) != 0) {
                count++;
            }
        }
        return count;
    }

    /**
     * Returns the count of connected rooks. Connected rooks protect each other, and are either in the same row or column
     * @return
     */
    public int rooksConnected(long rooks, long board){
        for (int rank = 0; rank < 8; rank++) {
            long rankMask = 0xFFL << (rank * 8);
            long rooksOnRank = rooks & rankMask;
            if (Long.bitCount(rooksOnRank) > 1 && (rankMask & board) == rooksOnRank) {
                return 1;
            }
        }
        for (int file = 0; file < 8; file++) {
            long fileMask = 0x0101010101010101L << file;
            long rooksOnFile = rooks & fileMask;
            if (Long.bitCount(rooksOnFile) > 1 && (fileMask & board) == rooksOnFile) {
                return 1;
            }
        }
        return 0;
    }

    /**
     * Returns 1 if this teams king has castled. 0 Otherwise
     * @param COLOR
     * @return
     */
    public int kingCastled(){
        int score = 0;
        //TODO:
        return score;
    }

    /**
     * Returns the total value of enemy pieces attacking the adjacent squares of this teams king
     * @param COLOR
     * @return
     */
    public int kingAttackedValue(){
        int score = 0;
        //TODO:
        return score;
    }

    /**
     * Returns the total value of team pieces protecting the adjacent squares of this teams king
     * @param COLOR
     * @return
     */
    public int kingDefendedValue(){
        int score = 0;
        //TODO:
        return score;
    }

    










}
