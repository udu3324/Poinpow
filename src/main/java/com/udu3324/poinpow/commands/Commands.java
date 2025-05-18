package com.udu3324.poinpow.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.udu3324.poinpow.Config;
import com.udu3324.poinpow.utils.*;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.net.URI;
import java.util.concurrent.atomic.AtomicBoolean;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class Commands {
    // this bool stops the utils from running when showing examples
    public static Boolean running = false;

    //register poinpow commands in the mc brigadier
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        ServerLookup.registerCommand(dispatcher);
        MuteLobbyChat.registerCommand(dispatcher);

        dispatcher.register(literal("poinpow")
                .executes(ctx -> help(ctx.getSource()))

                .then(literal(AutoSkipBarrier.name)
                        .executes(ctx -> description(ctx.getSource(), AutoSkipBarrier.name, AutoSkipBarrier.description, AutoSkipBarrier.toggled))
                        .then(literal("true").executes(ctx -> toggle(ctx.getSource(), AutoSkipBarrier.name, AutoSkipBarrier.toggled, true)))
                        .then(literal("false").executes(ctx -> toggle(ctx.getSource(), AutoSkipBarrier.name, AutoSkipBarrier.toggled, false))))

                .then(literal(ChatPhraseFilter.name)
                        .executes(ctx -> description(ctx.getSource(), ChatPhraseFilter.name, ChatPhraseFilter.description, ChatPhraseFilter.toggled))
                        .then(literal("add").then(argument("regex", StringArgumentType.string()).executes(ChatPhraseFilter::add)))
                        .then(literal("remove").then(argument("regex", StringArgumentType.string()).executes(ChatPhraseFilter::remove)))
                        .then(literal("list").executes(ctx -> ChatPhraseFilter.list(ctx.getSource())))
                        .then(literal("true").executes(ctx -> toggle(ctx.getSource(), ChatPhraseFilter.name, ChatPhraseFilter.toggled, true)))
                        .then(literal("false").executes(ctx -> toggle(ctx.getSource(), ChatPhraseFilter.name, ChatPhraseFilter.toggled, false))))

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

                .then(literal(BlockLobbyMapAds.name)
                        .executes(ctx -> description(ctx.getSource(), BlockLobbyMapAds.name, BlockLobbyMapAds.description, BlockLobbyMapAds.toggled))
                        .then(literal("true").executes(ctx -> toggle(ctx.getSource(), BlockLobbyMapAds.name, BlockLobbyMapAds.toggled, true)))
                        .then(literal("false").executes(ctx -> toggle(ctx.getSource(), BlockLobbyMapAds.name, BlockLobbyMapAds.toggled, false))))

                .then(literal(HubCommandBack.name)
                        .executes(ctx -> description(ctx.getSource(), HubCommandBack.name, HubCommandBack.description, HubCommandBack.toggled))
                        .then(literal("true").executes(ctx -> toggle(ctx.getSource(), HubCommandBack.name, HubCommandBack.toggled, true)))
                        .then(literal("false").executes(ctx -> toggle(ctx.getSource(), HubCommandBack.name, HubCommandBack.toggled, false))))

                .then(literal(BlockRaids.name)
                        .executes(ctx -> description(ctx.getSource(), BlockRaids.name, BlockRaids.description, BlockRaids.toggled))
                        .then(literal("true").executes(ctx -> toggle(ctx.getSource(), BlockRaids.name, BlockRaids.toggled, true)))
                        .then(literal("false").executes(ctx -> toggle(ctx.getSource(), BlockRaids.name, BlockRaids.toggled, false))))

                .then(literal(BlockChestAds.name)
                        .executes(ctx -> description(ctx.getSource(), BlockChestAds.name, BlockChestAds.description, BlockChestAds.toggled))
                        .then(literal("true").executes(ctx -> toggle(ctx.getSource(), BlockChestAds.name, BlockChestAds.toggled, true)))
                        .then(literal("false").executes(ctx -> toggle(ctx.getSource(), BlockChestAds.name, BlockChestAds.toggled, false))))

                .then(literal(ToggleSpecificAds.name)
                        .executes(ctx -> ToggleSpecificAds.description(ctx.getSource()))
                        .then(literal("default").executes(ctx -> ToggleSpecificAds.toggle(ctx.getSource(), "default")))
                        .then(literal("vip").executes(ctx -> ToggleSpecificAds.toggle(ctx.getSource(), "vip")))
                        .then(literal("vipPlus").executes(ctx -> ToggleSpecificAds.toggle(ctx.getSource(), "vipPlus")))
                        .then(literal("pro").executes(ctx -> ToggleSpecificAds.toggle(ctx.getSource(), "pro")))
                        .then(literal("legend").executes(ctx -> ToggleSpecificAds.toggle(ctx.getSource(), "legend")))
                        .then(literal("patron").executes(ctx -> ToggleSpecificAds.toggle(ctx.getSource(), "patron"))))
        );
    }

    //this generalizes the toggling function of common/repetitive commands/features
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

    //this generalizes creating the lore for the text in /poinpow command
    private static int description(FabricClientCommandSource source, String name, String description, AtomicBoolean toggled) {
        running = true;

        //show the command
        source.sendFeedback(Text.literal("\n/poinpow " + name + " " + toggled.get()).styled(style -> style
                .withHoverEvent(new HoverEvent.ShowText(Text.literal("Click to Toggle!")))
                .withClickEvent(new ClickEvent.RunCommand("/poinpow " + name + " " + !toggled.get()))
                .withColor(Formatting.GOLD)
                .withUnderline(true)));

        //show the command's description
        source.sendFeedback(Text.literal(description).styled(style -> style
                .withHoverEvent(new HoverEvent.ShowText(Text.literal("Click to Toggle!")))
                .withClickEvent(new ClickEvent.RunCommand("/poinpow " + name + " " + !toggled.get()))));

        running = false;
        return Command.SINGLE_SUCCESS;
    }

    //help command (/poinpow) for the mod
    private static int help(FabricClientCommandSource source) {
        running = true;

        //auto skip barrier
        source.sendFeedback(Text.literal("[" + AutoSkipBarrier.toggled + "] " + AutoSkipBarrier.name).styled(style -> style
                .withHoverEvent(new HoverEvent.ShowText(Text.literal(AutoSkipBarrier.description + "\n\nClick to Toggle")))
                .withClickEvent(new ClickEvent.SuggestCommand("/poinpow " + AutoSkipBarrier.name + " " + !AutoSkipBarrier.toggled.get()))
                .withColor(Formatting.DARK_GRAY)
        ));

        //block lobby welcome
        source.sendFeedback(Text.literal("[" + BlockLobbyWelcome.toggled + "] " + BlockLobbyWelcome.name).styled(style -> style
                .withHoverEvent(new HoverEvent.ShowText(Text.literal(BlockLobbyWelcome.description + "\n\nClick to Toggle")))
                .withClickEvent(new ClickEvent.SuggestCommand("/poinpow " + BlockLobbyWelcome.name + " " + !BlockLobbyWelcome.toggled.get()))
                .withColor(Formatting.DARK_GRAY)
        ));

        //block lobby ads
        source.sendFeedback(Text.literal("[" + BlockLobbyAds.toggled + "] " + BlockLobbyAds.name).styled(style -> style
                .withHoverEvent(new HoverEvent.ShowText(Text.literal(BlockLobbyAds.description + "\n\nClick to Toggle")))
                .withClickEvent(new ClickEvent.SuggestCommand("/poinpow " + BlockLobbyAds.name + " " + !BlockLobbyAds.toggled.get()))
                .withColor(Formatting.DARK_GRAY)
        ));

        //block minehut ads
        source.sendFeedback(Text.literal("[" + BlockMinehutAds.toggled + "] " + BlockMinehutAds.name).styled(style -> style
                .withHoverEvent(new HoverEvent.ShowText(Text.literal(BlockMinehutAds.description + "\n\nClick to Toggle")))
                .withClickEvent(new ClickEvent.SuggestCommand("/poinpow " + BlockMinehutAds.name + " " + !BlockMinehutAds.toggled.get()))
                .withColor(Formatting.DARK_GRAY)
        ));

        //block free credits
        source.sendFeedback(Text.literal("[" + BlockFreeCredits.toggled + "] " + BlockFreeCredits.name).styled(style -> style
                .withHoverEvent(new HoverEvent.ShowText(Text.literal(BlockFreeCredits.description + "\n\nClick to Toggle")))
                .withClickEvent(new ClickEvent.SuggestCommand("/poinpow " + BlockFreeCredits.name + " " + !BlockFreeCredits.toggled.get()))
                .withColor(Formatting.DARK_GRAY)
        ));

        //block lobby map ads
        source.sendFeedback(Text.literal("[" + BlockLobbyMapAds.toggled + "] " + BlockLobbyMapAds.name).styled(style -> style
                .withHoverEvent(new HoverEvent.ShowText(Text.literal(BlockLobbyMapAds.description + "\n\nClick to Toggle")))
                .withClickEvent(new ClickEvent.SuggestCommand("/poinpow " + BlockLobbyMapAds.name + " " + !BlockLobbyMapAds.toggled.get()))
                .withColor(Formatting.DARK_GRAY)
        ));

        //block lobby raids
        source.sendFeedback(Text.literal("[" + BlockRaids.toggled + "] " + BlockRaids.name).styled(style -> style
                .withHoverEvent(new HoverEvent.ShowText(Text.literal(BlockRaids.description + "\n\nClick to Toggle")))
                .withClickEvent(new ClickEvent.SuggestCommand("/poinpow " + BlockRaids.name + " " + !BlockRaids.toggled.get()))
                .withColor(Formatting.DARK_GRAY)
        ));

        //block chest ads
        source.sendFeedback(Text.literal("[" + BlockChestAds.toggled + "] " + BlockChestAds.name).styled(style -> style
                .withHoverEvent(new HoverEvent.ShowText(Text.literal(BlockChestAds.description + "\n\nClick to Toggle")))
                .withClickEvent(new ClickEvent.SuggestCommand("/poinpow " + BlockChestAds.name + " " + !BlockChestAds.toggled.get()))
                .withColor(Formatting.DARK_GRAY)
        ));

        //hub command back
        source.sendFeedback(Text.literal("[" + HubCommandBack.toggled + "] " + HubCommandBack.name).styled(style -> style
                .withHoverEvent(new HoverEvent.ShowText(Text.literal(HubCommandBack.description + "\n\nClick to Toggle")))
                .withClickEvent(new ClickEvent.SuggestCommand("/poinpow " + HubCommandBack.name + " " + !HubCommandBack.toggled.get()))
                .withColor(Formatting.DARK_GRAY)
        ));

        //toggle lobby chat
        source.sendFeedback(Text.literal("[" + MuteLobbyChat.toggled + "] " + MuteLobbyChat.name).styled(style -> style
                .withHoverEvent(new HoverEvent.ShowText(Text.literal(MuteLobbyChat.description + "\n\nClick to Toggle")))
                .withClickEvent(new ClickEvent.SuggestCommand("/" + MuteLobbyChat.name))
                .withColor(Formatting.DARK_GRAY)
        ));

        //chat phrase filter
        source.sendFeedback(Text.literal("/poinpow " + ChatPhraseFilter.name + " <true/false/list/add/remove>").styled(style -> style
                .withHoverEvent(new HoverEvent.ShowText(Text.literal(ChatPhraseFilter.description + "\n\nClick to change")))
                .withClickEvent(new ClickEvent.SuggestCommand("/poinpow " + ChatPhraseFilter.name + " "))
                .withColor(Formatting.GRAY)
        ));

        //toggle specific ads
        source.sendFeedback(Text.literal("/poinpow " + ToggleSpecificAds.name + " <rank>").styled(style -> style
                .withHoverEvent(new HoverEvent.ShowText(Text.literal(ToggleSpecificAds.description + "\n\nClick to Toggle")))
                .withClickEvent(new ClickEvent.SuggestCommand("/poinpow " + ToggleSpecificAds.name + " "))
                .withColor(Formatting.GRAY)
        ));

        //lookup server
        source.sendFeedback(Text.literal("/" + ServerLookup.name + " <serverName>").styled(style -> style
                .withHoverEvent(new HoverEvent.ShowText(Text.literal(ServerLookup.description + "\n\nClick to lookup a server!")))
                .withClickEvent(new ClickEvent.SuggestCommand("/" + ServerLookup.name + " "))
                .withColor(Formatting.GRAY)
        ));

        //credits <3
        source.sendFeedback(Text.literal("[+] Poinpow v" + Config.version + " by udu3324 [+]").styled(style -> style
                .withHoverEvent(new HoverEvent.ShowText(Text.literal("Poinpow is by udu3324. \nFeedback can be sent through the repo by clicking.")))
                .withClickEvent(new ClickEvent.OpenUrl(URI.create("https://github.com/udu3324/poinpow")))
                .withColor(Formatting.GOLD)
                .withBold(true)));

        running = false;

        return Command.SINGLE_SUCCESS;
    }
}
