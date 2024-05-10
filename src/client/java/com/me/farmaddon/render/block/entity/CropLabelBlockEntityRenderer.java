package com.me.farmaddon.render.block.entity;

import com.me.farmaddon.block.CropLabelBlock;
import com.me.farmaddon.block.entity.CropLabelBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.*;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

@Environment(EnvType.CLIENT)
public class CropLabelBlockEntityRenderer implements BlockEntityRenderer<CropLabelBlockEntity> {
	public CropLabelBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}

	@Override
	public void render(CropLabelBlockEntity cropLabelBlockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.push();

		BlockState blockState = cropLabelBlockEntity.getCachedState();
		Direction direction = blockState.get(CropLabelBlock.FACING);

		renderItem(cropLabelBlockEntity, matrices, vertexConsumers, direction, 1, light);

		Vec3d blockEntityPos = cropLabelBlockEntity.getPos().toCenterPos();
		Vec3d crosshairTarget = MinecraftClient.getInstance().getBlockEntityRenderDispatcher().crosshairTarget.getPos();

		if (crosshairTarget.isInRange(blockEntityPos, 0.5f) && cropLabelBlockEntity.shouldRenderLabel())
			renderText(cropLabelBlockEntity.getHandledCrop().getName().getString(), matrices, vertexConsumers, light, direction);

		matrices.pop();
	}

	private void renderItem(CropLabelBlockEntity cropLabelBlockEntity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, Direction direction, int seed, int light) {
		float d = 0.299f; // distance from middle of block to crop label

		matrices.translate(0.5f + direction.getOffsetX() * d, 0.2f, 0.5f + direction.getOffsetZ() * d); // translate to center of block then add/subtract d based on direction
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(direction.getHorizontal() * 90 + (direction.getOffsetZ() + 1) * 180));
		matrices.scale(0.3f, 0.3f, 0.3f);

		ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
		itemRenderer.renderItem(cropLabelBlockEntity.getHandledCrop(),
				ModelTransformationMode.FIXED,
				light,
				OverlayTexture.DEFAULT_UV,
				matrices,
				vertexConsumers,
				cropLabelBlockEntity.getWorld(),
				seed);
	}

	private void renderText(String text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Direction direction) {
		Quaternionf yaw = MinecraftClient.getInstance().getBlockEntityRenderDispatcher().camera.getRotation();
		int dir = direction.getHorizontal();
		int xOffset = direction.getOffsetX();
		int zOffset = direction.getOffsetZ();

		matrices.translate(0f, 1.1f, 0f);
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float) ((zOffset + Math.abs(xOffset)) * dir * 90)));
		matrices.multiply(yaw);
		matrices.scale(-0.05f, -0.05f, 0.05f);
		Matrix4f matrix = matrices.peek().getPositionMatrix();

		TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
		float x = (float)(-textRenderer.getWidth(text) / 2);
		float opacity = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F);
		int backgroundColor = (int)(opacity * 255.0F) << 24;

		textRenderer.draw(text, x, 0, -1, false, matrix, vertexConsumers, TextRenderer.TextLayerType.NORMAL, backgroundColor, light);
	}
}
