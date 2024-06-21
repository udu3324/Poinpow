package com.udu3324.poinpow.mixin;

import com.udu3324.poinpow.api.Minehut;
import com.udu3324.poinpow.commands.Commands;
import com.udu3324.poinpow.utils.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public class ChatMixin {
    @Inject(method = "Lnet/minecraft/client/gui/hud/ChatHud;addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V", at = @At("HEAD"), cancellable = true)
    private void onMessage(Text message, MessageSignatureData signature, MessageIndicator indicator, CallbackInfo ci) {
        String chat = message.getString();

        //don't allow blockers to run while help command is running
        if (Commands.running) return;

        //if lobby chat is toggled, don't run checks
        if (MuteLobbyChat.toggled) {
            // if they're in lobby & the message isn't allowed, cancel.
            Boolean inLobby = Minehut.inLobby();
            if (inLobby != null && inLobby && !MuteLobbyChat.isAllowed(chat)) {
                ci.cancel();
                return;
            }
        }

        if (BlockLobbyWelcome.check(chat, ci)) return;

        if (BlockLobbyAds.check(chat, ci)) return;

        if (BlockFreeCredits.check(chat, ci)) return;

        if (BlockMinehutAds.checkChat(chat, ci)) return;

        if (BlockRaids.check(chat, ci)) return;

        ChatPhraseFilter.check(chat, ci);
    }

    @Inject(method = "Lnet/minecraft/client/gui/hud/ChatHud;render(Lnet/minecraft/client/gui/DrawContext;III)V", at = @At("RETURN"), cancellable = true)
    private void onRender(DrawContext context, int currentTick, int mouseX, int mouseY, CallbackInfo ci) {
        MuteLobbyChat.check();
    }
}
