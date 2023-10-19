package ChessEulMelk.ImageHandler;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ButtonIconLoader {
    private static final String[] ICON_PATHS = { "first.png", "prev.png", "undo.png",
            "next.png", "last.png", "resign.png" };

    private static final String ICON_LOCATION = "assets" + File.separator + "Icons" + File.separator;

    public static ImageIcon[] loadControlIcons() {
        ImageIcon[] icons = new ImageIcon[6];

        try {
            for (int i = 0; i < 6; ++i) {
                BufferedImage image = ImageIO.read(new File(ICON_LOCATION + ICON_PATHS[i]));

                icons[i] = new ImageIcon(image);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return icons;
    }
}
