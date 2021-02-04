package net.darktree.fading.block;

import net.darktree.fading.Fading;
import net.darktree.fading.util.Utils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LanternBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class ExtinguishedLanternBlock extends LanternBlock {

    public ExtinguishedLanternBlock( Settings settings ) {
        super( settings );
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean moved) {
    }

    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return false;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);

        if( Utils.testAndHandle(itemStack, player, hand, pos, world) ) {
            world.setBlockState( pos, getLitState( state.getBlock() ).with( Properties.HANGING, state.get(Properties.HANGING) ) );
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    public BlockState getLitState( Block block ) {
        if( block == Fading.EXTINGUISHED_LANTERN ) return Blocks.LANTERN.getDefaultState();
        return Blocks.SOUL_LANTERN.getDefaultState();
    }

}
