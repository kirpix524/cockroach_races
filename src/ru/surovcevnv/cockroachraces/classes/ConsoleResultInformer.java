package ru.surovcevnv.cockroachraces.classes;

import ru.surovcevnv.cockroachraces.interfaces.RaceResultsInformer;

import java.util.ArrayList;

public class ConsoleResultInformer implements RaceResultsInformer {
    private final int NAME_COLOUMN_SIZE = 40;
    private final int PLACE_COLOUMN_SIZE = 10;
    private final int TIME_COLOUMN_SIZE = 20;



    @Override
    public void showResultTable(Race race, Cockroach[] cockroaches) {
        ArrayList<RaceNode> races =race.getRaces();
        if (races.size()<1) {
            //todo
            return;
        }
        System.out.println("Забег номер "+race.getNum()+" завершен! Результаты в таблице");
        String format = "|%-"+PLACE_COLOUMN_SIZE+"s|%-"+NAME_COLOUMN_SIZE+"s|%-"+TIME_COLOUMN_SIZE+"s|";
        System.out.println("--------------------------------------------------------------------------");
        System.out.println(String.format(format,"Место","Имя","Время"));
        System.out.println("--------------------------------------------------------------------------");

        for(int i=0; i<races.size(); i++) {
            System.out.println(String.format(format,""+(i+1),races.get(i).getName(),""+races.get(i).getTimeInWay(race.getStartTime())));
        }
        System.out.println("--------------------------------------------------------------------------");
    }

    @Override
    public void informRaceWasStopped(Race race) {
        System.out.println("Забег номер "+race.getNum()+" был прерван!");
    }
}
