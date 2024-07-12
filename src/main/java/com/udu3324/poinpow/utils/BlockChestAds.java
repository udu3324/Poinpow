package com.udu3324.poinpow.utils;

import com.udu3324.poinpow.Poinpow;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
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

        Text name = slot.getStack().getName();

        if (name.getString().equals("Air")) return;

        if (name.withoutStyle().size() < 2) return;

        Style style = name.withoutStyle().get(1).getStyle();

        if (style.getColor() == TextColor.fromFormatting(Formatting.DARK_GRAY)) {
            ItemStack item = Items.BLACK_STAINED_GLASS_PANE.getDefaultStack();
            item.set(DataComponentTypes.CUSTOM_NAME, Text.literal(""));

            slot.setStack(item);
        }
    }
}
