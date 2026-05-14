package com.github.tartaricacid.swapadoll.network;

import com.github.tartaricacid.swapadoll.network.payload.SetPlayerDollDataPackage;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class NetworkHandler {
    private static final String VERSION = "1.0.0";

    public static void registerPacket(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(VERSION).optional();

        registrar.playToServer(SetPlayerDollDataPackage.TYPE, SetPlayerDollDataPackage.STREAM_CODEC, SetPlayerDollDataPackage::handle);
    }
}
