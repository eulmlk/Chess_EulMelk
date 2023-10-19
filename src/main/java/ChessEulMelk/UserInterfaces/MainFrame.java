package ChessEulMelk.UserInterfaces;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ChessEulMelk.Logics.Board;
import ChessEulMelk.Logics.BoardState;
import ChessEulMelk.Logics.Game;

public class MainFrame extends JFrame {
    private static final double HISTORY_WIDTH_RATIO = 0.14;
    private static final double BOARD_WIDTH_RATIO = 0.45;
    private static final double CONTROLS_WIDTH_RATIO = 0.38;
    private static final double CLOCK_HEIGHT_RATIO = 0.1;
    private static final double CONTROLS_HEIGHT_RATIO = 0.35;

    private JMenuBar menuBar;
    private MoveHistoryPanel historyPanel;
    private ChessBoard chessBoard;
    private EvalPanel evalPanel;
    private ControlsPanel controlsPanel;

    JPanel panel2;
    JPanel panel3;
    JPanel panel6;
    JPanel panel7;

    private Board board;
    private Game game;

    public MainFrame() {
        setTitle("Chess EulMelk - Chess GUI");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setPreferredSize(new Dimension(900, 540));
        setMinimumSize(new Dimension(800, 450));
        setLayout(null);

        board = new Board();
        BoardState.setupStartPosition(board);

        game = new Game();
        game.addPosition(BoardState.generateFENString(board));

        initMenuBars();
        initPanels();

        addComponentListener(new ResizeHandler());

        pack();
        setVisible(true);
    }

    public void updateEvalBar() {
        evalPanel.setBounds(evalPanel.getBounds());
    }

    private void initPanels() {

        historyPanel = new MoveHistoryPanel(board);
        chessBoard = new ChessBoard(this, board, historyPanel, game);
        evalPanel = new EvalPanel(board);
        controlsPanel = new ControlsPanel(board, game);

        panel2 = new JPanel();
        panel3 = new JPanel();
        panel6 = new JPanel();
        panel7 = new JPanel();

        panel2.setBackground(Color.green);
        panel3.setBackground(Color.blue);
        panel6.setBackground(Color.yellow);
        panel7.setBackground(Color.red);

        getContentPane().add(historyPanel);
        getContentPane().add(panel2);
        getContentPane().add(chessBoard);
        getContentPane().add(panel3);
        getContentPane().add(evalPanel);
        getContentPane().add(controlsPanel);
        getContentPane().add(panel6);
        getContentPane().add(panel7);
    }

    private void initMenuBars() {
        menuBar = new JMenuBar();

        JMenu gameMenu = new JMenu("Game");

        JMenuItem newGame = new JMenuItem("New 2 Player Game");
        JMenuItem playEngine = new JMenuItem("Play Againt Engine");
        JMenuItem preferences = new JMenuItem("Preferences");
        JMenuItem exit = new JMenuItem("Exit");

        newGame.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MainFrame.this, "New Game Menu", "Option Pane",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        });

        playEngine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MainFrame.this, "Play Engine Menu", "Option Pane",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        preferences.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MainFrame.this, "Options Menu", "Option Pane",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        gameMenu.add(newGame);
        gameMenu.add(playEngine);
        gameMenu.add(preferences);
        gameMenu.add(exit);

        menuBar.add(gameMenu);

        setJMenuBar(menuBar);
    }

    private class ResizeHandler extends ComponentAdapter {

        @Override
        public void componentResized(ComponentEvent event) {
            int windowWidth = getContentPane().getWidth(), windowHeight = getContentPane().getHeight();

            int historyX = 0, historyY = 0;
            int historyWidth = (int) (windowWidth * HISTORY_WIDTH_RATIO), historyHeight = windowHeight;

            int clockX = historyWidth, clockTopY = 0, clockWidth = (int) (windowWidth * BOARD_WIDTH_RATIO);
            int clockHeight = (int) (windowHeight * CLOCK_HEIGHT_RATIO), clockBottomY = windowHeight - clockHeight;

            int canvasX = clockX, canvasY = clockHeight, canvasWidth = clockWidth;
            int canvasHeight = windowHeight - (2 * clockHeight);

            int controlWidth = (int) (windowWidth * CONTROLS_WIDTH_RATIO);

            int evalX = canvasWidth + historyWidth, evalY = 0, evalHeight = windowHeight;
            int evalWidth = windowWidth - (historyWidth + clockWidth + controlWidth);

            int controlX = evalX + evalWidth, controlY = 0;
            int controlHeight = (int) (windowHeight * CONTROLS_HEIGHT_RATIO);

            int engineLeftX = controlX, engineY = controlHeight, engineLeftWidth = controlWidth / 2;
            int engineRightWidth = controlWidth - engineLeftWidth;
            int engineRightX = engineLeftX + engineLeftWidth, engineHeight = windowHeight - controlHeight;

            historyPanel.setBounds(historyX, historyY, historyWidth, historyHeight);
            panel2.setBounds(clockX, clockTopY, clockWidth, clockHeight);
            chessBoard.setBounds(canvasX, canvasY, canvasWidth, canvasHeight);
            panel3.setBounds(clockX, clockBottomY, clockWidth, clockHeight);
            evalPanel.setBounds(evalX, evalY, evalWidth, evalHeight);
            controlsPanel.setBounds(controlX, controlY, controlWidth, controlHeight);
            panel6.setBounds(engineLeftX, engineY, engineLeftWidth, engineHeight);
            panel7.setBounds(engineRightX, engineY, engineRightWidth, engineHeight);
        }

    }
}