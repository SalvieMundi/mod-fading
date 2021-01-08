package net.darktree.fading.mixin;

import net.darktree.fading.Fading;
import net.darktree.fading.util.Utils;
import net.minecraft.block.*;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Random;

@Mixin(WallTorchBlock.class)
public abstract class WallTorchBlockMixin extends TorchBlock {

    protected WallTorchBlockMixin(Settings settings, ParticleEffect particle) {
        super(settings, particle);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        world.setBlockState( pos, getWallUnlitState( state.getBlock() ).with(HorizontalFacingBlock.FACING, state.get(HorizontalFacingBlock.FACING)) );
        Utils.playExtinguishSound( pos, world );
        Utils.playWallTorchSmokeEffect( pos, world );
    }

    public BlockState getWallUnlitState( Block block ) {
        if( block == Blocks.WALL_TORCH ) return Fading.EXTINGUISHED_WALL_TORCH.getDefaultState();
        return Fading.EXTINGUISHED_WALL_SOUL_TORCH.getDefaultState();
    }

}
