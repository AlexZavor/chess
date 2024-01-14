package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;

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
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
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
            default -> {
                return new ArrayList<>();
            }
        }
    }

    private Collection<ChessMove> checkMove(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition, ChessPosition checkedPos){
        if(checkedPos.getRow() > 8 || checkedPos.getRow() < 1 ||checkedPos.getColumn() > 8 || checkedPos.getColumn() < 1){
            //Out of bounds
            return moves;
        }
        if (board.getPiece(checkedPos) == null){
            //Free Space
            moves.add(new ChessMove(myPosition,checkedPos,null));
        }
        else if (board.getPiece(checkedPos).getTeamColor() != pieceColor){
            //Enemy space
            moves.add(new ChessMove(myPosition,checkedPos,null));
        }
        return moves;
    }

    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        var row = myPosition.getRow();
        var col = myPosition.getColumn();
        //up right check
        int i = 1;
        while (row + i <= 8 && col + i <= 8){
            var checkedPos = new ChessPosition(row + i, col + i);
            moves = checkMove(moves,board,myPosition,checkedPos);
            if (board.getPiece(checkedPos) != null){
                break;
            }
            i++;
        }
        //down right check
        i = 1;
        while (row - i >= 1 && col + i <= 8){
            var checkedPos = new ChessPosition(row - i, col + i);
            moves = checkMove(moves,board,myPosition,checkedPos);
            if (board.getPiece(checkedPos) != null){
                break;
            }
            i++;
        }
        //down left check
        i = 1;
        while (row - i >= 1 && col - i >= 1){
            var checkedPos = new ChessPosition(row - i, col - i);
            moves = checkMove(moves,board,myPosition,checkedPos);
            if (board.getPiece(checkedPos) != null){
                break;
            }
            i++;
        }
        //up left check
        i = 1;
        while (row + i <= 8 && col - i >= 1){
            var checkedPos = new ChessPosition(row + i, col - i);
            moves = checkMove(moves,board,myPosition,checkedPos);
            if (board.getPiece(checkedPos) != null){
                break;
            }
            i++;
        }
        return moves;
    }

    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        var row = myPosition.getRow();
        var col = myPosition.getColumn();
        moves = checkMove(moves,board,myPosition,new ChessPosition(row+1,col+1));
        moves = checkMove(moves,board,myPosition,new ChessPosition(row+1,col));
        moves = checkMove(moves,board,myPosition,new ChessPosition(row+1,col-1));
        moves = checkMove(moves,board,myPosition,new ChessPosition(row,col-1));
        moves = checkMove(moves,board,myPosition,new ChessPosition(row-1,col-1));
        moves = checkMove(moves,board,myPosition,new ChessPosition(row-1,col));
        moves = checkMove(moves,board,myPosition,new ChessPosition(row-1,col+1));
        moves = checkMove(moves,board,myPosition,new ChessPosition(row,col+1));
        return moves;
    }

    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        var row = myPosition.getRow();
        var col = myPosition.getColumn();
        moves = checkMove(moves,board,myPosition,new ChessPosition(row+2,col+1));
        moves = checkMove(moves,board,myPosition,new ChessPosition(row+1,col+2));
        moves = checkMove(moves,board,myPosition,new ChessPosition(row+2,col-1));
        moves = checkMove(moves,board,myPosition,new ChessPosition(row+1,col-2));
        moves = checkMove(moves,board,myPosition,new ChessPosition(row-2,col-1));
        moves = checkMove(moves,board,myPosition,new ChessPosition(row-1,col-2));
        moves = checkMove(moves,board,myPosition,new ChessPosition(row-2,col+1));
        moves = checkMove(moves,board,myPosition,new ChessPosition(row-1,col+2));
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
}
