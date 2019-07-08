package ru.surovcevnv.cockroachraces.classes.racefield;

import ru.surovcevnv.cockroachraces.classes.RaceControlCenter;
import ru.surovcevnv.cockroachraces.classes.exceptions.ResourceNotInitialisedException;

public class RaceField {
    public static final int DEFAULT_TRACK_WIDTH = 80;
    public static final int DEFAULT_TRACK_INDENT = 30;
    private static final int TRACK_LENGTH = 800;
    private static final int TRACK_START_X = 100;
    public static final int TRACK_START_Y = 80;
    //
    private int trackWidth;
    private int trackIndent;
    private int trackLength;
    private int trackStartX;
    private int trackStartY;
    //
    private RaceControlCenter raceControlCenter;
    private RaceFieldGR raceFieldGR;

    public RaceField(RaceControlCenter raceControlCenter) {
        init(raceControlCenter, DEFAULT_TRACK_WIDTH, DEFAULT_TRACK_INDENT, TRACK_LENGTH, TRACK_START_X, TRACK_START_Y);
    }

    private void init(RaceControlCenter raceControlCenter, int trackWidth, int trackIndent,int trackLength, int trackStartX, int trackStartY) {
        this.raceControlCenter = raceControlCenter;
        this.trackWidth = trackWidth;
        this.trackIndent = trackIndent;
        this.trackLength = trackLength;
        this.trackStartX = trackStartX;
        this.trackStartY = trackStartY;
        raceFieldGR = new RaceFieldGR(raceControlCenter, this);
    }

    public int getTrackWidth() {
        return trackWidth;
    }

    public int getTrackIndent() {
        return trackIndent;
    }

    public int getTrackLength() {
        return trackLength;
    }

    public int getTrackStartX() {
        return trackStartX;
    }

    public int getTrackStartY() {
        return trackStartY;
    }

    public int getFullTrackWidth() {
        return (getTrackWidth() + 2 * getTrackIndent());
    }

    public int getFinishX() {
        return trackStartX+trackLength;
    }

    public int getFinishY(int number) {
        if (number<0) {
            throw new IllegalArgumentException("track number must be >0");
        }
        return trackStartY + (number * getFullTrackWidth() + trackIndent);
    }

    public RaceFieldGR getRaceFieldGR() {
        checkRaceFieldGR();
        return raceFieldGR;
    }

    private void checkRaceFieldGR() {
        if (raceFieldGR==null) {
            throw new ResourceNotInitialisedException("raceFieldGR is null");
        }
    }
}
