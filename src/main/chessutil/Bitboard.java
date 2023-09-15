package main.chessutil;

public class Bitboard {
    private long bitboard;

    public Bitboard(long initialPosition) {
        this.bitboard = initialPosition;
    }

    // Utility methods for bit manipulation

    public long getLong() {
        return this.bitboard;
    }
    
    /**
     * Sets a bit at a current {@code integer} location using bitwise OR
     * @param position
     */
    public void setBit(int position) {
        this.bitboard |= (1L << position);
    }

    public void clearBit(int position) {
        this.bitboard &= ~(1L << position);
    }

    public boolean isBitSet(int position) {
        return (this.bitboard & (1L << position)) != 0;
    }

}

