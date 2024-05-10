package com.me.farmaddon;

import com.me.farmaddon.block.entity.CropLabelBlockEntityRenderer;
import com.me.farmaddon.registry.BlockEntityRegister;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

import static com.me.farmaddon.FarmersAddons.LOGGER;

public class FarmersAddonsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockEntityRendererFactories.register(BlockEntityRegister.CROP_LABEL_BLOCK_ENTITY, CropLabelBlockEntityRenderer::new);
		LOGGER.info("entity rendering registered");
	}
}
