package tests;

import java.util.HashMap;
import java.util.HashSet;

import main.java.Board;
import main.java.util.Move;
import main.java.util.Piece;

public class test {
    
    public static Board board;
    
    public static void main(String[] args){
        board = new Board();
        board.setBoard("8/2p5/3p4/KP5r/1R2Pp1k/8/6P1/8 b - e3 0 1");

        //board.printCurrentMoves();
        nodeCount(3);
        
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
                    System.out.println(move.getStr() + ":" + newNodes);
                }
                nodes += newNodes;
                
                board.undoMove(currentMove);
            }
        }
        if(depth==start){
            System.out.println("NODES: " + nodes);
        }
        return nodes;
    }
}
