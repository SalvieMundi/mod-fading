package net.darktree.fading;

import net.darktree.fading.util.MinuteRange;
import net.darktree.fading.util.SimpleConfig;

import java.util.Date;

public class Settings {

    private String provider( String name ) {
        return    "# Configuration file for mod Fading, intended for modpack makers and advanced users!\n"
                + "# Generated on " + new Date().toString() + "\n"
                + "# See: https://github.com/magistermaks/mod-fading/blob/master/HELP.md";
    }

    private final SimpleConfig CONFIG = SimpleConfig.of( "fading" ).provider( this::provider ).request();

    Settings() {
        if( CONFIG.isBroken() ) CONFIG.delete();
    }

    public final MinuteRange campfireTime = new MinuteRange(
            CONFIG.getOrDefault("time.campfire.min", 6),
            CONFIG.getOrDefault("time.campfire.max", 22));

    public final MinuteRange torchTime = new MinuteRange(
            CONFIG.getOrDefault("time.torch.min", 4),
            CONFIG.getOrDefault("time.torch.max", 13));

    public final MinuteRange lanternTime = new MinuteRange(
            CONFIG.getOrDefault("time.lantern.min", 7),
            CONFIG.getOrDefault("time.lantern.max", 18));

    public final int durability_flint =
            CONFIG.getOrDefault("item.durability.flint", 48);

    public final int durability_gold =
            CONFIG.getOrDefault("item.durability.gold", 32);

    public final int durability_diamond =
            CONFIG.getOrDefault("item.durability.diamond", 512);

    public final boolean disintegrate =
            CONFIG.getOrDefault("other.disintegrate", false);
}
