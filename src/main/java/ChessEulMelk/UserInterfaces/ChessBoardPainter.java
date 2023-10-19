package ChessEulMelk.UserInterfaces;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import ChessEulMelk.Globals.Globals;
import ChessEulMelk.ImageHandler.ImageLoader;
import ChessEulMelk.ImageHandler.ImageLoader.PieceSource;
import ChessEulMelk.Logics.Board;
import ChessEulMelk.Logics.Piece;

public class ChessBoardPainter {
    private Board board;
    private BufferedImage[] images;

    private int squareSize;

    private Graphics graphics;
    private BoardFlags flags;

    public ChessBoardPainter(Board board) {
        this.board = board;

        images = ImageLoader.loadImages(PieceSource.CHESS_PIECE_MASTERS);
    }

    public void setSquareSize(int squareSize) {
        this.squareSize = squareSize;
    }

    public int getSquareSize() {
        return squareSize;
    }

    public void paintChessBoard(Graphics graphics, BoardFlags flags) {
        this.graphics = graphics;
        this.flags = flags;

        drawChessBoard();
        drawPieces();
        drawLegalSquareCircles();
    }

    private void drawPieces() {

        for (int row = 0; row < Globals.BOARD_SIZE; ++row) {
            for (int col = 0; col < Globals.BOARD_SIZE; ++col) {
                if (flags.isDragged(row, col))
                    continue;

                Piece curPiece = board.getPiece(row, col);

                if (curPiece == Piece.NONE)
                    continue;

                BufferedImage loadedImage = images[curPiece.ordinal() - 1];

                if (loadedImage == null)
                    continue;

                BufferedImage scaledImage = getScaledImage(loadedImage);

                int x = col * squareSize;
                int y = row * squareSize;

                graphics.drawImage(scaledImage, x, y, null);
            }
        }
    }

    public void drawDraggedPiece(Graphics g, Piece draggedPiece, int dragX, int dragY) {
        if (draggedPiece == Piece.NONE)
            return;

        BufferedImage loadedImage = images[draggedPiece.ordinal() - 1];

        if (loadedImage == null)
            return;

        BufferedImage scaledImage = getScaledImage(loadedImage);

        int x = dragX - squareSize / 2;
        int y = dragY - squareSize / 2;

        g.drawImage(scaledImage, x, y, null);
    }

    private BufferedImage getScaledImage(BufferedImage loadedImage) {
        BufferedImage scaledImage = new BufferedImage(squareSize, squareSize, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics2d = scaledImage.createGraphics();

        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        graphics2d.drawImage(loadedImage, 0, 0, squareSize, squareSize, null);
        graphics2d.dispose();

        return scaledImage;
    }

    private void drawLegalSquareCircles() {
        Color normalColor = new Color(0, 192, 0, 128);
        Color captureColor = new Color(255, 0, 0, 128);
        Color specialColor = new Color(0, 64, 192, 128);

        int circleDiameter = 4 * squareSize / 5;
        int offset = squareSize / 10;

        for (int row = 0; row < Globals.BOARD_SIZE; ++row) {
            for (int col = 0; col < Globals.BOARD_SIZE; ++col) {
                if (!flags.isLegal(row, col))
                    continue;

                if (flags.isSpecial(row, col))
                    graphics.setColor(specialColor);
                else
                    graphics.setColor(board.getPiece(row, col) != Piece.NONE ? captureColor : normalColor);

                int x = col * squareSize + offset;
                int y = row * squareSize + offset;

                graphics.fillOval(x, y, circleDiameter, circleDiameter);
            }
        }
    }

    private void drawChessBoard() {
        for (int row = 0; row < Globals.BOARD_SIZE; ++row) {
            for (int col = 0; col < Globals.BOARD_SIZE; ++col) {
                boolean isWhiteSquare = (row + col) % 2 == 0;
                Color squareColor;

                if (flags.isCheck(row, col))
                    squareColor = new Color(96, 16, 16);
                else
                    squareColor = isWhiteSquare ? new Color(255, 204, 153) : new Color(51, 0, 102);

                int x = col * squareSize;
                int y = row * squareSize;

                graphics.setColor(squareColor);
                graphics.fillRect(x, y, squareSize, squareSize);
            }
        }
    }
}