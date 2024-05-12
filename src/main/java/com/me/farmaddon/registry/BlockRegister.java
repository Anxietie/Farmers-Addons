package com.me.farmaddon.registry;

import com.me.farmaddon.block.CropLabelBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.me.farmaddon.FarmersAddons.MODID;

public class BlockRegister {
	private static final Map<Identifier, Block> BLOCKS = new HashMap<>();

	public static void registerBlocks() {
		createBlocks();
	}

	private static Block register(String id, Block block) {
		Identifier identifier = new Identifier(MODID, id);
		BLOCKS.put(identifier, block);
		return Registry.register(Registries.BLOCK, identifier, block);
	}

	private static void createBlocks() {
		for (Planks p : Planks.values())
			register(formatId(p), createBlock(p));
	}

	private static String formatId(Planks planks) {
		String path = planks.getId().getPath();
		int i = path.indexOf("_planks");
		return path.substring(0, i) + "_crop_label";
	}

	private static Block createBlock(Planks planks) {
		FabricBlockSettings settings = FabricBlockSettings.copyOf(planks.getBlock())
				.breakInstantly()
				.noCollision()
				.pistonBehavior(PistonBehavior.DESTROY);

		if (planks != Planks.WARPED_PLANKS && planks != Planks.CRIMSON_PLANKS)
			settings.burnable();

		return new CropLabelBlock(settings);
	}

	public static Map<Identifier, Block> getBlocks() { return BLOCKS; }

	public enum Planks {
		ACACIA_PLANKS("acacia_planks"),
		BAMBOO_PLANKS("bamboo_planks"),
		BIRCH_PLANKS("birch_planks"),
		CHERRY_PLANKS("cherry_planks"),
		CRIMSON_PLANKS("crimson_planks"),
		DARK_OAK_PLANKS("dark_oak_planks"),
		JUNGLE_PLANKS("jungle_planks"),
		MANGROVE_PLANKS("mangrove_planks"),
		OAK_PLANKS("oak_planks"),
		SPRUCE_PLANKS("spruce_planks"),
		WARPED_PLANKS("warped_planks");

		private final Identifier id;
		Planks(String id) { this.id = new Identifier(id); }
		public Identifier getId() { return this.id; }
		public Block getBlock() { return Registries.BLOCK.get(id); }
	}
}
