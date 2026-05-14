package com.github.tartaricacid.swapadoll.init;

import com.github.tartaricacid.swapadoll.SwapADoll;
import com.google.common.collect.Lists;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Util;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public interface ModTabs {
    DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SwapADoll.MOD_ID);

    DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_TAB = TABS.register("swapadoll", () -> CreativeModeTab.builder()
            .title(Component.translatable("item_group.swapadoll"))
            .icon(() -> new ItemStack(ModItems.PLAYER_DOLL.get()))
            .displayItems((parameters, output) ->
                    addDefaultPlayerDoll(output))
            .build());

    List<String> DEFAULT_SKINS = Util.make(Lists.newArrayList(), list -> {
        list.add("alex");
        list.add("ari");
        list.add("efe");
        list.add("kai");
        list.add("makena");
        list.add("noor");
        list.add("steve");
        list.add("sunny");
        list.add("zuri");
    });

    static void addDefaultPlayerDoll(CreativeModeTab.Output output) {
        for (var name : DEFAULT_SKINS) {
            ItemStack stack = ModItems.PLAYER_DOLL.get().getDefaultInstance();
            ResolvableProfile alex = ResolvableProfile.createUnresolved(name);
            stack.set(DataComponents.PROFILE, alex);
            output.accept(stack);
        }
    }
}
