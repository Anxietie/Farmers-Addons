package com.me.farmaddon.registry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collection;

import static com.me.farmaddon.FarmersAddons.MODID;

public class ItemRegister {
	private static final Collection<ItemStack> ITEMS = new ArrayList<>();

	private static ItemGroup FARMING_ADDONS = FabricItemGroup.builder()
			.icon(() -> new ItemStack(Items.WHEAT))
			.displayName(Text.literal("Farmers Addons"))
			.entries((((displayContext, entries) -> entries.addAll(ITEMS))))
			.build();

	public static void registerItems() {
		registerBlockItems();
	}

	public static void registerItemGroups() {
		register("farming_addons", FARMING_ADDONS);
	}

	private static void registerBlockItems() {
		BlockRegister.getBlocks().forEach(block -> register(Registries.BLOCK.getId(block).getPath(), new BlockItem(block, new Item.Settings())));
	}

	private static Item register(String id, Item item) {
		ITEMS.add(item.getDefaultStack());
		return Registry.register(Registries.ITEM, new Identifier(MODID, id), item);
	}

	private static ItemGroup register(String id, ItemGroup itemGroup) {
		return Registry.register(Registries.ITEM_GROUP, new Identifier(MODID, id), itemGroup);
	}

	public static Collection<ItemStack> getItems() { return ITEMS; }
}
