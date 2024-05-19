package com.udu3324.poinpow.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.stream.Collectors;

@Mixin(HandledScreen.class)
public class ChestMixin {
    @Inject(method = "drawSlot", at = @At("HEAD"), cancellable = true)
    private void onItemRender(DrawContext context, Slot slot, CallbackInfo ci) {
        ItemStack item = slot.getStack();
        String name = slot.getStack().getName().getString();

        if (name.equals("Air")) return;

        //System.out.println("fount item " + slot.getStack().getName().getString());

        if (item.getNbt() != null) {
            NbtCompound displayTag = item.getNbt().getCompound("display");
            if (displayTag.contains("Lore", 9)) {
                NbtList loreList = displayTag.getList("Lore", 8);
                String lore = loreList.stream().map(NbtElement::asString).collect(Collectors.joining(", "));
            }
        }
    }
}
