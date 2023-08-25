/**
 * * The board evaluator has 2 different evaluators. Both evaluators use the nega-max algorithm to traverse all moves to a certain depth.
 * * Each board position is then evaluated and given a score. The move that results in the highest score is then chosen. The first evaluator
 * * uses a static hand-crafted evaluator with chosen weights.
 */
package main.java;
import main.java.util.*;
import java.util.*;

public class BoardEvaluator {
    
    private Board BOARD;

    private static final Square E4 = new Square(4,4);

    private static final Square E5 = new Square(4,3);

    private static final Square D4 = new Square(3,4);

    private static final Square D5 = new Square(3,3);


    public BoardEvaluator(Board board, boolean COLOR){ this.BOARD = board; }

    /**
     * Returns the best move out of these currentLegalMoves of this depth
     * @param currentLegalMoves
     * @param depth
     * @param COLOR
     * @return
     */
    public Move bestMove(HashMap<Piece, HashSet<Move>> currentLegalMoves, int depth, boolean COLOR){
        double maxScore = Double.NEGATIVE_INFINITY;
        Move bestMove = null; 
        for(Piece piece: currentLegalMoves.keySet()){
            for(Move move: currentLegalMoves.get(piece)){
                Move currentMove = BOARD.movePiece(move, true);
                HashMap<Piece, HashSet<Move>> nextLegalMoves= BOARD.getCurrentLegalMoves();
                double score = ((!COLOR)?1.0:-1.0) * negamax(nextLegalMoves,depth-1,COLOR);
                BOARD.undoMove(currentMove);

                if(score > maxScore){
                    maxScore = score;
                    bestMove = move;
                }
            }
        }
        return bestMove;
    }

    private double negamax(HashMap<Piece, HashSet<Move>>currentLegalMoves,int depth, boolean COLOR){
        if(depth<=0) return staticEvaluation(currentLegalMoves,COLOR);

        double maxScore = Double.NEGATIVE_INFINITY;
        for(Piece piece: currentLegalMoves.keySet()){
            for(Move move: currentLegalMoves.get(piece)){
                Move currentMove = BOARD.movePiece(move, true);
                HashMap<Piece, HashSet<Move>> nextLegalMoves= BOARD.getCurrentLegalMoves();
                double score = ((!COLOR)?1.0:-1.0) * negamax(nextLegalMoves,depth-1,!COLOR);
                BOARD.undoMove(currentMove);

                if(score > maxScore){
                    maxScore = score;
                }
            }
        }

        return maxScore;
    }
    

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
    
    PASS_PAWN_WEIGHT = 1000.0,
    
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
    public double staticEvaluation(HashMap<Piece, HashSet<Move>> currentLegalMoves, boolean COLOR){
        double score = 0;
        HashSet<Piece> TEAM = BOARD.getPieces(COLOR);

        //GENERAL INFORMATION
        score += weakCount(currentLegalMoves, TEAM, !COLOR)         *WEAK_COUNT_WEIGHT;

        //PAWN INFORMATION
        score += materialCount(COLOR ? Name.PAWNW:Name.PAWNB,TEAM)  *MATERIAL_COUNT_PAWN_WEIGHT;
        score += centerPawnCount(COLOR)                             *CENTER_PAWN_COUNT_WEIGHT;
        score += kingPawnShield(COLOR)                              *KING_PAWN_SHIELD_WEIGHT;
        score += isolatedPawn(TEAM, COLOR)                          *ISOLATED_PAWN_WEIGHT;
        score += doublePawn(TEAM, COLOR)                            *DOUBLE_PAWN_WEIGHT;
        score += passPawn(TEAM, COLOR)                              *PASS_PAWN_WEIGHT;
        score += rankPassPawn(TEAM, COLOR)                          *RANK_PASS_PAWN_WEIGHT;
        score += backwardPawn(TEAM)                                 *BACKWARD_PAWN_WEIGHT;
        score += blockedPawn(TEAM)                                  *BLOCKED_PAWN_WEIGHT;
        score += blockedPassPawn(TEAM)                              *BLOCKED_PASSED_PAWN_WEIGHT;

        //KNIGHT INFORMATION
        score += materialCount(Name.KNIGHT,TEAM)                    *MATERIAL_COUNT_KNIGHT_WEIGHT;
        score += mobility(currentLegalMoves,Name.KNIGHT)            *KNIGHT_MOBILITY_WEIGHT;
        score += knightOnOutpost(TEAM, COLOR)                       *KNIGHT_ON_OUTPOST_WEIGHT;
        score += knightOnCenter(TEAM)                               *KNIGHT_ON_CENTER_WEIGHT;
        score += knightOnOuterEdge1(TEAM)                           *KNIGHT_ON_OUTER_EDGE_1_WEIGHT;
        score += knightOnOuterEdge2(TEAM)                           *KNIGHT_ON_OUTER_EDGE_2_WEIGHT;
        score += knightOnOuterEdge3(TEAM)                           *KNIGHT_ON_OUTER_EDGE_3_WEIGHT;
        score += knightSupportedByPawn(TEAM)                        *KNIGHT_SUPPORTED_BY_PAWN_WEIGHT;

        //BISHOP INFORMATION
        score += materialCount(Name.BISHOP,TEAM)                    *MATERIAL_COUNT_BISHOP_WEIGHT;
        score += mobility(currentLegalMoves, Name.BISHOP)           *BISHOP_MOBILITY_WEIGHT;
        score += bishopOnLargeDiagonal(TEAM)                        *BISHOP_ON_LARGE_DIAGONAL_WEIGHT;
        score += bishopPair(TEAM)                                   *BISHOP_PAIR_WEIGHT;

        //ROOK INFORMATION
        score += materialCount(Name.ROOK,TEAM)                      *MATERIAL_COUNT_ROOK_WEIGHT;
        score += mobility(currentLegalMoves,Name.ROOK)              *ROOK_MOBILITY_WEIGHT;
        score += rookBehindPassPawn(TEAM)                           *ROOK_BEHIND_PASS_PAWN;
        score += rookOnClosedFile(TEAM)                             *ROOK_ON_CLOSED_FILE;
        score += rookOnOpenFile(TEAM)                               *ROOK_ON_OPEN_FILE;
        score += rookOnSemiOpenFile(TEAM)                           *ROOK_ON_SEMI_OPEN_FILE;
        score += rooksConnected(TEAM)                               *ROOKS_CONNECTED_WEIGHT;

        //QUEEN INFORMATION
        score += materialCount(Name.QUEEN,TEAM)                     *MATERIAL_COUNT_QUEEN_WEIGHT;
        score += mobility(currentLegalMoves, Name.QUEEN)            *QUEEN_MOBILITY_WEIGHT;

        //KING INFORMATION
        score += kingCastled(COLOR)                                 *KING_CASTLED_WEIGHT;
        score += kingAttackedValue(COLOR)                           *KING_ATTACKED_VALUE_WEIGHT;
        score += kingDefendedValue(COLOR)                           *KING_DEFENDED_VALUE_WEIGHT;

        return score;
    }


    /**
     * Returns the number of squares that cannot be protected by this team
     * @return
     */
    public int weakCount(HashMap<Piece, HashSet<Move>> currentLegalMoves, HashSet<Piece> TEAM, boolean enemyColor){
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
    public int materialCount(Name name,HashSet<Piece> TEAM){
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
    public int mobility(HashMap<Piece, HashSet<Move>> currentLegalMoves, Name name){
        int score = 0;
        //TODO:
        return score;
        
    }

    /**
     * Returns the count of pawns on the center squares: E4 D4 E5 D5
     * @param COLOR
     * @return
     */
    public int centerPawnCount(boolean COLOR){
        int score = 0;
        //TODO:
        return score;
    }

    /**
     * Returns tge count of pawns that are on the adjacent squares of the king
     * @param COLOR
     * @return
     */
    public int kingPawnShield(boolean COLOR){
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
    public int isolatedPawn(HashSet<Piece> TEAM, boolean COLOR){
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
    public int doublePawn(HashSet<Piece> TEAM, boolean COLOR){
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
    public int passPawn(HashSet<Piece> TEAM, boolean COLOR){
        int score = 0;
        //TODO:
        return score;
    }

    /**
     * Returns the total ranks of all pass pawns. Measured by how far up a pawn has moved, not its current rank
     * @param TEAM
     * @return
     */
    public int rankPassPawn(HashSet<Piece> TEAM, boolean COLOR){
        int score = 0;
        //TODO:
        return score;
    }
    /**
     * Returns the number of backward pawns. A pawn is backwards if its neighboring pawns are ahead of it
     * @param TEAM
     * @return
     */
    public int backwardPawn(HashSet<Piece> TEAM){
        int score = 0;
        //TODO:
        return score;
    }


    /**
     * Returns the number of central pawns that are blocked by a team piece
     * @param TEAM
     * @return
     */
    public int blockedPawn(HashSet<Piece> TEAM){
        int score = 0;
        //TODO:
        return score;
    }

    /**
     * Returns the number of pass pawns blocked by an enemy piece
     * @param TEAM
     * @return
     */
    public int blockedPassPawn(HashSet<Piece> TEAM){
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
    public int knightOnOutpost(HashSet<Piece> TEAM, boolean COLOR){
        int score = 0;
        //TODO:
        return score;
    }


    /**
     * Returns how many knights are supported by ones pawns
     * @param TEAM
     * @return
     */
    public int knightSupportedByPawn(HashSet<Piece> TEAM){
        int score = 0;
        //TODO:
        return score;
    }

    /**
     * Returns how many knights are on the center of the board
     * @param TEAM
     * @return
     */
    public int knightOnCenter(HashSet<Piece> TEAM){
        int score = 0;
        //TODO:
        return score;
    }

    /**
     * Returns how many knights are on the outer edge of the board
     * @param TEAM
     * @return
     */
    public int knightOnOuterEdge3(HashSet<Piece> TEAM){
        int score = 0;
        //TODO:
        return score;
    }

    /**
     * Returns how many knights are on the inner outer edge of the board
     * @param TEAM
     * @return
     */
    public int knightOnOuterEdge2(HashSet<Piece> TEAM){
        int score = 0;
        //TODO:
        return score;
    }

    /**
     * Returns how many knights are on the inner inner outer edge of the board
     * @param TEAM
     * @return
     */
    public int knightOnOuterEdge1(HashSet<Piece> TEAM){
        int score = 0;
        //TODO:
        return score;
    }

    /**
     * Returns the amount of ones bishops on the large diagonals of the board
     * @param TEAM
     * @return
     */
    public int bishopOnLargeDiagonal(HashSet<Piece> TEAM){
        int score = 0;
        //TODO:
        return score;
    }

    /**
     * Returns if this team has 2 bishops
     * @param TEAM
     * @return
     */
    public int bishopPair(HashSet<Piece> TEAM){
        int score = 0;
        //TODO:
        return score;
    }

    /**
     * Returns how many rooks are behind a pass pawn
     * @param TEAM
     * @return
     */
    public int rookBehindPassPawn(HashSet<Piece> TEAM){
        int score = 0;
        //TODO:
        return score;
    }

    /**
     * Returns the number of rooks on an open file. An open file is a column with no pieces
     * @param TEAM
     * @return
     */
    public int rookOnOpenFile(HashSet<Piece> TEAM){
        int score = 0;
        //TODO:
        return score;
    }

    /**
     * Returns the number of rooks on a semi open file. A semi open file is a column with no friendly pawns
     * @param TEAM
     * @return
     */
    public int rookOnSemiOpenFile(HashSet<Piece> TEAM){
        int score = 0;
        //TODO:
        return score;
    }


    /**
     * Returns the number of rooks on a closed file. A closed file is a column with both friendly and enemy pawns
     * @param TEAM
     * @return
     */
    public int rookOnClosedFile(HashSet<Piece> TEAM){
        int score = 0;
        //TODO:
        return score;
    }

    /**
     * Returns the count of connected rooks. Connected rooks protect each other, and are either in the same row or column
     * @param TEAM
     * @return
     */
    public int rooksConnected(HashSet<Piece> TEAM){
        int score = 0;
        //TODO:
        return score;
    }

    /**
     * Returns 1 if this teams king has castled. 0 Otherwise
     * @param COLOR
     * @return
     */
    public int kingCastled(boolean COLOR){
        return (BOARD.getKing(COLOR).kingCastled) ? 1:0;
    }

    /**
     * Returns the total value of enemy pieces attacking the adjacent squares of this teams king
     * @param COLOR
     * @return
     */
    public int kingAttackedValue(boolean COLOR){
        int score = 0;
        //TODO:
        return score;
    }

    /**
     * Returns the total value of team pieces protecting the adjacent squares of this teams king
     * @param COLOR
     * @return
     */
    public int kingDefendedValue(boolean COLOR){
        int score = 0;
        //TODO:
        return score;
    }

    










}
