package com.me.farmaddon.registry;

import com.me.farmaddon.block.CropLabelBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSetType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static com.me.farmaddon.FarmersAddons.MODID;
import static net.minecraft.block.Blocks.OAK_PLANKS;

public class BlockRegister {
    public static final Block OAK_CROP_LABEL = new CropLabelBlock(FabricBlockSettings.create()
            .noCollision()
            .burnable()
            .pistonBehavior(PistonBehavior.DESTROY)
            .mapColor(OAK_PLANKS.getDefaultMapColor())
            .strength(3.0f),
    BlockSetType.OAK);

    public static void registerBlocks() {
        register("oak_crop_label", OAK_CROP_LABEL, true);
    }

    private static Block register(String id, Block block, boolean createBlockItem) {
        if (createBlockItem) Registry.register(Registries.ITEM, new Identifier(MODID, id), block.asItem());
        return Registry.register(Registries.BLOCK, new Identifier(MODID, id), block);
    }
}
