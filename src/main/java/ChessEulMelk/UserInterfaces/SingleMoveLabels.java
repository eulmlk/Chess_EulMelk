package ChessEulMelk.UserInterfaces;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class SingleMoveLabels {
    JPanel backPanel;
    JLabel moveNumberLabel;
    JLabel whiteMoveLabel;
    JLabel blackMoveLabel;

    private static Color evenColor = new Color(133, 174, 172);
    private static Color oddColor = new Color(164, 174, 133);

    public SingleMoveLabels(int moveCount, String whiteMove, String blackMove) {
        backPanel = new JPanel();

        backPanel.setLayout(null);
        backPanel.setBackground(moveCount % 2 == 0 ? evenColor : oddColor);

        moveNumberLabel = new JLabel(moveCount + "");
        whiteMoveLabel = new JLabel(whiteMove);
        blackMoveLabel = new JLabel(blackMove);

        moveNumberLabel.setHorizontalAlignment(JLabel.CENTER);
        whiteMoveLabel.setHorizontalAlignment(JLabel.CENTER);
        blackMoveLabel.setHorizontalAlignment(JLabel.CENTER);

        moveNumberLabel.addMouseListener(new MouseHandler(moveNumberLabel));
        whiteMoveLabel.addMouseListener(new MouseHandler(whiteMoveLabel));
        blackMoveLabel.addMouseListener(new MouseHandler(blackMoveLabel));

        backPanel.add(moveNumberLabel);
        backPanel.add(whiteMoveLabel);
        backPanel.add(blackMoveLabel);
    }

    public void setBounds(int x, int y, int width, int height) {
        int fontSize = 2 * width / 25;

        Font font = new Font(Font.MONOSPACED, Font.BOLD, fontSize);

        int gap = width / 30;
        int moveStringWidth = (int) (width * 0.4);
        int moveNumberWidth = width - (2 * (gap + moveStringWidth));

        int moveNumberX = 0, whiteMoveX = moveNumberX + moveNumberWidth + gap;
        int blackMoveX = whiteMoveX + moveStringWidth + gap;

        backPanel.setBounds(x, y, width, height);
        moveNumberLabel.setBounds(moveNumberX, 0, moveNumberWidth, height);
        whiteMoveLabel.setBounds(whiteMoveX, 0, moveStringWidth, height);
        blackMoveLabel.setBounds(blackMoveX, 0, moveStringWidth, height);

        moveNumberLabel.setFont(font);
        whiteMoveLabel.setFont(font);
        blackMoveLabel.setFont(font);
    }

    private class MouseHandler implements MouseListener {

        private JLabel label;
        private Border border = BorderFactory.createEtchedBorder();
        private Color downColor = Color.yellow;
        private Color upColor = Color.black;

        public MouseHandler(JLabel label) {
            this.label = label;
        }

        @Override
        public void mouseClicked(MouseEvent event) {
            System.out.println("Hello!!");
        }

        @Override
        public void mouseEntered(MouseEvent event) {
            label.setBorder(border);
        }

        @Override
        public void mouseExited(MouseEvent event) {
            label.setBorder(null);
        }

        @Override
        public void mousePressed(MouseEvent event) {
            label.setForeground(downColor);
        }

        @Override
        public void mouseReleased(MouseEvent event) {
            label.setForeground(upColor);
        }
    }
}