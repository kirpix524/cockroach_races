package ru.surovcevnv.cockroachraces.classes.statistics;

import ru.surovcevnv.cockroachraces.classes.cockroach.Cockroach;
import ru.surovcevnv.cockroachraces.interfaces.statistics.RaceResultsInformer;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConsoleResultInformer implements RaceResultsInformer {
    private final int NAME_COLOUMN_SIZE = 40;
    private final int PLACE_COLOUMN_SIZE = 10;
    private final int TIME_COLOUMN_SIZE = 20;

    @Override
    public void showResultTable(Race race, Cockroach[] cockroaches) {
        RaceNode[] races =race.getRaceNodesAsArray();
        if (races.length<1) {
            //todo
            return;
        }
        System.out.println("Забег номер "+race.getNum()+" завершен в "+(new SimpleDateFormat("HH:mm:ss").format(new Date()))+ "! Результаты в таблице");
        String format = "|%-"+PLACE_COLOUMN_SIZE+"s|%-"+NAME_COLOUMN_SIZE+"s|%-"+TIME_COLOUMN_SIZE+"s|";
        System.out.println("--------------------------------------------------------------------------");
        System.out.println(String.format(format,"Место","Имя","Время"));
        System.out.println("--------------------------------------------------------------------------");

        for(int i=0; i<races.length; i++) {
            System.out.println(String.format(format,""+(i+1),races[i].getName(),""+races[i].getTimeInWay()));
        }
        System.out.println("--------------------------------------------------------------------------");
    }

    @Override
    public void informRaceWasStopped(Race race) {
        System.out.println("Забег номер "+race.getNum()+" был прерван в "+(new SimpleDateFormat("HH:mm:ss").format(new Date()))+ "!");
    }

    @Override
    public void informRaceWasStarted(Race race) {
        System.out.println("Забег номер "+race.getNum()+" был начат в "+(new SimpleDateFormat("HH:mm:ss").format(new Date()))+ "!");
    }
}
