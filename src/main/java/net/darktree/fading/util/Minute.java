package net.darktree.fading.util;

import java.util.Random;

public class Minute {

    private final int min;
    private final int delta;

    public Minute( int min, int max ) {
        this.min = min * 60 * 20;
        this.delta = (max - min) * 60 * 20;
    }

    public int getTicks( Random random ) {
        return min + random.nextInt( delta );
    }

}
