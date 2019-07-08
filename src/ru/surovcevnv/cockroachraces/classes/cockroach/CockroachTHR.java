package ru.surovcevnv.cockroachraces.classes.cockroach;

import ru.surovcevnv.cockroachraces.classes.exceptions.ResourceNotInitialisedException;

public class CockroachTHR implements Runnable {
    private int minSleepTime;
    private int maxSleepTime;
    private String threadNamePrefix;

    Cockroach cockroach;
    private Thread thread;

    CockroachTHR(Cockroach cockroach, int minSleepTime, int maxSleepTime, String threadNamePrefix) {
        this.cockroach = cockroach;
        this.minSleepTime = minSleepTime;
        this.maxSleepTime = maxSleepTime;
        this.threadNamePrefix = threadNamePrefix;
    }

    synchronized private int getSleepTime() {
        int sleepTime = (int) (Math.random() * maxSleepTime);
        if (sleepTime < minSleepTime) sleepTime = minSleepTime;
        return sleepTime;
    }

    @Override
    public void run() {
        checkCockRoach();
        while (!Thread.currentThread().isInterrupted()) {
            cockroach.moveCockroach();
            try {
                Thread.sleep(getSleepTime());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    void startRace() {
        checkCockRoach();
        thread = new Thread(this, threadNamePrefix + cockroach.getID());
        thread.start();
    }

    void stopRace() {
        checkThread();
        thread.interrupt();
    }

    private void checkCockRoach() {
        if (cockroach==null) {
            throw new ResourceNotInitialisedException("cockroach is null");
        }
    }

    private void checkThread() {
        if (thread==null) {
            throw new ResourceNotInitialisedException("thread is null");
        }
    }
}
