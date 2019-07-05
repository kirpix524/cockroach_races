package ru.surovcevnv.cockroachraces;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
    private final Dimension PREFERRED_SIZE_BOTTOM_MENU = new Dimension(1, 60);
    private final String DEFAULT_CAPTION = "Тараканьи бега";
    private final int DEFAULT_X = 100;
    private final int DEFAULT_Y = 100;
    private final int DEFAULT_WIDTH = 1000;
    private final int DEFAULT_HEIGHT = 400;
    private final String CAPTION_BUTTON_START_RACE = "Начать";
    private final String CAPTION_BUTTON_EXIT = "Выход";
    private final String CAPTION_BUTTON_RESTART_RACE = "Новый забег";
    private final String CAPTION_BUTTON_STOP_RACE = "Остановить забег";
    private final int numberOfTracks;

    private JPanel bottomMenu;

    public Window(int numberOfTracks) {
        this.numberOfTracks = numberOfTracks;
        setTitle(DEFAULT_CAPTION);
        setBounds(DEFAULT_X, DEFAULT_Y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        //
        initMenusAndPanels();
    }

    private void initMenusAndPanels() {
        bottomMenu = getBottomMenu();
        this.add(bottomMenu, BorderLayout.SOUTH);
        //
        JPanel mainMenu = getMainMenu();
        bottomMenu.add(mainMenu, "mainMenu");
        //
        JPanel raceMenu = getRaceMenu();
        bottomMenu.add(raceMenu, "raceMenu");
        //
        ((CardLayout) bottomMenu.getLayout()).show(bottomMenu, "mainMenu");
        //

    }

    private JPanel getBottomMenu() {
        JPanel bottom = new JPanel(new CardLayout());
        bottom.setPreferredSize(PREFERRED_SIZE_BOTTOM_MENU); //
        return bottom;
    }

    private JPanel getMainMenu() {
        JPanel mainMenu = new JPanel(new GridLayout());
        JButton buttonStartRace = new JButton(CAPTION_BUTTON_START_RACE);
        buttonStartRace.addActionListener(e -> {
            startRace();
            ((CardLayout) bottomMenu.getLayout()).show(bottomMenu, "raceMenu");
        });
        mainMenu.add(buttonStartRace);
        //
        JButton buttonExit = new JButton(CAPTION_BUTTON_EXIT);
        buttonExit.addActionListener(e -> {
            System.exit(0);
        });
        mainMenu.add(buttonExit);
        //
        return mainMenu;
    }

    private JPanel getRaceMenu() {
        JPanel raceMenu = new JPanel(new GridLayout());
        //
        JButton buttonRestartRace = new JButton(CAPTION_BUTTON_RESTART_RACE);
        buttonRestartRace.addActionListener(e -> {
            //
            restartRace();
        });
        raceMenu.add(buttonRestartRace);
        //
        JButton buttonStopRace = new JButton(CAPTION_BUTTON_STOP_RACE);
        buttonStopRace.addActionListener(e -> {
            //
            stopRace();
            ((CardLayout) bottomMenu.getLayout()).show(bottomMenu, "mainMenu");
        });
        raceMenu.add(buttonStopRace);
        return raceMenu;
    }

    private void startRace() {

    }

    private void restartRace() {

    }

    private void stopRace() {

    }

}
