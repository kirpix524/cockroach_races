package ru.surovcevnv.cockroachraces.classes.cockroach;

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
        while (!Thread.currentThread().isInterrupted()) {
            cockroach.moveCockroach();
            try {
                Thread.sleep(getSleepTime());
            } catch (InterruptedException e) {
//                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }

    void startRace() {
        thread = new Thread(this, threadNamePrefix + cockroach.getID());
        thread.start();
    }

    void stopRace() {
        thread.interrupt();
    }
}
