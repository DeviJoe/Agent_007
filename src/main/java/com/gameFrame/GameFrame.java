package com.gameFrame;

import com.game.GameLogic;
import com.utils.JTableUtils;
import com.utils.SwingUtils;

import javax.swing.*;

public class GameFrame extends JFrame {
    private JPanel panelMain;
    private JTable gameField;
    private JButton resetButton;
    private JMenuBar menuBarMain;
    private JMenu menuLookAndFeel;


    public GameFrame() throws Exception {

        /** Создание новой игры */
        GameLogic game = new GameLogic();

        /** Отображение формы */
        this.setTitle("Agent 007");
        this.setContentPane(panelMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();

        /** Отображение меню-бара */
        menuBarMain = new JMenuBar();
        setJMenuBar(menuBarMain);

        /** Отображение книпки "Вид" */
        menuLookAndFeel = new JMenu();
        menuLookAndFeel.setText("Вид");
        menuBarMain.add(menuLookAndFeel);
        SwingUtils.initLookAndFeelMenu(menuLookAndFeel);

        /** Установка размеров окна */
        SwingUtils.setFixedSize(this, 600, 600);

        /** Инициализация поля */


    }


}
