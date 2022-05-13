public class King implements Piece{
    private static final boolean white = true;
    private static final boolean black = false;
    private boolean color;
    private boolean hasMoved;

    public King(boolean color){
        this.color = color;
        this.hasMoved = false;
    }

    public String letterRepresentation(){
        if(!this.isWhite()){
            return "\u2654";
        } else {
            return "\u265A";
        }
    }

    public boolean isWhite(){
        return this.color;
    }

    public void setHasMoved(){
        this.hasMoved = true;
    }

    public boolean getHasMoved(){
        return this.hasMoved;
    }

    public boolean canMove(Piece[][] board, int startPosition, int endPosition) {
        int startX = startPosition / 10;
        int startY = startPosition % 10;
        int endX = endPosition / 10;
        int endY = endPosition % 10;

        if (startX > 7 || startX < 0 || startY > 7 || startY < 0 || endX > 7 || endX < 0 || endY > 7 || endY < 0){
            System.out.println("this is a thing in king class just for checkmate checking");
            return false;
        }

        //check for castle first
        if(!this.hasMoved && (startY == endY) && ((endX == 2) || endX == 6)){
            if(isWhite()){
                if(endX == 2){
                    return board[2][0] == null && board[3][0] == null;
                } else {
                    return board[5][0] == null && board[6][0] == null;
                }
            } else {
                if(endX == 2){
                    return board[2][7] == null && board[3][7] == null;
                } else {
                    return board[5][7] == null && board[6][7] == null;
                }
            }
        }
        int changeX = Math.abs(endX - startX);
        int changeY = Math.abs(endY - startY);
        return changeX <= 1 && changeY <= 1;
    }
}
