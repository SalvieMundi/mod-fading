package net.darktree.fading.util;

import java.util.Random;

public class Minute {

    private final int minutes;
    private int randMinutes;
    private int randSeconds;
    private int randTicks;

    public Minute(int minutes) {
        this.minutes = minutes;
    }

    public Minute randMinutes(int minutes ) {
        randMinutes = minutes;
        return this;
    }

    public Minute randSeconds( int seconds ) {
        randSeconds = seconds;
        return this;
    }

    public Minute randTicks( int ticks ) {
        randTicks = ticks;
        return this;
    }

    public int getTicks() {
        return 20 * 60 * minutes;
    }

    public int getTicks( Random random ) {
        return getTicks() + 20 * ( Utils.strangeInt( random, randSeconds ) + 60 * Utils.strangeInt( random, randMinutes ) ) + Utils.strangeInt( random, randTicks );
    }

}
