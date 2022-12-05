package com.udu3324.poinpow.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.udu3324.poinpow.Config;
import com.udu3324.poinpow.utils.*;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class PoinpowHelp {
    public static Boolean running = false;
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal("poinpow")
                .executes(ctx -> data(ctx.getSource()))
        );
    }

    private static int data(FabricClientCommandSource source) {
        running = true;

        //credits + version
        source.sendFeedback(Text.literal("[+] Poinpow v" + Config.version + " by udu3324 [+]").styled(style -> style
                .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/udu3324"))
                .withColor(Formatting.GOLD)
                .withBold(true)));

        //remove lobby ranks
        source.sendFeedback(Text.literal("/remove_lobby_ranks [toggled|" + RemoveLobbyRanks.toggled + "] (click)").styled(style -> style
                .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/remove_lobby_ranks " + !RemoveLobbyRanks.toggled))
                .withColor(Formatting.DARK_PURPLE)));

        //auto skip barrier
        source.sendFeedback(Text.literal("/auto_skip_barrier [toggled|" + AutoSkipBarrier.toggled + "] (click)").styled(style -> style
                .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/auto_skip_barrier " + !AutoSkipBarrier.toggled))
                .withColor(Formatting.BLUE)));
        source.sendFeedback(Text.literal("Auto-skips transition ads when joining a server").styled(style -> style
                .withColor(Formatting.DARK_GRAY)));

        //block lobby welcome
        source.sendFeedback(Text.literal("/block_lobby_welcome [toggled|" + BlockLobbyWelcome.toggled + "] (click)").styled(style -> style
                .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/block_lobby_welcome " + !BlockLobbyWelcome.toggled))
                .withColor(Formatting.DARK_GREEN)));
        source.sendFeedback(Text.literal("Welcome back, Notch (next line) Sale! Sale!!!").styled(style -> style
                .withColor(Formatting.DARK_GRAY)));

        //block lobby ads
        source.sendFeedback(Text.literal("/block_lobby_ads [toggled|" + BlockLobbyAds.toggled + "] (click)").styled(style -> style
                .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/block_lobby_ads " + !BlockLobbyAds.toggled))
                .withColor(Formatting.DARK_GREEN)));
        source.sendFeedback(Text.literal("[AD] overlord35: /join fishwind fish!! fight!! build!!").styled(style -> style
                .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/join fishwind")) //lol
                .withColor(Formatting.DARK_GRAY)));

        //block free credits
        source.sendFeedback(Text.literal("/block_free_credits [toggled|" + BlockFreeCredits.toggled + "] (click)").styled(style -> style
                .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/block_free_credits " + !BlockFreeCredits.toggled))
                .withColor(Formatting.DARK_GREEN)));
        source.sendFeedback(Text.literal("[Minehut] NintendoOS just got free credits by voting via /vote").styled(style -> style
                .withColor(Formatting.DARK_GRAY)));

        running = false;

        return Command.SINGLE_SUCCESS;
    }
}
