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
    
    ISOLATED_PAWN_WEIGHT = 1.0,
    
    DOUBLE_PAWN_WEIGHT = 1.0,
    
    RANK_PASSED_PAWN_WEIGHT = 1.0,
    
    BLOCKED_PAWN_WEIGHT = 1.0,
    
    BLOCKED_PASSED_PAWN_WEIGHT = 1.0,
    
    BACKWARD_PAWN_WEIGHT = 1.0,


    
    MATERIAL_COUNT_KNIGHT_WEIGHT = 1.0,
    
    KNIGHT_MOBILITY_WEIGHT = 1.0,
    
    KNIGHT_ON_OUTPOST_WEIGHT = 1.0,
    


    MATERIAL_COUNT_BISHOP_WEIGHT = 1.0,
    
    BISHOP_MOBILITY_WEIGHT = 1.0,



    
    MATERIAL_COUNT_ROOK_WEIGHT = 1.0,
    
    ROOK_MOBILITY_WEIGHT = 1.0,



    
    MATERIAL_COUNT_QUEEN_WEIGHT = 1.0,
    
    QUEEN_MOBILITY_WEIGHT = 1.0,



    
    KING_CASTLED_WEIGHT = 1.0;

/**
 * * = = == == == === === === === === == == == = =
 */



    /**
     * <p>
     * * A static evaluation function. Uses defined functions to evaluate a board 
     * <p>
     * 
     * <p>
     * * Each function is multiplied by a certain weight: 
     * <p>
     * 
     * @param currentLegalMoves The current legal moves for the given board position
     * @param COLOR Current team's turn. True if white's turn, false if black's turn
     * @see https://www.cmpe.boun.edu.tr/~gungort/undergraduateprojects/Tuning%20of%20Chess%20Evaluation%20Function%20by%20Using%20Genetic%20Algorithms.pdf
     * @return An integer rating the position of this board
     */
    public double staticEvaluation(HashMap<Piece, HashSet<Move>> currentLegalMoves, boolean COLOR){
        double score = 0;
        HashSet<Piece> TEAM = BOARD.getPieces(COLOR);

        score += weakCount(currentLegalMoves, TEAM, !COLOR)         *   WEAK_COUNT_WEIGHT;

        score += materialCount(COLOR ? Name.PAWNW:Name.PAWNB,TEAM)  *   MATERIAL_COUNT_PAWN_WEIGHT;
        score += centerPawnCount(COLOR)                             *   CENTER_PAWN_COUNT_WEIGHT;
        score += kingPawnShield(COLOR)                              *   KING_PAWN_SHIELD_WEIGHT;
        score += isolatedPawn(TEAM, COLOR)                          *   ISOLATED_PAWN_WEIGHT;
        score += doublePawn(TEAM, COLOR)                            *   DOUBLE_PAWN_WEIGHT;

        score += materialCount(Name.KNIGHT,TEAM)                    *   MATERIAL_COUNT_KNIGHT_WEIGHT;
        score += mobility(currentLegalMoves,Name.KNIGHT)            *   KNIGHT_MOBILITY_WEIGHT;
        score += knightOnOutpost(TEAM, COLOR)                              *   KNIGHT_ON_OUTPOST_WEIGHT;

        score += materialCount(Name.BISHOP,TEAM)                    *   MATERIAL_COUNT_BISHOP_WEIGHT;
        score += mobility(currentLegalMoves, Name.BISHOP)           *   BISHOP_MOBILITY_WEIGHT;

        score += materialCount(Name.ROOK,TEAM)                      *   MATERIAL_COUNT_ROOK_WEIGHT;
        score += mobility(currentLegalMoves,Name.ROOK)              *   ROOK_MOBILITY_WEIGHT;

        score += materialCount(Name.QUEEN,TEAM)                     *   MATERIAL_COUNT_QUEEN_WEIGHT;
        score += mobility(currentLegalMoves, Name.QUEEN)            *   QUEEN_MOBILITY_WEIGHT;

        score += kingCastled(COLOR)                                 *   KING_CASTLED_WEIGHT;

        return score;
    }


    /**
     * Returns the number of squares that cannot be protected by this team
     * @return
     */
    public int weakCount(HashMap<Piece, HashSet<Move>> currentLegalMoves, HashSet<Piece> TEAM, boolean enemyColor){
        HashSet<Square> moves = new HashSet<>();
        for(Piece piece: currentLegalMoves.keySet()){
            for(Move move: currentLegalMoves.get(piece)){
                moves.add(move.targetPosition);
            }
        }
        return 64 - TEAM.size() - moves.size() - BOARD.getPieces(enemyColor).size();
    }

    public int materialCount(Name name,HashSet<Piece> TEAM){
        int score = 0;
        for(Piece piece: TEAM){
            if(piece.name == name) score++;
        }
        return score;
    }

    public int mobility(HashMap<Piece, HashSet<Move>> currentLegalMoves, Name name){
        int score = 0;

        for(Piece p:currentLegalMoves.keySet()){
            if(p.name == name){
                score += currentLegalMoves.get(p).size();
            }
        }
        return score;
        
    }

    public int centerPawnCount(boolean COLOR){
        int score = 0;

        score += (BOARD.getPiece(E4) != null && BOARD.getPiece(E4).name == (COLOR ? Name.PAWNW:Name.PAWNB) ? 1:0);
        score += (BOARD.getPiece(E5) != null && BOARD.getPiece(E5).name == (COLOR ? Name.PAWNW:Name.PAWNB) ? 1:0);
        score += (BOARD.getPiece(D4) != null && BOARD.getPiece(D4).name == (COLOR ? Name.PAWNW:Name.PAWNB) ? 1:0);
        score += (BOARD.getPiece(D5) != null && BOARD.getPiece(D5).name == (COLOR ? Name.PAWNW:Name.PAWNB) ? 1:0);

        return score;
    }

    public int kingPawnShield(boolean COLOR){
        Piece king = BOARD.getKing(COLOR);
        int score = 0;

        for(Square displacement: BOARD.PSEUDO_LEGAL_MOVEMENT.get(Name.QUEEN)){
            Square current = king.getPosition().displace(displacement);
            if(!BOARD.validPosition(current)) continue;
            score += (BOARD.getPiece(current) != null && BOARD.getPiece(current).name == (COLOR ? Name.PAWNW:Name.PAWNB) ? 1:0);
        }
        return score;
    }

    public int isolatedPawn(HashSet<Piece> TEAM, boolean COLOR){
        int score = 0;

        for(Piece piece: TEAM){
            if(piece.name == (COLOR ? Name.PAWNW:Name.PAWNB)){
                for(Square displacement: BOARD.PSEUDO_LEGAL_MOVEMENT.get(Name.KING)){
                    Square current = piece.getPosition().displace(displacement);
                    if(!BOARD.validPosition(current)) continue;
                    score += (BOARD.getPiece(current) != null && BOARD.getPiece(current).name == (COLOR ? Name.PAWNW:Name.PAWNB) ? 0:1);
                }
            }
        }
        return score;
    }

    public int doublePawn(HashSet<Piece> TEAM, boolean COLOR){
        int score = 0;

        for(Piece piece: TEAM){
            if(piece.name == (COLOR ? Name.PAWNW:Name.PAWNB)){
                
                Square displacement = COLOR ? new Square(0,-1): new Square(1,0);
                Square current = piece.getPosition().displace(displacement);
                while(BOARD.validPosition(current)){
                    if(BOARD.getPiece(current) != null && BOARD.getPiece(current).name == (COLOR ? Name.PAWNW:Name.PAWNB)){
                        score++;
                        break;
                    }
                    current = current.displace(displacement);
                }
                
            }
        }
        return score;
    }

    //TODO:
    public int passPawn(HashSet<Piece> TEAM, boolean COLOR){
        int score = 0;

        for(Piece piece: TEAM){
            if(piece.name == (COLOR ? Name.PAWNW:Name.PAWNB)){
                
                Square displacement = COLOR ? new Square(0,-1): new Square(1,0);
                Square current = piece.getPosition().displace(displacement);
                Square left = current.displace(new Square(-1,0));
                Square right = current.displace(new Square(1,0));

                boolean leftColumnClear = false;
                boolean rightColumnClear = false;
                
            }
        }
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
        for(Piece piece: TEAM){
            if(piece.name == Name.KING){
                Square upLeft = new Square(-1,(COLOR ? -1:1));
                Square upRight = new Square(1,(COLOR ? -1:1));
                score += getPiece(upLeft)

            }
        }
        return score;
    }


    public int kingCastled(boolean COLOR){
        return (BOARD.getKing(COLOR).kingCastled) ? 1:0;
    }

    










}
