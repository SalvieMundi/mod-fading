package net.darktree.fading.util;

import net.darktree.fading.Fading;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    private static final List<Item> fuelArray = createFuelMap();

    private static List<Item> createFuelMap() {
        List<Item> arr = new ArrayList<>();
        AbstractFurnaceBlockEntity.createFuelTimeMap().forEach( (k, v) -> arr.add(k) );
        return arr;
    }

    public static boolean isFuel(ItemStack stack) {
        return fuelArray.contains( stack.getItem() );
    }

    public static ItemStack igniteItemStack(ItemStack stack) {
        Item item = stack.getItem();
        if( item == Fading.EXTINGUISHED_TORCH_ITEM ) return new ItemStack( Items.TORCH, stack.getCount() );
        if( item == Fading.EXTINGUISHED_SOUL_TORCH_ITEM ) return new ItemStack( Items.SOUL_TORCH, stack.getCount() );
        return ItemStack.EMPTY;
    }

    public static void playItemIgniteSound( BlockPos pos, World world ) {
        world.playSound( null, pos, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F );
    }

    public static void playExtinguishSound( BlockPos pos, World world ) {
        world.playSound( null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.04F, 2.0F );
    }

    @Environment(EnvType.CLIENT)
    public static void playWallTorchSmokeEffect( BlockPos pos, World world ) {
        BlockState state = world.getBlockState(pos);
        Direction dir = state.get(WallTorchBlock.FACING).getOpposite();
        playSmokeEffect( (ServerWorld) world, pos, 0.27 * dir.getOffsetX(), 0.22, 0.27 * dir.getOffsetZ());
    }

    @Environment(EnvType.CLIENT)
    public static void playTorchSmokeEffect( BlockPos pos, World world ) {
        playSmokeEffect( (ServerWorld) world, pos, 0, 0, 0);
    }

    private static void playSmokeEffect(ServerWorld world, BlockPos pos, double a, double b, double c) {
        double d = pos.getX() + 0.5D + a;
        double e = pos.getY() + 0.7D + b;
        double f = pos.getZ() + 0.5D + c;
        world.spawnParticles(ParticleTypes.SMOKE, d, e, f, 2, 0, 0, 0, 0);
    }

}
