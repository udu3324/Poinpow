package com.udu3324.poinpow.mixin;

import com.udu3324.poinpow.commands.Commands;
import com.udu3324.poinpow.utils.*;
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
    @Inject(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V", at = @At("HEAD"), cancellable = true)
    private void onMessage(Text message, MessageSignatureData signature, MessageIndicator indicator, CallbackInfo ci) {
        String chat = message.getString();

        //don't allow blockers to run while help command is running
        if (Commands.running) return;

        if (BlockLobbyWelcome.check(chat, ci)) return;

        if (BlockLobbyAds.check(chat, ci)) return;

        if (BlockFreeCredits.check(chat, ci)) return;

        if (BlockMinehutAds.check(chat, ci)) return;

        ChatPhraseFilter.check(chat, ci);
    }
}
