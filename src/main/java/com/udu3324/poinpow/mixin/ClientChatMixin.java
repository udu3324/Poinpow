package com.udu3324.poinpow.mixin;

import com.udu3324.poinpow.utils.HubCommandBack;
import net.minecraft.client.gui.screen.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
public class ClientChatMixin {
    @Inject(method = "sendMessage", at = @At("HEAD"), cancellable = true)

    private void onSendMessage(String chatText, boolean addToHistory, CallbackInfo ci) {
        HubCommandBack.scan(chatText, ci);
    }
}
