package ru.surovcevnv.cockroachraces;

import ru.surovcevnv.cockroachraces.classes.RaceControlCenter;

import javax.swing.*;

public class Main{
    public static void main(String[] args) {
        int tracks = 5;
//        if (args.length<1) {
//            System.out.println("Вы не ввели число дорожек! Нужно ввести целое положительное число");
//            return;
//        }
//        try {
//            tracks = Integer.parseInt(args[0]);
//        } catch(NumberFormatException e) {
//            System.out.println("Ввели некорректное число дорожек! Нужно ввести целое положительное число");
//            return;
//        }
//        if (tracks<=0) {
//            System.out.println("Ввели некорректное число дорожек! Нужно ввести целое положительное число");
//            return;
//        }
        RaceControlCenter raceControlCenter = new RaceControlCenter(tracks);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Thread.setDefaultUncaughtExceptionHandler(raceControlCenter);
                MainWindow w = new MainWindow(raceControlCenter);
            }
        });
    }
}
