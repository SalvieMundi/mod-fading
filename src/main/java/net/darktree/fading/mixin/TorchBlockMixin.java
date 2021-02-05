package net.darktree.fading.mixin;

import net.darktree.fading.Fading;
import net.darktree.fading.util.Utils;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
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
    public boolean hasRandomTicks(BlockState state) {
        return canFade() || randomTicks;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if( world.hasRain(pos) && world.random.nextInt(Fading.SETTINGS.rain_torch_rarity) == 0 && canFade() ) {
            scheduledTick(state, world, pos, random);
        }
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        scheduleIfApplicable(world, pos);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if( canFade() ) {
            Utils.setUnlitBlock(world, pos, Fading.EXTINGUISHED_TORCH.getDefaultState());
            Utils.playExtinguishSound(pos, world);
            Utils.playTorchSmokeEffect(pos, world);
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if( !world.isClient && player.getStackInHand(hand).isEmpty() && player.isSneaking() && canFade() ) {
            scheduledTick( state, (ServerWorld) world, pos, world.random );
            return ActionResult.SUCCESS;
        }

        return super.onUse(state, world, pos, player, hand, hit);
    }

    private void scheduleIfApplicable( World world, BlockPos pos ) {
        if( canFade() ) {
            world.getBlockTickScheduler().schedule(pos, (TorchBlock) (Object) this, Utils.getTorchTime(world));
        }
    }

    public boolean canFade() {
        return Utils.isExtinguishable(this);
    }

}
