package com.udu3324.poinpow.mixin;

import com.udu3324.poinpow.utils.BlockLobbyMapAds;
import com.udu3324.poinpow.utils.BlockRaids;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.text.Text;
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

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void onBossbarRender(DrawContext drawContext, CallbackInfo ci) {
        if (bossBars.isEmpty()) return;

        bossBars.values().forEach(clientBossBar -> {
            String displayText = clientBossBar.getName().getString();

            if (BlockLobbyMapAds.checkBossbar(displayText)) {
                clientBossBar.setName(Text.literal(""));
            } else if (BlockRaids.checkBossbar(displayText)) {
                clientBossBar.setName(Text.literal(""));
            }
        });
    }
}
