package com.udu3324.poinpow.utils;

import com.udu3324.poinpow.Poinpow;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;

import java.util.concurrent.atomic.AtomicBoolean;

// ty for contributing https://github.com/DutchO7 !!
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
                int tries = 0;
                for (int i = 0; i < 3000; i += 10) {
                    Thread.sleep(i);

                    MinecraftClient client = MinecraftClient.getInstance();

                    // return if player hasn't loaded in it
                    if (client.player == null) return;

                    if (client.interactionManager == null) return;
                  
                    ItemStack items;
                    for (int itemIterator = 0; itemIterator < 9; itemIterator++) {
                        items = client.player.getInventory().getStack(itemIterator);
                        if (items.getName().getString().equals("Right click to continue")) {
                            // set item slot to wherever a item with this name is
                            client.player.getInventory().selectedSlot = itemIterator;

                            //send right click
                            client.interactionManager.interactItem(client.player, client.player.getActiveHand());
                        } else {
                            tries++;
                        }
                    }

                    if (tries > 90) break;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}