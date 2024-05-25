package com.udu3324.poinpow.utils;

import com.udu3324.poinpow.Poinpow;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

public class BlockMinehutAds {
    public static String name = "block_minehut_ads";
    public static String description = "Blocks ads made by minehut that sometimes shows up in free sub-servers, and clean the bossbar and actionbar.";
    public static AtomicBoolean toggled = new AtomicBoolean(true);

    final static Pattern pattern = Pattern.compile("^(\\n\\n|/n/n)\\[Minehut].*(\\n\\n|/n/n)$");

    public static Boolean runOnceOnLoad = false;

    public static Boolean checkChat(String chat, CallbackInfo ci) {
        // return false if toggled off
        if (!toggled.get()) return false;

        // return if not on minehut
        if (!Poinpow.onMinehut) return false;

        if (pattern.matcher(chat).find()) {
            Poinpow.log.info("Blocked: " + chat);
            ci.cancel();
        }

        return pattern.matcher(chat).find();
    }

    public static void checkActionbar(String actionbar, CallbackInfo ci) {
        // return false if toggled off
        if (!toggled.get()) return;

        // return if not on minehut
        if (!Poinpow.onMinehut) return;

        //text when hovering over map ads in lobby
        if (actionbar.contains("[Billboard]")) {
            ci.cancel();
        }
    }

    public static void checkBossbar(Map<UUID, ClientBossBar> bossbars, CallbackInfo ci) {
        // return false if toggled off
        if (!toggled.get()) return;

        // return if not on minehut
        if (!Poinpow.onMinehut) return;

        //boss bar shown when hovering over map ads
        // if (bossbar.getName().getString().contains("[Billboard]")) {
        //    ci.cancel();
        //}

        //loop through all bossbars
        UUID bossbar = null;
        for (Map.Entry<UUID, ClientBossBar> entry : bossbars.entrySet()) {
            Text text = entry.getValue().getName();

            if (text.getString().contains("[Billboard]")) {
                bossbar = entry.getKey();
                break;
            }
        }

        bossbars.remove(bossbar);
        ci.cancel();
    }
}
