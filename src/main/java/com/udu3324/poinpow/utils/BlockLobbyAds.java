package com.udu3324.poinpow.utils;

import com.udu3324.poinpow.Poinpow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

public class BlockLobbyAds {
    public static String name = "block_lobby_ads";
    public static String description = "Blocks ads made by players in the lobby.";
    public static AtomicBoolean toggled = new AtomicBoolean(true);

    final static Pattern pattern = Pattern.compile("\\[AD]");

    public static Boolean check(String chat, CallbackInfo ci) {
        // return false if toggled off
        if (!toggled.get()) return false;

        // return if not on minehut
        if (!Poinpow.onMinehut) return false;

        //if it's not a suspected ad, return
        if (!(pattern.matcher(chat).find() || chat.contains(": /join"))) return false;

        //confirmed an ad

        //check if the rank is allowed to send it, return to allow sending
        if (ToggleSpecificAds.checkRank(chat)) return false;

        Poinpow.log.info("Blocked: {}", chat.replace("\n", ""));
        ci.cancel();

        return true;
    }
}