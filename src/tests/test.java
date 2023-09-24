package tests;

import java.util.*;
import main.Board;
import main.chessutil.*;

public class test {
    
    public static Board board;
    public static TreeSet<String> NODES;

    public static void main(String[] args){
        board = new Board();
        //board = new Board();
        NODES = new TreeSet<>();

        nodeCount(6);
    }

    public static long nodeCount(int depth){ 
        return nodeCount(depth,depth);
    }
    
    public static long nodeCount(int start, int depth){
        if(depth==0) return 1;

        int nodes = 0;
        List<Move> moves = board.getCurrentLegalMoves(board.IS_WHITE_TURN);
        

        for(Move move: moves){

            board.movePiece(move, true);

            long newNodes = nodeCount(start,depth-1);
            if(depth==start){
                System.out.println(move.getStr() + ":" + newNodes);
                NODES.add(move.getStr() + ":" + newNodes);
            }
            nodes += newNodes;
            
            board.undoMove(move,true);
        }
        
        if(depth==start){

            System.out.println("NODES: " + nodes);
        }
        return nodes;
    }
}
