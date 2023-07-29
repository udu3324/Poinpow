package com.udu3324.poinpow.utils;

import com.udu3324.poinpow.Config;
import com.udu3324.poinpow.Poinpow;
import net.minecraft.client.MinecraftClient;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

public class ChatPhraseFilter {
    public static String name = "chat_phrase_filter";
    public static String description = "Filters out messages with certain words that are sent in lobby. Uses regex! Go to https://regex101.com to build some.";
    public static AtomicBoolean toggled = new AtomicBoolean(true);

    public static ArrayList<Pattern> list = Config.getListOfRegex();

    public static void check(String chat, CallbackInfo ci) {
        // return false if toggled off
        if (!toggled.get()) return;

        // return if not on minehut
        if (!Poinpow.onMinehut) return;

        // return if there's nothing in the list
        if (list == null || list.size() == 0) {
            System.out.println("!!!!!!!!!! nothing in list");
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
            System.out.println("!!!!!!!!!!! " + p.toString());
        }
    }
}
