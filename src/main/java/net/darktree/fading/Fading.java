package net.darktree.fading;

import net.darktree.fading.block.ExtinguishedLanternBlock;
import net.darktree.fading.block.ExtinguishedTorchBlock;
import net.darktree.fading.block.ExtinguishedWallTorchBlock;
import net.darktree.fading.recipes.Recipes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.*;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Fading implements ModInitializer, ClientModInitializer {

    public static Settings SETTINGS = new Settings();
    public static String NAMESPACE = "fading";

    // Blocks
    public static final Block EXTINGUISHED_WALL_TORCH = new ExtinguishedWallTorchBlock( AbstractBlock.Settings.of(Material.SUPPORTED).noCollision().breakInstantly().sounds(BlockSoundGroup.WOOD) );
    public static final Block EXTINGUISHED_TORCH = new ExtinguishedTorchBlock( AbstractBlock.Settings.of(Material.SUPPORTED).noCollision().breakInstantly().sounds(BlockSoundGroup.WOOD) );
    public static final Block EXTINGUISHED_LANTERN = new ExtinguishedLanternBlock( AbstractBlock.Settings.of(Material.METAL).requiresTool().strength(3.5F).sounds(BlockSoundGroup.LANTERN).nonOpaque() );
    public static final Block EXTINGUISHED_WALL_SOUL_TORCH = new ExtinguishedWallTorchBlock( AbstractBlock.Settings.of(Material.SUPPORTED).noCollision().breakInstantly().sounds(BlockSoundGroup.WOOD) );
    public static final Block EXTINGUISHED_SOUL_TORCH = new ExtinguishedTorchBlock( AbstractBlock.Settings.of(Material.SUPPORTED).noCollision().breakInstantly().sounds(BlockSoundGroup.WOOD) );
    public static final Block EXTINGUISHED_SOUL_LANTERN = new ExtinguishedLanternBlock( AbstractBlock.Settings.of(Material.METAL).requiresTool().strength(3.5F).sounds(BlockSoundGroup.LANTERN).nonOpaque() );

    // Items
    public static final Item EXTINGUISHED_TORCH_ITEM = new WallStandingBlockItem(EXTINGUISHED_TORCH, EXTINGUISHED_WALL_TORCH, new Item.Settings().group(ItemGroup.DECORATIONS) );
    public static final Item EXTINGUISHED_LANTERN_ITEM = new BlockItem(EXTINGUISHED_LANTERN, new Item.Settings().group(ItemGroup.DECORATIONS) );
    public static final Item EXTINGUISHED_SOUL_TORCH_ITEM = new WallStandingBlockItem(EXTINGUISHED_SOUL_TORCH, EXTINGUISHED_WALL_SOUL_TORCH, new Item.Settings().group(ItemGroup.DECORATIONS) );
    public static final Item EXTINGUISHED_SOUL_LANTERN_ITEM = new BlockItem(EXTINGUISHED_SOUL_LANTERN, new Item.Settings().group(ItemGroup.DECORATIONS) );

    // Tags
    public static Tag<Block> EXTINGUISHABLE_CAMPFIRES;
    public static Tag<Block> EXTINGUISHABLE_LANTERNS;
    public static Tag<Block> EXTINGUISHABLE_TORCHES;
    public static Tag<Block> EXTINGUISHABLE;

    // Stats
    public static final Identifier EXTINGUISH_TORCH = new Identifier(NAMESPACE, "extinguish_torch");
    public static final Identifier EXTINGUISH_LANTERN = new Identifier(NAMESPACE, "extinguish_lantern");
    public static final Identifier IGNITE_TORCH = new Identifier(NAMESPACE, "ignite_torch");
    public static final Identifier IGNITE_LANTERN = new Identifier(NAMESPACE, "ignite_lantern");

    // Flints
    public static Item FLINT_AND_FLINT = null;
    public static Item FLINT_AND_GOLD = null;
    public static Item FLINT_AND_DIAMOND = null;

    @Override
    public void onInitialize() {
        Registry.register( Registry.BLOCK, new Identifier("extinguished_wall_torch"), EXTINGUISHED_WALL_TORCH);
        Registry.register( Registry.BLOCK, new Identifier("extinguished_torch"), EXTINGUISHED_TORCH);
        Registry.register( Registry.ITEM, new Identifier("extinguished_torch"), EXTINGUISHED_TORCH_ITEM);
        Registry.register( Registry.BLOCK, new Identifier("extinguished_lantern"), EXTINGUISHED_LANTERN);
        Registry.register( Registry.ITEM, new Identifier("extinguished_lantern"), EXTINGUISHED_LANTERN_ITEM);
        Registry.register( Registry.BLOCK, new Identifier("extinguished_wall_soul_torch"), EXTINGUISHED_WALL_SOUL_TORCH);
        Registry.register( Registry.BLOCK, new Identifier("extinguished_soul_torch"), EXTINGUISHED_SOUL_TORCH);
        Registry.register( Registry.ITEM, new Identifier("extinguished_soul_torch"), EXTINGUISHED_SOUL_TORCH_ITEM);
        Registry.register( Registry.BLOCK, new Identifier("extinguished_soul_lantern"), EXTINGUISHED_SOUL_LANTERN);
        Registry.register( Registry.ITEM, new Identifier("extinguished_soul_lantern"), EXTINGUISHED_SOUL_LANTERN_ITEM);

        // flints
        if( SETTINGS.flints ) {
            FLINT_AND_FLINT = new FlintAndSteelItem(new Item.Settings().maxDamage(SETTINGS.durability_flint).group(ItemGroup.TOOLS));
            FLINT_AND_GOLD = new FlintAndSteelItem(new Item.Settings().maxDamage(SETTINGS.durability_gold).group(ItemGroup.TOOLS));
            FLINT_AND_DIAMOND = new FlintAndSteelItem(new Item.Settings().maxDamage(SETTINGS.durability_diamond).group(ItemGroup.TOOLS));
            Registry.register(Registry.ITEM, new Identifier(NAMESPACE, "flint_and_flint"), FLINT_AND_FLINT);
            Registry.register(Registry.ITEM, new Identifier(NAMESPACE, "flint_and_gold"), FLINT_AND_GOLD);
            Registry.register(Registry.ITEM, new Identifier(NAMESPACE, "flint_and_diamond"), FLINT_AND_DIAMOND);

            // dynamically create recipes
            Recipes.init();
        }

        // tags
        EXTINGUISHABLE_CAMPFIRES = TagRegistry.block( new Identifier(NAMESPACE, "extinguishable_campfires") );
        EXTINGUISHABLE_LANTERNS = TagRegistry.block( new Identifier(NAMESPACE, "extinguishable_lanterns") );
        EXTINGUISHABLE_TORCHES = TagRegistry.block( new Identifier(NAMESPACE, "extinguishable_torches") );
        EXTINGUISHABLE = TagRegistry.block( new Identifier(NAMESPACE, "extinguishable") );

        // stats
        registerStat(EXTINGUISH_TORCH);
        registerStat(EXTINGUISH_LANTERN);
        registerStat(IGNITE_TORCH);
        registerStat(IGNITE_LANTERN);
    }

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(),
                EXTINGUISHED_WALL_TORCH,
                EXTINGUISHED_TORCH,
                EXTINGUISHED_LANTERN,
                EXTINGUISHED_WALL_SOUL_TORCH,
                EXTINGUISHED_SOUL_TORCH,
                EXTINGUISHED_SOUL_LANTERN
        );
    }

    private static void registerStat( Identifier id ) {
        Registry.register(Registry.CUSTOM_STAT, id, id);
        Stats.CUSTOM.getOrCreateStat(id, StatFormatter.DEFAULT);
    }

}
