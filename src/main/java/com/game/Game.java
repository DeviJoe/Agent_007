package com.game;

import com.gameFrame.GameFrame;
import com.utils.SwingUtils;

import java.util.Locale;

/**
 * Agent007
 * http://igroflot.ru/flash_games_31.htm
 * @author Devijoe (https://github.com/DeviJoe)
 */
public class Game {

    public static void main(String[] args) {

        //SwingUtils.setLookAndFeelByName("Windows");
        Locale.setDefault(Locale.ROOT);
        //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        SwingUtils.setDefaultFont("Microsoft Sans Serif", 18);

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GameFrame().setVisible(true);
            }
        });

    }
}
