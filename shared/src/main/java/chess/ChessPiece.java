package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }


    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        switch (type){
            case BISHOP -> {
                return bishopMoves(board, myPosition);
            }
            case KING -> {
                return kingMoves(board, myPosition);
            }
            case KNIGHT -> {
                return knightMoves(board, myPosition);
            }
            case PAWN -> {
                return pawnMoves(board, myPosition);
            }
            case QUEEN -> {
                return queenMoves(board, myPosition);
            }
            case ROOK -> {
                return rookMoves(board, myPosition);
            }
            default -> {
                return new ArrayList<>();
            }
        }
    }

    private void checkMove(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition, ChessPosition checkedPos){
        if(checkedPos.getRow() > 8 || checkedPos.getRow() < 1 ||checkedPos.getColumn() > 8 || checkedPos.getColumn() < 1){
            //Out of bounds
            return;
        }
        if (board.getPiece(checkedPos) == null){
            //Free Space
            moves.add(new ChessMove(myPosition,checkedPos,null));
        }
        else if (board.getPiece(checkedPos).getTeamColor() != pieceColor){
            //Enemy space
            moves.add(new ChessMove(myPosition,checkedPos,null));
        }
    }

    private void pawnPromotionCheck(Collection<ChessMove> moves, ChessPosition myPosition, ChessPosition checkedPos, ChessGame.TeamColor team){
        if(checkedPos.getRow() > 8 || checkedPos.getRow() < 1 ||checkedPos.getColumn() > 8 || checkedPos.getColumn() < 1){
            //Out of bounds
            return;
        }
        if (team == ChessGame.TeamColor.WHITE && checkedPos.getRow() == 8){
            // add white promotion options
            moves.add(new ChessMove(myPosition,checkedPos,PieceType.BISHOP));
            moves.add(new ChessMove(myPosition,checkedPos,PieceType.KNIGHT));
            moves.add(new ChessMove(myPosition,checkedPos,PieceType.QUEEN));
            moves.add(new ChessMove(myPosition,checkedPos,PieceType.ROOK));
        }
        else if (team == ChessGame.TeamColor.BLACK && checkedPos.getRow() == 1){
            // add black promotion options
            moves.add(new ChessMove(myPosition,checkedPos,PieceType.BISHOP));
            moves.add(new ChessMove(myPosition,checkedPos,PieceType.KNIGHT));
            moves.add(new ChessMove(myPosition,checkedPos,PieceType.QUEEN));
            moves.add(new ChessMove(myPosition,checkedPos,PieceType.ROOK));
        }
        else {
            moves.add(new ChessMove(myPosition,checkedPos,null));
        }
    }

    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        var row = myPosition.getRow();
        var col = myPosition.getColumn();
        //up right check
        for (int i = 1; row + i <= 8 && col + i <= 8; i++){
            var checkedPos = new ChessPosition(row + i, col + i);
            checkMove(moves,board,myPosition,checkedPos);
            if (board.getPiece(checkedPos) != null){
                break;
            }
        }
        //down right check
        for (int i = 1; row - i >= 1 && col + i <= 8; i++){
            var checkedPos = new ChessPosition(row - i, col + i);
            checkMove(moves,board,myPosition,checkedPos);
            if (board.getPiece(checkedPos) != null){
                break;
            }
        }
        //down left check
        for (int i = 1; row - i >= 1 && col - i >= 1; i++){
            var checkedPos = new ChessPosition(row - i, col - i);
            checkMove(moves,board,myPosition,checkedPos);
            if (board.getPiece(checkedPos) != null){
                break;
            }
        }
        //up left check
        for (int i = 1; row + i <= 8 && col - i >= 1; i++){
            var checkedPos = new ChessPosition(row + i, col - i);
            checkMove(moves,board,myPosition,checkedPos);
            if (board.getPiece(checkedPos) != null){
                break;
            }
        }
        return moves;
    }

    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new ArrayList<>();
        var row = myPosition.getRow();
        var col = myPosition.getColumn();
        checkMove(moves,board,myPosition,new ChessPosition(row+1,col+1));
        checkMove(moves,board,myPosition,new ChessPosition(row+1,col));
        checkMove(moves,board,myPosition,new ChessPosition(row+1,col-1));
        checkMove(moves,board,myPosition,new ChessPosition(row,col-1));
        checkMove(moves,board,myPosition,new ChessPosition(row-1,col-1));
        checkMove(moves,board,myPosition,new ChessPosition(row-1,col));
        checkMove(moves,board,myPosition,new ChessPosition(row-1,col+1));
        checkMove(moves,board,myPosition,new ChessPosition(row,col+1));
        return moves;
    }

    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new ArrayList<>();
        var row = myPosition.getRow();
        var col = myPosition.getColumn();
        checkMove(moves,board,myPosition,new ChessPosition(row+2,col+1));
        checkMove(moves,board,myPosition,new ChessPosition(row+1,col+2));
        checkMove(moves,board,myPosition,new ChessPosition(row+2,col-1));
        checkMove(moves,board,myPosition,new ChessPosition(row+1,col-2));
        checkMove(moves,board,myPosition,new ChessPosition(row-2,col-1));
        checkMove(moves,board,myPosition,new ChessPosition(row-1,col-2));
        checkMove(moves,board,myPosition,new ChessPosition(row-2,col+1));
        checkMove(moves,board,myPosition,new ChessPosition(row-1,col+2));
        return moves;
    }

    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new HashSet<>();
        var row = myPosition.getRow();
        var col = myPosition.getColumn();
        if(pieceColor == ChessGame.TeamColor.WHITE){
            //white pawn moves up
            var checkedPos = new ChessPosition(row+1,col);
            if (board.getPiece(checkedPos) == null && checkedPos.getRow() <= 8){
                //Free Space
                pawnPromotionCheck(moves, myPosition, checkedPos, pieceColor);
                //if not moved yet, can move up two
                if(row == 2){
                    checkedPos = new ChessPosition(row+2,col);
                    if (board.getPiece(checkedPos) == null){
                        //Free Space
                        moves.add(new ChessMove(myPosition,checkedPos,null));
                    }
                }
            }
            //Diagonal Attacks
            checkedPos = new ChessPosition(row+1,col-1);
            if (board.getPiece(checkedPos) != null && board.getPiece(checkedPos).getTeamColor() == ChessGame.TeamColor.BLACK){
                //attack up right
                pawnPromotionCheck(moves, myPosition, checkedPos, pieceColor);
            }
            checkedPos = new ChessPosition(row+1,col+1);
            if (board.getPiece(checkedPos) != null && board.getPiece(checkedPos).getTeamColor() == ChessGame.TeamColor.BLACK){
                //attack up left
                pawnPromotionCheck(moves, myPosition, checkedPos, pieceColor);
            }
        } else {
            //black pawn moves down
            var checkedPos = new ChessPosition(row-1,col);
            if (board.getPiece(checkedPos) == null && checkedPos.getRow() >= 1){
                //Free Space
                pawnPromotionCheck(moves, myPosition, checkedPos, pieceColor);
                //if not moved yet, can move up two
                if(row == 7){
                    checkedPos = new ChessPosition(row-2,col);
                    if (board.getPiece(checkedPos) == null){
                        //Free Space
                        moves.add(new ChessMove(myPosition,checkedPos,null));
                    }
                }
            }
            //Diagonal Attacks
            checkedPos = new ChessPosition(row-1,col-1);
            if (board.getPiece(checkedPos) != null && board.getPiece(checkedPos).getTeamColor() == ChessGame.TeamColor.WHITE){
                //attack down right
                pawnPromotionCheck(moves, myPosition, checkedPos, pieceColor);
            }
            checkedPos = new ChessPosition(row-1,col+1);
            if (board.getPiece(checkedPos) != null && board.getPiece(checkedPos).getTeamColor() == ChessGame.TeamColor.WHITE){
                //attack down left
                pawnPromotionCheck(moves, myPosition, checkedPos, pieceColor);
            }
        }
        return moves;
    }

    private Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>(rookMoves(board, myPosition));
        moves.addAll(bishopMoves(board, myPosition));
        return moves;
    }

    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new HashSet<>();
        var row = myPosition.getRow();
        var col = myPosition.getColumn();
        //up check
        for (int i = 1; row + i <= 8; i++){
            var checkedPos = new ChessPosition(row + i, col);
            checkMove(moves,board,myPosition,checkedPos);
            if (board.getPiece(checkedPos) != null){
                break;
            }
        }
        //right check
        for (int i = 1; col + i <= 8; i++){
            var checkedPos = new ChessPosition(row, col + i);
            checkMove(moves,board,myPosition,checkedPos);
            if (board.getPiece(checkedPos) != null){
                break;
            }
        }
        //down check
        for (int i = 1; row - i >= 1; i++){
            var checkedPos = new ChessPosition(row - i, col);
            checkMove(moves,board,myPosition,checkedPos);
            if (board.getPiece(checkedPos) != null){
                break;
            }
        }
        //left check
        for (int i = 1; col - i >= 1; i++){
            var checkedPos = new ChessPosition(row, col - i);
            checkMove(moves,board,myPosition,checkedPos);
            if (board.getPiece(checkedPos) != null){
                break;
            }
        }
        return moves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        return "Piece{" +
                pieceColor +
                ", " + type +
                '}';
    }
}
