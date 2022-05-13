import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

//Check if the move is the same as the square that it's already on, check if you are moving to a square with a piece that is the same color as yours.
//Check if you are moving off the board.
public class Board {
    private static final boolean white = true;
    private static final boolean black = false;
    private Piece[][] board;
    private int whiteKingSquare;
    private int blackKingSquare;
    private boolean whoseTurn;
    private Stack<Piece[][]> previousBoardPositions;//used for undo but can also be used for repetitions
    private Set<Integer> whiteLocations;
    private Set<Integer> blackLocations;
    private Boolean isCheckmated;
    private Boolean isStalemated;
    public static final String ANSI_BLACK = "\u001b[32m";
    public static final String ANSI_RESET = "\u001B[0m";

    public Board() {
        this.board = new Piece[8][8];
        this.whoseTurn = white;
        this.previousBoardPositions = new Stack<>();
        this.whiteLocations = new HashSet<>();
        this.blackLocations = new HashSet<>();
        this.setUpBoard();
        this.isCheckmated = false;
        this.whiteKingSquare = 40;
        this.blackKingSquare = 47;
    }

    private void setUpBoard() {
        //Set up white pawns.
        for (int x = 0; x <= 7; x++) {
            this.board[x][1] = new Pawn(white);
        }

        //Set up black pawns.
        for (int x = 0; x <= 7; x++) {
            this.board[x][6] = new Pawn(black);
        }

        //Set up rest of white pieces.
        this.board[0][0] = new Rook(white);
        this.board[1][0] = new Knight(white);
        this.board[2][0] = new Bishop(white);
        this.board[3][0] = new Queen(white);
        this.board[4][0] = new King(white);
        this.board[5][0] = new Bishop(white);
        this.board[6][0] = new Knight(white);
        this.board[7][0] = new Rook(white);

        //Set up rest of black pieces.
        this.board[0][7] = new Rook(black);
        this.board[1][7] = new Knight(black);
        this.board[2][7] = new Bishop(black);
        this.board[3][7] = new Queen(black);
        this.board[4][7] = new King(black);
        this.board[5][7] = new Bishop(black);
        this.board[6][7] = new Knight(black);
        this.board[7][7] = new Rook(black);

        //add all of the white positions.
        for (int i = 0; i <= 70; i += 10) {
            this.whiteLocations.add(i);
        }
        for (int i = 1; i <= 71; i += 10) {
            this.whiteLocations.add(i);
        }

        //add all of the black positions.
        for (int i = 6; i <= 76; i += 10) {
            this.blackLocations.add(i);
        }
        for (int i = 7; i <= 77; i += 10) {
            this.blackLocations.add(i);
        }
    }

    private boolean checkQueenSideCastleForAttackingPieces(boolean color) {
        if (color) {
            for (Integer integer : this.blackLocations) {
                Piece piece = this.numberToPiece(integer);
                if (piece.canMove(board, integer, 20) || piece.canMove(board, integer, 30)) {
                    return false;
                }
            }
        } else {
            for (Integer integer : this.whiteLocations) {
                Piece piece = this.numberToPiece(integer);
                if (piece.canMove(board, integer, 20) || piece.canMove(board, integer, 30)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void doQueenSideCastle(boolean color) {
        //Move the white piece.
        if (color) {
            this.doTheActualMove(0, 3, 0, 0);
            this.updatePieceLocations(this.whoseTurn, 0, 30);
        } //Move the black piece.
        else {
            this.doTheActualMove(0, 3, 7, 7);
            this.updatePieceLocations(this.whoseTurn, 7, 37);
        }
    }

    private boolean checkQueenSideRookForCastling(boolean color) {
        //If there is no rook in the corner that is of the same color and has not yet moved, return false.
        if (color) {
            return board[0][0] != null && board[0][0] instanceof Rook && board[0][0].isWhite() == board[4][0].isWhite() && !((Rook) board[0][0]).getHasMoved();
        } else {
            return board[0][7] != null && board[0][7] instanceof Rook && board[0][7].isWhite() == board[4][7].isWhite() && !((Rook) board[0][7]).getHasMoved();
        }
    }

    private boolean checkQueenSideForInterveningPieces(boolean color) {
        if (color) {
            return board[2][0] == null && board[3][0] == null;
        } else {
            return board[2][7] == null && board[3][7] == null;
        }
    }

    private boolean checkKingSideCastleForAttackingPieces(boolean color) {
        if (color) {
            for (Integer integer : this.blackLocations) {
                Piece piece = this.numberToPiece(integer);
                if (piece.canMove(board, integer, 50) || piece.canMove(board, integer, 60)) {
                    return false;
                }
            }
        } else {
            for (Integer integer : this.whiteLocations) {
                Piece piece = this.numberToPiece(integer);
                if (piece.canMove(board, integer, 57) || piece.canMove(board, integer, 67)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void doKingSideCastle(boolean color) {
        //White castle
        if (color) {
            //move the rook.
            this.doTheActualMove(7, 5, 0, 0);
            this.updatePieceLocations(this.whoseTurn, 70, 50);
        } //Black castle
        else {
            //move the rook.
            this.doTheActualMove(7, 5, 7, 7);
            this.updatePieceLocations(this.whoseTurn, 77, 57);
        }
    }

    private boolean checkKingSideRookForCastling(boolean color) {
        //If there is no rook in the corner that is of the same color and has not yet moved, return false.
        if (color) {
            return board[7][0] != null && board[7][0] instanceof Rook && board[7][0].isWhite() == board[4][0].isWhite() && !((Rook) board[7][0]).getHasMoved();
        } else {
            return board[7][7] != null && board[7][7] instanceof Rook && board[7][7].isWhite() == board[4][7].isWhite() && !((Rook) board[7][7]).getHasMoved();
        }
    }

    private boolean checkKingSideForInterveningPieces(boolean color) {
        if (color) {
            return board[5][0] == null && board[6][0] == null;
        } else {
            return board[5][7] == null && board[6][7] == null;
        }
    }

    private boolean checkCastle(King king, int startY, int endY, int endX, int startX, int startPosition, int endPosition) {
        //If the king has not yet moved, is not currently in check, and is moving to one of the squares for a castle, do the rest of the checks.
        if (!king.getHasMoved() && (startY == endY) && ((endX == 2) || endX == 6) && generalIsInCheck(this.whoseTurn)) {
            //If the king is castling queenside.
            if (endX == 2) {
                //If there is no rook in the corner that is of the same color and has not yet moved, return false.
                if (!this.checkQueenSideRookForCastling(king.isWhite())) {
                    return false;
                }
                //If there are any pieces in the way, return false.
                if (!this.checkQueenSideForInterveningPieces(king.isWhite())) {
                    return false;
                }
                //if the king needs to pass through a check, return false.
                if (!checkQueenSideCastleForAttackingPieces(king.isWhite())) {
                    return false;
                }
                //Otherwise do the castle and return true.
                this.doQueenSideCastle(king.isWhite());
            } //kingside castle.
            else {
                //If there is no rook in the corner that is of the same color and has not yet moved, return false.
                if (!this.checkKingSideRookForCastling(king.isWhite())) {
                    return false;
                }
                //If there are any pieces in the way, return false.
                if (!this.checkKingSideForInterveningPieces(king.isWhite())) {
                    return false;
                }
                //if the king needs to pass through a check, return false.
                if (!this.checkKingSideCastleForAttackingPieces(king.isWhite())) {
                    return false;
                }
                //Otherwise do the castle and return true.
                this.doKingSideCastle(king.isWhite());
            }
        }
        king.setHasMoved();
        this.updateKingSquare(endPosition);
        return true;
    }

    private void updateKingSquare(int endPosition) {
        if (this.whoseTurn == white) {
            this.whiteKingSquare = endPosition;
        } else {
            this.blackKingSquare = endPosition;
        }
    }

    public boolean move(int startPosition, int endPosition) {//check if moving to square already on and whether going past the board
        int startX = startPosition / 10;
        int startY = startPosition % 10;
        int endX = endPosition / 10;
        int endY = endPosition % 10;

        //you're off the board
        if (startX > 7 || startX < 0 || startY > 7 || startY < 0 || endX > 7 || endX < 0 || endY > 7 || endY < 0) {
            System.out.println("you're off the board");
            return false;
        }

        Piece piece1 = board[startX][startY];
        Piece piece2 = board[endX][endY];

        //there was no piece at the starting position so return false
        if (piece1 == null) {
            System.out.println("there was no piece at the starting position");
            return false;
        }


        //you're trying to move the other color's piece
        if (whoseTurn != piece1.isWhite()) {
            System.out.println("you're trying to move the other color's piece");
            return false;
        }

        //you're trying to move onto your own piece
        if (piece2 != null && piece2.isWhite() == piece1.isWhite()) {
            System.out.println("you're trying to move onto your own piece");
            return false;
        }

        if (!piece1.canMove(this.board, startPosition, endPosition)) {
            System.out.println("can move failed");
            return false;
        }


        //check for castling and check if can castle as well
        if (piece1 instanceof King) {
            King king = (King) piece1;
            if (!this.checkCastle(king, startY, endY, endX, startX, startPosition, endPosition)) {
                return false;
            }
        }
        if (piece1 instanceof Rook) {
            Rook rook = (Rook) piece1;
            rook.setHasMoved();
        }
        if (piece1 instanceof Pawn) {
            Pawn pawn = (Pawn) piece1;
            pawn.setHasMoved();
        }
        Piece[][] temp = new Piece[8][8];
        for (int i = 0; i < 8; i++) {
            System.arraycopy(this.board[i], 0, temp[i], 0, 8);
        }
        this.previousBoardPositions.push(temp);
        this.updatePieceLocations(this.whoseTurn, startPosition, endPosition);
        this.board[startX][startY] = null;
        this.board[endX][endY] = piece1;

        if (piece1 instanceof Pawn && (endY == 7 || endY == 0)) {
            board[endX][endY] = new Queen(whoseTurn);
        }

        if (this.areYouInCheck()) {
            //this.previousBoardPositions.pop();
            this.undo();
            System.out.println("You are in check.");
            return false;
        }

        this.checkCheckMate();
        this.switchWhoseTurn();

        return true;

    }
    private Set<Integer> getMovesSet(){
        Set<Integer> moves = new HashSet<>();
        moves.add(1);
        moves.add(-1);
        moves.add(9);
        moves.add(-9);
        moves.add(10);
        moves.add(-10);
        moves.add(11);
        moves.add(-11);
        return moves;
    }
    private boolean checkKingMoves(boolean color){
        Set<Integer> moves = this.getMovesSet();
        for(Integer integer : moves){
            if(color){
                if(numberToPiece(this.whiteKingSquare).canMove(this.board, this.whiteKingSquare, this.whiteKingSquare + integer)){
                    this.whiteKingSquare = this.whiteKingSquare + integer;
                    if(this.whiteIsInCheck()){
                        this.switchWhoseTurn();
                        this.whiteKingSquare = this.whiteKingSquare - integer;
                        return true;
                    }
                }
            } else {
                if(numberToPiece(this.blackKingSquare).canMove(this.board, this.blackKingSquare, this.blackKingSquare + integer)){
                    this.blackKingSquare = this.blackKingSquare + integer;
                    if(this.whiteIsInCheck()){
                        this.switchWhoseTurn();
                        this.blackKingSquare = this.blackKingSquare - integer;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean canCaptureCheckingPiece(boolean color, int attackingSquare){
        if(color){
            for (int number : this.blackLocations) {
                if (numberToPiece(number).canMove(this.board, number, attackingSquare)) {
                    if (numberToPiece(number) instanceof King) {//don't let the king kill the queen in four move check mate
                        int oldBlackKingSquare = this.blackKingSquare;
                        this.blackKingSquare = attackingSquare;
                        if (!blackIsInCheck()) {
                            this.switchWhoseTurn();
                            this.blackKingSquare = oldBlackKingSquare;
                            System.out.println("9");
                            return true;//the guy can kill the piece to get out of check so no checkmate
                        }
                        this.blackKingSquare = oldBlackKingSquare;
                    }
                }
            }
        } else {
            for (int number : this.whiteLocations) {
                if (numberToPiece(number).canMove(this.board, number, attackingSquare)) {
                    if (numberToPiece(number) instanceof King) {//don't let the king kill the queen in four move check mate
                        int oldWhiteKingSquare = this.whiteKingSquare;
                        this.whiteKingSquare = attackingSquare;
                        if (!this.whiteIsInCheck()) {
                            this.switchWhoseTurn();
                            this.whiteKingSquare = oldWhiteKingSquare;
                            System.out.println("9");
                            return true;//the guy can kill the piece to get out of check so no checkmate
                        }
                        this.whiteKingSquare = oldWhiteKingSquare;
                    }
                }
            }
        }
        return false;
    }

    private Set<Integer> getAttackingSquare(boolean color){
        Set<Integer> attackingSquares = new HashSet<>();
        if(color){
            int whiteSquareThatCanKillBlackKing = 0; //MAKE SURE THIS IS ALWAYS INITIALIZED. SHOULD BE BECAUSE ITS IN CHECK
            for (int number : whiteLocations) {
                if (numberToPiece(number).canMove(this.board, number, blackKingSquare)) {
                    attackingSquares.add(number);
                }
            }
        } else {
            int blackSquareThatCanKillBlackKing = 0; //MAKE SURE THIS IS ALWAYS INITIALIZED. SHOULD BE BECAUSE ITS IN CHECK
            for (int number : this.blackLocations) {
                if (numberToPiece(number).canMove(this.board, number, this.whiteKingSquare)) {
                    attackingSquares.add(number);
                }
            }
        }
        return attackingSquares;
    }
    private void checkCheckMate(){
        Set<Integer> attackingSquares = this.getAttackingSquare(this.whoseTurn);
        if(this.generalIsInCheck(!this.whoseTurn)){
            return;
        }
        if(attackingSquares.size() > 1){
            if (!this.checkKingMoves(!this.whoseTurn)) {
                this.isCheckmated = true;
            }
            return;
        }
        int attackingSquare = 0;
        for(Integer integer : attackingSquares){
            attackingSquare = integer;
        }
        if(this.checkKingMoves(!this.whoseTurn)){
            return;
        }
        if(this.canCaptureCheckingPiece(!this.whoseTurn, attackingSquare)){
            return;
        }
        if (whoseTurn == white && blackIsInCheck()) {//figure out for multiple pieces attacking the king

            //Set whiteSquaresThatCanKillBlackKing = new HashSet<Integer>();

            //if (this.numberToPiece(whiteSquareThatCanKillBlackKing) instanceof Knight){

            if (this.getX(attackingSquare) == this.getX(blackKingSquare)) {//same column, same Y
                int sharedX = this.getX(blackKingSquare);
                int int1 = this.getY(attackingSquare);
                int int2 = this.getY(blackKingSquare);
                int leftBound;
                int rightBound;
                if (int1 > int2) {
                    rightBound = int1;
                    leftBound = int2;
                } else {
                    rightBound = int2;
                    leftBound = int1;
                }
                for (int i = leftBound + 1; i < rightBound - 1; i++) {
                    for (int number : blackLocations) {

                        if (numberToPiece(number).canMove(this.board, number, Integer.parseInt(String.valueOf(sharedX) + i))) {
                            switchWhoseTurn();
                            System.out.println("10");
                            return;
                        }
                    }
                }


            } else if (this.getY(attackingSquare) == this.getY(blackKingSquare)) {
                int sharedY = this.getY(blackKingSquare);
                int int1 = this.getX(attackingSquare);
                int int2 = this.getX(blackKingSquare);
                int leftBound;
                int rightBound;
                if (int1 > int2) {
                    rightBound = int1;
                    leftBound = int2;
                } else {
                    rightBound = int2;
                    leftBound = int1;
                }
                for (int i = leftBound + 1; i < rightBound - 1; i++) {
                    for (int number : blackLocations) {

                        if (numberToPiece(number).canMove(this.board, number, Integer.parseInt(String.valueOf(sharedY) + i))) {
                            switchWhoseTurn();
                            System.out.println("11");
                            return;
                        }
                    }
                }
            } else {
                //diagonal. ACTUAL VERY BAD LOGICAL ERROR POSSIBLY CAN THIS BE A KNIGHT HERE SO NOT NECESSARILY DIAGONAL
                if (numberToPiece(attackingSquare) instanceof Bishop) {


                    int startX1 = this.getX(attackingSquare);
                    int endX1 = this.getX(blackKingSquare);
                    int startY1 = this.getY(attackingSquare);
                    int endY1 = this.getY(blackKingSquare);
                    int xDirection;
                    if (endX1 - startX1 > 0) {
                        xDirection = 1;
                    } else {
                        xDirection = -1;
                    }
                    int yDirection;
                    if (endY1 - startY1 > 0) {
                        yDirection = 1;
                    } else {
                        yDirection = -1;
                    }

                    int yPosition = startY1 + yDirection;
                    for (int xPosition = startX1 + xDirection; xPosition != endX1; xPosition += xDirection, yPosition += yDirection) {

                        for (int number : blackLocations) {

                            if (numberToPiece(number).canMove(this.board, number, Integer.parseInt(String.valueOf(xPosition) + yPosition))) {
                                switchWhoseTurn();
                                System.out.println("12");
                                return;
                            }
                        }

                    }
                }
            }
//VERY MUCH HAVE TO CHECK SOMEWHERE IF THE KING CAN SIMPLY ESCAPE BECAUSE HE CAN MOVE UP THERE. IF FINALLY IF NOT RETURN TRUE BUT FIRST SET ISCHECKMATED
            this.isCheckmated = true;//switch turn?
            System.out.println("CHECKMATE!");
        }
    }

    private boolean areYouInCheck() {
        return this.whoseTurn ? this.whiteIsInCheck() : this.blackIsInCheck();
    }

    //deal with king or rook or pawn set as moved if it was actually illegal bc of check
    private void doTheActualMove(int startX, int endX, int startY, int endY) {
        this.board[endX][endY] = this.board[startX][startY];
        this.board[startX][startY] = null;
    }

    private void updatePieceLocations(boolean turn, int startPosition, int endPosition) {
        if (turn) {
            this.whiteLocations.remove(startPosition);
            this.whiteLocations.add(endPosition);
            this.blackLocations.remove(endPosition);
        } else {
            this.blackLocations.remove(startPosition);
            this.blackLocations.add(endPosition);
            this.whiteLocations.remove(endPosition);
        }
    }

    public void undo() {
        this.board = this.previousBoardPositions.pop();
        this.whiteLocations.clear();
        this.blackLocations.clear();
        for (int y = 0; y <= 7; y++) {
            for (int x = 0; x <= 7; x++) {
                Integer position = Integer.valueOf(String.valueOf(x) + y);
                if (this.board[x][y] != null && this.board[x][y].isWhite()) {
                    this.whiteLocations.add(position);
                } else if (this.board[x][y] != null && !this.board[x][y].isWhite()) {
                    this.blackLocations.add(position);
                }
            }
        }
    }

    private boolean generalIsInCheck(Boolean color) {
        if (color) {
            return !this.whiteIsInCheck();
        } else {
            return !this.blackIsInCheck();
        }
    }

    private boolean whiteIsInCheck() {
        for (int position : this.blackLocations) {
            if (numberToPiece(position).canMove(this.board, position, this.whiteKingSquare)) {
                return true;
            }
        }

        return false;

    }

    private boolean blackIsInCheck() {
        for (int position : this.whiteLocations) {
            if (numberToPiece(position).canMove(this.board, position, this.blackKingSquare)) {
                return true;
            }
        }
        return false;
    }

    public boolean getWhoseTurn() {
        return this.whoseTurn;
    }

    private void switchWhoseTurn() {
        if (this.whoseTurn == white) {
            this.whoseTurn = black;
        } else {
            this.whoseTurn = white;
        }
    }

    private int getX(int number) {
        return number / 10;
    }

    private int getY(int number) {
        return number % 10;
    }

    public Piece numberToPiece(int number) {
        return this.board[getX(number)][getY(number)];
    }

    public boolean isCheckmated() {
        return isCheckmated;
    }

    public void printoutWhite() {
        StringBuilder printout = new StringBuilder("  A  B  C  D  E  F  G  H\n");
        System.out.println("Hello");
        for (int i = 0; i < 8; i++) {
            printout.append(8 - i);
            for (int j = 0; j < 8; j++) {
                if (this.board[j][7 - i] == null) {
                    if ((i + j) % 2 == 0) {
                        printout.append("[ ]");
                    } else {
                        printout.append(ANSI_BLACK + "[ ]" + ANSI_RESET);
                    }
                } else {
                    if ((i + j) % 2 == 0) {
                        printout.append("[").append(this.board[j][7 - i].letterRepresentation()).append("]");
                    } else {
                        printout.append(ANSI_BLACK + "[" + ANSI_RESET).append(this.board[j][7 - i].letterRepresentation()).append(ANSI_BLACK + "]" + ANSI_RESET);
                    }

                }
            }
            printout.append(8 - i).append("\n");
        }
        printout.append("  A  B  C  D  E  F  G  H\n");
        System.out.println(printout);
    }

    public void printoutBlack() {
        StringBuilder printout = new StringBuilder("  H  G  F  E  D  C  B  A\n");
        System.out.println("Hello");
        for (int i = 0; i < 8; i++) {
            printout.append(i + 1);
            for (int j = 0; j < 8; j++) {
                if (this.board[7 - j][i] == null) {
                    if ((i + j) % 2 == 0) {
                        printout.append("[ ]");
                    } else {
                        printout.append(ANSI_BLACK + "[ ]" + ANSI_RESET);
                    }
                } else {
                    if ((i + j) % 2 == 0) {
                        printout.append("[").append(this.board[7 - j][i].letterRepresentation()).append("]");
                    } else {
                        printout.append(ANSI_BLACK + "[" + ANSI_RESET).append(this.board[7 - j][i].letterRepresentation()).append(ANSI_BLACK + "]" + ANSI_RESET);
                    }

                }
            }
            printout.append(i + 1).append("\n");
        }
        printout.append("  H  G  F  E  D  C  B  A\n");
        System.out.println(printout);
    }

    private boolean isStalemated() {
        return this.isStalemated;
    }

    private void checkForStalemate(boolean whoseTurn) {
        if (whoseTurn) {
            colorStaleMate(this.blackLocations);
        } else {
            colorStaleMate(this.whiteLocations);
        }
    }

    private void colorStaleMate(Set<Integer> locations) {
        for (int number : locations) {
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    int place = Integer.parseInt(String.valueOf(x) + y);
                    if (numberToPiece(number).canMove(this.board, number, place)) {
                        return;
                    }
                }
            }
            this.isStalemated = true;

        }
    }
}
