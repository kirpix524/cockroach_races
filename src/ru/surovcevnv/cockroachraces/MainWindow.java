package ru.surovcevnv.cockroachraces;

import ru.surovcevnv.cockroachraces.classes.RaceField;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    public static final String DEFAULT_INPUT_NAME_LABEL_TEXT = "         Имя:";
    public static final int DEFAULT_VGAP_SIDE_PANEL = 100;
    public static final int DEFAULT_HGAP_SIDE_PANEL = 10;
    public static final int DEFAULT_COLS_SIDE_PANEL = 2;
    private final String COCKROACH_ID_PREFIX = "cockroach";
    private final int numberOfTracks;
    //region constants
    //
    private final Dimension PREFERRED_SIZE_BOTTOM_MENU = new Dimension(1, 60);
    private final String DEFAULT_CAPTION = "Тараканьи бега";
    private final int DEFAULT_X = 100;
    private final int DEFAULT_Y = 100;
    private final int DEFAULT_WIDTH = 1300;
    private final int DEFAULT_HEIGHT = 500;
    private final String CAPTION_BUTTON_START_RACE = "Начать";
    private final String CAPTION_BUTTON_EXIT = "Выход";
    private final String CAPTION_BUTTON_RESTART_RACE = "Новый забег";
    private final String CAPTION_BUTTON_STOP_RACE = "Остановить забег";
    //endregion

    private String[] cockroachID;
    private JPanel bottomMenu;
    private JPanel sidePanel;
    private RaceField raceField;

    public MainWindow(int numberOfTracks) {
        this.numberOfTracks = numberOfTracks;
        setTitle(DEFAULT_CAPTION);
        setBounds(DEFAULT_X, DEFAULT_Y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        //
        initCockroachID();
        initMenusAndPanels();
    }

    private void initCockroachID() {
        cockroachID = new String[numberOfTracks];
        for (int i=0; i< numberOfTracks; i++) {
            cockroachID[i]= COCKROACH_ID_PREFIX +i;
        }
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
        raceField = new RaceField(this);
        sidePanel = getSidePanel();
        JPanel fieldPanel = new JPanel(new BorderLayout());
        fieldPanel.add(sidePanel, BorderLayout.WEST);
        fieldPanel.add(raceField, BorderLayout.CENTER);
        JScrollPane scrollPane = new JScrollPane(fieldPanel);
        this.add(scrollPane,BorderLayout.CENTER);
    }

//region menus
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
    //endregion

    private JPanel getSidePanel() {
        JPanel sidePanel = new JPanel(new GridLayout(numberOfTracks+2, DEFAULT_COLS_SIDE_PANEL, DEFAULT_HGAP_SIDE_PANEL, DEFAULT_VGAP_SIDE_PANEL));
        sidePanel.add(new JLabel(""));
        sidePanel.add(new JLabel(""));
        for (int i=0; i<numberOfTracks; i++) {
            sidePanel.add(new JLabel(DEFAULT_INPUT_NAME_LABEL_TEXT));
            sidePanel.add(new JTextField());  //
        }
        sidePanel.add(new JLabel(""));
        sidePanel.add(new JLabel(""));
        return sidePanel;
    }

    private void startRace() {

    }

    private void restartRace() {

    }

    private void stopRace() {

    }

    public int getNumberOfTracks() {
        return numberOfTracks;
    }
}
