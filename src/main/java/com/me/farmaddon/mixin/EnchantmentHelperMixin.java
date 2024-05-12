package com.me.farmaddon.mixin;

import com.google.common.collect.ImmutableList;
import com.me.farmaddon.item.ScytheItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {
	@Unique
	private static final List<Enchantment> APPLICABLE_ENCHANTMENTS = ImmutableList.of( // applicable in enchantment table
				Enchantments.SHARPNESS,
				Enchantments.UNBREAKING,
				Enchantments.VANISHING_CURSE
			);

	@Inject(method = "getPossibleEntries", at = @At("HEAD"), cancellable = true)
	private static void getPossibleEntries(int power, ItemStack stack, boolean treasureAllowed, CallbackInfoReturnable<List<EnchantmentLevelEntry>> cir) {
		if (stack.getItem() instanceof ScytheItem) {
			List<EnchantmentLevelEntry> enchantments = new ArrayList<>();

			for (Enchantment enchantment : APPLICABLE_ENCHANTMENTS) {
				for (int i = enchantment.getMaxLevel(); i > enchantment.getMinLevel() - 1; --i) {
					if (power < enchantment.getMinPower(i) || power > enchantment.getMaxPower(i)) continue;
					enchantments.add(new EnchantmentLevelEntry(enchantment, i));
				}
			}

			cir.setReturnValue(enchantments);
		}
	}
}
