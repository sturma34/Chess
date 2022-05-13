import java.util.Scanner;

public class ChessPlayer {
    private static final boolean white = true;
    private static final boolean black = false;
    public static void main(String[] args){
        int argumentCounter = 0;
        Scanner scanner = new Scanner(System.in);
        Board board = new Board();
        while (!board.isCheckmated()){
            if(board.getWhoseTurn()){
                board.printoutWhite();
            } else {
                board.printoutBlack();
            }
            String startPosition = scanner.next();
            int int1 = startPosition.charAt(0) - 65;
            int int2 = Integer.parseInt(String.valueOf(startPosition.charAt(1))) - 1;
            Integer intStartPosition = Integer.valueOf(String.valueOf(int1) + int2);
            argumentCounter +=1;
            String endPosition = scanner.next();
            int int3 = endPosition.charAt(0) - 65;
            int int4 = Integer.parseInt(String.valueOf(endPosition.charAt(1))) - 1;
            Integer intEndPosition = Integer.valueOf(String.valueOf(int3) + int4);
            argumentCounter += 1;
            if (argumentCounter % 2 == 0){
                boolean moveWorked = board.move(intStartPosition,intEndPosition);
                if (board.isCheckmated()){
                    if (board.getWhoseTurn()) {
                        board.printoutBlack();
                    } else {
                        board.printoutWhite();
                    }
                    break;
                }
                if (!moveWorked){
                    System.out.println("That  was illegal. Please try a new move");
                }else {
                    System.out.println("your move was successful");
                }

                if (board.getWhoseTurn() == white){
                    System.out.println("white, it is your turn to move");
                }else{
                    System.out.println("black, it is your turn to move");
                }
            }
            }
        }


    }


