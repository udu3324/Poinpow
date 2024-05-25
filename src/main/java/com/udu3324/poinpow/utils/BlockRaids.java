package com.udu3324.poinpow.utils;

import com.udu3324.poinpow.Poinpow;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.entity.boss.BossBarManager;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

public class BlockRaids {
    public static String name = "block_raids";
    public static String description = "Blocks the raid alerts shown in lobby.";
    public static AtomicBoolean toggled = new AtomicBoolean(true);

    final static Pattern pattern = Pattern.compile("Minehut \\| Raid starts in [0-9]+ Seconds!");
    final static Pattern pattern2 = Pattern.compile("Minehut \\| A new raid is ready! Enter the green circle to begin\\.");

    public static Boolean check(String chat, CallbackInfo ci) {
        // return false if toggled off
        if (!toggled.get()) return false;

        // return if not on minehut
        if (!Poinpow.onMinehut) return false;

        if (pattern.matcher(chat).find() || pattern2.matcher(chat).find()) {
            Poinpow.log.info("Blocked: " + chat);
            ci.cancel();
        }

        return pattern.matcher(chat).find();
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

            if (text.getString().contains("Raid")) {
                bossbar = entry.getKey();
                break;
            }
        }

        if (bossbar == null) return;
        bossbars.remove(bossbar);
        ci.cancel();
    }
}
