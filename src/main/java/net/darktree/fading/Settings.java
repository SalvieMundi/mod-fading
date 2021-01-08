package net.darktree.fading;

import net.darktree.fading.util.Minute;

public class Settings {

    public Minute campfireTime = new Minute(5).randMinutes(4).randSeconds(60);
    public Minute torchTime = new Minute(2).randMinutes(3).randSeconds(60);
    public Minute lanternTime = new Minute(6).randMinutes(3).randSeconds(60);

}
