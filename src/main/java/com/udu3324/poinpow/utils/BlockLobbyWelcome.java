package com.udu3324.poinpow.utils;

import com.udu3324.poinpow.Poinpow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

public class BlockLobbyWelcome {
    public static String name = "block_lobby_welcome";
    public static String description = "Blocks the welcome message in lobby that is obstructive and could contain ads.";
    public static AtomicBoolean toggled = new AtomicBoolean(true);

    public static int limit = 0;
    private static Boolean ignoreChat = false;
    public static Boolean check(String chat, CallbackInfo ci) {
        // return false if toggled off
        if (!toggled.get()) return false;

        // return if not on minehut
        if (!Poinpow.onMinehut) return false;

        boolean blocked = false;

        if (ignoreChat) {
            limit++;

            //parsing failed
            if (limit >= 6) {
                ignoreChat = false;
                limit = 0;
            }

            if (chat.contains("https://go.minehut.com")) ignoreChat = false;
            blocked = true;
        }

        Pattern pattern = Pattern.compile("^Welcome back, [a-zA-Z0-9_.]{1,16}$");

        if (pattern.matcher(chat).find()) {
            ignoreChat = true;
            blocked = true;
        }

        if (blocked) {
            Poinpow.log.info("Blocked: " + chat);
            ci.cancel();
        }

        return blocked;
    }
}