package com.udu3324.poinpow.utils;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.udu3324.poinpow.Config;
import com.udu3324.poinpow.Poinpow;
import com.udu3324.poinpow.commands.CmdUtils;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class AutoSkipBarrier {
    public static String name = "auto_skip_barrier";
    public static String description = "Auto-skips the ads when joining free sub-servers/minehut.";
    public static Boolean toggled = true;
    public static void rename() {
        // return if toggled off (no need for bool)
        if (!toggled) return;

        // return if not on minehut
        if (!Poinpow.onMinehut) return;

        new Thread(() -> {
            try {
                //todo better code where you don't need to wait a bit (caused because of too early mixin)
                //I would shorten it down, but i have to account for slower computers
                Thread.sleep(3500);

                MinecraftClient client = MinecraftClient.getInstance();

                assert client.player != null;

                // ignore creative mode
                if (client.player.getAbilities().creativeMode) return;

                //slot 0 is usually where the barrier is
                ItemStack items = client.player.getInventory().getStack(0);
                if (items.getName().getString().equals("Right Click To Skip")) {
                    Poinpow.LOGGER.info("Auto-skipping world transition");
                    //select the slot
                    client.player.getInventory().selectedSlot = 0;
                    //right click
                    assert client.interactionManager != null;
                    client.interactionManager.interactItem(client.player, client.player.getActiveHand());
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal(name)
                .executes(ctx -> CmdUtils.getStatus(ctx.getSource(), name, toggled))
                .then(literal("true")
                        .executes(ctx -> on(ctx.getSource()))
                )
                .then(literal("false")
                        .executes(ctx -> off(ctx.getSource()))
                )
        );
    }

    private static int on(FabricClientCommandSource source) {
        source.sendFeedback(CmdUtils.getOutput(name, true));
        Config.setValueFromConfig(name, "true");
        toggled = true;
        return Command.SINGLE_SUCCESS;
    }

    private static int off(FabricClientCommandSource source) {
        source.sendFeedback(CmdUtils.getOutput(name, false));
        Config.setValueFromConfig(name, "false");
        toggled = false;
        return Command.SINGLE_SUCCESS;
    }
}
