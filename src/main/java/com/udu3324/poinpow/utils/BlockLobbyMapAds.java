package com.udu3324.poinpow.utils;

import com.udu3324.poinpow.Poinpow;
import com.udu3324.poinpow.api.Minehut;
import net.minecraft.client.render.entity.state.ItemFrameEntityRenderState;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.sound.SoundCategory;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicBoolean;

public class BlockLobbyMapAds {
    public static String name = "block_lobby_map_ads";
    public static String description = "Removes the humungous map art that advertises things in lobby, including the actionbar and bossbar.";

    public static AtomicBoolean toggled = new AtomicBoolean(true);

    public static void block(ItemFrameEntityRenderState ifr) {
        // return if toggled off (no need for bool)
        if (!toggled.get()) return;

        // return if not on minehut
        if (!Poinpow.onMinehut) return;

        Boolean inLobby = Minehut.inLobby();
        if (inLobby == null || !inLobby) return;

        // remove if item frame has a map in it
        if (ifr.mapId == null) return;
        ifr.mapId = null;

    }

    public static boolean checkActionbar(String actionbar, CallbackInfo ci) {
        // return false if toggled off
        if (!toggled.get() || !Poinpow.onMinehut) return false;

        //text when hovering over map ads in lobby
        if (actionbar.contains("[Billboard]")) {
            ci.cancel();
        }

        return true;
    }

    public static boolean checkBossbar(String text) {
        // return false if toggled off
        if (!toggled.get()) return false;

        // return if not on minehut
        if (!Poinpow.onMinehut) return false;

        return text.contains("[Billboard]");
    }

    public static boolean checkSound(PlaySoundS2CPacket packet) {
        // return false if toggled off
        if (!toggled.get()) return false;

        // return if not on minehut
        if (!Poinpow.onMinehut) return false;

        // played as an ambient sound
        if (packet.getCategory() != SoundCategory.AMBIENT) return false;

        // unregistered
        if (!packet.getSound().getIdAsString().equals("[unregistered]")) return false;

        Poinpow.log.info("Blocking sound from map ad");
        return true;
    }
}
