package net.darktree.fading.block;

import net.darktree.fading.Fading;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TorchBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class ExtinguishedTorchBlock extends TorchBlock {

    public ExtinguishedTorchBlock( Settings settings ) {
        super( settings, null );
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean moved) {
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        if( itemStack.getItem() instanceof FlintAndSteelItem ) {
            itemStack.damage(1, player, (p) -> p.sendToolBreakStatus( hand ));
            world.playSound(null, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, world.random.nextFloat() * 0.4F + 0.8F);
            world.setBlockState( pos, getLitState( state.getBlock() ) );
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    public BlockState getLitState( Block block ) {
        if( block == Fading.EXTINGUISHED_TORCH ) return Blocks.TORCH.getDefaultState();
        return Blocks.SOUL_TORCH.getDefaultState();
    }

}
