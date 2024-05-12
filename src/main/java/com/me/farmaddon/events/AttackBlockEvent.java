package com.me.farmaddon.events;

import com.me.farmaddon.item.ScytheItem;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.ActionResult;

public class AttackBlockEvent {
	public static void registerBlockAttacks() {
		AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
			ItemStack stack = player.getStackInHand(hand);
			if (stack.getItem() instanceof ScytheItem) {
				if (!world.getBlockState(pos).isIn(BlockTags.CROPS))
					return ActionResult.FAIL;
			}

			return ActionResult.PASS;
		});
	}
}
