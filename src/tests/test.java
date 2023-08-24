package tests;

import java.util.*;
import main.java.Board;
import main.java.util.*;

public class test {
    
    public static Board board;
    public static TreeSet<String> NODES;
    public static void main(String[] args){
        board = new Board();
        
        board.setBoard();

        NODES = new TreeSet<>();

        /*
         *
         */
        nodeCount(3);
        
    }

    public static int nodeCount(int depth){ 
        return nodeCount(depth,depth);
    }
    
    public static int nodeCount(int start, int depth){
        if(depth==0) return 1;

        int nodes = 0;
        HashMap<Piece, HashSet<Move>> moves = board.getCurrentLegalMoves();
        

        for(Piece piece: moves.keySet()){
            HashSet<Move> pieceMoves = moves.get(piece);
            for(Move move: pieceMoves){
                Move currentMove = board.movePiece(move, true);
                int newNodes = nodeCount(start,depth-1);
                if(depth==start){
                    NODES.add(move.getStr() + ":" + newNodes);
                }
                nodes += newNodes;
                
                board.undoMove(currentMove);
            }
        }
        if(depth==start){
            for(String move: NODES){
                System.out.println(move);
            }
            System.out.println("NODES: " + nodes);
        }
        return nodes;
    }
}
