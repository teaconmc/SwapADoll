package com.github.tartaricacid.swapadoll.client.model;

import com.github.tartaricacid.swapadoll.SwapADoll;
import com.github.tartaricacid.swapadoll.blockentity.PlayerDollBlockEntity;
import com.github.tartaricacid.swapadoll.client.bedrock.AbstractBedrockModel;
import com.google.common.base.Suppliers;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;

import java.io.InputStream;
import java.util.function.Supplier;

public class PlayerDollModel extends AbstractBedrockModel<Unit> {
    private static final Identifier WIDE_DEFAULT = Identifier.fromNamespaceAndPath(SwapADoll.MOD_ID, "models/bedrock/wide/default.json");
    private static final Identifier WIDE_CASUAL_WAVE = Identifier.fromNamespaceAndPath(SwapADoll.MOD_ID, "models/bedrock/wide/casual_wave.json");
    private static final Identifier WIDE_CHEER = Identifier.fromNamespaceAndPath(SwapADoll.MOD_ID, "models/bedrock/wide/cheer.json");
    private static final Identifier WIDE_PERFECT_BALANCE = Identifier.fromNamespaceAndPath(SwapADoll.MOD_ID, "models/bedrock/wide/perfect_balance.json");
    private static final Identifier WIDE_SHY_POSE = Identifier.fromNamespaceAndPath(SwapADoll.MOD_ID, "models/bedrock/wide/shy_pose.json");
    private static final Identifier WIDE_ZOMBIE = Identifier.fromNamespaceAndPath(SwapADoll.MOD_ID, "models/bedrock/wide/zombie.json");

    private static final Identifier SLIM_DEFAULT = Identifier.fromNamespaceAndPath(SwapADoll.MOD_ID, "models/bedrock/slim/default.json");
    private static final Identifier SLIM_CASUAL_WAVE = Identifier.fromNamespaceAndPath(SwapADoll.MOD_ID, "models/bedrock/slim/casual_wave.json");
    private static final Identifier SLIM_CHEER = Identifier.fromNamespaceAndPath(SwapADoll.MOD_ID, "models/bedrock/slim/cheer.json");
    private static final Identifier SLIM_PERFECT_BALANCE = Identifier.fromNamespaceAndPath(SwapADoll.MOD_ID, "models/bedrock/slim/perfect_balance.json");
    private static final Identifier SLIM_SHY_POSE = Identifier.fromNamespaceAndPath(SwapADoll.MOD_ID, "models/bedrock/slim/shy_pose.json");
    private static final Identifier SLIM_ZOMBIE = Identifier.fromNamespaceAndPath(SwapADoll.MOD_ID, "models/bedrock/slim/zombie.json");

    public static final Supplier<PlayerDollModel> WIDE_DEFAULT_MODEL = Suppliers.memoize(() -> getModel(WIDE_DEFAULT));
    public static final Supplier<PlayerDollModel> WIDE_CASUAL_WAVE_MODEL = Suppliers.memoize(() -> getModel(WIDE_CASUAL_WAVE));
    public static final Supplier<PlayerDollModel> WIDE_CHEER_MODEL = Suppliers.memoize(() -> getModel(WIDE_CHEER));
    public static final Supplier<PlayerDollModel> WIDE_PERFECT_BALANCE_MODEL = Suppliers.memoize(() -> getModel(WIDE_PERFECT_BALANCE));
    public static final Supplier<PlayerDollModel> WIDE_SHY_POSE_MODEL = Suppliers.memoize(() -> getModel(WIDE_SHY_POSE));
    public static final Supplier<PlayerDollModel> WIDE_ZOMBIE_MODEL = Suppliers.memoize(() -> getModel(WIDE_ZOMBIE));

    public static final Supplier<PlayerDollModel> SLIM_DEFAULT_MODEL = Suppliers.memoize(() -> getModel(SLIM_DEFAULT));
    public static final Supplier<PlayerDollModel> SLIM_CASUAL_WAVE_MODEL = Suppliers.memoize(() -> getModel(SLIM_CASUAL_WAVE));
    public static final Supplier<PlayerDollModel> SLIM_CHEER_MODEL = Suppliers.memoize(() -> getModel(SLIM_CHEER));
    public static final Supplier<PlayerDollModel> SLIM_PERFECT_BALANCE_MODEL = Suppliers.memoize(() -> getModel(SLIM_PERFECT_BALANCE));
    public static final Supplier<PlayerDollModel> SLIM_SHY_POSE_MODEL = Suppliers.memoize(() -> getModel(SLIM_SHY_POSE));
    public static final Supplier<PlayerDollModel> SLIM_ZOMBIE_MODEL = Suppliers.memoize(() -> getModel(SLIM_ZOMBIE));

    private PlayerDollModel(InputStream stream) {
        super(stream);
    }

    private PlayerDollModel() {
        super();
    }

    public static PlayerDollModel getDefaultModel(boolean isSlim) {
        return isSlim ? SLIM_DEFAULT_MODEL.get() : WIDE_DEFAULT_MODEL.get();
    }

    public static PlayerDollModel getModel(PlayerDollBlockEntity.Pose pose, boolean isSlim) {
        return switch (pose) {
            case CASUAL_WAVE -> isSlim ? SLIM_CASUAL_WAVE_MODEL.get() : WIDE_CASUAL_WAVE_MODEL.get();
            case CHEER -> isSlim ? SLIM_CHEER_MODEL.get() : WIDE_CHEER_MODEL.get();
            case PERFECT_BALANCE -> isSlim ? SLIM_PERFECT_BALANCE_MODEL.get() : WIDE_PERFECT_BALANCE_MODEL.get();
            case SHY_POSE -> isSlim ? SLIM_SHY_POSE_MODEL.get() : WIDE_SHY_POSE_MODEL.get();
            case ZOMBIE -> isSlim ? SLIM_ZOMBIE_MODEL.get() : WIDE_ZOMBIE_MODEL.get();
            default -> getDefaultModel(isSlim);
        };
    }

    private static PlayerDollModel getModel(Identifier type) {
        return Minecraft.getInstance().getResourceManager().getResource(type).map(resource -> {
            try (var stream = resource.open()) {
                return new PlayerDollModel(stream);
            } catch (Exception ignore) {
            }
            return new PlayerDollModel();
        }).orElse(new PlayerDollModel());
    }

    @Override
    public void setupAnim(Unit state) {
    }
}
