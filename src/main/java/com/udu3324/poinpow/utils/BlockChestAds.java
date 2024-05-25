package com.udu3324.poinpow.utils;

import com.udu3324.poinpow.Poinpow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;

import java.util.concurrent.atomic.AtomicBoolean;

public class BlockChestAds {
    public static String name = "block_chest_ads";
    public static String description = "This removes the ads in the compass server listing.";
    public static AtomicBoolean toggled = new AtomicBoolean(true);

    private static final ItemStack item = Items.BLACK_STAINED_GLASS_PANE.getDefaultStack().setCustomName(Text.literal(""));

    public static void check(Slot slot) {
        // return false if toggled off
        if (!toggled.get()) return;

        // return if not on minehut
        if (!Poinpow.onMinehut) return;

        String name = slot.getStack().getName().getString();

        if (name.equals("Air")) return;

        if (name.contains("[AD]")) {
            slot.setStack(item);
        }
    }
}
