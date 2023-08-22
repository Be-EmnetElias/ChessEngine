package tests;

import java.util.*;
import main.java.Board;
import main.java.util.*;

public class test {
    
    public static Board board;
    public static TreeSet<String> NODES;
    public static void main(String[] args){
        board = new Board();
        
        board.setBoard("r3k2r/p1ppqpb1/bn2pQp1/3PN3/1p2P3/2N5/PPPBBPpP/R3K2R w KQkq - 0 2");

        NODES = new TreeSet<>();

        nodeCount(2);
        
    }

    public static int nodeCount(int depth){ 
        return nodeCount(depth,depth);
    }
    
    public static int nodeCount(int start, int depth){
        // 20,400,8902,197281,4865609,119060324,3195901860,84998978956,2439530234167,69352859712417
        if(depth==0) return 1;

        int nodes = 0;
        HashMap<Piece, HashSet<Move>> moves = board.getCurrentLegalMoves();
        

        for(Piece piece: moves.keySet()){
            HashSet<Move> pieceMoves = moves.get(piece);
            for(Move move: pieceMoves){
                Move currentMove = board.movePiece(move);
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
