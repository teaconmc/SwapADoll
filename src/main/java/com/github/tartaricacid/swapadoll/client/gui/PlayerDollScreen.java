package com.github.tartaricacid.swapadoll.client.gui;

import com.github.tartaricacid.swapadoll.blockentity.PlayerDollBlockEntity;
import com.github.tartaricacid.swapadoll.network.payload.SetPlayerDollDataPackage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.component.ResolvableProfile;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class PlayerDollScreen extends Screen {
    private final PlayerDollBlockEntity blockEntity;

    private EditBox playerId;
    private EditBox shortContent;
    private MultiLineEditBox longContent;

    public PlayerDollScreen(PlayerDollBlockEntity blockEntity) {
        super(Component.literal("Player Doll"));
        this.blockEntity = blockEntity;
    }

    public static void open(PlayerDollBlockEntity blockEntity) {
        PlayerDollScreen screen = new PlayerDollScreen(blockEntity);
        Minecraft.getInstance().setScreen(screen);
    }

    @Override
    protected void init() {
        int xo = (this.width - 240) / 2;
        int yo = (this.height - 200) / 2;

        ResolvableProfile profile = this.blockEntity.getProfile();
        this.playerId = new EditBox(this.font, xo, yo, 240, 20, Component.literal("Player Name"));
        this.playerId.setMaxLength(64);
        if (profile != null && profile.name().isPresent()) {
            this.playerId.setValue(profile.name().get());
        } else {
            this.playerId.setValue("");
        }

        this.shortContent = new EditBox(this.font, xo, yo + 35, 240, 20, Component.literal("Short Content"));
        this.shortContent.setMaxLength(512);
        this.shortContent.setValue(blockEntity.getShortContent());

        this.longContent = MultiLineEditBox.builder()
                .setX(xo).setY(yo + 70)
                .build(this.font, 240, 70, CommonComponents.EMPTY);
        this.longContent.setValue(blockEntity.getLongContent());

        this.addRenderableWidget(this.playerId);
        this.addRenderableWidget(this.shortContent);
        this.addRenderableWidget(this.longContent);

        this.addRenderableWidget(
                Button.builder(CommonComponents.GUI_CANCEL, button -> this.onClose())
                        .bounds(xo, yo + 150, 115, 20)
                        .build()
        );

        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> {
                    BlockPos pos = blockEntity.getBlockPos();
                    ClientPacketDistributor.sendToServer(new SetPlayerDollDataPackage(
                            pos,
                            this.playerId.getValue(),
                            this.shortContent.getValue(),
                            this.longContent.getValue()
                    ));
                    this.onClose();
                }).bounds(xo + 125, yo + 150, 115, 20).build()
        );
    }

    @Override
    public void resize(int width, int height) {
        String oldPlayerId = this.playerId.getValue();
        String oldShortContent = this.shortContent.getValue();
        String oldLongContent = this.longContent.getValue();

        this.init(width, height);

        this.playerId.setValue(oldPlayerId);
        this.shortContent.setValue(oldShortContent);
        this.longContent.setValue(oldLongContent);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);

        int color = 0xFF_EEEEEE;

        graphics.text(font, Component.translatable("gui.swapadoll.player_doll.player_id"),
                this.playerId.getX() + 2, this.playerId.getY() - 10, color);

        graphics.text(font, Component.translatable("gui.swapadoll.player_doll.short_content"),
                this.shortContent.getX() + 2, this.shortContent.getY() - 10, color);

        graphics.text(font, Component.translatable("gui.swapadoll.player_doll.long_content"),
                this.longContent.getX() + 2, this.longContent.getY() - 10, color);
    }
}
