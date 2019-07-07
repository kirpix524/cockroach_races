package ru.surovcevnv.cockroachraces;

import ru.surovcevnv.cockroachraces.classes.*;
import ru.surovcevnv.cockroachraces.classes.racefield.RaceField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame implements ActionListener {
    public static final String DEFAULT_INPUT_NAME_LABEL_TEXT = "         Имя:";
    public static final int DEFAULT_VGAP_SIDE_PANEL = RaceField.DEFAULT_TRACK_WIDTH + (2 * RaceField.DEFAULT_TRACK_INDENT) - 20;
    public static final int DEFAULT_HGAP_SIDE_PANEL = 10;
    public static final int DEFAULT_COLS_SIDE_PANEL = 2;
    public static final String NAME_PREFIX_DELIMETER = "#";
    //region constants
    //
    private final Dimension PREFERRED_SIZE_BOTTOM_MENU = new Dimension(1, 60);
    private final String DEFAULT_CAPTION = "Тараканьи бега";
    private final String DEFAULT_INPUT_NAME_TIP = "Ввод имени для таракана ";
    private final String DEFAULT_TEXT_FIELD_NAME_PREFIX = "NameTextField";
    private final int DEFAULT_X = 50;
    private final int DEFAULT_Y = 100;
    private final int DEFAULT_WIDTH = 1200;
    private final int DEFAULT_HEIGHT = 500;
    private final String CAPTION_BUTTON_START_RACE = "Начать";
    private final String CAPTION_BUTTON_EXIT = "Выход";
    private final String CAPTION_BUTTON_RESTART_RACE = "Новый забег";
    private final String CAPTION_BUTTON_STOP_RACE = "Остановить забег";
    //endregion

    private JPanel bottomMenu;
    private JPanel sidePanel;

    //
    private RaceControlCenter raceControlCenter;
    //
    public MainWindow(RaceControlCenter raceControlCenter) {
        this.raceControlCenter = raceControlCenter;
        raceControlCenter.setMainWindow(this);
        setTitle(DEFAULT_CAPTION);
        setBounds(DEFAULT_X, DEFAULT_Y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        initMenusAndPanels();
        setVisible(true);

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
        sidePanel = getSidePanel();
        JPanel fieldPanel = new JPanel(new BorderLayout());
        fieldPanel.add(sidePanel, BorderLayout.WEST);
        fieldPanel.add(raceControlCenter.getRaceFieldGR(), BorderLayout.CENTER);
        JScrollPane scrollPane = new JScrollPane(fieldPanel);
        this.add(scrollPane, BorderLayout.CENTER);
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
            raceControlCenter.startRace();
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
            raceControlCenter.restartRace();
        });
        raceMenu.add(buttonRestartRace);
        //
        JButton buttonStopRace = new JButton(CAPTION_BUTTON_STOP_RACE);
        buttonStopRace.addActionListener(e -> {
            //
            raceControlCenter.stopRace();
            returnToMainMenu();
        });
        raceMenu.add(buttonStopRace);
        return raceMenu;
    }

    public void returnToMainMenu() {
        ((CardLayout) bottomMenu.getLayout()).show(bottomMenu, "mainMenu");
    }

    private JPanel getSidePanel() {
        JPanel sidePanel = new JPanel(new GridLayout(raceControlCenter.getNumberOfTracks() + 2, DEFAULT_COLS_SIDE_PANEL, DEFAULT_HGAP_SIDE_PANEL, DEFAULT_VGAP_SIDE_PANEL));
        sidePanel.add(new JLabel(""));
        sidePanel.add(new JLabel(""));
        for (int i = 0; i < raceControlCenter.getNumberOfTracks(); i++) {
            JTextField textField = new JTextField();
            textField.setToolTipText(DEFAULT_INPUT_NAME_TIP + (i + 1));
            textField.setName(DEFAULT_TEXT_FIELD_NAME_PREFIX + NAME_PREFIX_DELIMETER + i);
            textField.addActionListener(this);
            //
            sidePanel.add(new JLabel(DEFAULT_INPUT_NAME_LABEL_TEXT));
            sidePanel.add(textField);  //
        }
        sidePanel.add(new JLabel(""));
        sidePanel.add(new JLabel(""));
        return sidePanel;
    }
    //endregion

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JTextField) {
            JTextField textField = (JTextField) (e.getSource());
            String fieldName = textField.getName();
            String[] arr = fieldName.split(NAME_PREFIX_DELIMETER);
            if (arr[0].equals(DEFAULT_TEXT_FIELD_NAME_PREFIX)) {
                raceControlCenter.renameCockroach(Integer.parseInt(arr[1]), textField.getText());
                textField.setText("");
            }
        }
    }


}
