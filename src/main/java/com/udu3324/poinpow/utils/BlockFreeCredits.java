package com.udu3324.poinpow.utils;

import com.udu3324.poinpow.Poinpow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

public class BlockFreeCredits {
    public static String name = "block_free_credits";
    public static String description = "Blocks minehut encouraging /vote when other players do it.";
    public static AtomicBoolean toggled = new AtomicBoolean(true);

    final static Pattern pattern = Pattern.compile("^\\[Minehut] [a-zA-Z0-9_.]{1,16} just got free credits( and gems|) by voting via /vote$");

    public static Boolean check(String chat, CallbackInfo ci) {
        // return false if toggled off
        if (!toggled.get()) return false;

        // return if not on minehut
        if (!Poinpow.onMinehut) return false;

        if (pattern.matcher(chat).find()) {
            Poinpow.log.info("Blocked: {}", chat);
            ci.cancel();
        }

        return pattern.matcher(chat).find();
    }
}