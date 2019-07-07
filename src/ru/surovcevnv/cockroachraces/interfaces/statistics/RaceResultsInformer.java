package ru.surovcevnv.cockroachraces.interfaces.statistics;

import ru.surovcevnv.cockroachraces.classes.cockroach.Cockroach;
import ru.surovcevnv.cockroachraces.classes.statistics.Race;

public interface RaceResultsInformer {
    void showResultTable(Race race, Cockroach[] cockroaches);
    void informRaceWasStopped(Race race);
    void informRaceWasStarted(Race race);
}
