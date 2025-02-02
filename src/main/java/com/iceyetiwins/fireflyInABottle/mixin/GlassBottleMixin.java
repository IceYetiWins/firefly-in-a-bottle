package com.iceyetiwins.fireflyInABottle.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.GlassBottleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.potion.Potions;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GlassBottleItem.class)
public class GlassBottleMixin {

    @Inject(method = "use", at = @At("TAIL"), cancellable = true)
    private void injectUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (cir.getReturnValue().isAccepted()) return;

        ItemStack itemStack = user.getStackInHand(hand);
        BlockHitResult hitResult = (BlockHitResult) user.raycast(user.getBlockInteractionRange(), 0.0F, false);

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockPos = hitResult.getBlockPos();
            Block block = world.getBlockState(blockPos).getBlock();

            if (block == Blocks.FIREFLY_BUSH) {
                if (!world.isClient) {
                    world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                    world.emitGameEvent(user, GameEvent.FLUID_PICKUP, blockPos);

                    ItemStack waterBottle = PotionContentsComponent.createStack(Items.POTION, Potions.WATER);

                    if (!user.getAbilities().creativeMode) {
                        if (itemStack.getCount() == 1) {
                            user.setStackInHand(hand, waterBottle);
                        } else {
                            itemStack.decrement(1);
                            if (!user.getInventory().insertStack(waterBottle)) {
                                user.dropItem(waterBottle, false);
                            }
                        }
                    } else {
                        user.getInventory().insertStack(waterBottle);
                    }

                    cir.setReturnValue(ActionResult.SUCCESS);
                }
            }
        }
    }
}
