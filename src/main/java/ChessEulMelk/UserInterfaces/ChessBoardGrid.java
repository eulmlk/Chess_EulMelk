package ChessEulMelk.UserInterfaces;

import java.awt.Graphics;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;

import ChessEulMelk.BitBoards.BitBoards;
import ChessEulMelk.BitBoards.LegalFlagGenerator;
import ChessEulMelk.Globals.Globals;
import ChessEulMelk.Logics.Board;
import ChessEulMelk.Logics.BoardState;
import ChessEulMelk.Logics.Game;
import ChessEulMelk.Logics.Move;
import ChessEulMelk.Logics.MoveGenerator;
import ChessEulMelk.Logics.MoveHandler;
import ChessEulMelk.Logics.Piece;
import ChessEulMelk.Logics.PieceColor;

public class ChessBoardGrid extends JPanel implements MouseListener, MouseMotionListener {
    private Board board;
    private Game game;
    private Piece draggedPiece;
    private int dragX, dragY;

    private Move hypoMove;
    private BoardFlags flags;

    private MainFrame mainFrame;
    private MoveHistoryPanel historyPanel;
    private ChessBoardPainter painter;

    public ChessBoardGrid(MainFrame mainFrame, Board board, MoveHistoryPanel historyPanel, Game game) {
        this.mainFrame = mainFrame;
        this.board = board;
        this.historyPanel = historyPanel;
        this.game = game;

        painter = new ChessBoardPainter(board);

        PromotionMenu.init();
        LegalFlagGenerator.init();

        addMouseListener(this);
        addMouseMotionListener(this);

        draggedPiece = Piece.NONE;
        dragX = 0;
        dragY = 0;

        hypoMove = new Move();
        flags = new BoardFlags();
    }

    public void resizeBoard(int squareSize) {
        painter.setSquareSize(squareSize);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        painter.paintChessBoard(g, flags);
        painter.drawDraggedPiece(g, draggedPiece, dragX, dragY);
    }

    @Override
    public void mouseClicked(MouseEvent event) {
    }

    @Override
    public void mouseEntered(MouseEvent event) {
    }

    @Override
    public void mouseExited(MouseEvent event) {
    }

    @Override
    public void mousePressed(MouseEvent event) {
        if (event.getButton() != MouseEvent.BUTTON1)
            return;

        int squareSize = painter.getSquareSize();
        int lowBound = 0, highBound = Globals.BOARD_SIZE * squareSize;
        int col = event.getX(), row = event.getY();

        if (col < lowBound || col > highBound || row < lowBound || row > highBound) {
            return;
        }

        col /= squareSize;
        row /= squareSize;

        if (MoveGenerator.generateMoves(board, hypoMove, flags, row, col)) {
            flags.dragFlags = BitBoards.getMask(row, col);
            draggedPiece = board.getPiece(row, col);
            hypoMove.setSourceFields(row, col, draggedPiece);

            dragX = event.getX();
            dragY = event.getY();
        }

        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        if (event.getButton() != MouseEvent.BUTTON1)
            return;

        if (draggedPiece == Piece.NONE)
            return;

        int squareSize = painter.getSquareSize();
        int lowBound = 0, highBound = Globals.BOARD_SIZE * squareSize;
        int col = event.getX(), row = event.getY();

        if (col < lowBound || col > highBound || row < lowBound || row > highBound) {
            flags.dragFlags = BitBoards.EMPTY_BITBOARD;
            flags.legalFlags = BitBoards.EMPTY_BITBOARD;
            draggedPiece = Piece.NONE;
            repaint();
            return;
        }

        col /= squareSize;
        row /= squareSize;

        flags.dragFlags = BitBoards.EMPTY_BITBOARD;
        Piece oldPiece = board.getPiece(row, col);
        Piece promotedPiece = Piece.NONE;

        PieceColor promotionColor = MoveHandler.isPromotion(row, draggedPiece);

        if (promotionColor != PieceColor.NONE) {
            hypoMove.setPromotion(true);
            promotedPiece = PromotionMenu.showPromotionDialog(mainFrame, this, promotionColor);
        }

        hypoMove.setDestFields(row, col, oldPiece, promotedPiece);

        if (flags.isLegal(row, col)) {
            MoveHandler.makeMove(board, hypoMove, flags);
            historyPanel.updateMoveHistory(hypoMove);
            historyPanel.updateHistory();
            game.addPosition(BoardState.generateFENString(board));
        }

        flags.legalFlags = BitBoards.EMPTY_BITBOARD;
        draggedPiece = Piece.NONE;
        mainFrame.updateEvalBar();
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        if ((event.getModifiersEx() & InputEvent.BUTTON1_DOWN_MASK) == 0)
            return;

        dragX = event.getX();
        dragY = event.getY();
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent event) {
    }

}
