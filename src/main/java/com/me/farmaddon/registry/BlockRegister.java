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

import static com.me.farmaddon.FarmersAddons.MODID;

public class BlockRegister {
	private static final Collection<Block> BLOCKS = new ArrayList<>();

	public static void registerBlocks() {
		createBlocks();
	}

	private static Block register(String id, Block block) {
		BLOCKS.add(block);
		return Registry.register(Registries.BLOCK, new Identifier(MODID, id), block);
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
		return new CropLabelBlock(FabricBlockSettings.copyOf(planks.getBlock())
				.noCollision()
				.pistonBehavior(PistonBehavior.DESTROY)
				.strength(0.5f));
	}

	public static Collection<Block> getBlocks() { return BLOCKS; }

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
