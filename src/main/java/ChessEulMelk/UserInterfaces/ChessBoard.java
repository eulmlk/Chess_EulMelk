package ChessEulMelk.UserInterfaces;

import java.awt.Color;

import javax.swing.JPanel;

import ChessEulMelk.Globals.Globals;
import ChessEulMelk.Logics.Game;

public class ChessBoard extends JPanel {
    private ChessBoardGrid boardGrid;
    private JPanel canvas;

    private JPanel cornerPanel1;
    private JPanel cornerPanel2;
    private JPanel cornerPanel3;
    private JPanel cornerPanel4;

    private FileRankLabel[] fileLabels;
    private FileRankLabel[] rankLabels;

    public ChessBoard(MainFrame mainFrame, ChessEulMelk.Logics.Board board, MoveHistoryPanel historyPanel, Game game) {
        setBackground(new Color(140, 171, 235));
        setLayout(null);

        initComponents();

        boardGrid = new ChessBoardGrid(mainFrame, board, historyPanel, game);
        canvas.add(boardGrid);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);

        resizeBoard(width, height);
    }

    private void initComponents() {
        canvas = new JPanel();
        canvas.setLayout(null);

        initCornerPanels();
        initLabels();

        add(canvas);
    }

    private void initCornerPanels() {
        cornerPanel1 = new JPanel();
        cornerPanel2 = new JPanel();
        cornerPanel3 = new JPanel();
        cornerPanel4 = new JPanel();

        cornerPanel1.setBackground(Color.darkGray);
        cornerPanel2.setBackground(Color.darkGray);
        cornerPanel3.setBackground(Color.darkGray);
        cornerPanel4.setBackground(Color.darkGray);

        canvas.add(cornerPanel1);
        canvas.add(cornerPanel2);
        canvas.add(cornerPanel3);
        canvas.add(cornerPanel4);
    }

    private void initLabels() {
        fileLabels = new FileRankLabel[Globals.LABEL_COUNT];
        rankLabels = new FileRankLabel[Globals.LABEL_COUNT];

        for (int i = 0; i < Globals.LABEL_COUNT; ++i) {
            fileLabels[i] = new FileRankLabel(i, true);
            rankLabels[i] = new FileRankLabel(i, false);

            canvas.add(fileLabels[i]);
            canvas.add(rankLabels[i]);
        }
    }

    private void resizeBoard(int canvasWidth, int canvasHeight) {
        int boardSize = Math.min(canvasWidth, canvasHeight);
        int squareSize = 3 * boardSize / 26;
        int cornerSize = boardSize / 26, corner1 = 0, corner2 = 8 * squareSize + cornerSize;
        int totalSize = 2 * cornerSize + 8 * squareSize;
        int offsetX = (canvasWidth - totalSize) / 2, offsetY = (canvasHeight - totalSize) / 2;

        canvas.setBounds(offsetX, offsetY, totalSize, totalSize);

        cornerPanel1.setBounds(corner1, corner1, cornerSize, cornerSize);
        cornerPanel2.setBounds(corner1, corner2, cornerSize, cornerSize);
        cornerPanel3.setBounds(corner2, corner1, cornerSize, cornerSize);
        cornerPanel4.setBounds(corner2, corner2, cornerSize, cornerSize);

        for (int i = 0; i < Globals.LABEL_COUNT; ++i) {
            int index = i % Globals.BOARD_SIZE;

            int fileX = cornerSize + (squareSize * index), fileY = i < 8 ? corner1 : corner2;
            int rankY = cornerSize + (squareSize * index), rankX = i < 8 ? corner1 : corner2;

            fileLabels[i].setBounds(fileX, fileY, squareSize, cornerSize);
            rankLabels[i].setBounds(rankX, rankY, cornerSize, squareSize);
        }

        boardGrid.setBounds(cornerSize, cornerSize, 8 * squareSize, 8 * squareSize);
        boardGrid.resizeBoard(squareSize);
    }

}
