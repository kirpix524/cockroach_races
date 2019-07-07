package ru.surovcevnv.cockroachraces.classes.cockroach;

import ru.surovcevnv.cockroachraces.MainWindow;
import ru.surovcevnv.cockroachraces.classes.RaceControlCenter;

import java.awt.*;

public class Cockroach {
    private final String THREAD_NAME_PREFIX = "CockroachThread";
    private final int KICK_MOVE_KOEF = 5;
    private final int MAX_SLEEP_TIME = 300;
    private final int MIN_SLEEP_TIME = 100;
    private final int MAX_MOVE = 10;
    public static final int DEFAULT_WIDTH = 70;
    public static final int DEFAULT_HEIGHT = 40;
    private final float DEFAULT_PAW_WIDTH = 3.0f;
    private final int NUMBER_OF_PAW_PAIRS = 4;
    private final int MIN_STEP_CHANGE_TIME = 150;
    private final String DEFAULT_NAME_PREFIX = "Таракан ";
    //id
    private int id;
    private String name;
    //coordinates
    private int curX;
    private int curY;
    private int width;
    private int height;
    private int startPositionX;
    private int startPositionY;
    //
    private long lastMoveTime;
    //
    private boolean isRacing;
    //assistants
    private CockroachTHR cockroachTHR;
    private CockroachGR cockroachGR;
    //master
    private RaceControlCenter raceControlCenter;

    public Cockroach(RaceControlCenter raceControlCenter, int id, int curX, int curY) {
        init(raceControlCenter, id, curX, curY, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_PAW_WIDTH, new Color((int) (255 * Math.random()), (int) (255 * Math.random()), (int) (255 * Math.random())), false, DEFAULT_NAME_PREFIX + (id + 1));
    }

    private void init(RaceControlCenter raceControlCenter, int id, int curX, int curY, int width, int height, float pawWidth, Color color, boolean isRacing, String name) {
        this.raceControlCenter = raceControlCenter;
        this.id = id;
        this.curX = curX;
        this.curY = curY;
        this.width = width;
        this.height = height;
        this.isRacing = false;
        this.name = name;

        this.startPositionX = curX;
        this.startPositionY = curY;

        cockroachTHR = new CockroachTHR(this,MIN_SLEEP_TIME, MAX_SLEEP_TIME, THREAD_NAME_PREFIX);
        cockroachGR = new CockroachGR(this, color, pawWidth, NUMBER_OF_PAW_PAIRS, MIN_STEP_CHANGE_TIME);
    }

    public void startRace() {
        cockroachTHR.startRace();
        isRacing=true;
    }

    public void stopRace() {
        cockroachTHR.stopRace();
        isRacing=false;
    }

    public void returnToStart() {
        setCoordinates(startPositionX, startPositionY);
        setLastMoveTime(System.currentTimeMillis());
    }

    synchronized public void moveCockroach(boolean isKicked) {
        if (!isRacing) return;
        if (isKicked) {
            makeMove(MAX_MOVE* KICK_MOVE_KOEF,0);
        } else {
            moveCockroach();
        }
    }

    synchronized public boolean isMyCoord(int x, int y) {
        if ((getTop()<=y) && (getBottom() >= y) && (getLeft() <= x) && (getRight() >= x)) return true;
        return false;
    }

    public void draw(Graphics g) {
        cockroachGR.draw(g);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
//        return getName()+" до финиша осталось "+(raceControlCenter.getFinishX()-getPosX());
        return getName();
    }

    public int getID() {
        return id;
    }

    public long getTime() {
        return lastMoveTime;
    }

    public int getPosX() {
        return getLeft();
    }

    synchronized int getTop() {
        return curY - height / 2;
    }

    synchronized int getBottom() {
        return curY + height / 2;
    }

    synchronized int getLeft() {
        return curX - width / 2;
    }

    synchronized int getRight() {
        return curX + width / 2;
    }

    synchronized int getHeight() {
        return height;
    }

    synchronized int getWidth() {
        return width;
    }

    synchronized void moveCockroach() {
        if (!isRacing) return;
        makeMove((int) (Math.random() * MAX_MOVE), 0);
    }

    synchronized private void setLastMoveTime(long lastMoveTime) {
        this.lastMoveTime = lastMoveTime;
    }

    synchronized private void makeMove(int moveX, int moveY) {
        setCoordinates(curX + moveX, curY+moveY);
        setLastMoveTime(System.currentTimeMillis());
        raceControlCenter.updateRaceNode(id, getTime(), getPosX());
        if (getLeft() >= raceControlCenter.getFinishX()) {
            stopRace();
            raceControlCenter.sayFinishedAndUpdateRace(id);
        }
    }

    synchronized private void setCoordinates(int newX, int newY) {  //
        curX = newX;
        curY = newY;
    }

    boolean isRacing() {
        return isRacing;
    }
}
