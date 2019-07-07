package ru.surovcevnv.cockroachraces.classes.statistics;

public class RaceNode implements Comparable<RaceNode> {
    public static final int DEFAULT_COCKROACH_POSITION_X = 0;
    private int id;
    private String name;
    private long cockroachTime;
    private int cockroachPositionX;
    private boolean finished;

    public RaceNode(int id, String name, long cockroachTime, int cockroachPositionX) {
        init(id,name, cockroachTime,cockroachPositionX,false);
    }

    public RaceNode(int id, String name) {
        init(id, name, System.currentTimeMillis(), DEFAULT_COCKROACH_POSITION_X,false);
    }

    private void init(int id, String name, long cockroachTime, int cockroachPositionX, boolean finished) {
        this.id = id;
        this.name = name;
        this.cockroachTime = cockroachTime;
        this.cockroachPositionX = cockroachPositionX;
        this.finished = finished;
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

    public float getTimeInWay(long startTime) {
        return (float)(getTime() - startTime)/1000f;
    }

    public void setTime(long time) {
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