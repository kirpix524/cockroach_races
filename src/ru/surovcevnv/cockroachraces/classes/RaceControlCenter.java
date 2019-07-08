package ru.surovcevnv.cockroachraces.classes;

import ru.surovcevnv.cockroachraces.MainWindow;
import ru.surovcevnv.cockroachraces.classes.cockroach.Cockroach;
import ru.surovcevnv.cockroachraces.classes.exceptions.ResourceNotInitialisedException;
import ru.surovcevnv.cockroachraces.classes.racefield.RaceField;
import ru.surovcevnv.cockroachraces.classes.racefield.RaceFieldGR;
import ru.surovcevnv.cockroachraces.classes.statistics.ConsoleResultInformer;
import ru.surovcevnv.cockroachraces.classes.statistics.RaceJournal;
import ru.surovcevnv.cockroachraces.classes.statistics.RaceNode;
import ru.surovcevnv.cockroachraces.interfaces.statistics.RaceResultsInformer;

import java.awt.*;
import java.util.logging.Logger;

public class RaceControlCenter implements Thread.UncaughtExceptionHandler {
    private final int numberOfTracks;
    private Cockroach[] cockroaches;
    private RaceJournal raceJournal;
    private RaceField raceField;
    private RaceResultsInformer resultsInformer;
    private MainWindow mainWindow;

    public RaceControlCenter(int numberOfTracks) {
        if (numberOfTracks<1) {
            throw new IllegalArgumentException("numberOfTracks must be >0");
        }
        this.numberOfTracks = numberOfTracks;
        raceJournal = new RaceJournal();
        resultsInformer = new ConsoleResultInformer();
        raceField = new RaceField(this);
        initCockroaches();
    }

    public void setMainWindow(MainWindow mainWindow) {
        if (mainWindow == null) {
            throw new IllegalArgumentException("main window can't be null");
        }
        this.mainWindow = mainWindow;
    }

    public int getNumberOfTracks() {
        return numberOfTracks;
    }

    private void initCockroaches() {
        cockroaches = new Cockroach[numberOfTracks];
        for (int i = 0; i < numberOfTracks; i++) {
            cockroaches[i] = new Cockroach(this, i, Cockroach.DEFAULT_WIDTH / 2, raceField.getTrackStartY() + (raceField.getFullTrackWidth() / 2) + (i * raceField.getFullTrackWidth()));
        }
    }

    public void updateRaceNode(int id, long newTime, int newPosX) {
        checkCockroachID(id);
        if (raceJournal.getCurrentRace()==null) {
            throw new ResourceNotInitialisedException("current race is null");
        } else {
            raceJournal.getCurrentRace().setNewPosXAndTime(id, newTime, newPosX);
        }
    }

    public void startRace() {
        checkRaceJournal();
        checkCockroaches();
        checkResultsInformer();
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
        checkRaceJournal();
        checkCockroaches();
        checkResultsInformer();
        if (raceJournal.getCurrentRace() !=null) {
            raceJournal.getCurrentRace().setFinished();
            resultsInformer.informRaceWasStopped(raceJournal.getCurrentRace());
        }
        for (int i = 0; i < cockroaches.length; i++) {
            cockroaches[i].stopRace();
        }
    }

    public String getStatInfo() {
        checkRaceJournal();
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
        checkCockroachID(id);
        checkRaceJournal();
        checkResultsInformer();
        checkMainWindow();
        if (raceJournal.getCurrentRace()!=null) {
            raceJournal.getCurrentRace().setNodeFinished(id);
            if (raceJournal.getCurrentRace().isFinished()) {
                resultsInformer.showResultTable(raceJournal.getCurrentRace(), cockroaches);
                mainWindow.returnToMainMenu();
            }
        }
    }

    public Cockroach[] getCockroaches() {
        checkCockroaches();
        return cockroaches;
    }

    public int getFinishX() {
        checkRaceField();
        return raceField.getFinishX();
    }

    public void renameCockroach(int id, String name) {
        checkCockroachID(id);
        checkCockroaches();
        checkRaceJournal();
        cockroaches[id].setName(name);
        if (raceJournal.getCurrentRace()!=null) {
            raceJournal.getCurrentRace().renameCockroach(id, name);
        }
    }

    public void kickCockroachAtCoordinates(int x, int y) {
        checkCockroaches();
        for (int i =0; i<cockroaches.length; i++) {
            if (cockroaches[i].isMyCoord(x,y)) {
                cockroaches[i].moveCockroach(true);
                return;
            }
        }
    }

    public RaceFieldGR getRaceFieldGR() {
        checkRaceField();
        return raceField.getRaceFieldGR();
    }


    public void checkCockroachID(int id) {
        if ((id<0)||id>=cockroaches.length) {
            throw new IllegalArgumentException("cockroachID is out of bounds");
        }
    }

    private void checkRaceJournal() {
        if (raceJournal==null) {
            throw new ResourceNotInitialisedException("raceJournal is null");
        }
    }

    private void checkCockroaches() {
        if (cockroaches==null) {
            throw new ResourceNotInitialisedException("Cockroach array is null");
        }
        if (cockroaches.length==0) {
            throw new ResourceNotInitialisedException("Cockroach array is empty");
        }
    }

    private void checkResultsInformer() {
        if (resultsInformer==null) {
            throw new ResourceNotInitialisedException("resultsInformer is null");
        }
    }

    private void checkMainWindow() {
        if (mainWindow==null) {
            throw new ResourceNotInitialisedException("mainWindow is null");
        }
    }

    private void checkRaceField() {
        if (raceField==null) {
            throw new ResourceNotInitialisedException("raceField is null");
        }
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        e.printStackTrace();
    }
}
