package com.udu3324.poinpow.utils;

import com.udu3324.poinpow.Config;
import com.udu3324.poinpow.commands.Commands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import com.mojang.brigadier.Command;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ToggleSpecificAds {
    public static String name = "toggle_specific_ads";
    public static String description = "This toggles and blocks certain ads based on ranks.";

    public static boolean defaultRank = false;
    public static boolean vip = false;
    public static boolean vipPlus = false;
    public static boolean pro = false;
    public static boolean legend = false;
    public static boolean patron = false;

    //return true if a rank that is toggled is matched
    public static boolean checkRank(String chat) {
        String rank = chat.substring(0, chat.indexOf(":"));

        //return true if none are activated
        if (!(defaultRank || vip || vipPlus || pro || legend || patron))
            return false;

        //check for if the rank portion contains it
        if (rank.contains("[VIP]") && vip) {
            return true;
        } else if (rank.contains("[VIP+]") && vipPlus) {
            return true;
        } else if (rank.contains("[PRO]") && pro) {
            return true;
        } else if (rank.contains("[LEGEND]") && legend) {
            return true;
        } else if (rank.contains("[PATRON]") && patron) {
            return true;
        } else return !(rank.contains("VIP") || rank.contains("PRO") || rank.contains("LEGEND") || rank.contains("PATRON")) && defaultRank;
    }

    public static int toggle(FabricClientCommandSource source, String rank) {
        Commands.running = true;

        switch (rank) {
            case "default":
                defaultRank = !defaultRank;
                source.sendFeedback(Text.literal("Default rank ads toggled: " + defaultRank));
                Config.setValueFromConfig(name + "_default", String.valueOf(defaultRank));
                break;
            case "vip":
                vip = !vip;
                source.sendFeedback(Text.literal("Vip rank ads toggled: " + vip));
                Config.setValueFromConfig(name + "_vip", String.valueOf(vip));
                break;
            case "vipPlus":
                vipPlus = !vipPlus;
                source.sendFeedback(Text.literal("Vip+ rank ads toggled: " + vipPlus));
                Config.setValueFromConfig(name + "_vipPlus", String.valueOf(vipPlus));
                break;
            case "pro":
                pro = !pro;
                source.sendFeedback(Text.literal("Pro rank ads toggled: " + pro));
                Config.setValueFromConfig(name + "_pro", String.valueOf(pro));
                break;
            case "legend":
                legend = !legend;
                source.sendFeedback(Text.literal("Legend rank ads toggled: " + legend));
                Config.setValueFromConfig(name + "_legend", String.valueOf(legend));
                break;
            case "patron":
                patron = !patron;
                source.sendFeedback(Text.literal("Patron rank ads toggled: " + patron));
                Config.setValueFromConfig(name + "_patron", String.valueOf(patron));
                break;
        }

        Commands.running = false;
        return Command.SINGLE_SUCCESS;
    }

    public static int description(FabricClientCommandSource source) {
        Commands.running = true;

        //show the command
        source.sendFeedback(Text.literal("\n/poinpow " + name + " (rank)").styled(style -> style.withColor(Formatting.GOLD)));

        //show the command's description
        source.sendFeedback(Text.literal(description));
        source.sendFeedback(Text.literal("Default: " + defaultRank + "\nVIP: " + vip + "\nVIP+: " + vipPlus + "\nPro: " + pro + "\nLegend: " + legend + "\nPatron: " + patron).styled(style -> style
                .withColor(Formatting.GRAY)
                .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/poinpow " + ToggleSpecificAds.name + " "))));

        Commands.running = false;
        return Command.SINGLE_SUCCESS;
    }
}
