package com.udu3324.poinpow.utils;

import com.udu3324.poinpow.Poinpow;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class BlockLobbyMapAds {
    public static String name = "block_lobby_map_ads";
    public static String description = "Removes the humungous map art that advertises things in lobby, including the actionbar and bossbar.";
    public static AtomicBoolean toggled = new AtomicBoolean(true);
    private static final ItemStack item = new ItemStack(Items.DIAMOND, 1).setCustomName(Text.of("Poinpow by udu3324"));

    public static void block(Entity entity) {
        // return if toggled off (no need for bool)
        if (!toggled.get()) return;

        // return if not on minehut
        if (!Poinpow.onMinehut) return;

        if (MinecraftClient.getInstance().player == null) return;

        // check for scoreboard
        Scoreboard scoreboard = MinecraftClient.getInstance().player.getScoreboard();
        ArrayList<String> scores = new ArrayList<>();
        for (ScoreboardObjective objective : scoreboard.getObjectives()) {
            scores.add(objective.getDisplayName().toString());
        }

        if (!scores.toString().toLowerCase().contains("minehut")) return;

        // remove if item frame has a map in it
        ItemFrameEntity itemFrame = (ItemFrameEntity) entity;

        if (!itemFrame.containsMap()) return;

        //Poinpow.log.info("Blocked: Lobby Map Ad (" + itemFrame.getBlockX() + ", " + itemFrame.getBlockY() + ", " + itemFrame.getBlockZ() + ")");

        itemFrame.setHeldItemStack(item);
        itemFrame.setRotation(1);
    }

    public static boolean checkActionbar(String actionbar, CallbackInfo ci) {
        // return false if toggled off
        if (!toggled.get()) return false;

        // return if not on minehut
        if (!Poinpow.onMinehut) return false;

        //text when hovering over map ads in lobby
        if (actionbar.contains("[Billboard]")) {
            ci.cancel();
        }

        return true;
    }

    public static void checkBossbar(Map<UUID, ClientBossBar> bossbars, CallbackInfo ci) {
        // return false if toggled off
        if (!toggled.get()) return;

        // return if not on minehut
        if (!Poinpow.onMinehut) return;

        //return if bossbars is empty
        if (bossbars.isEmpty()) return;

        //check all the bossbars present
        UUID bossbar = null;

        for (Map.Entry<UUID, ClientBossBar> entry : bossbars.entrySet()) {
            Text text = entry.getValue().getName();

            if (text.getString().contains("[Billboard]")) {
                bossbar = entry.getKey();
                break;
            }
        }

        if (bossbar == null) return;

        bossbars.remove(bossbar);
        ci.cancel();
    }
}
