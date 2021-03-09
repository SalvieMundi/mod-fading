package net.darktree.fading.mixin;

import net.darktree.fading.Fading;
import net.darktree.fading.util.Utils;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(CampfireBlock.class)
public abstract class CampfireBlockMixin extends Block {

    public CampfireBlockMixin(Settings settings) {
        super(settings);
    }

    @Shadow protected abstract boolean doesBlockCauseSignalFire(BlockState state);

    private static final IntProperty SIZE = IntProperty.of("size", 0, 3);

    @Inject(at=@At("TAIL"), method="<init>(ZILnet/minecraft/block/AbstractBlock$Settings;)V")
    public void init(boolean emitsParticles, int fireDamage, AbstractBlock.Settings settings, CallbackInfo ci) {
        setDefaultState( getDefaultState().with(SIZE, 0).with(CampfireBlock.LIT, false) );
    }

    @Inject(at=@At("HEAD"), method= "appendProperties(Lnet/minecraft/state/StateManager$Builder;)V")
    public void appendProperties(StateManager.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(SIZE);
    }

    @Inject(at=@At("HEAD"), method="onUse(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;", cancellable = true)
    public void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if( canFade() ) {
            if (state.get(CampfireBlock.LIT)) {
                ItemStack itemStack = player.getStackInHand(hand);

                if (!world.isClient && Utils.isFuel(itemStack)) {
                    int s = state.get(SIZE);
                    if (s != 3) {
                        world.setBlockState(pos, state.with(SIZE, s + 1));

                        if (!player.abilities.creativeMode) {
                            itemStack.decrement(1);
                        }

                        player.incrementStat(Stats.INTERACT_WITH_CAMPFIRE);
                        cir.setReturnValue(ActionResult.SUCCESS);
                    }
                }

                if (!world.isClient) {
                    ItemStack ignited = Utils.igniteItemStack(itemStack);
                    if (!ignited.isEmpty()) {
                        player.setStackInHand(hand, ignited);
                        Utils.playItemIgniteSound(pos, world);
                    }
                }

            } else {
                Item item = player.getStackInHand(hand).getItem();

                if (item instanceof FlintAndSteelItem || item == Items.FIRE_CHARGE) {
                    schedule(world, pos);
                }
            }
        }
    }

    @Inject(at=@At("HEAD"), method="spawnSmokeParticle(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;ZZ)V", cancellable = true)
    private static void spawnSmokeParticle(World world, BlockPos pos, boolean isSignal, boolean lotsOfSmoke, CallbackInfo ci) {
        if( world.random.nextInt(1 + (int) Math.pow( 2, world.getBlockState(pos).get(SIZE) ) ) == 0 ) {
            if( Utils.isExtinguishable( world.getBlockState(pos).getBlock() ) ) {
                ci.cancel();
            }
        }
    }

    @Inject(at=@At("HEAD"), method="getPlacementState(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/block/BlockState;", cancellable = true)
    public void getPlacementState(ItemPlacementContext ctx, CallbackInfoReturnable<BlockState> cir) {
        if( ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() != Fluids.WATER ) {
            cir.setReturnValue( getDefaultState().with( CampfireBlock.FACING, ctx.getPlayerFacing() ).with( CampfireBlock.SIGNAL_FIRE, doesBlockCauseSignalFire(ctx.getWorld().getBlockState( ctx.getBlockPos().down() ))) );
        }
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return canFade() || randomTicks;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if( world.hasRain(pos) && world.random.nextInt(Fading.SETTINGS.rain_campfire_rarity) == 0 && canFade() ) {
            scheduledTick(state, world, pos, random);
        }
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if( canFade() ) {
            schedule(world, pos);
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if( state.get(CampfireBlock.LIT) && canFade() ) {
            int s = state.get(SIZE);
            if( s == 0 ) {
                world.setBlockState( pos, state.with(CampfireBlock.LIT, false) );
                Utils.playExtinguishSound( pos, world );
            }else{
                world.setBlockState( pos, state.with(SIZE, s - 1) );
                schedule(world, pos);
            }
        }
    }

    private void schedule( World world, BlockPos pos ) {
        world.getBlockTickScheduler().schedule(pos, (CampfireBlock) (Object) this, Utils.getCampfireTime(world));
    }

    public boolean canFade() {
        return Utils.isExtinguishable(this);
    }

}
