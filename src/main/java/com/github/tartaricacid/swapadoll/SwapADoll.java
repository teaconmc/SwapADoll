package com.github.tartaricacid.swapadoll;

import com.github.tartaricacid.swapadoll.init.ModBlocks;
import com.github.tartaricacid.swapadoll.init.ModItems;
import com.github.tartaricacid.swapadoll.init.ModTabs;
import com.github.tartaricacid.swapadoll.network.NetworkHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(SwapADoll.MOD_ID)
public class SwapADoll {
    public static final String MOD_ID = "swapadoll";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public SwapADoll(IEventBus eventBus, ModContainer modContainer) {
        ModBlocks.BLOCKS.register(eventBus);
        ModBlocks.BLOCK_ENTITIES.register(eventBus);
        ModItems.ITEMS.register(eventBus);
        ModTabs.TABS.register(eventBus);

        eventBus.addListener(NetworkHandler::registerPacket);
    }
}