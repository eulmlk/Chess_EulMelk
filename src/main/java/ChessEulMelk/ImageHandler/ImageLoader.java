package ChessEulMelk.ImageHandler;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import ChessEulMelk.Logics.Piece;

public class ImageLoader {

    public enum PieceSource {
        CREATIVE_COMMONS, CHESS_PIECE_MASTERS
    }

    private static final String[] PIECE_IMGS = {
            "whiteKing.png", "whiteQueen.png", "whiteRook.png", "whiteBishop.png", "whiteKnight.png", "whitePawn.png",
            "blackKing.png", "blackQueen.png", "blackRook.png", "blackBishop.png", "blackKnight.png", "blackPawn.png"
    };

    private static final String[] PIECE_DIRS = {
            "CreativeCommonsClassic", "ChessPieceMasterClassic"
    };

    private static String getPiecePath(PieceSource source, Piece piece) {
        return "assets" + File.separator + PIECE_DIRS[source.ordinal()] + File.separator
                + PIECE_IMGS[piece.ordinal() - 1];
    }

    public static BufferedImage[] loadImages(PieceSource source) {
        int size = Piece.values().length;
        BufferedImage[] images = new BufferedImage[size - 1];

        for (int i = 1; i < size; i++) {
            String imagePath = getPiecePath(source, Piece.values()[i]);

            images[i - 1] = loadImage(imagePath);
        }

        return images;
    }

    private static BufferedImage loadImage(String imagePath) {
        BufferedImage loadedImage = null;

        try {
            loadedImage = ImageIO.read(new File(imagePath));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return loadedImage;
    }

    public static ImageIcon[] loadImageIcons(PieceSource source) {
        int size = 8;
        ImageIcon[] imageIcons = new ImageIcon[size];

        String[] imagePaths = {
                getPiecePath(source, Piece.WHITE_QUEEN),
                getPiecePath(source, Piece.WHITE_ROOK),
                getPiecePath(source, Piece.WHITE_BISHOP),
                getPiecePath(source, Piece.WHITE_KNIGHT),
                getPiecePath(source, Piece.BLACK_QUEEN),
                getPiecePath(source, Piece.BLACK_ROOK),
                getPiecePath(source, Piece.BLACK_BISHOP),
                getPiecePath(source, Piece.BLACK_KNIGHT)
        };

        for (int i = 0; i < size; ++i) {
            ImageIcon imageIcon = new ImageIcon(imagePaths[i]);
            resizeImage(imageIcon, 100, 100);
            imageIcons[i] = imageIcon;
        }

        return imageIcons;
    }

    private static void resizeImage(ImageIcon imageIcon, int width, int height) {
        Image originalImage = imageIcon.getImage();

        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();

        g2d.drawImage(originalImage, 0, 0, width, height, null);
        g2d.dispose();

        imageIcon.setImage(resizedImage);
    }
}