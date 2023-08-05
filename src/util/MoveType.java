package util;
    
public enum MoveType {

    DEFAULT, CAPTURE, CASTLE_KING_SIDE, CASTLE_QUEEN_SIDE, PROMOTION, ENPASSANT, CHECK;
    
    @Override
    public String toString(){
        switch(this.ordinal()){
            case 0:
                return "DEFAULT";
            case 1:
                return "CAPTURE";
            case 2:
                return "CASTLE_KING_SIDE";
            case 3:
                return "CASTLE_QUEEN_SIDE";
            case 4:
                return "PROMOTION";
            case 5:
                return "ENPASSANT";
            case 6:
                return "CHECK";
            default:
                return null;
        }
    }
}

