//I'm pretending this is just a Pawn test
import org.junit.jupiter.api.Test;
public class PawnTest {
    @Test
    public void yeee(){
        Piece[][] board = new Piece[8][8];
        Pawn pawn = new Pawn(true);

        assert pawn.canMove(board, 01, 02);
        assert pawn.canMove(board, 01, 03);
        assert !pawn.canMove(board, 01, 04);
        assert !pawn.canMove(board, 01, 05);
        assert !pawn.canMove(board, 01, 06);
        assert !pawn.canMove(board, 01, 07);
        board[0][2] = new Bishop(true);
        assert !pawn.canMove(board, 01, 02);
        assert !pawn.canMove(board, 01, 03);
        board[0][2] = new Bishop(false);
        assert !pawn.canMove(board, 01, 02);
        assert !pawn.canMove(board, 01, 03);
        assert !pawn.canMove(board, 01, 12);
        board[1][2] = new Bishop(true);
        //assert !pawn.canMove(board, 01, 12); checked on board level
        board[1][2] = new Bishop(false);
        assert pawn.canMove(board, 01, 12);




    }

}
