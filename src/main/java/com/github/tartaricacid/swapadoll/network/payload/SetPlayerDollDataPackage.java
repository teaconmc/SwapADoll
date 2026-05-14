package com.github.tartaricacid.swapadoll.network.payload;

import com.github.tartaricacid.swapadoll.SwapADoll;
import com.github.tartaricacid.swapadoll.blockentity.PlayerDollBlockEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetPlayerDollDataPackage(
        BlockPos pos,
        String playerId, String shortContent, String longContent
) implements CustomPacketPayload {
    public static final Type<SetPlayerDollDataPackage> TYPE = new Type<>(
            Identifier.fromNamespaceAndPath(SwapADoll.MOD_ID, "set_player_doll_data"));

    public static final StreamCodec<ByteBuf, SetPlayerDollDataPackage> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, SetPlayerDollDataPackage::pos,
            ByteBufCodecs.STRING_UTF8, SetPlayerDollDataPackage::playerId,
            ByteBufCodecs.STRING_UTF8, SetPlayerDollDataPackage::shortContent,
            ByteBufCodecs.STRING_UTF8, SetPlayerDollDataPackage::longContent,
            SetPlayerDollDataPackage::new
    );


    public static void handle(SetPlayerDollDataPackage message, IPayloadContext context) {
        if (context.flow().isServerbound()) {
            context.enqueueWork(() -> {
                Player player = context.player();
                // 必须是创造模式玩家
                if (!player.isCreative()) {
                    return;
                }
                if (player.level().getBlockEntity(message.pos) instanceof PlayerDollBlockEntity doll) {
                    doll.setData(message.playerId, message.shortContent, message.longContent);
                }
            });
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
