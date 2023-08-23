package main.java.util;

public class Piece {
    
    public Name name;
    public boolean white;
    public int col;
    public int row;
    public boolean firstMove;
    public boolean canSlide;

    
    public Piece(Name name, boolean isWhite, int col, int row) {
        this.name = name;
        this.white = isWhite;
        this.col = col;
        this.row = row;

        this.firstMove = true;
        this.canSlide = !(name == Name.KING || name == Name.PAWNW || name == Name.PAWNB || name == Name.KNIGHT);
    }

    public Square getPosition() {
        return new Square(this.col,this.row);
    }

    /**
     * Used for printing the board in ASCII format
     * 
     * @return Returns the letter representation of this piece. 
     */
    
    public String getLetter() {
        if (this.white) {
            if (this.name == Name.KNIGHT) {
                return "N ";
            }
            return this.name.toString().substring(0, 1) + " ";
        } else {
            if (this.name == Name.KNIGHT) {
                return "n ";
            }
            return this.name.toString().substring(0, 1).toLowerCase() + " ";
        }
    }
}

