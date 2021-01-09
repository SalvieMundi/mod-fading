package net.darktree.fading;

import net.darktree.fading.util.Minute;
import net.darktree.fading.util.SimpleConfig;

import java.util.Date;

public class Settings {

    private String provider( String name ) {
        return    "# Configuration file for mod Fading, intended for modpack makers and advanced users!\n"
                + "# Generated on " + new Date().toString() + "\n\n"
                + "# Following values are burnout time ranges in minutes:\n"
                + "#time.campfire.min=5\n"
                + "#time.campfire.max=21\n"
                + "#time.torch.min=3\n"
                + "#time.torch.max=13\n"
                + "#time.lantern.min=6\n"
                + "#time.lantern.max=17\n\n"
                + "# Change if you don't want better flint and steel:\n"
                + "item.flint_and_steel=true\n";
    }

    private final SimpleConfig CONFIG = SimpleConfig.of( "fading" ).provider( this::provider ).request();

    Settings() {
        if( CONFIG.isBroken() ) CONFIG.delete();
    }

    public final Minute campfireTime = new Minute(
            CONFIG.getOrDefault("time.campfire.min", 5),
            CONFIG.getOrDefault("time.campfire.max", 21));

    public final Minute torchTime = new Minute(
            CONFIG.getOrDefault("time.torch.min", 3),
            CONFIG.getOrDefault("time.torch.max", 13));

    public final Minute lanternTime = new Minute(
            CONFIG.getOrDefault("time.lantern.min", 6),
            CONFIG.getOrDefault("time.lantern.max", 17));

    public final boolean betterFlints =
            CONFIG.getOrDefault("item.flint_and_steel", true);


}
