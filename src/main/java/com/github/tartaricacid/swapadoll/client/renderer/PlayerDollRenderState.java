package com.github.tartaricacid.swapadoll.client.renderer;

import com.github.tartaricacid.swapadoll.blockentity.PlayerDollBlockEntity.Pose;
import com.github.tartaricacid.swapadoll.client.model.PlayerDollModel;
import com.mojang.math.Transformation;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;

public class PlayerDollRenderState extends BlockEntityRenderState {
    public PlayerDollModel model;
    public Transformation transformation = Transformation.IDENTITY;
    public Pose pose = Pose.DEFAULT;
    public RenderType renderType;
}
