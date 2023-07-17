package com.udu3324.poinpow.utils;

import com.udu3324.poinpow.Poinpow;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

public class RemoveLobbyRanks {
    public static String name = "remove_lobby_ranks";
    public static String description = "Remove ranks from players and generalize chat color in the MH lobby.";
    public static AtomicBoolean toggled = new AtomicBoolean(false);
    private static Boolean running = true;

    final static Pattern join = Pattern.compile("^(\\[(PRO|VIP|LEGEND|PATRON)] )[a-zA-Z0-9_.]{1,16} joined your lobby.$");
    final static Pattern rankedMsg = Pattern.compile("^(\\[(PRO|VIP|LEGEND|PATRON)] )[a-zA-Z0-9_.]{1,16}: ");
    final static Pattern normalMsg = Pattern.compile("^[a-zA-Z0-9_.]{1,16}: ");

    public static void check(String chat, CallbackInfo ci) {
        // return false if toggled off
        if (!toggled.get()) return;

        // return if not on minehut
        if (!Poinpow.onMinehut) return;

        //ranked users joining
        if (join.matcher(chat).find()) {
            Poinpow.log.info("Blocked: " + chat);
            ci.cancel();
            return;
        }

        //ranked users messaging
        if (rankedMsg.matcher(chat).find()) {
            Poinpow.log.info("Original: " + chat);
            ci.cancel();

            //remove the rank prefix
            if (chat.contains("]"))
                chat = chat.substring(chat.indexOf("]") + 2);

            //send it to client
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal(chat));
            return;
        }

        //normal users chatting
        if (normalMsg.matcher(chat).find() && running) {
            ci.cancel();

            running = false;
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal(chat).styled(style -> style.withColor(Formatting.WHITE)));
            running = true;
        }
    }
}
