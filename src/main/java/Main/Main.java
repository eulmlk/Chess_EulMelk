package Main;

import javax.swing.SwingUtilities;

import ChessEulMelk.UserInterfaces.MainFrame;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new MainFrame();
            }

        });
    }
}