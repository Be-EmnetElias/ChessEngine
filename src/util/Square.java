package util;

public class Square {
    public int col;
    public int row;

    public Square(int col, int row){
        this.col = col;
        this.row = row;
    }

    public void displace(Square displacement){
        this.col += displacement.col;
        this.row += displacement.row;

    }

    @Override
    public String toString(){
        return "[" + this.col + " , " + this.row + "]";
    }
}
