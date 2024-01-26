package chess;

import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamTurn = TeamColor.WHITE;

    private ChessBoard board = new ChessBoard();

    public ChessGame() {
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if(board.getPiece(startPosition) == null){
            return null;
        }
        if(board.getPiece(startPosition).getTeamColor() != teamTurn){
            return null;
        }
        var moves = board.getPiece(startPosition).pieceMoves(board,startPosition);

        //Add in optional moves here

        for(ChessMove move : moves){

        }
        return moves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (validMoves(move.getStartPosition()) != null && validMoves(move.getStartPosition()).contains(move)){
            if(move.getPromotionPiece() != null){
                board.addPiece(move.getEndPosition(), new ChessPiece(board.getPiece(move.getStartPosition()).getTeamColor(), move.getPromotionPiece()));
            }else{
                board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
            }
            board.addPiece(move.getStartPosition(), null);
            teamTurn = teamTurn == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;
        }else{
            throw new InvalidMoveException("Invalid move Called");
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        //Find kings position
        ChessPosition kingPos = null;
        for(int row = 1; row <= 8; row++){
            for(int col = 1; col <= 8; col++){
                ChessPosition pos = new ChessPosition(row, col);
                if(board.getPiece(pos) != null && board.getPiece(pos).equals(new ChessPiece(teamColor, ChessPiece.PieceType.KING))){
                    kingPos = pos;
                    break;
                }
            }
            if(kingPos != null){break;}
        }
        //See if moves put king in danger
        for(int row = 1; row <= 8; row++){
            for(int col = 1; col <= 8; col++){
                ChessPosition pos = new ChessPosition(row, col);
                if(board.getPiece(pos) != null && board.getPiece(pos).pieceMoves(board, pos).contains(new ChessMove(pos, kingPos, null))){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return isInStalemate(teamColor) && isInCheck(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        for(int row = 1; row <= 8; row++){
            for(int col = 1; col <= 8; col++){
                ChessPosition pos = new ChessPosition(row, col);
                if(board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() == teamColor){
                    if(validMoves(pos) != null){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
