package com.github.tartaricacid.swapadoll.client.renderer;

import com.github.tartaricacid.swapadoll.block.PlayerDollBlock;
import com.github.tartaricacid.swapadoll.blockentity.PlayerDollBlockEntity;
import com.github.tartaricacid.swapadoll.client.model.PlayerDollModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.PlayerSkinRenderCache;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import static net.minecraft.client.renderer.blockentity.SkullBlockRenderer.TRANSFORMATIONS;

public class PlayerDollBlockRenderer implements BlockEntityRenderer<PlayerDollBlockEntity, PlayerDollRenderState> {
    private final PlayerSkinRenderCache playerSkinRenderCache;
    private final PlayerDollModel slimModel;
    private final PlayerDollModel wideModel;

    public PlayerDollBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.playerSkinRenderCache = context.playerSkinRenderCache();
        this.slimModel = PlayerDollModel.SLIM_MODEL.get();
        this.wideModel = PlayerDollModel.WIDE_MODEL.get();
    }

    @Override
    public PlayerDollRenderState createRenderState() {
        return new PlayerDollRenderState();
    }

    @Override
    public void extractRenderState(
            PlayerDollBlockEntity doll,
            PlayerDollRenderState state,
            float partialTicks,
            Vec3 cameraPosition,
            ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress
    ) {
        BlockEntityRenderer.super.extractRenderState(doll, state, partialTicks, cameraPosition, breakProgress);
        state.pose = doll.getPose();

        int rot = doll.getBlockState().getValue(PlayerDollBlock.ROTATION);
        state.transformation = TRANSFORMATIONS.freeTransformations(rot);

        ResolvableProfile profile = doll.getProfile();
        if (profile != null) {
            state.renderType = this.playerSkinRenderCache.getOrDefault(profile).renderType();
            profile.skinPatch().model().ifPresentOrElse(model -> {
                if (model == PlayerModelType.SLIM) {
                    state.model = this.slimModel;
                } else {
                    state.model = this.wideModel;
                }
            }, () -> state.model = this.slimModel);
        } else {
            state.renderType = PlayerSkinRenderCache.DEFAULT_PLAYER_SKIN_RENDER_TYPE;
            state.model = this.slimModel;
        }
    }

    @Override
    public void submit(PlayerDollRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (state.model == null) {
            return;
        }
        poseStack.pushPose();
        poseStack.translate(0, 1.5, 0);
        poseStack.mulPose(state.transformation);
        submitNodeCollector.submitModel(state.model, Unit.INSTANCE, poseStack, state.renderType,
                state.lightCoords, OverlayTexture.NO_OVERLAY, 0, state.breakProgress);
        poseStack.popPose();
    }
}