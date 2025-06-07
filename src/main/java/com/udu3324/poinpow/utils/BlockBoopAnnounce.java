package com.udu3324.poinpow.utils;

import com.udu3324.poinpow.Poinpow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicBoolean;

public class BlockBoopAnnounce {
    public static String name = "block_boop_announce";
    public static String description = "Blocks the boop arena announcements in the lobby.";
    public static AtomicBoolean toggled = new AtomicBoolean(true);

    public static Boolean check(String chat, CallbackInfo ci) {
        // return false if toggled off
        if (!toggled.get()) return false;

        // return if not on minehut
        if (!Poinpow.onMinehut) return false;

        //return if it's not a boop message
        if (!chat.contains(" Boop Arena! Try to boop them out and stay inside to stake your claim on the leaderboard")) return false;

        Poinpow.log.info("Blocked: {}", chat.replace("\n", ""));
        ci.cancel();

        return true;
    }
}
