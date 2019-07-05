package ru.surovcevnv.cockroachraces;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
//        int tracks = Integer.parseInt(args[0]);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainWindow w = new MainWindow(5);
            }
        });
    }
}
