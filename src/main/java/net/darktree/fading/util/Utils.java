package net.darktree.fading.util;

import net.darktree.fading.Fading;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    private static final List<Item> fuelArray = createFuelList();

    private static List<Item> createFuelList() {
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

    public static void playWallTorchSmokeEffect( BlockPos pos, World world ) {
        BlockState state = world.getBlockState(pos);
        Direction dir = state.get(WallTorchBlock.FACING).getOpposite();
        playSmokeEffect( (ServerWorld) world, pos, 0.27 * dir.getOffsetX(), 0.22, 0.27 * dir.getOffsetZ());
    }

    public static void playTorchSmokeEffect( BlockPos pos, World world ) {
        playSmokeEffect( (ServerWorld) world, pos, 0, 0, 0);
    }

    private static void playSmokeEffect(ServerWorld world, BlockPos pos, double a, double b, double c) {
        double d = pos.getX() + 0.5D + a;
        double e = pos.getY() + 0.7D + b;
        double f = pos.getZ() + 0.5D + c;
        world.spawnParticles(ParticleTypes.SMOKE, d, e, f, 2, 0, 0, 0, 0);
    }

    public static boolean testAndHandle(ItemStack stack, PlayerEntity player, Hand hand, BlockPos pos, World world) {

        if( stack.getItem() instanceof FlintAndSteelItem ) {
            stack.damage(1, player, (p) -> p.sendToolBreakStatus(hand));
            world.playSound(null, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, world.random.nextFloat() * 0.4F + 0.8F);
            return true;
        }

        if( stack.getItem() == Items.FIRE_CHARGE ) {
            stack.decrement(1);
            world.playSound(null, pos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0F, world.random.nextFloat() * 0.4F + 0.8F);
            return true;
        }

        return false;
    }

    public static int getTorchTime(World world) {
        return Fading.SETTINGS.torchTime.getTicks( world.random );
    }

    public static int getLanternTime(World world) {
        return Fading.SETTINGS.lanternTime.getTicks( world.random );
    }

    public static int getCampfireTime(World world) {
        return Fading.SETTINGS.campfireTime.getTicks( world.random );
    }

    public static boolean isExtinguishable( Block block ) {
        return Fading.EXTINGUISHABLE.contains(block);
    }

    public static void setUnlitBlock( World world, BlockPos pos, BlockState state ) {
        if( !Fading.SETTINGS.disintegrate || world.hasRain(pos) ) {
            world.setBlockState(pos, state);
        }else{
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
    }

}
