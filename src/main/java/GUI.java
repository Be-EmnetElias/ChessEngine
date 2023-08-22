package main.java;
import main.java.util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class GUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Chess");
            frame.setSize(900, 900);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false); 

            ChessPanel chessPanel;
            try {
                chessPanel = new ChessPanel();
                frame.add(chessPanel);
            } catch (IOException e) {
                e.printStackTrace();
            }
            frame.setVisible(true);
        });
    }
}

class ChessPanel extends JPanel {

    private Image boardImage;
    private BufferedImage allPieces;
    private HashMap<Character,Image> pieceImages = new HashMap<>();

    private Piece clickedPiece = null;
    private HashSet<Move> moveHints = null;

    private static final String BG_PATH = "src/main/resources/assets/imgs/bg_brown.png";
    private static final String PIECES_PATH = "src/main/resources/assets/imgs/pieces.png";

    private static final Board board = new Board();
    
    private int dragx = 0;
    private int dragy = 0;

    public ChessPanel() throws IOException {

        boardImage = ImageIO.read(new File(BG_PATH));
        allPieces = ImageIO.read(new File(PIECES_PATH));
        boardImage = boardImage.getScaledInstance(800, 800, BufferedImage.SCALE_SMOOTH);
        
        board.setBoard("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");

        // Slice the allPieces image into individual piece images
        char[] pieceLetters = new char[]{'K','Q','B','N','R','P','k','q','b','n','r','p'};
        int ind = 0;
        for (int y = 0; y < 400; y += 200) {
            for (int x = 0; x < 1200; x += 200) {
                pieceImages.put(pieceLetters[ind],allPieces.getSubimage(x, y, 200, 200).getScaledInstance(100, 100, BufferedImage.SCALE_SMOOTH));
                ind++;
                
            }
        }


        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Handle mouse pressed logic
                Square pressedSquare = new Square(e.getX()/100,e.getY()/100);
                clickedPiece = board.getPiece(pressedSquare);
                moveHints = board.legalMoves(clickedPiece, true);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // Handle mouse release logic
                if(clickedPiece != null){
                    Move userMove = board.userValidMove(clickedPiece, new Square(e.getX()/100, e.getY()/100));
                    if(userMove != null){
                        if(userMove.promotionName != null && userMove.promotionName != Name.KNIGHT) {
                            Move userMove2 = userMove;
                            userMove2.promotionName = Name.KNIGHT;
                            board.movePiece(userMove2);

                        }else{
                            board.movePiece(userMove);
                        }
                    }
                    moveHints = null;
                    clickedPiece = null;
                }
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(clickedPiece != null){
                    dragx = e.getX()-50;
                    dragy = e.getY()-50;
                }
                repaint();

            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the board
        g.drawImage(boardImage, 0, 0, 800, 800, null);

        // Draw move hints
        if(moveHints != null){
            for(Move move: moveHints){
                Square hint = move.targetPosition;
                g.setColor(Color.RED);
                g.fillRect(hint.col*100,hint.row*100, 100,100);
            }
        }
        // Draw the pieces
        /* 
        //int tileSize = getWidth() / 8;
        // for (int i = 0; i < 12; i++) {
        //     int row = i / 6;
        //     int col = i % 6;
        //     if (pieceImages[i] != dragImage) {
        //         g.drawImage(pieceImages[i], col * tileSize, row * tileSize, tileSize, tileSize, null);
        //     }
        // }

        // // Draw the dragged piece
        // if (dragImage != null && dragPoint != null) {
        //     g.drawImage(dragImage, dragPoint.x - tileSize / 2, dragPoint.y - tileSize / 2, tileSize, tileSize, null);
        // }
        */
        for(int row=0;row<8;row++){
            for(int col=0;col<8;col++){
                Piece current = board.getPiece(new Square(col,row));
                if(current != null && current != clickedPiece){
                    g.drawImage(pieceImages.get(current.getLetter().charAt(0)),col*100,row*100,null);
                }
            }
        }
        if(clickedPiece != null) g.drawImage(pieceImages.get(clickedPiece.getLetter().charAt(0)), dragx,dragy,null);
    
    }

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("Chess Board");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ChessPanel panel = new ChessPanel();
        frame.add(panel);
        frame.setSize(800, 800);
        frame.setVisible(true);
    }
}

