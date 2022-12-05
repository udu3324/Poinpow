package com.udu3324.poinpow.utils;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.udu3324.poinpow.Config;
import com.udu3324.poinpow.Poinpow;
import com.udu3324.poinpow.commands.CmdUtils;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.regex.Pattern;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class RemoveLobbyRanks {
    public static String name = "remove_lobby_ranks";
    public static Boolean toggled = true;
    private static Boolean running = true;

    public static void check(String chat, CallbackInfo ci) {
        // return false if toggled off
        if (!toggled) return;

        //ranked users joining
        Pattern join = Pattern.compile("^(\\[(PRO|VIP|LEGEND|PATRON)] )[a-zA-Z0-9_.]{1,16} joined your lobby.$");
        if (join.matcher(chat).find()) {
            Poinpow.LOGGER.info("Blocked: " + chat);
            ci.cancel();
            return;
        }

        //ranked users messaging
        Pattern rankedMsg = Pattern.compile("^(\\[(PRO|VIP|LEGEND|PATRON)] )[a-zA-Z0-9_.]{1,16}: ");
        if (rankedMsg.matcher(chat).find()) {
            Poinpow.LOGGER.info("Original: " + chat);
            ci.cancel();

            //remove the rank prefix
            if (chat.contains("]"))
                chat = chat.substring(chat.indexOf("]") + 2);

            //send it to client
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal(chat));
            return;
        }

        //normal users chatting
        Pattern normalMsg = Pattern.compile("^[a-zA-Z0-9_.]{1,16}: ");
        if (normalMsg.matcher(chat).find() && running) {
            ci.cancel();

            running = false;
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal(chat).styled(style -> style.withColor(Formatting.WHITE)));
            running = true;
        }
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
