package com.udu3324.poinpow.utils;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;
import com.udu3324.poinpow.Poinpow;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicBoolean;

public class HubCommandBack {
    public static final String name = "hubCommandBack";
    public static final String description = "Adds back the /hub command in sub-servers that don't have a hub.";

    public static AtomicBoolean toggled = new AtomicBoolean(true);

    public static void scan(String msg, CallbackInfo ci) {
        // return if toggled off (no need for bool)
        if (!toggled.get()) return;

        // return if not on mh
        if (!Poinpow.onMinehut) return;

        // return if the message isn't the command
        if (!msg.contains("/hub")) return;

        // return if the sub-server already has /hub registered as a command
        ClientPlayNetworkHandler clientPlayNetworkHandler = MinecraftClient.getInstance().getNetworkHandler();
        if (clientPlayNetworkHandler == null) return;

        CommandDispatcher<ClientCommandSource> dispatcher = clientPlayNetworkHandler.getCommandDispatcher();
        if (isCommandRegistered(dispatcher, "hub")) return;

        // Prevent original message from sending
        ci.cancel();

        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;

        player.networkHandler.sendChatCommand("mh");
    }

    public static boolean isCommandRegistered(CommandDispatcher<ClientCommandSource> dispatcher, String commandName) {
        CommandNode<ClientCommandSource> rootCommand = dispatcher.getRoot();

        // Check if the command is present in the command tree
        return rootCommand.getChild(commandName) != null;
    }
}
