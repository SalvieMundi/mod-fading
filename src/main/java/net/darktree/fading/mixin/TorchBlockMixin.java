package net.darktree.fading.mixin;

import net.darktree.fading.Fading;
import net.darktree.fading.util.Utils;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Random;

@Mixin(TorchBlock.class)
public abstract class TorchBlockMixin extends Block {

    public TorchBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if( world.getBlockTickScheduler().isScheduled(pos, (TorchBlock) (Object) this) ) {
            schedule( world, pos );
        }

        super.neighborUpdate(state, world, pos, block, fromPos, notify);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if( Utils.isNotRedstoneTorch(this) && Utils.isVanilla(this) ) {
            schedule(world, pos);
        }else{
            super.onBlockAdded(state, world, pos, oldState, notify);
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if( Utils.isNotRedstoneTorch(this) && Utils.isVanilla(this) ) {
            world.setBlockState(pos, getUnlitState(state.getBlock()));
            Utils.playExtinguishSound(pos, world);
            Utils.playTorchSmokeEffect(pos, world);
        }else{
            super.scheduledTick(state, world, pos, random);
        }
    }

    private void schedule( World world, BlockPos pos ) {
        world.getBlockTickScheduler().schedule(pos, (TorchBlock) (Object) this, Fading.SETTINGS.torchTime.getTicks(world.random));
    }

    public BlockState getUnlitState( Block block ) {
        if( block == Blocks.TORCH ) return Fading.EXTINGUISHED_TORCH.getDefaultState();
        return Fading.EXTINGUISHED_SOUL_TORCH.getDefaultState();
    }

}
