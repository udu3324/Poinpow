package com.udu3324.poinpow.utils;

import com.udu3324.poinpow.Poinpow;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;

import java.util.concurrent.atomic.AtomicBoolean;

public class AutoSkipBarrier {
    public static String name = "auto_skip_barrier";
    public static String description = "Auto-skips the ads when joining free sub-servers/minehut.";
    public static AtomicBoolean toggled = new AtomicBoolean(true);
    public static void rename() {
        // return if toggled off (no need for bool)
        if (!toggled.get()) return;

        // return if not on minehut
        if (!Poinpow.onMinehut) return;

        new Thread(() -> {
            try {
                for (int i = 0; i < 3000; i += 10) {
                    Thread.sleep(i);

                    MinecraftClient client = MinecraftClient.getInstance();
                    // return if player hasn't loaded in it
                    if (client.player == null) return;

                    // ignore creative mode
                    if (client.player.getAbilities().creativeMode) break;

                    ItemStack items = client.player.getInventory().getStack(0);
                    if (items.getName().getString().equals("Right Click To Skip")) {
                        Poinpow.log.info("Auto-skipping world transition");

                        //select the slot
                        client.player.getInventory().selectedSlot = 0;

                        if (client.interactionManager == null) return;

                        //right click
                        client.interactionManager.interactItem(client.player, client.player.getActiveHand());
                        for (int e = 0; e < 1500; e += 10) {
                            Thread.sleep(i);

                            //break if item is gone
                            items = client.player.getInventory().getStack(0);
                            if (!items.getName().getString().equals("Right Click To Skip")) break;

                            //if (client.player == null) break;

                            if (client.player.getInventory().selectedSlot == 0)
                                client.player.getInventory().selectedSlot = 1;
                            else
                                client.player.getInventory().selectedSlot = 0;

                            client.interactionManager.interactItem(client.player, client.player.getActiveHand());
                        }
                        break;
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
