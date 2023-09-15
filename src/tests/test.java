package tests;

import java.util.*;
import main.Board;
import main.chessutil.*;

public class test {
    
    public static Board board;
    public static TreeSet<String> NODES;
    public static void main(String[] args){
        board = new Board("n1n1k3/PPP5/8/8/8/8/4Kppp/5N1N w - - 1 2");

        NODES = new TreeSet<>();

        nodeCount(2);
    }

    public static int nodeCount(int depth){ 
        return nodeCount(depth,depth);
    }
    
    public static int nodeCount(int start, int depth){
        if(depth==0) return 1;

        int nodes = 0;
        List<Move> moves = board.getCurrentLegalMoves();
        

        for(Move move: moves){
            //System.out.println();
            // for(int i = depth; i<start; i++){
            //     System.out.print("\t");
            // }
            board.movePiece(move, true);
            if(move.getStr().equals("b7c8q")){
                board.printBoard();
            }
            int newNodes = nodeCount(start,depth-1);
            if(depth==start){
                System.out.println(move.getStr() + ":" + newNodes);
                NODES.add(move.getStr() + ":" + newNodes);
            }
            nodes += newNodes;
            
            board.undoMove(move,true);
        }
        
        if(depth==start){
            // for(String move: NODES){
            //     System.out.println(move);
            // }
            System.out.println("NODES: " + nodes);
        }
        return nodes;
    }
}
