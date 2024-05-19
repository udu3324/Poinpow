package com.udu3324.poinpow.mixin;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(InventoryS2CPacket.class)
public class ChestMixin {
    @Inject(method = "write", at = @At("HEAD"))
    private void pleaseWork(PacketByteBuf buf) {
        //todo
        //System.out.println("!!!!!!!!!!!!!!!!!!!");
        //System.out.println("buf: " + buf.readString());
        //System.out.println("found " + inventory.getStack(0).getItem().getName().getString());
        //System.out.println("found " + inventory.getStack(0).getItem().getName().getLiteralString());
    }
}
