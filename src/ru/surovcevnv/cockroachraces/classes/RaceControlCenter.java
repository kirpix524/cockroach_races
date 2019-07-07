package ru.surovcevnv.cockroachraces.classes;

import ru.surovcevnv.cockroachraces.MainWindow;
import ru.surovcevnv.cockroachraces.classes.cockroach.Cockroach;
import ru.surovcevnv.cockroachraces.classes.statistics.ConsoleResultInformer;
import ru.surovcevnv.cockroachraces.classes.statistics.RaceJournal;
import ru.surovcevnv.cockroachraces.classes.statistics.RaceNode;
import ru.surovcevnv.cockroachraces.interfaces.statistics.RaceResultsInformer;

public class RaceControlCenter {
    private final int numberOfTracks;
    private Cockroach[] cockroaches;
    private RaceJournal raceJournal;
    private RaceResultsInformer resultsInformer;
    private MainWindow mainWindow;

    public RaceControlCenter(int numberOfTracks) {
        this.numberOfTracks = numberOfTracks;
        initCockroaches();
        raceJournal = new RaceJournal();
        resultsInformer = new ConsoleResultInformer();
    }

    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    public int getNumberOfTracks() {
        return numberOfTracks;
    }

    private void initCockroaches() {
        cockroaches = new Cockroach[numberOfTracks];
        for (int i = 0; i < numberOfTracks; i++) {
            cockroaches[i] = new Cockroach(this, i, Cockroach.DEFAULT_WIDTH / 2, RaceField.TRACK_START_Y + (RaceField.getFullTrackWidth() / 2) + (i * RaceField.getFullTrackWidth()));
        }
    }

    public void updateRaceNode(int id, long newTime, int newPosX) {
        if (raceJournal.getCurrentRace()!=null) {
            raceJournal.getCurrentRace().setNewPosXAndTime(id, newTime, newPosX);
        } else {
            //todo
        }
    }

    public void startRace() {
        raceJournal.startNewRace();
        for (int i = 0; i < cockroaches.length; i++) {
            cockroaches[i].returnToStart();
            raceJournal.getCurrentRace().addRaceNode(new RaceNode(cockroaches[i].getID(),cockroaches[i].getName(), cockroaches[i].getTime(), cockroaches[i].getPosX(), raceJournal.getCurrentRace().getStartTime()));
        }
        for (int i = 0; i < cockroaches.length; i++) {
            cockroaches[i].startRace();
        }
        resultsInformer.informRaceWasStarted(raceJournal.getCurrentRace());
    }

    public void restartRace() {
        stopRace();
        startRace();
    }

    public void stopRace() {
        if (raceJournal.getCurrentRace() !=null) {
            raceJournal.getCurrentRace().setFinished();
            resultsInformer.informRaceWasStopped(raceJournal.getCurrentRace());
        }
        for (int i = 0; i < cockroaches.length; i++) {
            cockroaches[i].stopRace();
        }
    }

    public String getStatInfo() {
        String statInfo;
        if (raceJournal.getCurrentRace() !=null) {
            RaceNode leader = raceJournal.getCurrentRace().getLeader();
            statInfo = "Забег номер " + raceJournal.getCurrentRace().getNum();
            boolean isFinished = raceJournal.getCurrentRace().isFinished();
            boolean isEveryoneFinished = raceJournal.getCurrentRace().isEveryoneFinished();
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
                    statInfo += ", победил " + leader.getName();
                } else if (isFinished && (!isEveryoneFinished)) {
                    statInfo += ", последний лидировал " + leader.getName();
                }else {
                    statInfo += ", лидирует " + leader.getName();
                }
            }
        } else {
            statInfo = "Еще ни одного забега не состоялось";
        }
        return statInfo;
    }

    public void sayFinishedAndUpdateRace(int id) {
        if (raceJournal.getCurrentRace()!=null) {
            raceJournal.getCurrentRace().setNodeFinished(id);
            if (raceJournal.getCurrentRace().isFinished()) {
                resultsInformer.showResultTable(raceJournal.getCurrentRace(), cockroaches);
                mainWindow.returnToMainMenu();
            }
        }
    }

    public Cockroach[] getCockroaches() {
        return cockroaches;
    }

    public int getFinishX() {
        return RaceField.getFinishX();
    }

    public void renameCockroach(int id, String name) {
        if ((id >= 0) && (id < cockroaches.length)) {
            cockroaches[id].setName(name);
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
