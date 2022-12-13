package com.udu3324.poinpow.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.udu3324.poinpow.Config;
import com.udu3324.poinpow.utils.*;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
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
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Poinpow was made by udu3324. \nIf you have any feedback, click on this text, and go to poinpow repo to create a issue.")))
                .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/udu3324/poinpow"))
                .withColor(Formatting.GOLD)
                .withBold(true)));

        //discord
        source.sendFeedback(Text.literal("official discord").styled(style -> style
                .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/NXm9tJvyBT"))
                .withColor(Formatting.BLUE)
                .withUnderline(true)));

        //remove lobby ranks
        source.sendFeedback(Text.literal("/" + RemoveLobbyRanks.name + " [toggled|" + RemoveLobbyRanks.toggled + "]").styled(style -> style
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(RemoveLobbyRanks.description + "\n\nClick to Toggle")))
                .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + RemoveLobbyRanks.name + " " + !RemoveLobbyRanks.toggled))
                .withColor(Formatting.DARK_GREEN)));

        //auto skip barrier
        source.sendFeedback(Text.literal("/" + AutoSkipBarrier.name + " [toggled|" + AutoSkipBarrier.toggled + "]").styled(style -> style
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(AutoSkipBarrier.description + "\n\nClick to Toggle")))
                .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + AutoSkipBarrier.name + " " + !AutoSkipBarrier.toggled))
                .withColor(Formatting.DARK_GREEN)));

        //block lobby welcome
        source.sendFeedback(Text.literal("/" + BlockLobbyWelcome.name + " [toggled|" + BlockLobbyWelcome.toggled + "]").styled(style -> style
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(BlockLobbyWelcome.description + "\n\nClick to Toggle")))
                .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + BlockLobbyWelcome.name + " " + !BlockLobbyWelcome.toggled))
                .withColor(Formatting.DARK_GREEN)));

        //block lobby ads
        source.sendFeedback(Text.literal("/" + BlockLobbyAds.name + " [toggled|" + BlockLobbyAds.toggled + "]").styled(style -> style
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(BlockLobbyAds.description + "\n\nClick to Toggle")))
                .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + BlockLobbyAds.name + " " + !BlockLobbyAds.toggled))
                .withColor(Formatting.DARK_GREEN)));

        //block minehut ads
        source.sendFeedback(Text.literal("/" + BlockMinehutAds.name + " [toggled|" + BlockMinehutAds.toggled + "]").styled(style -> style
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(BlockMinehutAds.description + "\n\nClick to Toggle")))
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Click to Toggle\n" + BlockMinehutAds.description)))
                .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + BlockMinehutAds.name + " " + !BlockMinehutAds.toggled))
                .withColor(Formatting.DARK_GREEN)));

        //block free credits
        source.sendFeedback(Text.literal("/" + BlockFreeCredits.name + " [toggled|" + BlockFreeCredits.toggled + "]").styled(style -> style
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(BlockFreeCredits.description + "\n\nClick to Toggle")))
                .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + BlockFreeCredits.name + " " + !BlockFreeCredits.toggled))
                .withColor(Formatting.DARK_GREEN)));

        running = false;

        return Command.SINGLE_SUCCESS;
    }
}
