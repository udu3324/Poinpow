package com.udu3324.poinpow.mixin;

import com.udu3324.poinpow.utils.BlockMinehutAds;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.entity.boss.BossBar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BossBarHud.class)
public class BossbarMixin {
    @Inject(method = "renderBossBar", at = @At("HEAD"), cancellable = true)
    private void onBossbarRender(DrawContext context, int x, int y, BossBar bossBar, CallbackInfo ci) {
        BlockMinehutAds.checkBossbar(bossBar, ci);
    }
}
