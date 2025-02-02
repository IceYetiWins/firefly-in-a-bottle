package com.iceyetiwins.fireflyInABottle;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FireflyBottle extends Item {
    public FireflyBottle(Settings settings){
        super(settings);
    }

    @Override
    public ActionResult use (World world, PlayerEntity user, Hand hand){
        ItemStack itemStack = user.getStackInHand(hand);
        BlockHitResult hitResult = (BlockHitResult) user.raycast(user.getBlockInteractionRange(), 0.0F, false);

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockPos = hitResult.getBlockPos();
            Block block = world.getBlockState(blockPos).getBlock();

            if (block == Blocks.BUSH) {
                if (!world.isClient) {
                    world.setBlockState(blockPos, Blocks.FIREFLY_BUSH.getDefaultState());

                    world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_BEE_POLLINATE, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                    //world.emitGameEvent(user, GameEvent.FLUID_PICKUP, blockPos);

                    ItemStack glassBottle = new ItemStack(Items.GLASS_BOTTLE);

                    if (!user.getAbilities().creativeMode) {
                        if (itemStack.getCount() == 1) {
                            user.setStackInHand(hand, glassBottle);
                        } else {
                            itemStack.decrement(1);
                            if (!user.getInventory().insertStack(glassBottle)) {
                                user.dropItem(glassBottle, false);
                            }
                        }
                    } else {
                        user.getInventory().insertStack(glassBottle);
                    }

                    return ActionResult.SUCCESS;
                }
            }
        }

        return ActionResult.PASS;
    }
}
