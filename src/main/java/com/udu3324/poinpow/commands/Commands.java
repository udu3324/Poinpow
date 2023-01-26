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

import java.util.concurrent.atomic.AtomicBoolean;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class Commands {
    // this bool stops the utils from running when showing examples
    public static Boolean running = false;

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal("poinpow")
                .executes(ctx -> help(ctx.getSource()))

                .then(literal(RemoveLobbyRanks.name)
                        .executes(ctx -> description(ctx.getSource(), RemoveLobbyRanks.name, RemoveLobbyRanks.description, RemoveLobbyRanks.toggled))
                        .then(literal("true").executes(ctx -> toggle(ctx.getSource(), RemoveLobbyRanks.name, RemoveLobbyRanks.toggled, true)))
                        .then(literal("false").executes(ctx -> toggle(ctx.getSource(), RemoveLobbyRanks.name, RemoveLobbyRanks.toggled, false))))

                .then(literal(AutoSkipBarrier.name)
                        .executes(ctx -> description(ctx.getSource(), AutoSkipBarrier.name, AutoSkipBarrier.description, AutoSkipBarrier.toggled))
                        .then(literal("true").executes(ctx -> toggle(ctx.getSource(), AutoSkipBarrier.name, AutoSkipBarrier.toggled, true)))
                        .then(literal("false").executes(ctx -> toggle(ctx.getSource(), AutoSkipBarrier.name, AutoSkipBarrier.toggled, false))))

                .then(literal(BlockLobbyWelcome.name)
                        .executes(ctx -> description(ctx.getSource(), BlockLobbyWelcome.name, BlockLobbyWelcome.description, BlockLobbyWelcome.toggled))
                        .then(literal("true").executes(ctx -> toggle(ctx.getSource(), BlockLobbyWelcome.name, BlockLobbyWelcome.toggled, true)))
                        .then(literal("false").executes(ctx -> toggle(ctx.getSource(), BlockLobbyWelcome.name, BlockLobbyWelcome.toggled, false))))

                .then(literal(BlockLobbyAds.name)
                        .executes(ctx -> description(ctx.getSource(), BlockLobbyAds.name, BlockLobbyAds.description, BlockLobbyAds.toggled))
                        .then(literal("true").executes(ctx -> toggle(ctx.getSource(), BlockLobbyAds.name, BlockLobbyAds.toggled, true)))
                        .then(literal("false").executes(ctx -> toggle(ctx.getSource(), BlockLobbyAds.name, BlockLobbyAds.toggled, false))))

                .then(literal(BlockMinehutAds.name)
                        .executes(ctx -> description(ctx.getSource(), BlockMinehutAds.name, BlockMinehutAds.description, BlockMinehutAds.toggled))
                        .then(literal("true").executes(ctx -> toggle(ctx.getSource(), BlockMinehutAds.name, BlockMinehutAds.toggled, true)))
                        .then(literal("false").executes(ctx -> toggle(ctx.getSource(), BlockMinehutAds.name, BlockMinehutAds.toggled, false))))

                .then(literal(BlockFreeCredits.name)
                        .executes(ctx -> description(ctx.getSource(), BlockFreeCredits.name, BlockFreeCredits.description, BlockFreeCredits.toggled))
                        .then(literal("true").executes(ctx -> toggle(ctx.getSource(), BlockFreeCredits.name, BlockFreeCredits.toggled, true)))
                        .then(literal("false").executes(ctx -> toggle(ctx.getSource(), BlockFreeCredits.name, BlockFreeCredits.toggled, false))))
        );
    }

    private static int toggle(FabricClientCommandSource source, String name, AtomicBoolean original, Boolean toggled) {
        running = true;
        original.set(toggled);

        Formatting color = Formatting.RED;
        if (toggled) color = Formatting.GREEN;
        Formatting finalColor = color;

        source.sendFeedback(Text.literal(name + " has been set to " + toggled + ".").styled(style -> style.withColor(finalColor)));
        Config.setValueFromConfig(name, String.valueOf(toggled));
        running = false;
        return Command.SINGLE_SUCCESS;
    }

    private static int description(FabricClientCommandSource source, String name, String description, AtomicBoolean toggled) {
        running = true;

        //show the command
        source.sendFeedback(Text.literal("\n/poinpow " + name + " [toggled|" + toggled.get() + "]").styled(style -> style
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Click to Toggle!")))
                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/poinpow " + name + " " + !toggled.get()))
                .withColor(Formatting.GOLD)
                .withUnderline(true)));
        //show the command's description
        source.sendFeedback(Text.literal(description).styled(style -> style
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Click to Toggle!")))
                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/poinpow " + name + " " + !toggled.get()))));

        running = false;
        return Command.SINGLE_SUCCESS;
    }

    private static int help(FabricClientCommandSource source) {
        running = true;

        //credits + version
        source.sendFeedback(Text.literal("[+] Poinpow v" + Config.version + " by udu3324 [+]").styled(style -> style
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Poinpow was made by udu3324. \nIf you have any feedback, click on this text, and go to poinpow repo to create a issue.")))
                .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/udu3324/poinpow"))
                .withColor(Formatting.GOLD)
                .withBold(true)));

        //discord
        source.sendFeedback(Text.literal("official discord lol").styled(style -> style
                .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/NXm9tJvyBT"))
                .withColor(Formatting.BLUE)
                .withUnderline(true)
        ));

        //remove lobby ranks
        source.sendFeedback(Text.literal("/poinpow " + RemoveLobbyRanks.name + " [toggled|" + RemoveLobbyRanks.toggled + "]").styled(style -> style
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(RemoveLobbyRanks.description + "\n\nClick to Toggle")))
                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + RemoveLobbyRanks.name + " " + !RemoveLobbyRanks.toggled.get()))
        ));

        //auto skip barrier
        source.sendFeedback(Text.literal("/poinpow " + AutoSkipBarrier.name + " [toggled|" + AutoSkipBarrier.toggled + "]").styled(style -> style
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(AutoSkipBarrier.description + "\n\nClick to Toggle")))
                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + AutoSkipBarrier.name + " " + !AutoSkipBarrier.toggled.get()))
        ));

        //block lobby welcome
        source.sendFeedback(Text.literal("/poinpow " + BlockLobbyWelcome.name + " [toggled|" + BlockLobbyWelcome.toggled + "]").styled(style -> style
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(BlockLobbyWelcome.description + "\n\nClick to Toggle")))
                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + BlockLobbyWelcome.name + " " + !BlockLobbyWelcome.toggled.get()))
        ));

        //block lobby ads
        source.sendFeedback(Text.literal("/poinpow " + BlockLobbyAds.name + " [toggled|" + BlockLobbyAds.toggled + "]").styled(style -> style
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(BlockLobbyAds.description + "\n\nClick to Toggle")))
                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + BlockLobbyAds.name + " " + !BlockLobbyAds.toggled.get()))
        ));

        //block minehut ads
        source.sendFeedback(Text.literal("/poinpow " + BlockMinehutAds.name + " [toggled|" + BlockMinehutAds.toggled + "]").styled(style -> style
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(BlockMinehutAds.description + "\n\nClick to Toggle")))
                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + BlockMinehutAds.name + " " + !BlockMinehutAds.toggled.get()))
        ));

        //block free credits
        source.sendFeedback(Text.literal("/poinpow " + BlockFreeCredits.name + " [toggled|" + BlockFreeCredits.toggled + "]").styled(style -> style
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(BlockFreeCredits.description + "\n\nClick to Toggle")))
                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + BlockFreeCredits.name + " " + !BlockFreeCredits.toggled.get()))
        ));

        running = false;

        return Command.SINGLE_SUCCESS;
    }
}
