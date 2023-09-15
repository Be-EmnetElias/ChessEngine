package main;
/**
 * * The board evaluator has 2 different evaluators. Both evaluators use the nega-max algorithm to traverse all moves to a certain depth.
 * * Each board position is then evaluated and given a score. The move that results in the highest score is then chosen. The first evaluator
 * * uses a static hand-crafted evaluator with chosen weights.
 */

import main.chessutil.*;

import java.util.*;



public class BoardEvaluator {
    
    private Board BOARD;

    // private static final Square E4 = new Square(4,4);

    // private static final Square E5 = new Square(4,3);

    // private static final Square D4 = new Square(3,4);

    // private static final Square D5 = new Square(3,3);


    // public BoardEvaluator(Board board, boolean COLOR){ 
    //     this.BOARD = board; 
    // }

    /**
     * Returns the best move out of these currentLegalMoves of this depth
     * @param currentLegalMoves
     * @param depth
     * @param COLOR
     * @return
     */
    // public Move bestMove(HashMap<Piece, HashSet<Move>> currentLegalMoves, int depth, boolean COLOR){
    //     double maxScore = Double.NEGATIVE_INFINITY;
    //     Move bestMove = null; 
    //     for(Piece piece: currentLegalMoves.keySet()){
    //         for(Move move: currentLegalMoves.get(piece)){
    //             Move currentMove = BOARD.movePiece(move, true);
    //             HashMap<Piece, HashSet<Move>> nextLegalMoves= BOARD.getCurrentLegalMoves();
    //             double score = ((!COLOR)?1.0:-1.0) * negamax(nextLegalMoves,depth-1,COLOR);
    //             BOARD.undoMove(currentMove);

    //             if(score > maxScore){
    //                 maxScore = score;
    //                 bestMove = move;
    //             }
    //         }
    //     }
    //     return bestMove;
    // }

    // private double negamax(HashMap<Piece, HashSet<Move>>currentLegalMoves,int depth, boolean COLOR){
    //     if(depth<=0) return staticEvaluation(currentLegalMoves,COLOR);

    //     double maxScore = Double.NEGATIVE_INFINITY;
    //     for(Piece piece: currentLegalMoves.keySet()){
    //         for(Move move: currentLegalMoves.get(piece)){
    //             Move currentMove = BOARD.movePiece(move, true);
    //             HashMap<Piece, HashSet<Move>> nextLegalMoves= BOARD.getCurrentLegalMoves();
    //             double score = ((!COLOR)?1.0:-1.0) * negamax(nextLegalMoves,depth-1,!COLOR);
    //             BOARD.undoMove(currentMove);

    //             if(score > maxScore){
    //                 maxScore = score;
    //             }
    //         }
    //     }

    //     return maxScore;
    // }
    

/*
* = = == == == === === WEIGHTS === === == == == = =
*/

    public double 
    
    WEAK_COUNT_WEIGHT = 1.0,

    
    MATERIAL_COUNT_PAWN_WEIGHT = 1.0, 
    
    CENTER_PAWN_COUNT_WEIGHT = 1.0,
    
    KING_PAWN_SHIELD_WEIGHT = 1.0,
    
    ISOLATED_PAWN_WEIGHT = -1.0,
    
    DOUBLE_PAWN_WEIGHT = -1.0,
    
    RANK_PASSED_PAWN_WEIGHT = 1.0,
    
    PASS_PAWN_WEIGHT = 1.0,
    
    RANK_PASS_PAWN_WEIGHT = 1.0,

    BLOCKED_PAWN_WEIGHT = -1.0,
    
    BLOCKED_PASSED_PAWN_WEIGHT = -1.0,
    
    BACKWARD_PAWN_WEIGHT = -1.0,


    
    MATERIAL_COUNT_KNIGHT_WEIGHT = 1.0,
    
    KNIGHT_MOBILITY_WEIGHT = 1.0,
    
    KNIGHT_ON_OUTPOST_WEIGHT = 1.0,
    
    KNIGHT_ON_CENTER_WEIGHT = 1.0,
    
    KNIGHT_ON_OUTER_EDGE_1_WEIGHT = 1.0,
    
    KNIGHT_ON_OUTER_EDGE_2_WEIGHT = 1.0,
    
    KNIGHT_ON_OUTER_EDGE_3_WEIGHT = 1.0,
    
    KNIGHT_SUPPORTED_BY_PAWN_WEIGHT = 1.0,
    

    MATERIAL_COUNT_BISHOP_WEIGHT = 1.0,
    
    BISHOP_MOBILITY_WEIGHT = 1.0,

    BISHOP_ON_LARGE_DIAGONAL_WEIGHT = 1.0,

    BISHOP_PAIR_WEIGHT = 1.0,

    
    MATERIAL_COUNT_ROOK_WEIGHT = 1.0,
    
    ROOK_MOBILITY_WEIGHT = 1.0,

    ROOK_BEHIND_PASS_PAWN = 1.0,

    ROOK_ON_CLOSED_FILE = 1.0,

    ROOK_ON_OPEN_FILE = 1.0,

    ROOK_ON_SEMI_OPEN_FILE = 1.0,

    ROOKS_CONNECTED_WEIGHT = 1.0,


    
    MATERIAL_COUNT_QUEEN_WEIGHT = 1.0,
    
    QUEEN_MOBILITY_WEIGHT = 1.0,

    
    KING_CASTLED_WEIGHT = 1.0,

    KING_ATTACKED_VALUE_WEIGHT = -1.0,

    KING_DEFENDED_VALUE_WEIGHT = 1.0;


/**
 * * = = == == == === === === === === == == == = =
 */



    /**
     * <p>
     * * A static evaluation function. Uses hand crafted functions multiplied by chosen weights to evaluate a board 
     * <p>
     * 
     * @param currentLegalMoves The current legal moves for the given board position
     * @param COLOR Current team's turn. True if white's turn, false if black's turn
     * @see https://www.cmpe.boun.edu.tr/~gungort/undergraduateprojects/Tuning%20of%20Chess%20Evaluation%20Function%20by%20Using%20Genetic%20Algorithms.pdf
     * @return An double evaluating the position of this board
     */
    public double staticEvaluation(/*HashMap<Piece, HashSet<Move>> currentLegalMoves, boolean COLOR*/){
        double score = 0;

        //GENERAL INFORMATION
        score += weakCount()         *WEAK_COUNT_WEIGHT;

        //PAWN INFORMATION
        score += materialCount()  *MATERIAL_COUNT_PAWN_WEIGHT;
        score += centerPawnCount()                             *CENTER_PAWN_COUNT_WEIGHT;
        score += kingPawnShield()                              *KING_PAWN_SHIELD_WEIGHT;
        score += isolatedPawn()                          *ISOLATED_PAWN_WEIGHT;
        score += doublePawn()                            *DOUBLE_PAWN_WEIGHT;
        score += passPawn()                              *PASS_PAWN_WEIGHT;
        score += rankPassPawn()                          *RANK_PASS_PAWN_WEIGHT;
        score += backwardPawn()                                 *BACKWARD_PAWN_WEIGHT;
        score += blockedPawn()                                  *BLOCKED_PAWN_WEIGHT;
        score += blockedPassPawn()                              *BLOCKED_PASSED_PAWN_WEIGHT;

        //KNIGHT INFORMATION
        score += materialCount()                    *MATERIAL_COUNT_KNIGHT_WEIGHT;
        score += mobility()            *KNIGHT_MOBILITY_WEIGHT;
        score += knightOnOutpost()                       *KNIGHT_ON_OUTPOST_WEIGHT;
        score += knightOnCenter()                               *KNIGHT_ON_CENTER_WEIGHT;
        score += knightOnOuterEdge1()                           *KNIGHT_ON_OUTER_EDGE_1_WEIGHT;
        score += knightOnOuterEdge2()                           *KNIGHT_ON_OUTER_EDGE_2_WEIGHT;
        score += knightOnOuterEdge3()                           *KNIGHT_ON_OUTER_EDGE_3_WEIGHT;
        score += knightSupportedByPawn()                        *KNIGHT_SUPPORTED_BY_PAWN_WEIGHT;

        //BISHOP INFORMATION
        score += materialCount()                    *MATERIAL_COUNT_BISHOP_WEIGHT;
        score += mobility()           *BISHOP_MOBILITY_WEIGHT;
        score += bishopOnLargeDiagonal()                        *BISHOP_ON_LARGE_DIAGONAL_WEIGHT;
        score += bishopPair()                                   *BISHOP_PAIR_WEIGHT;

        //ROOK INFORMATION
        score += materialCount()                      *MATERIAL_COUNT_ROOK_WEIGHT;
        score += mobility()              *ROOK_MOBILITY_WEIGHT;
        score += rookBehindPassPawn()                           *ROOK_BEHIND_PASS_PAWN;
        score += rookOnClosedFile()                             *ROOK_ON_CLOSED_FILE;
        score += rookOnOpenFile()                               *ROOK_ON_OPEN_FILE;
        score += rookOnSemiOpenFile()                           *ROOK_ON_SEMI_OPEN_FILE;
        score += rooksConnected()                               *ROOKS_CONNECTED_WEIGHT;

        //QUEEN INFORMATION
        score += materialCount()                     *MATERIAL_COUNT_QUEEN_WEIGHT;
        score += mobility()            *QUEEN_MOBILITY_WEIGHT;

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
    public int weakCount(){
        int score = 0;
        //TODO:
        return score;
    }

    /**
     * Returns the count of this type of piece on this team
     * @param name
     * @param TEAM
     * @return
     */
    public int materialCount(){
        int score = 0;
        //TODO:
        return score;
    }

    /**
     * Returns the amount of squares this piece can move to
     * @param currentLegalMoves
     * @param name
     * @return
     */
    public int mobility(){
        int score = 0;
        //TODO:
        return score;
        
    }

    /**
     * Returns the count of pawns on the center squares: E4 D4 E5 D5
     * @param COLOR
     * @return
     */
    public int centerPawnCount(){
        int score = 0;
        //TODO:
        return score;
    }

    /**
     * Returns tge count of pawns that are on the adjacent squares of the king
     * @param COLOR
     * @return
     */
    public int kingPawnShield(){
        int score = 0;
        //TODO:
        return score;
    }

    /**
     * Returns the count of isolated pawns. Isolated pawns have no neighboring pawns
     * @param TEAM
     * @param COLOR
     * @return
     */
    public int isolatedPawn(){
        int score = 0;
        //TODO:
        return score;
    }

    /**
     * Returns the count of doubled pawns. A pawn is doubled if it is on the same column as a friendly pawn
     * @param TEAM
     * @param COLOR
     * @return
     */
    public int doublePawn(){
        int score = 0;
        //TODO:
        return score;
    }

    /**
     * Returns the count of pass pawns. A pass pawn has no enemy pawns on its column and the columns to its left and right
     * @param TEAM
     * @param COLOR
     * @return
     */
    public int passPawn(){
        int score = 0;
        //TODO:
        return score;
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
     * @param TEAM
     * @param COLOR
     * @return Returns the amount of knights on an outpost
     */
    public int knightOnOutpost(){
        int score = 0;
        //TODO:
        return score;
    }


    /**
     * Returns how many knights are supported by ones pawns
     * @param TEAM
     * @return
     */
    public int knightSupportedByPawn(){
        int score = 0;
        //TODO:
        return score;
    }

    /**
     * Returns how many knights are on the center of the board
     * @param TEAM
     * @return
     */
    public int knightOnCenter(){
        int score = 0;
        //TODO:
        return score;
    }

    /**
     * Returns how many knights are on the outer edge of the board
     * @param TEAM
     * @return
     */
    public int knightOnOuterEdge3(){
        int score = 0;
        //TODO:
        return score;
    }

    /**
     * Returns how many knights are on the inner outer edge of the board
     * @param TEAM
     * @return
     */
    public int knightOnOuterEdge2(){
        int score = 0;
        //TODO:
        return score;
    }

    /**
     * Returns how many knights are on the inner inner outer edge of the board
     * @param TEAM
     * @return
     */
    public int knightOnOuterEdge1(){
        int score = 0;
        //TODO:
        return score;
    }

    /**
     * Returns the amount of ones bishops on the large diagonals of the board
     * @param TEAM
     * @return
     */
    public int bishopOnLargeDiagonal(){
        int score = 0;
        //TODO:
        return score;
    }

    /**
     * Returns if this team has 2 bishops
     * @param TEAM
     * @return
     */
    public int bishopPair(){
        int score = 0;
        //TODO:
        return score;
    }

    /**
     * Returns how many rooks are behind a pass pawn
     * @param TEAM
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
    public int rookOnOpenFile(){
        int score = 0;
        //TODO:
        return score;
    }

    /**
     * Returns the number of rooks on a semi open file. A semi open file is a column with no friendly pawns
     * @param TEAM
     * @return
     */
    public int rookOnSemiOpenFile(){
        int score = 0;
        //TODO:
        return score;
    }


    /**
     * Returns the number of rooks on a closed file. A closed file is a column with both friendly and enemy pawns
     * @param TEAM
     * @return
     */
    public int rookOnClosedFile(){
        int score = 0;
        //TODO:
        return score;
    }

    /**
     * Returns the count of connected rooks. Connected rooks protect each other, and are either in the same row or column
     * @param TEAM
     * @return
     */
    public int rooksConnected(){
        int score = 0;
        //TODO:
        return score;
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
