package com.udu3324.poinpow.utils;

import com.udu3324.poinpow.Poinpow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

public class BlockMinehutAds {
    public static String name = "block_minehut_ads";
    public static String description = "Blocks ads made by minehut that sometimes shows up in free sub-servers.";
    public static AtomicBoolean toggled = new AtomicBoolean(true);

    public static Boolean check(String chat, CallbackInfo ci) {
        // return false if toggled off
        if (!toggled.get()) return false;

        // return if not on minehut
        if (!Poinpow.onMinehut) return false;

        Pattern pattern = Pattern.compile("^(\\n\\n|/n/n)\\[Minehut].*(\\n\\n|/n/n)$");

        if (pattern.matcher(chat).find()) {
            Poinpow.log.info("Blocked: " + chat);
            ci.cancel();
        }

        return pattern.matcher(chat).find();
    }
}
