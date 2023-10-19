package ChessEulMelk.Logics;

import ChessEulMelk.BitBoards.BitBoards;
import ChessEulMelk.Globals.Globals;

public class Board {

    private Piece[][] pieceGrid = new Piece[Globals.BOARD_SIZE][Globals.BOARD_SIZE];
    private long whiteOccupancy;
    private long blackOccupancy;
    private long[] pieceOccupancy = new long[Globals.PIECE_COUNT];

    private boolean[] castlingRights = new boolean[4];
    private long enPassantMask;

    public PieceColor turn;
    public int halfMoveClock, fullMoveNumber;
    public boolean isCheck, isCheckMate, isStaleMate;

    public Board() {
        reset();
    }

    public void reset() {
        whiteOccupancy = BitBoards.EMPTY_BITBOARD;
        blackOccupancy = BitBoards.EMPTY_BITBOARD;

        for (int i = 0; i < Globals.PIECE_COUNT; ++i)
            pieceOccupancy[i] = BitBoards.EMPTY_BITBOARD;

        for (int i = 0; i < Globals.BOARD_SIZE; ++i) {
            for (int j = 0; j < Globals.BOARD_SIZE; ++j) {
                pieceGrid[i][j] = Piece.NONE;
            }
        }

        isCheck = isCheckMate = isStaleMate = false;

        castlingRights[0] = false;
        castlingRights[1] = false;
        castlingRights[2] = false;
        castlingRights[3] = false;

        enPassantMask = BitBoards.EMPTY_BITBOARD;
    }

    public Piece getPiece(int row, int col) {
        return pieceGrid[row][col];
    }

    public void setPiece(int row, int col, Piece piece) {
        long mask = BitBoards.getMask(row, col);
        PieceColor color = PieceColor.getColor(piece);

        pieceOccupancy[piece.ordinal()] |= mask;

        if (color == PieceColor.WHITE)
            whiteOccupancy |= mask;
        else if (color == PieceColor.BLACK)
            blackOccupancy |= mask;

        pieceGrid[row][col] = piece;
    }

    public void removePiece(int row, int col) {
        long mask = BitBoards.getMask(row, col);

        for (int i = 1; i < Globals.PIECE_COUNT; ++i) {
            pieceOccupancy[i] &= ~mask;
        }

        whiteOccupancy &= ~mask;
        blackOccupancy &= ~mask;

        pieceGrid[row][col] = Piece.NONE;
    }

    public long getWhiteOccupancy() {
        return whiteOccupancy;
    }

    public long getBlackOccupancy() {
        return blackOccupancy;
    }

    public long getTotalOccupancy() {
        return whiteOccupancy | blackOccupancy;
    }

    public long getPieceOccupancy(Piece piece) {
        return pieceOccupancy[piece.ordinal()];
    }

    public void setCastlingRight(Castling castling, boolean castlingRight) {
        castlingRights[castling.ordinal()] = castlingRight;
    }

    public void setEnPassantMask(int row, int col) {
        enPassantMask = BitBoards.getMask(row, col);
    }

    public void resetEnPassantMask() {
        enPassantMask = BitBoards.EMPTY_BITBOARD;
    }

    public boolean getCastlingRight(Castling castling) {
        return castlingRights[castling.ordinal()];
    }

    public long getEnPassantMask() {
        return enPassantMask;
    }

    public void swapTurn() {
        turn = turn == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;

        if (turn == PieceColor.WHITE)
            ++fullMoveNumber;
    }

    public int[] getPieceCounts() {
        int[] pieceCounts = new int[Globals.PIECE_COUNT];

        for (int row = 0; row < Globals.BOARD_SIZE; ++row) {
            for (int col = 0; col < Globals.BOARD_SIZE; ++col) {
                pieceCounts[pieceGrid[row][col].ordinal()] += 1;
            }
        }

        return pieceCounts;
    }

}