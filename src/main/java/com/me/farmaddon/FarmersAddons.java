package com.me.farmaddon;

import com.me.farmaddon.registry.BlockEntityRegister;
import com.me.farmaddon.registry.BlockRegister;
import com.me.farmaddon.registry.ItemRegister;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FarmersAddons implements ModInitializer {
    public static final String MODID = "farmaddon";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    @Override
    public void onInitialize() {
        LOGGER.info("initializing");

        BlockRegister.registerBlocks();
        LOGGER.info("blocks registered");

        BlockEntityRegister.registerBlockEntities();
        LOGGER.info("block entities registered");

	    ItemRegister.registerItems();
        ItemRegister.registerItemGroups();
        LOGGER.info("items registered");
    }
}
