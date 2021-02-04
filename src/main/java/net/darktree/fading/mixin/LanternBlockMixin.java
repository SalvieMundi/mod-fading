package net.darktree.fading.mixin;

import net.darktree.fading.Fading;
import net.darktree.fading.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LanternBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Random;

@Mixin(LanternBlock.class)
public abstract class LanternBlockMixin extends Block {

    public LanternBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if( world.getBlockTickScheduler().isScheduled(pos, (LanternBlock) (Object) this) ) {
            schedule( world, pos );
        }

        super.neighborUpdate(state, world, pos, block, fromPos, notify);
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if( world.hasRain(pos) && world.random.nextInt(Fading.SETTINGS.rain_lantern_rarity) == 0 ) {
            scheduledTick(state, world, pos, random);
        }
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        schedule(world, pos);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        world.setBlockState( pos, getUnlitState( state.getBlock() ).with( Properties.HANGING, state.get(Properties.HANGING) ) );
        Utils.playExtinguishSound( pos, world );
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if( !world.isClient && player.getStackInHand(hand).isEmpty() && player.isSneaking() ) {
            scheduledTick( state, (ServerWorld) world, pos, world.random );
            return ActionResult.SUCCESS;
        }

        return super.onUse(state, world, pos, player, hand, hit);
    }

    private void schedule( World world, BlockPos pos ) {
        world.getBlockTickScheduler().schedule(pos, (LanternBlock) (Object) this, Utils.getLanternTime(world));
    }

    public BlockState getUnlitState( Block block ) {
        if( block == Blocks.LANTERN ) return Fading.EXTINGUISHED_LANTERN.getDefaultState();
        return Fading.EXTINGUISHED_SOUL_LANTERN.getDefaultState();
    }

}
