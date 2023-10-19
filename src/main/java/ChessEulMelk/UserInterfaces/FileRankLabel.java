package ChessEulMelk.UserInterfaces;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ChessEulMelk.Globals.Globals;

public class FileRankLabel extends JPanel {

    private JLabel label;

    public FileRankLabel(int index, boolean isFile) {
        setBackground(new Color(153, 0, 0));
        setBorder(BorderFactory.createBevelBorder(1));
        setLayout(null);

        String[] labelStrings = isFile ? Globals.FILES : Globals.RANKS;

        label = new JLabel(labelStrings[index % Globals.BOARD_SIZE].toUpperCase());
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setForeground(Color.yellow);

        add(label);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        Font font = new Font(Font.MONOSPACED, Font.BOLD, Math.min(width, height));

        label.setBounds(0, 0, width, height);
        label.setFont(font);
    }
}