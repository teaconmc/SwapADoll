package com.github.tartaricacid.swapadoll.client.renderer;

import com.github.tartaricacid.swapadoll.client.model.PlayerDollModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.PlayerSkinRenderCache;
import net.minecraft.client.renderer.PlayerSkinRenderCache.RenderInfo;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

public class PlayerDollItemRenderer implements SpecialModelRenderer<RenderInfo> {
    private final PlayerSkinRenderCache playerSkinRenderCache;
    private final PlayerDollModel slimModel;
    private final PlayerDollModel wideModel;

    public PlayerDollItemRenderer(PlayerSkinRenderCache playerSkinRenderCache) {
        this.playerSkinRenderCache = playerSkinRenderCache;
        this.slimModel = PlayerDollModel.SLIM_MODEL.get();
        this.wideModel = PlayerDollModel.WIDE_MODEL.get();
    }

    @Override
    public void submit(
            @Nullable RenderInfo argument, PoseStack poseStack, SubmitNodeCollector collector,
            int light, int overlay, boolean hasFoil, int outlineColor
    ) {
        RenderType renderType;
        PlayerDollModel renderModel;
        if (argument == null) {
            renderType = PlayerSkinRenderCache.DEFAULT_PLAYER_SKIN_RENDER_TYPE;
            renderModel = slimModel;
        } else {
            renderType = argument.renderType();
            PlayerModelType model = argument.playerSkin().model();
            if (model == PlayerModelType.SLIM) {
                renderModel = slimModel;
            } else {
                renderModel = wideModel;
            }
        }
        collector.submitModel(renderModel, Unit.INSTANCE, poseStack, renderType,
                light, OverlayTexture.NO_OVERLAY, outlineColor, null);
    }

    @Override
    public void getExtents(Consumer<Vector3fc> output) {
        PoseStack poseStack = new PoseStack();
        this.wideModel.root().getExtentsForGui(poseStack, output);
    }

    @Override
    @Nullable
    public RenderInfo extractArgument(ItemStack stack) {
        ResolvableProfile profile = stack.get(DataComponents.PROFILE);
        return profile == null ? null : this.playerSkinRenderCache.getOrDefault(profile);
    }

    public record Unbaked() implements SpecialModelRenderer.Unbaked<RenderInfo> {
        public static final MapCodec<PlayerDollItemRenderer.Unbaked> MAP_CODEC = MapCodec.unit(PlayerDollItemRenderer.Unbaked::new);

        @Override
        public MapCodec<PlayerDollItemRenderer.Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public PlayerDollItemRenderer bake(SpecialModelRenderer.BakingContext context) {
            return new PlayerDollItemRenderer(context.playerSkinRenderCache());
        }
    }
}
