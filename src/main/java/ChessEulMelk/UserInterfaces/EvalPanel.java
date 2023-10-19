package ChessEulMelk.UserInterfaces;

import java.awt.Color;
import javax.swing.JPanel;

import ChessEulMelk.Logics.Board;
import ChessEulMelk.Logics.Piece;

public class EvalPanel extends JPanel {
    private Board board;
    private JPanel whitePanel;

    public EvalPanel(Board board) {
        this.board = board;

        setLayout(null);
        setBackground(Color.black);

        whitePanel = new JPanel();
        whitePanel.setBackground(Color.white);

        add(whitePanel);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);

        int eval = getEval();
        int halfHeight = height / 2;
        int evalHeight = (int) (halfHeight * Math.tanh(eval / 10.0));

        int whiteY = halfHeight - evalHeight;
        int whiteHeight = halfHeight + evalHeight;

        whitePanel.setBounds(0, whiteY, width, whiteHeight);
    }

    private int getEval() {
        return getMaterialEvaluation();
    }

    public int getMaterialEvaluation() {
        int[] pieceCounts = board.getPieceCounts();

        final int KING_VALUE = 300, QUEEN_VALUE = 9, ROOK_VALUE = 5, BISHOP_VALUE = 3, KNIGHT_VALUE = 3, PAWN_VALUE = 1;

        int whiteMaterial = pieceCounts[Piece.WHITE_KING.ordinal()] * KING_VALUE
                + pieceCounts[Piece.WHITE_QUEEN.ordinal()] * QUEEN_VALUE
                + pieceCounts[Piece.WHITE_ROOK.ordinal()] * ROOK_VALUE
                + pieceCounts[Piece.WHITE_BISHOP.ordinal()] * BISHOP_VALUE
                + pieceCounts[Piece.WHITE_KNIGHT.ordinal()] * KNIGHT_VALUE
                + pieceCounts[Piece.WHITE_PAWN.ordinal()] * PAWN_VALUE;

        int blackMaterial = pieceCounts[Piece.BLACK_KING.ordinal()] * KING_VALUE
                + pieceCounts[Piece.BLACK_QUEEN.ordinal()] * QUEEN_VALUE
                + pieceCounts[Piece.BLACK_ROOK.ordinal()] * ROOK_VALUE
                + pieceCounts[Piece.BLACK_BISHOP.ordinal()] * BISHOP_VALUE
                + pieceCounts[Piece.BLACK_KNIGHT.ordinal()] * KNIGHT_VALUE
                + pieceCounts[Piece.BLACK_PAWN.ordinal()] * PAWN_VALUE;

        return whiteMaterial - blackMaterial;
    }

}