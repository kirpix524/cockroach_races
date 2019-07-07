package ru.surovcevnv.cockroachraces;

import ru.surovcevnv.cockroachraces.classes.RaceControlCenter;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
//        int tracks = Integer.parseInt(args[0]);
        int tracks = 5;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                RaceControlCenter cc= new RaceControlCenter(tracks);
                MainWindow w = new MainWindow(cc);
            }
        });
    }
}
