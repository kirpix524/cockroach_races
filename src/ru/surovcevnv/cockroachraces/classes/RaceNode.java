package ru.surovcevnv.cockroachraces.classes;

public class RaceNode implements Comparable<RaceNode> {
    public static final int DEFAULT_COCKROACH_POSITION_X = 0;
    private int id;
    private long cockroachTime;
    private int cockroachPositionX;
    private boolean finished;

    public RaceNode(int id, long cockroachTime, int cockroachPositionX) {
        init(id, cockroachTime,cockroachPositionX,false);
    }

    public RaceNode(int id) {
        init(id, System.currentTimeMillis(), DEFAULT_COCKROACH_POSITION_X,false);
    }

    private void init(int id, long cockroachTime, int cockroachPositionX, boolean finished) {
        this.id = id;
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

    public long getTime() {
        return cockroachTime;
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