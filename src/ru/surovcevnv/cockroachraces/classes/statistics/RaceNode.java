package ru.surovcevnv.cockroachraces.classes.statistics;

public class RaceNode implements Comparable<RaceNode> {
    public static final int DEFAULT_COCKROACH_POSITION_X = 0;
    private int id;
    private String name;
    private long raceStartTime;
    private long cockroachTime;
    private int cockroachPositionX;
    private boolean finished;

    public RaceNode(int id, String name, long cockroachTime, int cockroachPositionX, long raceStartTime) {
        init(id,name, cockroachTime,cockroachPositionX,false, raceStartTime);
    }

    public RaceNode(int id, String name) {
        init(id, name, System.currentTimeMillis(), DEFAULT_COCKROACH_POSITION_X,false, 0);
    }

    private void init(int id, String name, long cockroachTime, int cockroachPositionX, boolean finished, long raceStartTime) {
        this.id = id;
        this.name = name;
        this.cockroachTime = cockroachTime;
        this.cockroachPositionX = cockroachPositionX;
        this.finished = finished;
        this.raceStartTime = raceStartTime;
    }

    @Override
    public int compareTo(RaceNode o) {
        if (isFinished() && (!o.isFinished())) return -1;
        if ((!isFinished()) && o.isFinished()) return 1;
        if (isFinished() && o.isFinished()) return (int) (getTime() - o.getTime());
        return o.getPosX() - getPosX();
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getTime() {
        return cockroachTime;
    }

    public float getTimeInWay() {
        return (float)(getTime() - raceStartTime)/1000f;
    }

    public void setTime(long time) {
        if (time<0) throw new IllegalArgumentException("time must be >0");
        this.cockroachTime = time;
    }

    public int getPosX() {
        return cockroachPositionX;
    }

    public void setPosX(int posX) {
        this.cockroachPositionX = posX;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished() {
        finished = true;
    }
}