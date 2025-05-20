package com.udu3324.poinpow.utils;

import com.udu3324.poinpow.Poinpow;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.sound.SoundCategory;

import java.util.concurrent.atomic.AtomicBoolean;

public class BlockSoundAds {
    public static String name = "block_sound_ads";
    public static String description = "Blocks the sound ads played in lobby from the server texture pack.";
    public static AtomicBoolean toggled = new AtomicBoolean(true);

    public static boolean removeSound(PlaySoundS2CPacket packet) {
        // return false if toggled off
        if (!toggled.get()) return false;

        // return if not on minehut
        if (!Poinpow.onMinehut) return false;

        // played as an ambient sound
        if (packet.getCategory() != SoundCategory.AMBIENT) return false;

        // unregistered
        if (!packet.getSound().getIdAsString().equals("[unregistered]")) return false;

        return true;
    }
}
