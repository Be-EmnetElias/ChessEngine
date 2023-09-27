package main;

import main.chessutil.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.*;

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
    private EnumMap<Piece,Image> pieceImages = new EnumMap<>(Piece.class);

    private Piece clickedPiece = null;
    private int clickedCol = -1;
    private int clickedRow = -1;
    private ArrayList<Move> currentLegalMoves = new ArrayList<>();
    private HashSet<Move> moveHints = new HashSet<>();
    private int[] moveHistory = new int[]{-1,-1};

    private final String BG_PATH = "src/main/assets/imgs/bg_brown.png";
    private final String PIECES_PATH = "src/main/assets/imgs/pieces.png";

    private Board board = new Board();

    private JButton flipBoardButton = new JButton("Flip");

    private int dragHighlightX = 0;
    private int dragHighlightY = 0;
    
    private int dragx = 0;
    private int dragy = 0;

    private int depth = 4;

    public ChessPanel() throws IOException {

        boardImage = ImageIO.read(new File(BG_PATH));
        allPieces = ImageIO.read(new File(PIECES_PATH));
        boardImage = boardImage.getScaledInstance(800, 800, BufferedImage.SCALE_SMOOTH);

        // Slice the allPieces image into individual piece images
        Piece[] pieceLetters = new Piece[]{
            Piece.WHITE_KING, Piece.WHITE_QUEEN, Piece.WHITE_BISHOP, Piece.WHITE_KNIGHT, Piece.WHITE_ROOK, Piece.WHITE_PAWN,
            Piece.BLACK_KING, Piece.BLACK_QUEEN, Piece.BLACK_BISHOP, Piece.BLACK_KNIGHT, Piece.BLACK_ROOK, Piece.BLACK_PAWN
        };
        int ind = 0;
        for (int y = 0; y < 400; y += 200) {
            for (int x = 0; x < 1200; x += 200) {
                pieceImages.put(pieceLetters[ind],allPieces.getSubimage(x, y, 200, 200).getScaledInstance(100, 100, BufferedImage.SCALE_SMOOTH));
                ind++;
                
            }
        }

        flipBoardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        });


        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Handle mouse pressed logic
                int square = (e.getY()/100 * 8) + (e.getX()/100);
                if(board.getPieceAtSquare(square) != Piece.EMPTY){
                    for(Move move: currentLegalMoves){
                        if(move.fromSquare == square){
                            moveHints.add(move);
                        }
                    }
                }
                clickedPiece = board.getPieceAtSquare(square);
                clickedCol = e.getX()/100;
                clickedRow = e.getY()/100;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                boolean validMove = false;
                // Handle mouse release logic
                if(clickedPiece != null){
                    
                    Move userMove = board.validateUserMove(clickedRow * 8 + clickedCol, e.getY()/100 * 8 + e.getX()/100, clickedPiece);
                    currentLegalMoves.clear();
                    currentLegalMoves.addAll(board.getCurrentLegalMoves(board.IS_WHITE_TURN));
                    if(currentLegalMoves.isEmpty()){
                        System.out.println("CHECKMATE. YOU LOST");
                    }

                    if(userMove != null){ //MOVE PIECE
                        //System.out.println("USER MOVE: " + userMove);
                        board.movePiece(userMove,false);
                        moveHistory[0] = userMove.fromSquare;
                        moveHistory[1] = userMove.toSquare;
                        validMove = true;

                    }else{ //RESET PIECE
                        dragx = clickedCol * 100;
                        dragy = clickedRow * 100;
                    }
                    moveHints.clear();
                    clickedPiece = null;
                }
                repaint();
                if(validMove){
                    ArrayList<Move> moves = new ArrayList<>(board.getCurrentLegalMoves(board.IS_WHITE_TURN));
                    Move bestMove = board.HIVE.bestMove(moves,depth,board.IS_WHITE_TURN);
                    if(bestMove != null){
                        board.movePiece(bestMove,false);
                        moveHistory[0] = bestMove.fromSquare;
                        moveHistory[1] = bestMove.toSquare;
                    }
                    else System.out.println("CHECKMATE. YOU WON");
                }
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

            @Override
            public void mouseMoved(MouseEvent e){
                dragHighlightX = e.getX()/100 * 100;
                dragHighlightY = e.getY()/100 * 100;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the board
        g.drawImage(boardImage, 0, 0, 800, 800, null);

        if(moveHistory[0] != -1){
            g.setColor(new Color(155, 252, 114,100));
            int prevColHis = moveHistory[0] % 8;
            int prevRowHis = moveHistory[0] / 8;

            int currColHis = moveHistory[1] % 8;
            int currRowHis = moveHistory[1] / 8;
            g.fillRect(prevColHis * 100, prevRowHis * 100, 100,100);
            g.fillRect(currColHis * 100, currRowHis * 100, 100,100);

        }

        //dragged highlights

        g.setColor(new Color(255,255,255,100));
        g.fillRect(dragHighlightX,dragHighlightY,100,100);

        // Draw move hints
        if(moveHints != null){
            for(Move move: moveHints){
                int moveHintX = (move.toSquare % 8) * 100;
                int moveHintY = (move.toSquare / 8) * 100;

                g.setColor(new Color(250,175,2)); // yellow 
                //g.setColor(new Color(253,64,2)); // red
                //g.setColor(new Color(153, 153, 153,100));
                

                g.fillRect(moveHintX,moveHintY, 100,100);

            }
        }

        long[] currentPieces = board.getBoard();
        //draw all pieces
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                int square = row * 8 + col;
                Piece current = board.getPieceAtSquare(square,currentPieces);
                g.setColor(Color.RED);
                g.drawString((row*8 + col) + "", col*100,row*100 + 100);
                if (current != Piece.EMPTY) {
                    // If this is the piece being dragged, use dragx and dragy.
                    // Else, use the standard coordinates.
                    int drawX = (col == clickedCol && row == clickedRow) ? dragx : col * 100;
                    int drawY = (col == clickedCol && row == clickedRow) ? dragy : row * 100;
                    
                    g.drawImage(pieceImages.get(current), drawX, drawY, null);
                }
            }
        }
        //draw clickedpiece on top
        if(null != clickedPiece){
            g.drawImage(pieceImages.get(clickedPiece),dragx,dragy,null);
        }
    
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

