package com.github.tartaricacid.swapadoll.blockentity;

import com.github.tartaricacid.swapadoll.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.Locale;

public class PlayerDollBlockEntity extends BlockEntity {
    private static final String TAG_PROFILE = "profile";
    private static final String TAG_POSE = "pose";

    private @Nullable ResolvableProfile profile;
    private Pose pose = Pose.DEFAULT;

    public PlayerDollBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlocks.PLAYER_DOLL_BE.get(), blockPos, blockState);
    }

    @Override
    public void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.storeNullable(TAG_PROFILE, ResolvableProfile.CODEC, this.profile);
        output.storeNullable(TAG_POSE, Pose.CODEC, this.pose);
    }

    @Override
    public void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.profile = input.read(TAG_PROFILE, ResolvableProfile.CODEC).orElse(null);
        this.pose = input.read(TAG_POSE, Pose.CODEC).orElse(Pose.DEFAULT);
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

    public Pose getPose() {
        return pose;
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
