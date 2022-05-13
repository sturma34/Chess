public interface Piece {

    /**
     * @param startPosition piece's initial location on the board
     * @param endPosition the square to which the piece should move
     * @return true if the move can be successfully completed, false if not
     */
    boolean canMove(Piece[][] board, int startPosition, int endPosition);

    /**
     * @return boolean true if the piece is white, false if it's black
     */
    boolean isWhite();

    String letterRepresentation();
}
