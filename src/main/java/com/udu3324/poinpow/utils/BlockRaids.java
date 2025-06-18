package com.udu3324.poinpow.utils;

import com.udu3324.poinpow.Poinpow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

public class BlockRaids {
    public static String name = "block_raids";
    public static String description = "Blocks the raid alerts shown in lobby.";
    public static AtomicBoolean toggled = new AtomicBoolean(true);

    final static Pattern pattern = Pattern.compile("Minehut \\| Raid starts in [0-9]+ Seconds!");
    final static Pattern pattern2 = Pattern.compile("Minehut \\| A new raid is ready! Enter the green circle to begin\\.");
    final static Pattern pattern3 = Pattern.compile("Minehut \\| Raiding is on cooldown for another [0-9]+ Second(s|)\\.");
    final static Pattern pattern4 = Pattern.compile("Minehut \\| (Joined|Left) the raid");

    public static Boolean check(String chat, CallbackInfo ci) {
        // return false if toggled off
        if (!toggled.get()) return false;

        // return if not on minehut
        if (!Poinpow.onMinehut) return false;

        if (pattern.matcher(chat).find() || pattern2.matcher(chat).find() || pattern3.matcher(chat).find() || pattern4.matcher(chat).find()) {
            Poinpow.log.info("Blocked: {}", chat);
            ci.cancel();

            return true;
        }

        return false;
    }

    public static boolean checkBossbar(String text) {
        // return false if toggled off
        if (!toggled.get()) return false;

        // return if not on minehut
        if (!Poinpow.onMinehut) return false;

        return text.contains("Raid");
    }
}
