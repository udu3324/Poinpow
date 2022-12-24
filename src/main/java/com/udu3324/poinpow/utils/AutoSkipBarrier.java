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
                        break;
                    }
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
