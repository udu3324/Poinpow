package com.udu3324.poinpow.mixin;

import com.udu3324.poinpow.Poinpow;
import com.udu3324.poinpow.api.GitHubVersion;
import com.udu3324.poinpow.utils.AutoSkipBarrier;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ClientPlayConnectionEvents.class)
public class WorldLoadedMixin {
    @Inject(method = "lambda$static$2", at = @At("HEAD"))
    private static void onLoad(ClientPlayConnectionEvents.Join[] callbacks, ClientPlayNetworkHandler handler, PacketSender sender, MinecraftClient client, CallbackInfo ci) {
        //only enable the mod when in minehut
        if (!MinecraftClient.getInstance().isInSingleplayer()) {
            String ip = Objects.requireNonNull(MinecraftClient.getInstance().getCurrentServerEntry()).address;
            if (ip.contains("minehut")) Poinpow.onMinehut = true;
        }

        //check for latest version of mod
        if (Poinpow.onMinehut) GitHubVersion.check();

        AutoSkipBarrier.check();
    }
}
