package com.me.farmaddon.registry;

import com.me.farmaddon.item.ScytheItem;
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

	public static final Item WOODEN_SCYTHE = new ScytheItem(ToolMaterials.WOOD, 0f, -3f, new Item.Settings());
	public static final Item STONE_SCYTHE = new ScytheItem(ToolMaterials.STONE, 0.5f, -3f, new Item.Settings());
	public static final Item IRON_SCYTHE = new ScytheItem(ToolMaterials.IRON, 1f, -3f, new Item.Settings());
	public static final Item DIAMOND_SCYTHE = new ScytheItem(ToolMaterials.DIAMOND, 1.5f, -3f, new Item.Settings());
	public static final Item GOLDEN_SCYTHE = new ScytheItem(ToolMaterials.GOLD, 2f, -3f, new Item.Settings());
	public static final Item NETHERITE_SCYTHE = new ScytheItem(ToolMaterials.NETHERITE, 2.5f, -3f, new Item.Settings());

	private static final ItemGroup FARMING_ADDONS = FabricItemGroup.builder()
			.icon(() -> new ItemStack(Items.WHEAT))
			.displayName(Text.literal("Farmers Addons"))
			.entries((((displayContext, entries) -> entries.addAll(ITEMS))))
			.build();

	public static void registerItems() {
		registerBlockItems();
		register("wooden_scythe", WOODEN_SCYTHE);
		register("stone_scythe", STONE_SCYTHE);
		register("iron_scythe", IRON_SCYTHE);
		register("diamond_scythe", DIAMOND_SCYTHE);
		register("golden_scythe", GOLDEN_SCYTHE);
		register("netherite_scythe", NETHERITE_SCYTHE);
	}

	public static void registerItemGroups() {
		register("farming_addons", FARMING_ADDONS);
	}

	private static void registerBlockItems() {
		BlockRegister.getBlocks().values().forEach(block -> register(Registries.BLOCK.getId(block).getPath(), new BlockItem(block, new Item.Settings())));
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
