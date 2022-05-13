public class Knight implements Piece{
    private static final boolean white = true;
    private static final boolean black = false;
    private boolean color;

    public Knight(Boolean color){
        this.color = color;
    }

    @Override
    public String letterRepresentation(){
        if(!this.isWhite()){
            return "\u2658";
        } else {
            return "\u265E";
        }
    }

    @Override
    public boolean canMove(Piece[][] board, int startingPosition, int endPosition) {
        int startX = startingPosition / 10;
        int startY = startingPosition % 10;
        int endX = endPosition / 10;
        int endY = endPosition % 10;
        return (Math.abs(endX - startX) == 2 && Math.abs(endY - startY) == 1) || (Math.abs(endY - startY) == 2 && Math.abs(endX - startX) == 1);
    }

    public boolean isWhite(){
        return this.color;
    }
}
