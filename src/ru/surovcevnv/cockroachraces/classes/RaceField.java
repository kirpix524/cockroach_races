package ru.surovcevnv.cockroachraces.classes;

import ru.surovcevnv.cockroachraces.MainWindow;

import javax.swing.*;
import java.awt.*;

public class RaceField extends JPanel {
    private final Color DEFAULT_BORDER_COLOR = new Color(0, 0, 0);
    private final float DEFAULT_BORDER_WIDTH = 5.0f;
    private MainWindow mainWindow;
    public static final int TRACK_WIDTH = 100;
    public static final int TRACK_INDENT = 10;
    private final int TRACK_LENGTH = 900;
    private final int TRACK_START_X = 100;
    public static final int TRACK_START_Y = 80;
    private final int REFRESH_TIME = 50;

    private class EventGenerator extends Thread {
        JPanel me;
        int refreshTime;
        EventGenerator(JPanel me, int refreshTime) {
            this.me = me;
            this.refreshTime = refreshTime;
            start();
        }

        @Override
        public void run() {
            while (!isInterrupted()) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        me.repaint();
                    }
                });
                try {
                    sleep(refreshTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public RaceField(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        new EventGenerator(this, REFRESH_TIME);
    }

    public static int getFullTrackWidth() {
        return (TRACK_WIDTH + 2 * TRACK_INDENT);
    }

    private void drawField(Graphics g) {
        //
        Graphics2D g2d = (Graphics2D) g;
        Color oldColor = g2d.getColor();
        Stroke oldStroke = g2d.getStroke();
        //
        g2d.setColor(DEFAULT_BORDER_COLOR);
        g2d.setStroke(new BasicStroke(DEFAULT_BORDER_WIDTH));
        int numberOfTracks = mainWindow.getNumberOfTracks();
        for (int i = 0; i < numberOfTracks; i++) {
            int x1 = TRACK_START_X;
            int y1 = TRACK_START_Y + (i * getFullTrackWidth() + TRACK_INDENT);
            int x2 = x1 + TRACK_LENGTH;
            int y2 = y1;
            g2d.drawLine(x1, y1, x2, y2);
            y1=y1+TRACK_WIDTH;
            y2=y1;
            g2d.drawLine(x1, y1, x2, y2);
        }
        //
        g2d.setStroke(oldStroke);
        g2d.setColor(oldColor);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        drawField(g);
        Cockroach[] cockroaches = mainWindow.getCockroaches();
        for(int i=0; i<cockroaches.length; i++) {
            cockroaches[i].draw(g);
        }
    }
}
