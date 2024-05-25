package com.udu3324.poinpow.mixin;

import com.udu3324.poinpow.utils.BlockChestAds;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public class ChestMixin {
    @Inject(method = "drawSlot", at = @At("HEAD"), cancellable = true)
    private void onItemRender(DrawContext context, Slot slot, CallbackInfo ci) {
        BlockChestAds.check(slot);
    }
}
