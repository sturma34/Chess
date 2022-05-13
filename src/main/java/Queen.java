public class Queen implements Piece{
    private static final boolean white = true;
    private static final boolean black = false;
    private boolean color;

    public Queen(boolean color){
        this.color = color;
    }

    public String letterRepresentation(){
        if(!this.isWhite()){
            return "\u2655";
        } else {
            return "\u265B";
        }
    }

    //Do this method.
    @Override
    public boolean canMove(Piece[][] board, int startPosition, int endPosition) {
        int startX = startPosition / 10;
        int startY = startPosition % 10;
        int endX = endPosition / 10;
        int endY = endPosition % 10;
        if(endX == startX || endY == startY){
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
                return true;
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
                return true;
            }
        } else if (Math.abs(endX - startX) == Math.abs(endY - startY)){
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
        } else {
            return false;
        }
    }

    private boolean isEmpty(Piece piece){
        return piece == null;
    }

    @Override
    public boolean isWhite() {
        return this.color;
    }
}
