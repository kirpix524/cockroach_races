package ru.surovcevnv.cockroachraces.classes;

import java.awt.*;

public class Cockroach implements Runnable{
    public static final String COCKROACH_THREAD_NAME_PREFIX = "CockroachThread";
    private final double MAX_SLEEP_TIME = 700;
    private final int MIN_SLEEP_TIME = 300;
    private final int MAX_MOVE = 10;
    public static final int DEFAULT_WIDTH = 70;
    public static final int DEFAULT_HEIGHT = 40;
    private final float DEFAULT_PAW_WIDTH = 3.0f;
    private final boolean DEFAULT_STEP_LEFT = false;
    private final int NUMBER_OF_PAW_PAIRS = 4;
    private final int MIN_STEP_CHANGE_TIME = 130;
    private final String DEFAULT_NAME_PREFIX = "Таракан ";
    //
    private int id;
    private boolean isRacing;
    private Thread thread;
    //
    private int curX;
    private int curY;
    private int width;
    private int height;
    private float pawWidth;
    private Color color;
    private boolean stepLeft;
    private long lastStepTime;
    //
    private int startPositionX;
    private int startPositionY;
    private String name;



    public Cockroach(int id, int curX, int curY) {
        init(id,curX,curY,DEFAULT_WIDTH, DEFAULT_HEIGHT,DEFAULT_PAW_WIDTH,new Color((int)(255*Math.random()), (int)(255*Math.random()),(int)(255*Math.random())),false, DEFAULT_STEP_LEFT, DEFAULT_NAME_PREFIX+(id+1));
    }

    private void init(int id, int curX, int curY, int width, int height, float pawWidth, Color color, boolean isRacing, boolean stepLeft, String name) {
        this.id = id;
        this.curX = curX;
        this.curY = curY;
        this.width = width;
        this.height = height;
        this.pawWidth = pawWidth;
        this.color = color;
        this.isRacing = isRacing;
        this.stepLeft = stepLeft;
        this.name = name;

        this.startPositionX = curX;
        this.startPositionY = curY;
    }

    public void startRace() {
        isRacing=true;
        thread = new Thread(this, COCKROACH_THREAD_NAME_PREFIX +id);
        thread.start();
    }

    public void stopRace() {
        isRacing = false;
        thread.interrupt();
    }

    public void returnToStart() {
        setCoordinates(startPositionX,startPositionY);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            moveCockroach();
            try {
                Thread.sleep(getSleepTime());
            } catch (InterruptedException e) {
//                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }

    synchronized private int getSleepTime() {
        int sleepTime = (int)(Math.random()*MAX_SLEEP_TIME);
        if (sleepTime<MIN_SLEEP_TIME) sleepTime=MIN_SLEEP_TIME;
        return sleepTime;
    }

    synchronized private void moveCockroach() {
        setCoordinates(curX+(int)(Math.random()*MAX_MOVE),curY);
    }

    synchronized public void setCoordinates(int newX, int newY) {  //todo check bounds
        curX=newX;
        curY=newY;
    }

    synchronized public int getTop() {
        return curY-height/2;
    }

    synchronized public int getBottom() {
        return curY+height/2;
    }

    synchronized public int getLeft() {
        return curX-width/2;
    }

    synchronized public int getRight() {
        return curX+width/2;
    }

    synchronized public boolean isMyCoord(int x, int y) {
        if ((getTop()>=y)&&(getBottom()<=y)&&(getLeft()<=x)&&(getRight()>=x)) return true;
        return false;
    }

    private void changeStep() {
        if (lastStepTime>0) {
            if ((System.currentTimeMillis()-lastStepTime)>MIN_STEP_CHANGE_TIME) {
                if (stepLeft) {
                    stepLeft=false;
                } else {
                    stepLeft=true;
                }
                lastStepTime=System.currentTimeMillis();
            }
        } else {
            lastStepTime = System.currentTimeMillis();
        }

    }

    private int getPawInterval() {
        return width/(NUMBER_OF_PAW_PAIRS+1);
    }

    private int getDiffXPaw() {
        return getPawInterval()/2;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Color oldColor = g2d.getColor();
        Stroke oldStroke = g2d.getStroke();

        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(pawWidth));

        g2d.fillOval(getLeft(), getTop()+(height/4), width,height/2);

        for (int i=0; i<NUMBER_OF_PAW_PAIRS; i++) {
            if (stepLeft) {
                g2d.drawLine(getLeft()+(getPawInterval()*(i+1))-getDiffXPaw(), getBottom(), getLeft()+(getPawInterval()*(i+1))+getDiffXPaw(), getTop());
            } else {
                g2d.drawLine(getLeft()+(getPawInterval()*(i+1))+getDiffXPaw(), getBottom(), getLeft()+(getPawInterval()*(i+1))-getDiffXPaw(), getTop());
            }
        }
        changeStep();

        g2d.setColor(oldColor);
        g2d.setStroke(oldStroke);
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
