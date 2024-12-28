package com.me.farmaddon.mixin;

import com.me.farmaddon.item.ScytheItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.LuckEnchantment;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {
	@Unique
	private final Enchantment THIS = (Enchantment) (Object) this;

	@Inject(method = "isAcceptableItem", at = @At("HEAD"), cancellable = true)
	private void farmaddon$fortuneScytheAcceptance(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
		if ((Object) this instanceof LuckEnchantment) {
			String enchantmentName = THIS.getName(1).getString();
			int i = enchantmentName.indexOf(' ');
			enchantmentName = enchantmentName.substring(0, i).toLowerCase();
			if (enchantmentName.equals("fortune") && itemStack.getItem() instanceof ScytheItem)
				cir.setReturnValue(true);
		}
	}
}
