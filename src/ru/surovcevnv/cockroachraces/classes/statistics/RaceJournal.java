package ru.surovcevnv.cockroachraces.classes.statistics;

import java.util.ArrayList;

public class RaceJournal {
    private ArrayList<Race> raceJournal;

    public RaceJournal() {
        raceJournal = new ArrayList<>();
    }

    public void startNewRace() {
        Race newRace = new Race(raceJournal.size());
        raceJournal.add(newRace);
    }

    public Race getCurrentRace() {
        if (raceJournal==null) return null;
        if (raceJournal.size()==0) return null;
        return raceJournal.get(raceJournal.size()-1);
    }
}
