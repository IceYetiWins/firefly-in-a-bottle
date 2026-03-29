package com.iceyetiwins.fireflyInABottle;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class FireflyBottle extends Item {
    public FireflyBottle(Properties settings){
        super(settings);
    }

    @Override
    public InteractionResult use (Level world, Player user, InteractionHand hand){
        ItemStack itemStack = user.getItemInHand(hand);
        BlockHitResult hitResult = (BlockHitResult) user.pick(user.blockInteractionRange(), 0.0F, false);

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockPos = hitResult.getBlockPos();
            Block block = world.getBlockState(blockPos).getBlock();

            if (block == Blocks.BUSH) {
                if (!world.isClientSide()) {
                    world.setBlockAndUpdate(blockPos, Blocks.FIREFLY_BUSH.defaultBlockState());

                    world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.BEE_POLLINATE, SoundSource.NEUTRAL, 1.0F, 1.0F);
                    world.gameEvent(user, GameEvent.BLOCK_CHANGE, blockPos);

                    ItemStack glassBottle = new ItemStack(Items.GLASS_BOTTLE);

                    if (!user.getAbilities().instabuild) {
                        if (itemStack.getCount() == 1) {
                            user.setItemInHand(hand, glassBottle);
                        } else {
                            itemStack.shrink(1);
                            if (!user.getInventory().add(glassBottle)) {
                                user.drop(glassBottle, false);
                            }
                        }
                    } else {
                        user.getInventory().add(glassBottle);
                    }

                    return InteractionResult.SUCCESS;
                }
            }
        }

        return InteractionResult.PASS;
    }
}
