package com.udu3324.poinpow.mixin;

import com.udu3324.poinpow.utils.BlockMinehutAds;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.network.packet.s2c.play.BossBarS2CPacket;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.UUID;

//https://github.com/haze/starfruit/blob/de8250677ed640423a18ed30ddf5b081afa06e4d/src/main/java/software/tachyon/starfruit/mixin/gui/BossBarHudMixin.java#L19
@Mixin(BossBarHud.class)
public class BossbarMixin {

    @Final
    @Shadow
    private Map<UUID, ClientBossBar> bossBars;

    @Inject(method = "handlePacket", at = @At("HEAD"), cancellable = true)
    private void onBossbarRender(BossBarS2CPacket packet, CallbackInfo ci) {
        BlockMinehutAds.checkBossbar(bossBars, ci);
    }
}
