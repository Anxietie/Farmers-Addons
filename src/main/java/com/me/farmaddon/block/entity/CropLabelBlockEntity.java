package com.me.farmaddon.block.entity;

import com.me.farmaddon.registry.BlockEntityRegister;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class CropLabelBlockEntity extends BlockEntity {
	private ItemStack handledCrop = Items.AIR.getDefaultStack();

	public CropLabelBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntityRegister.CROP_LABEL_BLOCK_ENTITY, pos, state);
	}

	public ItemStack getHandledCrop() {
		return handledCrop;
	}
	public void setHandledCrop(ItemStack stack) {
		this.handledCrop = stack;
	}
	public boolean shouldRenderLabel() { return !handledCrop.isOf(Items.AIR); }

	@Override
	public void markDirty() {
		world.updateListeners(pos, getCachedState(), getCachedState(), 3);
		super.markDirty();
	}

	@Override
	public void writeNbt(NbtCompound nbt) {
		Identifier id = Registries.ITEM.getId(handledCrop.getItem());
		nbt.putString("internal_crop_namespace", id.getNamespace());
		nbt.putString("internal_crop_path", id.getPath());

		super.writeNbt(nbt);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);

		String namespace = nbt.getString("internal_crop_namespace");
		String path = nbt.getString("internal_crop_path");

		handledCrop = Registries.ITEM.get(new Identifier(namespace, path)).getDefaultStack();
	}

	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	@Override
	public NbtCompound toInitialChunkDataNbt() {
		return createNbt();
	}
}
