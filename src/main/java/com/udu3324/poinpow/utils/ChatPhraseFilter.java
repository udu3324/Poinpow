package com.udu3324.poinpow.utils;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.udu3324.poinpow.Config;
import com.udu3324.poinpow.Poinpow;
import com.udu3324.poinpow.commands.Commands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

public class ChatPhraseFilter {
    public static String name = "chat_phrase_filter";
    public static String description = "This filters out messages in lobby that match the regex. Go to https://regex101.com to create some.";
    public static AtomicBoolean toggled = new AtomicBoolean(true);

    public static ArrayList<Pattern> list = Config.getListOfRegex();

    public static void check(String chat, CallbackInfo ci) {
        // return false if toggled off
        if (!toggled.get()) return;

        // return if not on minehut
        if (!Poinpow.onMinehut) return;

        // return if there's nothing in the list
        if (list == null || list.size() == 0) {
            return;
        }

        if (MinecraftClient.getInstance().player == null) return;

        // check for scoreboard
        Scoreboard scoreboard = MinecraftClient.getInstance().player.getScoreboard();
        ArrayList<String> scores = new ArrayList<>();
        for (ScoreboardObjective objective : scoreboard.getObjectives()) {
            scores.add(objective.getDisplayName().toString());
        }

        if (!scores.toString().toLowerCase().contains("minehut")) return;

        for (Pattern p : list) {
            if (p.matcher(chat).find()) {
                Poinpow.log.info("Filtered: " + chat);
                ci.cancel();
                return;
            }
        }
    }

    public static int add(CommandContext<FabricClientCommandSource> context) {
        Commands.running = true;
        String input = StringArgumentType.getString(context, "regex");

        //check if regex already exists
        for (Pattern p : list) {
            if (p.toString().equals(input)) {
                context.getSource().sendFeedback(Text.literal("The regex you provided is already in the filter.").styled(style -> style.withColor(Formatting.RED)));
                Commands.running = false;
                return Command.SINGLE_SUCCESS;
            }
        }

        context.getSource().sendFeedback(Text.literal("\nAdded the regex below to ChatPhraseFilter").styled(style -> style.withColor(Formatting.GREEN)));
        context.getSource().sendFeedback(Text.literal(input + "\n").styled(style -> style.withColor(Formatting.GOLD)));

        System.out.println("addRegex(): " + input);

        Config.addRegex(input);
        list.add(Pattern.compile(input));

        Commands.running = false;
        return Command.SINGLE_SUCCESS;
    }

    public static int remove(CommandContext<FabricClientCommandSource> context) {
        Commands.running = true;
        String input = StringArgumentType.getString(context, "regex");

        //check if regex already exists
        for (Pattern p : list) {
            if (p.toString().equals(input)) {
                System.out.println("removeRegex(): " + input);

                Config.removeRegex(input);
                System.out.println("please!! " + list.remove(p));
                //list.remove(p);

                context.getSource().sendFeedback(Text.literal("Successfully removed \"" + input + "\" from the filter.").styled(style -> style.withColor(Formatting.GREEN)));

                Commands.running = false;
                return Command.SINGLE_SUCCESS;
            }
        }

        context.getSource().sendFeedback(Text.literal("Couldn't find \"" + input + "\" in the list of regex! \nDo \"/poinpow " + name + " list\" to see a clickable list to remove regex.").styled(style -> style
                .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/poinpow " + name + " list"))
                .withUnderline(true)
                .withColor(Formatting.RED)
        ));

        Commands.running = false;
        return Command.SINGLE_SUCCESS;
    }

    public static int list(FabricClientCommandSource source) {
        Commands.running = true;

        //first check if list has nothing in it
        if (list == null || list.size() == 0) {
            source.sendFeedback(Text.literal("\nThere is no regex in ChatPhraseFilter.").styled(style -> style.withColor(Formatting.BLUE)));
            Commands.running = false;
            return Command.SINGLE_SUCCESS;
        }

        //show the list
        source.sendFeedback(Text.literal("\nHere's the list of regex in ChatPhraseFilter.").styled(style -> style.withColor(Formatting.GREEN)));

        for (Pattern p : list) {
            source.sendFeedback(Text.literal(p.toString()).styled(style -> style
                    .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/poinpow " + name + " remove " + "\"" + p + "\""))
                    .withColor(Formatting.GOLD)
            ));
        }

        source.sendFeedback(Text.literal("Click on the text to remove it from the filter.\n").styled(style -> style.withColor(Formatting.RED)));

        Commands.running = false;
        return Command.SINGLE_SUCCESS;
    }
}
