package com.udu3324.poinpow.utils;

import com.udu3324.poinpow.Poinpow;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.concurrent.atomic.AtomicBoolean;

public class BlockChestAds {
    public static String name = "block_chest_ads";
    public static String description = "This removes the ads in the compass server listing.";
    public static AtomicBoolean toggled = new AtomicBoolean(true);

    public static void check(Slot slot) {
        // return false if toggled off
        if (!toggled.get()) return;

        // return if not on minehut
        if (!Poinpow.onMinehut) return;

        if (slot.getStack().getName().toString().contains("ᴀᴅ")) {
            ItemStack item = Items.DIAMOND.getDefaultStack();
            item.set(DataComponentTypes.CUSTOM_NAME, Text.literal("Server Advertisement").getWithStyle(Style.EMPTY.withColor(Formatting.AQUA).withBold(true).withItalic(false)).getFirst());
            item.set(DataComponentTypes.LORE, new LoreComponent(Text.literal("Blocked by Poinpow").getWithStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY).withItalic(false))));
            slot.setStack(item);
        }
    }

}
