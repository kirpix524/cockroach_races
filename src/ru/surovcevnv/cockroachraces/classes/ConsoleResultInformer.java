package ru.surovcevnv.cockroachraces.classes;

import ru.surovcevnv.cockroachraces.interfaces.RaceResultsInformer;

public class ConsoleResultInformer implements RaceResultsInformer {
    @Override
    public void showResultTable(Race race, Cockroach[] cockroaches) {
        System.out.println("Забег номер "+race.getNum()+" завершен! Победитель "+cockroaches[race.getLeader().getID()].getName());
    }

    @Override
    public void informRaceWasStopped(Race race) {
        System.out.println("Забег номер "+race.getNum()+" был прерван!");
    }
}
