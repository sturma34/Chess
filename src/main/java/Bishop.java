public class Bishop implements Piece{
    private static final boolean white = true;
    private static final boolean black = false;
    private boolean color;

    public Bishop(Boolean color){
        this.color = color;
    }

    public String letterRepresentation(){
        if(!this.isWhite()){
            return "\u2657";
        } else {
            return "\u265D";
        }
    }

    public boolean isWhite(){
        return this.color;
    }

    public boolean canMove(Piece[][] board, int startPosition, int endPosition) {
        int startX = startPosition / 10;
        int startY = startPosition % 10;
        int endX = endPosition / 10;
        int endY = endPosition % 10;


        if (startX - endX == 0 || startY - endY == 0) {
            return false;
        }
        //has to move diagonally
        if (Math.abs(endX - startX) != Math.abs(endY - startY)) {
            return false;
        }

        //can't move to a place where your own piece is
        if (board[endX][endY] != null && board[endX][endY].isWhite() == this.color) {
            return false;
        }

        int xDirection;
        if (endX - startX > 0) {
            xDirection = 1;
        } else {
            xDirection = -1;
        }
        int yDirection;
        if (endY - startY > 0) {
            yDirection = 1;
        } else {
            yDirection = -1;
        }

        int yPosition = startY + yDirection;
        for (int xPosition = startX + xDirection; xPosition != endX; xPosition += xDirection, yPosition += yDirection) {

            if (board[xPosition][yPosition] != null) {
                return false;
            }

        }
        return true;
    }

}



