package ChessEulMelk.UserInterfaces;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ChessEulMelk.BitBoards.AmbiguityTester;
import ChessEulMelk.BitBoards.BitBoards;
import ChessEulMelk.BitBoards.LegalFlagGenerator;
import ChessEulMelk.Globals.Globals;
import ChessEulMelk.Logics.Board;
import ChessEulMelk.Logics.Move;
import ChessEulMelk.Logics.Piece;
import ChessEulMelk.Logics.PieceColor;

public class MoveHistoryPanel extends JPanel {
    private Board board;

    private JScrollPane scrollPane;
    private JPanel scrollablePanel;
    private ArrayList<SingleMoveLabels> moveLabels = new ArrayList<>();

    private ArrayList<String> moveHistory = new ArrayList<>();

    public MoveHistoryPanel(Board board) {
        this.board = board;

        setLayout(null);

        scrollablePanel = new JPanel();
        scrollablePanel.setLayout(null);
        scrollablePanel.setBackground(Color.lightGray);
        scrollablePanel.setBorder(BorderFactory.createEtchedBorder());

        scrollPane = new JScrollPane(scrollablePanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane);
    }

    public void updateHistory() {
        moveLabels.clear();
        scrollablePanel.removeAll();

        int size = moveHistory.size();

        for (int i = 0; i < size; i += 2) {
            int moveNumber = 1 + i / 2;
            String whiteMove = moveHistory.get(i);
            String blackMove = i < size - 1 ? moveHistory.get(i + 1) : "";

            SingleMoveLabels moveLabel = new SingleMoveLabels(moveNumber, whiteMove,
                    blackMove);
            moveLabels.add(moveLabel);

            scrollablePanel.add(moveLabel.backPanel);
        }

        setBounds(getBounds());
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        scrollPane.setBounds(x, y, width, height);
        final int SCROLL_BAR_WIDTH = 15;

        int hgap = width / 25, singleWidth = width - 2 * hgap, singleX = x + hgap;
        int singleHeight = 4 * width / 25, vgap = singleHeight / 5, singleY = y + vgap;

        int scrollWidth = width;
        int scrollHeight = moveLabels.size() * (singleHeight + vgap) + vgap;

        scrollablePanel.setPreferredSize(new Dimension(scrollWidth, scrollHeight));
        scrollablePanel.setBounds(x, y, width, scrollHeight);

        int viewportHeight = scrollPane.getViewport().getBounds().height;
        int panelHeight = scrollablePanel.getPreferredSize().height;

        if (panelHeight > viewportHeight) {
            singleWidth -= SCROLL_BAR_WIDTH;
        }

        for (SingleMoveLabels moveLabel : moveLabels) {
            moveLabel.setBounds(singleX, singleY, singleWidth, singleHeight);
            singleY += singleHeight + vgap;
        }
    }

    public long getAmbiguityMoveMask(Piece piece, Move move) {
        long moveMasks = BitBoards.EMPTY_BITBOARD;
        PieceColor pieceColor = PieceColor.getColor(piece);

        long friendOccupancy = pieceColor == PieceColor.WHITE
                ? board.getWhiteOccupancy()
                : board.getBlackOccupancy();

        long enemyOccupancy = pieceColor == PieceColor.WHITE
                ? board.getBlackOccupancy()
                : board.getWhiteOccupancy();

        int index = move.sourceCol + move.sourceRow * Globals.BOARD_SIZE;
        long pieceMask = BitBoards.FIRST_BIT_MASK >>> index;

        enemyOccupancy |= pieceMask;

        moveMasks = LegalFlagGenerator.getMoveMasks(friendOccupancy, enemyOccupancy, piece, move.destRow, move.destCol);
        moveMasks &= board.getPieceOccupancy(piece);

        return moveMasks;
    }

    public void updateMoveHistory(Move move) {
        if ((move.sourceRow == 0 || move.sourceRow == 7) &&
                (move.movingPiece == Piece.WHITE_KING || move.movingPiece == Piece.BLACK_KING)) {
            if (move.sourceCol == 4 && move.destCol == 6) {
                moveHistory.add("O-O");
                return;
            } else if (move.sourceCol == 4 && move.destCol == 2) {
                moveHistory.add("O-O-O");
                return;
            }
        }

        if (move.movingPiece == Piece.WHITE_PAWN || move.movingPiece == Piece.BLACK_PAWN) {
            if (move.sourceCol != move.destCol && move.capturedPiece == Piece.NONE) {
                String algebraicString = Globals.FILES[move.sourceCol] + "x" +
                        Globals.FILES[move.destCol]
                        + Globals.RANKS[move.destRow];
                moveHistory.add(algebraicString);
                return;
            }
        }

        String destSquare = Globals.FILES[move.destCol] + Globals.RANKS[move.destRow];
        String captureFlag = move.capturedPiece != Piece.NONE ? "x" : "";
        String movingPiece = Globals.ALGEBRAIC_LETTERS[move.movingPiece.ordinal()];
        String promotionFlag = "=" + Globals.ALGEBRAIC_LETTERS[move.promotedPiece.ordinal()];
        String checkOrMateFlag = "";

        promotionFlag = move.promotedPiece != Piece.NONE ? promotionFlag : "";

        String sourceSquare = "";
        long moveMasks = getAmbiguityMoveMask(move.movingPiece, move);

        if (move.movingPiece == Piece.WHITE_PAWN || move.movingPiece == Piece.BLACK_PAWN) {
            if (move.capturedPiece != Piece.NONE)
                sourceSquare += Globals.FILES[move.sourceCol];
        } else if (AmbiguityTester.testFileAmbiguity(moveMasks, move.sourceCol))
            sourceSquare += Globals.FILES[move.sourceCol];
        else if (AmbiguityTester.testRankAmbiguity(moveMasks, move.sourceRow))
            sourceSquare += Globals.RANKS[move.sourceRow];
        else if (moveMasks != 0)
            sourceSquare += Globals.FILES[move.sourceCol] + Globals.RANKS[move.sourceRow];

        if (board.isCheck)
            checkOrMateFlag = board.isCheckMate ? "#" : "+";

        String algebraicString = movingPiece + sourceSquare + captureFlag + destSquare + promotionFlag
                + checkOrMateFlag;

        moveHistory.add(algebraicString);
    }

}