package com.github.tartaricacid.swapadoll.client.renderer;

import com.github.tartaricacid.swapadoll.block.PlayerDollBlock;
import com.github.tartaricacid.swapadoll.blockentity.PlayerDollBlockEntity;
import com.github.tartaricacid.swapadoll.blockentity.PlayerDollBlockEntity.Pose;
import com.github.tartaricacid.swapadoll.client.model.PlayerDollModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.PlayerSkinRenderCache;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.StringUtil;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.List;

import static net.minecraft.client.renderer.blockentity.SkullBlockRenderer.TRANSFORMATIONS;

public class PlayerDollBlockRenderer implements BlockEntityRenderer<PlayerDollBlockEntity, PlayerDollRenderState> {
    private final PlayerSkinRenderCache playerSkinRenderCache;
    private final Font font;

    public PlayerDollBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.playerSkinRenderCache = context.playerSkinRenderCache();
        this.font = context.font();
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
        state.shortContext = doll.getShortContent();

        int rot = doll.getBlockState().getValue(PlayerDollBlock.ROTATION);
        state.transformation = TRANSFORMATIONS.freeTransformations(rot);

        ResolvableProfile profile = doll.getProfile();
        Pose pose = doll.getPose();

        if (profile != null) {
            state.renderType = this.playerSkinRenderCache.getOrDefault(profile).renderType();
            profile.skinPatch().model().ifPresentOrElse(
                    model -> state.model = PlayerDollModel.getModel(pose, model == PlayerModelType.SLIM),
                    () -> state.model = PlayerDollModel.getModel(pose, true)
            );
        } else {
            state.renderType = PlayerSkinRenderCache.DEFAULT_PLAYER_SKIN_RENDER_TYPE;
            state.model = PlayerDollModel.getModel(pose, true);
        }
    }

    @Override
    public void submit(PlayerDollRenderState state, PoseStack poseStack, SubmitNodeCollector submitNode, CameraRenderState camera) {
        if (state.model != null) {
            submitBody(state, poseStack, submitNode);
        }
        if (!StringUtil.isBlank(state.shortContext)) {
            submitText(state, poseStack, submitNode, camera);
        }
    }

    private void submitBody(PlayerDollRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector) {
        poseStack.pushPose();
        poseStack.translate(0, 1.5, 0);
        poseStack.mulPose(state.transformation);
        submitNodeCollector.submitModel(state.model, Unit.INSTANCE, poseStack, state.renderType,
                state.lightCoords, OverlayTexture.NO_OVERLAY, 0, state.breakProgress);
        poseStack.popPose();
    }

    private void submitText(PlayerDollRenderState state, PoseStack poseStack, SubmitNodeCollector submitNode, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.translate(0.5, 1.625, 0.5);
        poseStack.mulPose(Axis.YN.rotationDegrees(camera.yRot));
        poseStack.mulPose(Axis.XN.rotationDegrees(-camera.xRot));
        poseStack.scale(-0.025F, -0.025F, -0.025F);

        float opacity = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
        int bgColor = (int) (opacity * 255.0F) << 24;

        Component component = Component.literal(state.shortContext);
        List<FormattedCharSequence> split = this.font.split(component, 100);

        int yStart = 28 - split.size() * 11;
        for (FormattedCharSequence sequence : split) {
            int width = this.font.width(sequence);
            if (width > 0) {
                float currentLineWidth = (float) (-width / 2);
                submitNode.submitText(poseStack, currentLineWidth, yStart, sequence,
                        false, Font.DisplayMode.NORMAL, state.lightCoords,
                        0xFFFFFFFF, bgColor, 0);
            }
            yStart += 11;
        }

        poseStack.popPose();
    }
}