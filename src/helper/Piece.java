package helper;

public class Piece {
    
    public Name name;
    public boolean isWhite;
    public int col;
    public int row;
    public int imgIndex;
    public boolean firstMove;
    public boolean enPassant;
    public boolean canSlide;

    
    public Piece(Name name, boolean isWhite, int col, int row, int imgIndex) {
        this.name = name;
        this.isWhite = isWhite;
        this.col = col;
        this.row = row;
        this.imgIndex = imgIndex;

        this.firstMove = true;
        this.enPassant = false;
        this.canSlide = !(name == Name.KING || name == Name.PAWNW || name == Name.PAWNB || name == Name.KNIGHT);
    }

    public int[] getPos() {
        return new int[]{this.col,this.row};
    }

    /**
     * Used for printing the board in ASCII format
     * 
     * @return Returns the letter representation of this piece. 
     */
    
    public String getLetter() {
        if (this.isWhite) {
            if (this.name == Name.KNIGHT) {
                return "N";
            }
            return this.name.toString().substring(0, 1);
        } else {
            if (this.name == Name.KNIGHT) {
                return "n";
            }
            return this.name.toString().substring(0, 1).toLowerCase();
        }
    }
}

