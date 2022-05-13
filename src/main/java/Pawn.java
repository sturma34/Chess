public class Pawn implements Piece{
//enpassant
    private static final boolean white = true;
    private static final boolean black = false;

    private boolean color;
    private boolean hasMoved;

    public Pawn(boolean color){
        this.color = color;
        this.hasMoved = false;
    }


    public String letterRepresentation(){
        if(!this.isWhite()){
            return "\u2659";
        } else {
            return "\u265F";
        }
    }

    /**
     * @param startingPosition piece's initial location on the board
     * @param endPosition the square to which the piece should move
     * @return true if the move can be successfully completed, false if not
     */
    public boolean canMove(Piece[][] board, int startingPosition, int endPosition){
        int startX = startingPosition / 10;
        int startY = startingPosition % 10;
        int endX = endPosition / 10;
        int endY = endPosition % 10;

        //If the pawn tries to move forward more than two spots, return false.
        if(Math.abs(startY - endY) > 2){
            return false;
        }

        //If the pawn has already moved and tries to move forward two spots, return false.
        if(Math.abs(startY - endY) == 2 && this.hasMoved){
            return false;
        }

        //If the piece tries to move backward, return false.
        if((color == white && endY <= startY) || (color == black && endY >= startY)){
            return false;
        }

        //If the pawn tries to move more than one square horizontally, or it tries to move one square not on the diagonal return false.
        if(Math.abs(startX - endX) > 1 || (Math.abs(startX - endX) == 1 && Math.abs(startY - endY) != 1)){
            return false;
        }

        //If the pawn tries to move two spaces and there is another piece in the way, return false.
        if(Math.abs(startY - endY) == 2){
            if(isWhite()){
                if(board[endX][startY + 1] != null){
                    return false;
                }
            } else {
                if(board[endX][startY - 1] != null){
                    return false;
                }
            }
        }
        //Get target piece.
        Piece square = board[endX][endY];

        //If the pawn is moving forward, return false if there is a piece in his way, true if not.
        if (Math.abs(startX - endX) == 0){
            return square == null;
        }

        //If the code reaches here, it is capturing a piece. Return false if the square is empty. or the pieces are different colors.
        if(square == null){
            return false;
        } else {
            return !(!square.isWhite() && !this.isWhite()) || (square.isWhite() && this.isWhite());
        }
    }

    public void setHasMoved(){
        this.hasMoved = true;
    }

    /**
     * @return boolean true if the piece is white, false if it's black
     */
    public boolean isWhite(){
        return color;
    }
}
