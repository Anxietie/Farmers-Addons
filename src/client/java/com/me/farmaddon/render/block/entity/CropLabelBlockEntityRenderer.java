package com.me.farmaddon.render.block.entity;

import com.me.farmaddon.block.CropLabelBlock;
import com.me.farmaddon.block.entity.CropLabelBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.*;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class CropLabelBlockEntityRenderer implements BlockEntityRenderer<CropLabelBlockEntity> {
	public CropLabelBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}

	@Override
	public void render(CropLabelBlockEntity cropLabelBlockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		BlockState blockState = cropLabelBlockEntity.getCachedState();
		Direction direction = blockState.get(CropLabelBlock.FACING);

		renderItem(cropLabelBlockEntity, matrices, vertexConsumers, direction, 1);
	}

	private void renderItem(CropLabelBlockEntity cropLabelBlockEntity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, Direction direction, int seed) {
		float d = 0.299f; // distance from middle of block to crop label

		matrices.push();
		matrices.translate(0.5f + direction.getOffsetX() * d, 0.2f, 0.5f + direction.getOffsetZ() * d); // translate to center of block then add/subtract d based on direction
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(direction.getHorizontal() * 90 + (direction.getOffsetZ() + 1) * 180));
		matrices.scale(0.3f, 0.3f, 0.3f);

		ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
		itemRenderer.renderItem(cropLabelBlockEntity.getHandledCrop(),
				ModelTransformationMode.FIXED,
				getLightLevel(cropLabelBlockEntity.getWorld(),
				cropLabelBlockEntity.getPos()),
				OverlayTexture.DEFAULT_UV,
				matrices,
				vertexConsumers,
				cropLabelBlockEntity.getWorld(),
				seed);

		matrices.pop();
	}

	private int getLightLevel(World world, BlockPos pos) {
		int blockLight = world.getLightLevel(LightType.BLOCK, pos);
		int skyLight = world.getLightLevel(LightType.SKY, pos);
		return LightmapTextureManager.pack(blockLight, skyLight);
	}
}
