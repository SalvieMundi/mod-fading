package net.darktree.fading;

import net.darktree.fading.util.Minute;

public class Settings {

    public Minute campfireTime = new Minute(5).randMinutes(15).randSeconds(60);
    public Minute torchTime = new Minute(3).randMinutes(9).randSeconds(60);
    public Minute lanternTime = new Minute(6).randMinutes(10).randSeconds(60);

}
