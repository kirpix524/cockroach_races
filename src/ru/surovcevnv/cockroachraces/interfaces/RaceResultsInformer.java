package ru.surovcevnv.cockroachraces.interfaces;

import ru.surovcevnv.cockroachraces.classes.cockroach.Cockroach;
import ru.surovcevnv.cockroachraces.classes.Race;

public interface RaceResultsInformer {
    void showResultTable(Race race, Cockroach[] cockroaches);
    void informRaceWasStopped(Race race);
    void informRaceWasStarted(Race race);
}
