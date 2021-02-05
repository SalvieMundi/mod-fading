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
        if( Utils.isExtinguishable(this) ) {
            Utils.setUnlitBlock(world, pos, Fading.EXTINGUISHED_WALL_TORCH.getDefaultState()
                    .with(HorizontalFacingBlock.FACING, state.get(HorizontalFacingBlock.FACING))
            );
            Utils.playExtinguishSound( pos, world );
            Utils.playWallTorchSmokeEffect( pos, world );
        }
    }

}
