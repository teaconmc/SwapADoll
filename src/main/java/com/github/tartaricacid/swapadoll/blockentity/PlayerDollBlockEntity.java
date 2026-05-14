package com.github.tartaricacid.swapadoll.blockentity;

import com.github.tartaricacid.swapadoll.init.ModBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.StringUtil;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.Locale;

public class PlayerDollBlockEntity extends BlockEntity {
    private static final String TAG_PROFILE = "profile";
    private static final String TAG_POSE = "pose";
    private static final String TAG_SHORT_CONTENT = "short_content";
    private static final String TAG_LONG_CONTENT = "long_content";

    private @Nullable ResolvableProfile profile;
    private Pose pose = Pose.DEFAULT;
    /**
     * 简短内容，渲染在玩偶头顶
     */
    private String shortContent = "";
    /**
     * 长内容，右键玩偶后，打印在玩家的聊天框里，支持多行，使用 \n 分隔
     */
    private String longContent = "";

    public PlayerDollBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlocks.PLAYER_DOLL_BE.get(), blockPos, blockState);
    }

    @Override
    public void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.storeNullable(TAG_PROFILE, ResolvableProfile.CODEC, this.profile);
        output.storeNullable(TAG_POSE, Pose.CODEC, this.pose);
        output.store(TAG_SHORT_CONTENT, Codec.STRING, this.shortContent);
        output.store(TAG_LONG_CONTENT, Codec.STRING, this.longContent);
    }

    @Override
    public void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.profile = input.read(TAG_PROFILE, ResolvableProfile.CODEC).orElse(null);
        this.pose = input.read(TAG_POSE, Pose.CODEC).orElse(Pose.DEFAULT);
        this.shortContent = input.read(TAG_SHORT_CONTENT, Codec.STRING).orElse("");
        this.longContent = input.read(TAG_LONG_CONTENT, Codec.STRING).orElse("");
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return this.saveCustomOnly(provider);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter components) {
        super.applyImplicitComponents(components);
        this.profile = components.get(DataComponents.PROFILE);
    }

    public @Nullable ResolvableProfile getProfile() {
        return profile;
    }

    public void markDirty() {
        this.setChanged();
        if (level != null) {
            BlockState state = level.getBlockState(worldPosition);
            level.sendBlockUpdated(worldPosition, state, state, Block.UPDATE_ALL);
        }
    }

    public Pose getPose() {
        return pose;
    }

    public String getShortContent() {
        return shortContent;
    }

    public String getLongContent() {
        return longContent;
    }

    public void setData(String playerId, String shortContent, String longContent) {
        if (StringUtil.isNullOrEmpty(playerId)) {
            this.profile = null;
        } else {
            this.profile = ResolvableProfile.createUnresolved(playerId);
        }
        this.shortContent = shortContent;
        this.longContent = longContent;
        this.markDirty();
    }

    public enum Pose implements StringRepresentable {
        DEFAULT;

        public static final EnumCodec<Pose> CODEC = StringRepresentable.fromEnum(Pose::values);

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase(Locale.ENGLISH);
        }
    }
}
