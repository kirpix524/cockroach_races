package ru.surovcevnv.cockroachraces;

import ru.surovcevnv.cockroachraces.classes.*;
import ru.surovcevnv.cockroachraces.interfaces.RaceResultsInformer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainWindow extends JFrame implements ActionListener {
    public static final String DEFAULT_INPUT_NAME_LABEL_TEXT = "         Имя:";
    public static final int DEFAULT_VGAP_SIDE_PANEL = RaceField.TRACK_WIDTH + (2 * RaceField.TRACK_INDENT) - 20;
    public static final int DEFAULT_HGAP_SIDE_PANEL = 10;
    public static final int DEFAULT_COLS_SIDE_PANEL = 2;
    private final int numberOfTracks;
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

    private Cockroach[] cockroaches;
    private JPanel bottomMenu;
    private JPanel sidePanel;
    private RaceField raceField;
    //
    private ArrayList<Race> raceJournal;
    private RaceResultsInformer resultsInformer;
    //
    public MainWindow(int numberOfTracks) {
        this.numberOfTracks = numberOfTracks;
        setTitle(DEFAULT_CAPTION);
        setBounds(DEFAULT_X, DEFAULT_Y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        //
        initCockroaches();
        initMenusAndPanels();
        initRaceJournal();
        //
        resultsInformer = new ConsoleResultInformer();
    }

    private void initRaceJournal() {
        raceJournal = new ArrayList<>();
    }

    private void initCockroaches() {
        cockroaches = new Cockroach[numberOfTracks];
        for (int i = 0; i < numberOfTracks; i++) {
            cockroaches[i] = new Cockroach(this, i, Cockroach.DEFAULT_WIDTH / 2, RaceField.TRACK_START_Y + (RaceField.getFullTrackWidth() / 2) + (i * RaceField.getFullTrackWidth()));
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
            returnToMainMenu();
        });
        raceMenu.add(buttonStopRace);
        return raceMenu;
    }
    //endregion

    private void returnToMainMenu() {
        ((CardLayout) bottomMenu.getLayout()).show(bottomMenu, "mainMenu");
    }

    private JPanel getSidePanel() {
        JPanel sidePanel = new JPanel(new GridLayout(numberOfTracks + 2, DEFAULT_COLS_SIDE_PANEL, DEFAULT_HGAP_SIDE_PANEL, DEFAULT_VGAP_SIDE_PANEL));
        sidePanel.add(new JLabel(""));
        sidePanel.add(new JLabel(""));
        for (int i = 0; i < numberOfTracks; i++) {
            JTextField textField = new JTextField();
            textField.setToolTipText(DEFAULT_INPUT_NAME_TIP + (i + 1));
            textField.setName(DEFAULT_TEXT_FIELD_NAME_PREFIX + "#" + i);
            textField.addActionListener(this);
            //
            sidePanel.add(new JLabel(DEFAULT_INPUT_NAME_LABEL_TEXT));
            sidePanel.add(textField);  //
        }
        sidePanel.add(new JLabel(""));
        sidePanel.add(new JLabel(""));
        return sidePanel;
    }

    private void startRace() {
        Race newRace = new Race(raceJournal.size());
        for (int i = 0; i < cockroaches.length; i++) {
            cockroaches[i].returnToStart();
            newRace.addRaceNode(new RaceNode(cockroaches[i].getID(), cockroaches[i].getTime(), cockroaches[i].getPosX()));
        }
        raceJournal.add(newRace);
        for (int i = 0; i < cockroaches.length; i++) {
            cockroaches[i].startRace();
        }
    }

    private void restartRace() {
        stopRace();
        startRace();
    }

    private void stopRace() {
        if (raceJournal.size() > 0) {
            Race race =raceJournal.get(raceJournal.size() - 1);
            race.setFinished();
            resultsInformer.informRaceWasStopped(race);
        }
        for (int i = 0; i < cockroaches.length; i++) {
            cockroaches[i].stopRace();
        }
    }

    public String getStatInfo() {
        String statInfo = "";
        if (raceJournal.size() > 0) {
            RaceNode leader = raceJournal.get(raceJournal.size() - 1).getLeader();
            statInfo = "Забег номер " + raceJournal.size();
            boolean isFinished = raceJournal.get(raceJournal.size() - 1).isFinished();
            boolean isEveryoneFinished = raceJournal.get(raceJournal.size() - 1).isEveryoneFinished();
            if (isFinished) {
                if (!isEveryoneFinished) {
                    statInfo += " завершен досрочно";
                } else {
                    statInfo += " завершен";
                }

            } else {
                statInfo += " продолжается";
            }
            if (leader != null) {
                if (isFinished && isEveryoneFinished) {
                    statInfo += ", победил " + cockroaches[leader.getID()].getName();
                } else if (isFinished && (!isEveryoneFinished)) {
                    statInfo += ", последний лидировал " + cockroaches[leader.getID()].getName();
                }else {
                    statInfo += ", лидирует " + cockroaches[leader.getID()].getName();
                }
            }
        } else {
            statInfo = "Еще ни одного забега не состоялось";
        }
        return statInfo;
    }

    public int getNumberOfTracks() {
        return numberOfTracks;
    }

    public Cockroach[] getCockroaches() {
        return cockroaches;
    }

    public int getFinishX() {
        return RaceField.getFinishX();
    }

    public void updateRaceNode(int id, long newTime, int newPosX) {
        if (raceJournal.size()>0) {
            raceJournal.get(raceJournal.size()-1).setNewPosXAndTime(id, newTime, newPosX);
        }
    }

    public void sayFinishedAndUpdateRace(int id) {
        if (raceJournal.size()>0) {
            Race race =raceJournal.get(raceJournal.size()-1);
            race.setNodeFinished(id);
            if (race.isFinished()) {
                resultsInformer.showResultTable(race, cockroaches);
                returnToMainMenu();
            }
        }
    }

    public long getStartRaceTime() {
        if (raceJournal.size() > 0) {
            return raceJournal.get(raceJournal.size() - 1).getStartTime();
        }
        return 0;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JTextField) {
            JTextField textField = (JTextField) (e.getSource());
            String fieldName = textField.getName();
            String[] arr = fieldName.split("#");
            if (arr[0].equals(DEFAULT_TEXT_FIELD_NAME_PREFIX)) {
                int fieldID = Integer.parseInt(arr[1]);
                if ((fieldID >= 0) && (fieldID < cockroaches.length)) {
                    cockroaches[fieldID].setName(textField.getText());
                    textField.setText("");
                }
            }
        }
    }

    public void kickCockroachAtCoordinates(int x, int y) {
        for (int i =0; i<cockroaches.length; i++) {
            if (cockroaches[i].isMyCoord(x,y)) {
                cockroaches[i].moveCockroach(true);
                return;
            }
        }
    }
}
