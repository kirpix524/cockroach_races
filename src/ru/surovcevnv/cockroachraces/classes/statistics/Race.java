package ru.surovcevnv.cockroachraces.classes.statistics;

import ru.surovcevnv.cockroachraces.classes.exceptions.ResourceNotInitialisedException;

import java.util.ArrayList;
import java.util.Collections;

public class Race {
    private int id;
    private ArrayList<RaceNode> races;
    private long startTime;
    private boolean finished;

    public Race(int id, ArrayList<RaceNode> list) {
        init(id, list, System.currentTimeMillis(),false);
    }

    public Race(int id) {
        init(id, null, System.currentTimeMillis(), false);
    }

    private void init(int id, ArrayList<RaceNode> list, long startTime, boolean finished) {
        if (list != null) {
            races = new ArrayList<>(list);
        } else {
            races = new ArrayList<>();
        }
        this.id = id;
        this.startTime = startTime;
        this.finished = false;
        sortNodes();
    }

    synchronized public void addRaceNode(RaceNode node) {
        checkRaces();
        races.add(node);
        sortNodes();
    }

    synchronized private void sortNodes() {
        checkRaces();
        Collections.sort(races);
    }

    synchronized public int getNodePositionByCockroachID(int cockroachID) {
        checkRaces();
        for (int i=0; i<races.size(); i++) {
            if (races.get(i).getID()==cockroachID) return i;
        }
        return -1;
    }

    synchronized public void setNewPosXAndTime(int cockroachID, long time, int posX) {
        int i = getNodePositionByCockroachID(cockroachID);
        if (i==-1) return;
        races.get(i).setPosX(posX);
        races.get(i).setTime(time);
        sortNodes();
    }

    synchronized public boolean isEveryoneFinished() {
        checkRaces();
        for (int i=0; i<races.size(); i++) {
            if (!races.get(i).isFinished()) {
                return false;
            }
        }
        return true;
    }

    synchronized public void setNodeFinished(int cockroachID) {
        checkRaces();
        int i = getNodePositionByCockroachID(cockroachID);
        if (i<0) return;
        races.get(i).setFinished();
        if (isEveryoneFinished()) {
            setFinished();
        }
        sortNodes();
    }

    public RaceNode[] getRaceNodesAsArray() {
        checkRaces();
        return races.toArray(RaceNode[]::new);
    }

    public RaceNode getLeader() {
        checkRaces();
        if (races.size()==0) {
            throw new ResourceNotInitialisedException("races is empty");
        }
        return races.get(0);
    }

    public long getStartTime() {
        return startTime;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished() {
        finished = true;
    }

    public int getNum() {
        return (id+1);
    }

    synchronized public void renameCockroach(int cockroachID, String name) {
        int i = getNodePositionByCockroachID(cockroachID);
        if (i==-1) return;
        races.get(i).setName(name);
    }

    private void checkRaces() {
        if (races==null) {
            throw new ResourceNotInitialisedException("races is null");
        }
    }
}
