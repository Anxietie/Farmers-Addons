package com.me.farmaddon.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Block.class)
public abstract class BlockMixin {
    @Shadow
    public abstract void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance);
}
