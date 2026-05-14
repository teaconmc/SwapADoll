package com.github.tartaricacid.swapadoll.client.model;

import com.github.tartaricacid.swapadoll.SwapADoll;
import com.github.tartaricacid.swapadoll.client.bedrock.AbstractBedrockModel;
import com.google.common.base.Suppliers;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;

import java.io.InputStream;
import java.util.function.Supplier;

public class PlayerDollModel extends AbstractBedrockModel<Unit> {
    private static final Identifier WIDE = Identifier.fromNamespaceAndPath(SwapADoll.MOD_ID, "models/bedrock/player_doll_wide.json");
    private static final Identifier SLIM = Identifier.fromNamespaceAndPath(SwapADoll.MOD_ID, "models/bedrock/player_doll_slim.json");

    public static final Supplier<PlayerDollModel> WIDE_MODEL = Suppliers.memoize(() -> getModel(WIDE));
    public static final Supplier<PlayerDollModel> SLIM_MODEL = Suppliers.memoize(() -> getModel(SLIM));

    private PlayerDollModel(InputStream stream) {
        super(stream);
    }

    private PlayerDollModel() {
        super();
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
