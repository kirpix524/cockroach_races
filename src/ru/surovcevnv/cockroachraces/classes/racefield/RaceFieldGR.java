package ru.surovcevnv.cockroachraces.classes.racefield;

import ru.surovcevnv.cockroachraces.classes.RaceControlCenter;
import ru.surovcevnv.cockroachraces.classes.cockroach.Cockroach;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class RaceFieldGR extends JPanel {
    private final float DEFAULT_SEPARATOR_WIDTH = 1.0f;
    private final Color DEFAULT_BORDER_COLOR = Color.BLACK;
    private final float DEFAULT_BORDER_WIDTH = 5.0f;
    private final int STAT_INFO_X = 100;
    private final int STAT_INFO_Y = 20;
    private final int TRACK_NUM_X = 0;
    private final int COCKROACH_NAME_X = TRACK_NUM_X + 100;
    private final int FINISH_CELLS_X = 2;
    private final int FINISH_CELLS_Y = 8;
    //
    private final int REFRESH_TIME = 50;
    //
    //
    private RaceControlCenter raceControlCenter;
    private RaceField raceField;
    //
    private final Font FONT_TRACK_NUM = new Font("Times New Roman", Font.BOLD, 38);
    private final Font FONT_COCKROACH_NAME = new Font("Times New Roman", Font.PLAIN, 16);
    private final Font FONT_STAT_INFO = new Font("Times New Roman", Font.PLAIN, 25);

    private class RepaintEventGenerator extends Thread {
        JPanel me;
        int refreshTime;

        RepaintEventGenerator(JPanel me, int refreshTime) {
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

    public RaceFieldGR(RaceControlCenter raceControlCenter, RaceField raceField) {
        this.raceControlCenter = raceControlCenter;
        this.raceField = raceField;
        new RepaintEventGenerator(this, REFRESH_TIME);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount()==2) {
                    raceControlCenter.kickCockroachAtCoordinates(e.getX(), e.getY());
                }
            }
        });
    }

    private void drawFinishLine(Graphics g, int id) {
        Graphics2D g2d = (Graphics2D) g;
        Color oldColor = g2d.getColor();
        //
        int finishCellSize = getFinishWidth()/FINISH_CELLS_X;
        int x,y;
        boolean isWhite = false;
        for (int i=0; i<FINISH_CELLS_X; i++) {
            isWhite=!isWhite;
            x=raceField.getFinishX()+i*finishCellSize;
            for (int j=0; j<FINISH_CELLS_Y; j++) {
                y=raceField.getFinishY(id)+j*finishCellSize;
                if (isWhite) {
                    g2d.setColor(Color.WHITE);
                    g2d.fillRect(x,y,finishCellSize,finishCellSize);
                    isWhite=false;
                } else {
                    g2d.setColor(Color.BLACK);
                    g2d.fillRect(x,y,finishCellSize,finishCellSize);
                    isWhite=true;
                }
            }
        }
        //
        g2d.setColor(oldColor);
    }

    private void drawTrack(Graphics g, int number) {
        Graphics2D g2d = (Graphics2D) g;
        Color oldColor = g2d.getColor();
        Stroke oldStroke = g2d.getStroke();
        //
        g2d.setColor(DEFAULT_BORDER_COLOR);
        int x1 = raceField.getTrackStartX();
        int y1 = raceField.getTrackStartY() + (number * raceField.getFullTrackWidth() + raceField.getTrackIndent());
        int x2 = x1 + raceField.getTrackLength();
        int y2 = y1;
        g2d.setStroke(new BasicStroke(DEFAULT_BORDER_WIDTH));
        g2d.drawLine(x1, y1, x2, y2);
        y1 = y1 + raceField.getTrackWidth();
        y2 = y1;
        g2d.drawLine(x1, y1, x2, y2);
        //
        g2d.setStroke(new BasicStroke(DEFAULT_SEPARATOR_WIDTH));
        g2d.drawLine(0, raceField.getTrackStartY() + (number * raceField.getFullTrackWidth()), this.getWidth(), raceField.getTrackStartY() + (number * raceField.getFullTrackWidth()));
        g2d.drawLine(0, raceField.getTrackStartY() + ((number+1) * raceField.getFullTrackWidth()), this.getWidth(), raceField.getTrackStartY() + ((number+1) * raceField.getFullTrackWidth()));
        //
        g2d.setStroke(oldStroke);
        g2d.setColor(oldColor);
    }

    private void drawTrackInfo(Graphics g, int number) {
        Cockroach[] cockroaches = raceControlCenter.getCockroaches();
        Graphics2D g2d = (Graphics2D) g;
        Font oldFont = g2d.getFont();
        //
        g2d.setFont(FONT_TRACK_NUM);
        g2d.drawString("" + (number + 1), TRACK_NUM_X, raceField.getTrackStartY() + (number * raceField.getFullTrackWidth()) + raceField.getTrackIndent());
        //
        g2d.setFont(FONT_COCKROACH_NAME);
        g2d.drawString(cockroaches[number].getInfo(), COCKROACH_NAME_X, raceField.getTrackStartY() + (number * raceField.getFullTrackWidth()) + raceField.getTrackIndent()/2);
        //
        g2d.setFont(oldFont);
    }

    private void drawStatInfo(Graphics g) {
        String raceInfo = raceControlCenter.getStatInfo();
        Graphics2D g2d = (Graphics2D) g;
        Font oldFont = g2d.getFont();
        //
        g2d.setFont(FONT_STAT_INFO);
        g2d.drawString(raceInfo, STAT_INFO_X, STAT_INFO_Y);
        //
        g2d.setFont(oldFont);
    }

    private void drawField(Graphics g) {
        drawStatInfo(g);
        int numberOfTracks = raceControlCenter.getNumberOfTracks();
        for (int i = 0; i < numberOfTracks; i++) {
            drawTrack(g,i);
            drawFinishLine(g,i);
            drawTrackInfo(g,i);
        }
    }

    private void prepareBuffer(BufferedImage bi, Graphics gbi, Graphics g) {
        gbi.setColor(this.getBackground());
        gbi.fillRect(0,0,getWidth(), getHeight());
        gbi.setColor(g.getColor());
        gbi.setFont(g.getFont());
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        //
        BufferedImage bi = new BufferedImage(getWidth(), getHeight(),BufferedImage.TYPE_INT_RGB);
        Graphics gbi = bi.getGraphics();
        prepareBuffer(bi, gbi, g);
        //
        drawField(gbi);
        Cockroach[] cockroaches = raceControlCenter.getCockroaches();
        for (int i = 0; i < cockroaches.length; i++) {
            cockroaches[i].draw(gbi);
        }
        g.drawImage(bi,0,0,null);
    }

    private int getFinishWidth(){
        return (raceField.getTrackWidth()/FINISH_CELLS_Y)*FINISH_CELLS_X;
    }
}
