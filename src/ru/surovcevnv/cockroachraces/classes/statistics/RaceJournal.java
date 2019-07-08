package ru.surovcevnv.cockroachraces.classes.statistics;

import ru.surovcevnv.cockroachraces.classes.exceptions.ResourceNotInitialisedException;

import java.util.ArrayList;

public class RaceJournal {
    private ArrayList<Race> raceJournal;

    public RaceJournal() {
        raceJournal = new ArrayList<>();
    }

    public void startNewRace() {
        checkRaceJournal();
        Race newRace = new Race(raceJournal.size());
        raceJournal.add(newRace);
    }

    public Race getCurrentRace() {
        checkRaceJournal();
        if (raceJournal.size()==0) return null;
        return raceJournal.get(raceJournal.size()-1);
    }

    public void checkRaceJournal() {
        if (raceJournal==null) {
            throw new ResourceNotInitialisedException("raceJournal is null");
        }
    }
}
