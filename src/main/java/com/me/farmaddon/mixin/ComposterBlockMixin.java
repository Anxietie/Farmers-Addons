package com.me.farmaddon.mixin;

import net.minecraft.block.ComposterBlock;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ComposterBlock.class)
public abstract class ComposterBlockMixin {
	@ModifyVariable(method = "emptyFullComposter", at = @At("STORE"))
	private static ItemEntity modifyItemEntity(ItemEntity original) {
		ItemStack stack = new ItemStack(Items.BONE_MEAL);
		stack.setCount(8);
		original.setStack(stack);
		return original;
	}
}
