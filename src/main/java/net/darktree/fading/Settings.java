package net.darktree.fading;

import net.darktree.fading.util.MinuteRange;
import net.darktree.fading.util.SimpleConfig;

import java.util.Date;

public class Settings {

    private String provider( String name ) {
        return    "# Configuration file for mod Fading, intended for modpack makers and advanced users!\n"
                + "# Generated on " + new Date().toString() + "\n"
                + "\n# Following values are burnout time ranges in minutes:\n"
                + "#time.campfire.min=5\n"
                + "#time.campfire.max=21\n"
                + "#time.torch.min=3\n"
                + "#time.torch.max=13\n"
                + "#time.lantern.min=6\n"
                + "#time.lantern.max=17\n"
                + "\n# Durability of 'flint and X' items:\n"
                + "item.durability.flint=48\n"
                + "item.durability.gold=32\n"
                + "item.durability.diamond=512\n";
    }

    private final SimpleConfig CONFIG = SimpleConfig.of( "fading" ).provider( this::provider ).request();

    Settings() {
        if( CONFIG.isBroken() ) CONFIG.delete();
    }

    public final MinuteRange campfireTime = new MinuteRange(
            CONFIG.getOrDefault("time.campfire.min", 5),
            CONFIG.getOrDefault("time.campfire.max", 21));

    public final MinuteRange torchTime = new MinuteRange(
            CONFIG.getOrDefault("time.torch.min", 3),
            CONFIG.getOrDefault("time.torch.max", 13));

    public final MinuteRange lanternTime = new MinuteRange(
            CONFIG.getOrDefault("time.lantern.min", 6),
            CONFIG.getOrDefault("time.lantern.max", 17));

    public final int durability_flint =
            CONFIG.getOrDefault("item.durability.flint", 48);

    public final int durability_gold =
            CONFIG.getOrDefault("item.durability.gold", 32);

    public final int durability_diamond =
            CONFIG.getOrDefault("item.durability.diamond", 512);


}
