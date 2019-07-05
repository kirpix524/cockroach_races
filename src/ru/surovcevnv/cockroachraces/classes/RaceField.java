package ru.surovcevnv.cockroachraces.classes;

import ru.surovcevnv.cockroachraces.MainWindow;

import javax.swing.*;
import java.awt.*;

public class RaceField extends JPanel {
    private final Color DEFAULT_BORDER_COLOR = new Color(0, 0, 0);
    private final float DEFAULT_BORDER_WIDTH = 5.0f;
    private MainWindow mainWindow;
    private final int TRACK_WIDTH = 100;
    private final int TRACK_INDENT = 10;
    private final int TRACK_LENGTH = 900;
    private final int TRACK_START_X = 100;
    private final int TRACK_START_Y = 80;

    public RaceField(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
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
            int y1 = TRACK_START_Y + (i * (TRACK_WIDTH + 2 * TRACK_INDENT) + TRACK_INDENT);
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
}
