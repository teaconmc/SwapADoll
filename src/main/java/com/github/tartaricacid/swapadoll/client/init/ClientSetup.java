package com.github.tartaricacid.swapadoll.client.init;

import com.github.tartaricacid.swapadoll.SwapADoll;
import com.github.tartaricacid.swapadoll.client.renderer.PlayerDollBlockRenderer;
import com.github.tartaricacid.swapadoll.client.renderer.PlayerDollItemRenderer;
import com.github.tartaricacid.swapadoll.init.ModBlocks;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterSpecialModelRendererEvent;

@EventBusSubscriber(modid = SwapADoll.MOD_ID, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent evt) {
        BlockEntityRenderers.register(ModBlocks.PLAYER_DOLL_BE.get(), PlayerDollBlockRenderer::new);
    }

    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
    }

    @SubscribeEvent
    public static void registerSpecialModel(RegisterSpecialModelRendererEvent event) {
        event.register(Identifier.fromNamespaceAndPath(SwapADoll.MOD_ID, "player_doll"), PlayerDollItemRenderer.Unbaked.MAP_CODEC);
    }
}
