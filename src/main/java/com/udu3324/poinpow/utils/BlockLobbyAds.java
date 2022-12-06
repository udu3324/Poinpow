package com.udu3324.poinpow.utils;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.udu3324.poinpow.Config;
import com.udu3324.poinpow.Poinpow;
import com.udu3324.poinpow.commands.CmdUtils;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.regex.Pattern;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class BlockLobbyAds {
    public static String name = "block_lobby_ads";
    public static String description = "Blocks ads made by players in the lobby.";
    public static Boolean toggled = true;

    public static Boolean check(String chat, CallbackInfo ci) {
        // return false if toggled off
        if (!toggled) return false;

        Pattern pattern = Pattern.compile("^\\[AD]");

        if (pattern.matcher(chat).find()) {
            Poinpow.LOGGER.info("Blocked: " + chat);
            ci.cancel();
        }

        return pattern.matcher(chat).find();
    }

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal(name)
                .executes(ctx -> CmdUtils.getStatus(ctx.getSource(), name, toggled))
                .then(literal("true")
                        .executes(ctx -> on(ctx.getSource()))
                )
                .then(literal("false")
                        .executes(ctx -> off(ctx.getSource()))
                )
        );
    }

    private static int on(FabricClientCommandSource source) {
        source.sendFeedback(CmdUtils.getOutput(name, true));
        Config.setValueFromConfig(name, "true");
        toggled = true;
        return Command.SINGLE_SUCCESS;
    }

    private static int off(FabricClientCommandSource source) {
        source.sendFeedback(CmdUtils.getOutput(name, false));
        Config.setValueFromConfig(name, "false");
        toggled = false;
        return Command.SINGLE_SUCCESS;
    }
}