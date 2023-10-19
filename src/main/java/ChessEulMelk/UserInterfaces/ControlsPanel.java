package ChessEulMelk.UserInterfaces;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import ChessEulMelk.ImageHandler.ButtonIconLoader;
import ChessEulMelk.Logics.Board;
import ChessEulMelk.Logics.BoardState;
import ChessEulMelk.Logics.Game;

public class ControlsPanel extends JPanel {

    private Board board;
    private Game game;

    private ImageIcon[] imageIcons;

    private JButton firstButton;
    private JButton prevButton;
    private JButton undoButton;
    private JButton nextButton;
    private JButton lastButton;
    private JButton resignButton;

    public ControlsPanel(Board board, Game game) {
        this.board = board;
        this.game = game;

        setLayout(null);
        setBorder(BorderFactory.createBevelBorder(0));
        setBackground(Color.lightGray);

        initButtons();
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);

        int gap = width / 25;
        int buttonSize = (width - 7 * gap) / 6;

        int buttonX = gap, buttonY = gap;

        firstButton.setBounds(buttonX, buttonY, buttonSize, buttonSize);
        buttonX += buttonSize + gap;
        prevButton.setBounds(buttonX, buttonY, buttonSize, buttonSize);
        buttonX += buttonSize + gap;
        undoButton.setBounds(buttonX, buttonY, buttonSize, buttonSize);
        buttonX += buttonSize + gap;
        nextButton.setBounds(buttonX, buttonY, buttonSize, buttonSize);
        buttonX += buttonSize + gap;
        lastButton.setBounds(buttonX, buttonY, buttonSize, buttonSize);
        buttonX += buttonSize + gap;
        resignButton.setBounds(buttonX, buttonY, buttonSize, buttonSize);
    }

    private void initButtons() {
        imageIcons = ButtonIconLoader.loadControlIcons();

        firstButton = new JButton(imageIcons[0]);
        prevButton = new JButton(imageIcons[1]);
        undoButton = new JButton(imageIcons[2]);
        nextButton = new JButton(imageIcons[3]);
        lastButton = new JButton(imageIcons[4]);
        resignButton = new JButton(imageIcons[5]);

        firstButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                game.toFirst();
                BoardState.parseFENString(board, game.getCurrent());
            }
        });

        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                game.toPrev();
                BoardState.parseFENString(board, game.getCurrent());
            }
        });

        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                game.popBack();
                BoardState.parseFENString(board, game.getCurrent());
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                game.toNext();
                BoardState.parseFENString(board, game.getCurrent());
            }
        });

        lastButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                game.toLast();
                BoardState.parseFENString(board, game.getCurrent());
            }
        });

        add(firstButton);
        add(prevButton);
        add(undoButton);
        add(nextButton);
        add(lastButton);
        add(resignButton);
    }

}
