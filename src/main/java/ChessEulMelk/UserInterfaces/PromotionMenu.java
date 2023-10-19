package ChessEulMelk.UserInterfaces;

import ChessEulMelk.ImageHandler.ImageLoader;
import ChessEulMelk.ImageHandler.ImageLoader.PieceSource;
import ChessEulMelk.Logics.Piece;
import ChessEulMelk.Logics.PieceColor;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class PromotionMenu {

    private static ImageIcon[] imageIcons;
    private static Piece chosenPiece;

    public static void init() {
        imageIcons = ImageLoader.loadImageIcons(PieceSource.CHESS_PIECE_MASTERS);
    }

    public static Piece showPromotionDialog(JFrame parentPanel, JPanel panel, PieceColor color) {
        JDialog dialog = new JDialog(parentPanel, "Piece Promotion", true);

        int start = color == PieceColor.WHITE ? 0 : 4, end = start + 4;
        chosenPiece = color == PieceColor.WHITE ? Piece.WHITE_QUEEN : Piece.BLACK_QUEEN;
        int initialPiece = chosenPiece.ordinal();

        JButton[] buttons = new JButton[4];

        for (int i = start; i < end; ++i) {
            int index = i - start;

            buttons[index] = new JButton(imageIcons[i]);
            Piece curPiece = Piece.values()[initialPiece + index];

            buttons[index].addActionListener(new ButtonHandlers(dialog, curPiece));
        }

        JPanel buttonsPanel = new JPanel();

        GridLayout layout = new GridLayout(1, 4, 20, 20);

        buttonsPanel.setLayout(layout);
        buttonsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        buttonsPanel.add(buttons[0]);
        buttonsPanel.add(buttons[1]);
        buttonsPanel.add(buttons[2]);
        buttonsPanel.add(buttons[3]);

        dialog.add(buttonsPanel);
        dialog.setSize(500, 180);
        dialog.setResizable(false);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(panel);
        dialog.setVisible(true);

        return chosenPiece;
    }

    private static class ButtonHandlers implements ActionListener {

        private JDialog dialog;
        private Piece piece;

        public ButtonHandlers(JDialog dialog, Piece piece) {
            this.dialog = dialog;
            this.piece = piece;
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            chosenPiece = piece;
            dialog.dispose();
        }
    }

}
