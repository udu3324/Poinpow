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

    public static void check() {
        // return if toggled off (no need for bool)
        if (!toggled.get()) return;

        // return if not on minehut
        if (!Poinpow.onMinehut) return;

        new Thread(() -> {
            try {
                int tries = 0;
                Poinpow.log.info("AutoSkipBarrier: Starting auto skip barrier period.");

                //only try for 50 times, lasts about 5 seconds to allow slow connections/computers to load server items
                while (tries <= 50) {
                    Thread.sleep(100);

                    MinecraftClient client = MinecraftClient.getInstance();

                    if (client.player == null || client.interactionManager == null) {
                        Poinpow.log.info("AutoSkipBarrier: Player/client null");
                        return;
                    }

                    //loop through the hot bar to check for each item's name
                    ItemStack items;
                    for (int itemIterator = 0; itemIterator < 9; itemIterator++) {
                        items = client.player.getInventory().getStack(itemIterator);

                        if (items.getName().getString().equals("Right click to continue")) {
                            client.player.getInventory().setSelectedSlot(itemIterator);
                            client.interactionManager.interactItem(client.player, client.player.getActiveHand());

                            Poinpow.log.info("AutoSkipBarrier: Found item! Attempting to skip...");
                        }
                    }

                    tries++;
                }

                Poinpow.log.info("AutoSkipBarrier: Ended auto skip barrier period.");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}