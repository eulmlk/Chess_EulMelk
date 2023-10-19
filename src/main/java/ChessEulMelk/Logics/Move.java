package ChessEulMelk.Logics;

public class Move {
    public int sourceRow, sourceCol, destRow, destCol;
    public Piece movingPiece, capturedPiece, promotedPiece;
    public boolean isPromotion, isCastling, isEnPassant;

    public void setSourceFields(int row, int col, Piece piece) {
        sourceRow = row;
        sourceCol = col;
        movingPiece = piece;
    }

    public void setDestFields(int row, int col, Piece capPiece, Piece promPiece) {
        destRow = row;
        destCol = col;
        capturedPiece = capPiece;
        promotedPiece = promPiece;
    }

    public void setPromotion(boolean isPromotion) {
        this.isPromotion = isPromotion;
    }

    public void setCastling(boolean isCastling) {
        this.isCastling = isCastling;
    }

    public void setEnPassant(boolean isEnPassant) {
        this.isEnPassant = isEnPassant;
    }
}
