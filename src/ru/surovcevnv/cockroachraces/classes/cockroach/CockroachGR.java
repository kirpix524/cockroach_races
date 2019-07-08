package ru.surovcevnv.cockroachraces.classes.cockroach;

import ru.surovcevnv.cockroachraces.classes.exceptions.ResourceNotInitialisedException;

import java.awt.*;

public class CockroachGR {
    private Cockroach cockroach;
    private Color color;
    private float pawWidth;
    private int numberOfPawPairs;
    private boolean stepLeft;
    private long lastStepChangeTime;
    private int minStepChangeTime;

    CockroachGR(Cockroach cockroach, Color color, float pawWidth, int numberOfPawPairs, int minStepChangeTime) {
        this.cockroach = cockroach;
        this.color = color;
        this.pawWidth = pawWidth;
        this.numberOfPawPairs = numberOfPawPairs;
        this.minStepChangeTime = minStepChangeTime;

    }

    synchronized private void changeStep() {
        checkCockRoach();
        if (!cockroach.isRacing()) return;
        if (lastStepChangeTime > 0) {
            if ((System.currentTimeMillis() - lastStepChangeTime) > minStepChangeTime) {
                if (stepLeft) {
                    stepLeft = false;
                } else {
                    stepLeft = true;
                }
                lastStepChangeTime = System.currentTimeMillis();
            }
        } else {
            lastStepChangeTime = System.currentTimeMillis();
        }

    }

    private int getPawInterval() {
        return cockroach.getWidth() / (numberOfPawPairs + 1);
    }

    private int getDiffXPaw() {
        return getPawInterval() / 2;
    }

    public void draw(Graphics g) {
        checkG(g);
        checkCockRoach();

        Graphics2D g2d = (Graphics2D) g;
        Color oldColor = g2d.getColor();
        Stroke oldStroke = g2d.getStroke();

        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(pawWidth));

        g2d.fillOval(cockroach.getLeft(), cockroach.getTop() + (cockroach.getHeight() / 4), cockroach.getWidth(), cockroach.getHeight() / 2);

        for (int i = 0; i < numberOfPawPairs; i++) {
            if (stepLeft) {
                g2d.drawLine(cockroach.getLeft() + (getPawInterval() * (i + 1)) - getDiffXPaw(), cockroach.getBottom(), cockroach.getLeft() + (getPawInterval() * (i + 1)) + getDiffXPaw(), cockroach.getTop());
            } else {
                g2d.drawLine(cockroach.getLeft() + (getPawInterval() * (i + 1)) + getDiffXPaw(), cockroach.getBottom(), cockroach.getLeft() + (getPawInterval() * (i + 1)) - getDiffXPaw(), cockroach.getTop());
            }
        }
        changeStep();

        g2d.setColor(oldColor);
        g2d.setStroke(oldStroke);
    }

    private void checkCockRoach() {
        if (cockroach==null) {
            throw new ResourceNotInitialisedException("cockroach is null");
        }
    }

    private void checkG(Graphics g) {
        if (g==null) {
            throw new IllegalArgumentException("graphics is null");
        }
    }
}
