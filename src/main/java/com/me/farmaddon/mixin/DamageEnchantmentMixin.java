package com.me.farmaddon.mixin;

import com.me.farmaddon.item.ScytheItem;
import net.minecraft.enchantment.DamageEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DamageEnchantment.class)
public abstract class DamageEnchantmentMixin extends Enchantment {
	private DamageEnchantmentMixin(Rarity weight, EnchantmentTarget target, EquipmentSlot[] slotTypes) { super(weight, target, slotTypes); }

	@Inject(method = "isAcceptableItem", at = @At("HEAD"), cancellable = true)
	public void farmaddon$sharpnessScytheAcceptance(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
		String enchantmentName = getName(1).getString();
		int i = enchantmentName.indexOf(' ');
		enchantmentName = enchantmentName.substring(0, i).toLowerCase();
		if (enchantmentName.equals("sharpness") && itemStack.getItem() instanceof ScytheItem)
				cir.setReturnValue(true);
	}
}
