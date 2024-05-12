package com.me.farmaddon.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.*;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.List;

public class ScytheItem extends ToolItem {
	private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

	public ScytheItem(ToolMaterial material, float attackDamage, float attackSpeed, Item.Settings settings) {
		super(material, settings);
		ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Tool modifier", attackDamage, EntityAttributeModifier.Operation.ADDITION));
		builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Tool modifier", attackSpeed, EntityAttributeModifier.Operation.ADDITION));
		this.attributeModifiers = builder.build();
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();

		if (!world.isClient()) {
			BlockPos pos = context.getBlockPos();
			BlockState state = world.getBlockState(pos);
			if (isHarvestableCrop(state)) {
				PlayerEntity playerEntity = context.getPlayer();
				ItemStack stack = context.getStack();
				if (playerEntity instanceof ServerPlayerEntity) {
					Criteria.ITEM_USED_ON_BLOCK.trigger((ServerPlayerEntity) playerEntity, pos, stack);
					if (!playerEntity.isCreative()) stack.damage(1, Random.create(), (ServerPlayerEntity) playerEntity);
				}

				int r = Math.max(0, Math.min(EnchantmentHelper.getLevel(Enchantments.SHARPNESS, stack) + 1, 5));
				for (BlockPos blockPos : BlockPos.iterateOutwards(pos, r, 0, r)) {
					BlockState blockState = world.getBlockState(blockPos);
					if (isHarvestableCrop(blockState)) {
						List<ItemStack> drops = Block.getDroppedStacks(blockState, (ServerWorld) world, blockPos, null);
						ItemStack mainDrop = drops.get(0);
						if (mainDrop != null) world.spawnEntity(new ItemEntity(world, blockPos.getX(), blockPos.getY(), blockPos.getZ(), mainDrop));

						for (int i = 1; i < drops.size(); i++) {
							ItemStack drop = drops.get(i);
							int count = (int) (Math.random() * drop.getCount());
							drop.setCount(count);

							world.spawnEntity(new ItemEntity(world, blockPos.getX(), blockPos.getY(), blockPos.getZ(), drop));
							world.setBlockState(blockPos, ((CropBlock) blockState.getBlock()).withAge(0));
							((ServerPlayerEntity) playerEntity).playSound(SoundEvents.BLOCK_CROP_BREAK, SoundCategory.BLOCKS, 0.5f, 1.0f);
							playerEntity.spawnSweepAttackParticles();
						}
					}
				}

				return ActionResult.SUCCESS;
			}
		}

		return super.useOnBlock(context);
	}

	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
		return slot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getAttributeModifiers(slot);
	}

	public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) { return world.getBlockState(pos).isIn(BlockTags.CROPS); }

	public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		stack.damage(1, attacker, (e) -> {
			e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
		});
		return true;
	}

	private boolean isHarvestableCrop(BlockState state) {
		return (state.isIn(BlockTags.CROPS) || state.getBlock() instanceof CropBlock) && ((CropBlock) state.getBlock()).isMature(state);
	}
}
