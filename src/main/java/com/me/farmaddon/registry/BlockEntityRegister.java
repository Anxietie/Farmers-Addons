package com.me.farmaddon.registry;

import com.me.farmaddon.block.entity.CropLabelBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static com.me.farmaddon.FarmersAddons.MODID;

public class BlockEntityRegister {
	public static final BlockEntityType<CropLabelBlockEntity> CROP_LABEL_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(CropLabelBlockEntity::new, BlockRegister.getBlocks().values().toArray(new Block[0])).build();

	public static void registerBlockEntities() {
		register("crop_label_block_entity", CROP_LABEL_BLOCK_ENTITY);
	}

	private static BlockEntityType<?> register(String id, BlockEntityType<?> blockEntity) {
		return Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(MODID, id), blockEntity);
	}
}
