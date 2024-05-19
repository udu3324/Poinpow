package com.udu3324.poinpow.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class ActionbarMixin {
    @Inject(method = "setOverlayMessage", at = @At("HEAD"), cancellable = true)
    private void onActionBarSet(Text message, boolean tinted, CallbackInfo ci) {
        String text = message.getLiteralString();
        System.out.println("smth changed ");
        if (text == null) return;
        System.out.println("found msg!!! " + text);

        if (text.equals("eee")) {
            ci.cancel();
        }
    }
}
