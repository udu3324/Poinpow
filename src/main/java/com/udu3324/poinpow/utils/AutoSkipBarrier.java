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
        if (!toggled.get() || !Poinpow.onMinehut) return;

        new Thread(() -> {
            try {
                int tries = 0;
                for (int i = 0; i < 3000; i += 10) {
                    Thread.sleep(i);

                    MinecraftClient client = MinecraftClient.getInstance();

                    if (client.player == null || client.interactionManager == null) return;

                    ItemStack items;
                    for (int itemIterator = 0; itemIterator < 9; itemIterator++) {
                        items = client.player.getInventory().getStack(itemIterator);
                        if (items.getName().getString().equals("Right click to continue")) {
                            client.player.getInventory().selectedSlot = itemIterator;
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