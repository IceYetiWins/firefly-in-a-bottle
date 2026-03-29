package com.iceyetiwins.fireflyInABottle.mixin;

import com.iceyetiwins.fireflyInABottle.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BottleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BottleItem.class)
public class GlassBottleMixin {

    @Inject(method = "use", at = @At("TAIL"), cancellable = true)
    private void injectUse(Level world, Player user, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (cir.getReturnValue().consumesAction()) return;

        ItemStack itemStack = user.getItemInHand(hand);
        BlockHitResult hitResult = (BlockHitResult) user.pick(user.blockInteractionRange(), 0.0F, false);

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockPos = hitResult.getBlockPos();
            Block block = world.getBlockState(blockPos).getBlock();

            if (block == Blocks.FIREFLY_BUSH) {
                if (!world.isClientSide()) {
                    world.setBlockAndUpdate(blockPos, Blocks.BUSH.defaultBlockState());

                    world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.BEE_POLLINATE, SoundSource.NEUTRAL, 1.0F, 1.0F);
                    world.gameEvent(user, GameEvent.BLOCK_CHANGE, blockPos);

                    ItemStack fireflyBottle = new ItemStack(ModItems.FIREFLY_BOTTLE);

                    if (!user.getAbilities().instabuild) {
                        if (itemStack.getCount() == 1) {
                            user.setItemInHand(hand, fireflyBottle);
                        } else {
                            itemStack.shrink(1);
                            if (!user.getInventory().add(fireflyBottle)) {
                                user.drop(fireflyBottle, false);
                            }
                        }
                    } else {
                        user.getInventory().add(fireflyBottle);
                    }

                    cir.setReturnValue(InteractionResult.SUCCESS);
                }
            }
        }
    }
}
