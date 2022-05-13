public class Rook implements Piece{

    private static final boolean white = true;
    private static final boolean black = false;

    private boolean color;
    private boolean hasMoved;

    public Rook(boolean color){
        this.color = color;
        this.hasMoved = false;
    }

    public String letterRepresentation(){
        if(!this.isWhite()){
            return "\u2656";
        } else {
            return "\u265C";
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

        //If the move is diagonal, return false.
        if((startX != endX) && (startY != endY)){
            return false;
        }

        //Automatically make the smaller number "startX"
        //If the Y coordinates are the same, the rook is moving horizontally.
        if(startY == endY){
            int upperBound;
            int lowerBound;
            if (startX > endX){
                upperBound = startX;
                lowerBound = endX;
            } else {
                upperBound = endX;
                lowerBound = startX;
            }
            for(int i = lowerBound + 1; i < upperBound - 1 ; i++){
                if(!isEmpty(board[i][startY])){
                    return false;
                }
            }
        } else {
            int leftBound;
            int rightBound;
            if (startY > endY){
                rightBound = startY;
                leftBound = endY;
            } else {
                rightBound = endY;
                leftBound = startY;
            }
            for(int i = leftBound + 1; i < rightBound - 1 ; i++){
                if(!isEmpty(board[startX][i])){
                    return false;
                }
            }
        }

        //can't move to a place where your own piece is
        return board[endX][endY] == null || board[endX][endY].isWhite() != this.color;
    }

    private boolean isEmpty(Piece piece){
        return piece == null;
    }
    /**
     * @return boolean true if the piece is white, false if it's black
     */
    public boolean isWhite(){
        return this.color;
    }

    public void setHasMoved(){
        this.hasMoved = true;
    }

    public boolean getHasMoved(){
        return this.hasMoved;
    }
}
