package ru.surovcevnv.cockroachraces.classes;

import java.awt.*;

public class Cockroach implements Runnable{
    private final double MAX_SLEEP_TIME = 700;
    private final int MIN_SLEEP_TIME = 300;
    private final int MAX_MOVE = 10;
    private int id;
    private boolean isRacing;
    private Thread thread;

    private int curX;
    private int curY;
    private int width;
    private int height;

    public Cockroach(int id, int curX, int curY) {
        init(id,curX,curY,false);
    }

    private void init(int id, int curX, int curY, boolean isRacing) {
        this.id = id;
        this.curX = curX;
        this.curY = curY;
        this.isRacing = isRacing;
    }

    public void startRace() {
        isRacing=true;
        thread = new Thread(this, "CockroachThread"+id);
        thread.start();
    }

    public void stopRace() {
        isRacing = false;
        thread.interrupt();
    }

    @Override
    public void run() {
        while (!thread.isInterrupted()) {
            moveCockroach();
            try {
                Thread.sleep(getSleepTime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private int getSleepTime() {
        int sleepTime = (int)(Math.random()*MAX_SLEEP_TIME);
        if (sleepTime<MIN_SLEEP_TIME) sleepTime=MIN_SLEEP_TIME;
        return sleepTime;
    }

    private void moveCockroach() {
        setCoordinates(curX+(int)(Math.random()*MAX_MOVE),curY);
    }

    public void setCoordinates(int newX, int newY) {  //todo check bounds
        curX=newX;
        curY=newY;
    }

    public int getTop() {
        return curY+height/2;
    }

    public int getBottom() {
        return curY-height/2;
    }

    public int getLeft() {
        return curX-height/2;
    }

    public int getRight() {
        return curX+height/2;
    }

    public void draw(Graphics g) {

    }
}
