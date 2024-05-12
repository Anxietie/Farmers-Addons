package com.me.farmaddon.events;

import com.me.farmaddon.item.ScytheItem;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.ActionResult;

public class UseBlockEvent {
	public static void registerBlockUses() {
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			ItemStack stack = player.getStackInHand(hand);
			if (stack.getItem() instanceof ScytheItem) {
				BlockState state = world.getBlockState(hitResult.getBlockPos());
				if (state.isIn(BlockTags.CROPS))
					return stack.getItem().useOnBlock(new ItemUsageContext(player, hand, hitResult));
			}

			return ActionResult.PASS;
		});
	}
}
