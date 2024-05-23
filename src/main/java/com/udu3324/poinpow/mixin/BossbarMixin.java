package com.udu3324.poinpow.mixin;

import com.udu3324.poinpow.utils.BlockMinehutAds;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BossBarHud.class)
public class BossbarMixin {
    @Inject(method = "renderBossBar", at = @At("HEAD"), cancellable = true)
    public static void onBossbarRender(DrawContext context, int x, int y, BossBar bossBar, int width, Identifier[] textures, Identifier[] notchedTextures, CallbackInfo ci) {
        BlockMinehutAds.checkBossbar(bossBar, ci);
    }
}
