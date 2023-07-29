package com.udu3324.poinpow.utils;

import com.udu3324.poinpow.Poinpow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

public class BlockMinehutAds {
    public static String name = "block_minehut_ads";
    public static String description = "Blocks ads made by minehut that show up before joining servers & ads in free sub-servers.";
    public static AtomicBoolean toggled = new AtomicBoolean(true);

    final static Pattern pattern = Pattern.compile("^(\\n\\n|/n/n)\\[Minehut].*(\\n\\n|/n/n)$");

    public static Boolean check(String chat, CallbackInfo ci) {
        // return false if toggled off
        if (!toggled.get()) return false;

        // return if not on minehut
        if (!Poinpow.onMinehut) return false;

        if (pattern.matcher(chat).find()) {
            Poinpow.log.info("Blocked: " + chat);
            ci.cancel();

            return true;
        }
        return false;
    }
}
