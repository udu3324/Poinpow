package com.udu3324.poinpow.commands;

import com.mojang.brigadier.Command;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class CmdUtils {
    public static int getStatus(FabricClientCommandSource source, String name, Boolean toggled) {
        source.sendFeedback(Text.literal(name + " is currently set to " + toggled));
        return Command.SINGLE_SUCCESS;
    }

    public static Text getOutput(String name, Boolean bool) {
        return Text.literal(name + " has been set to " + bool + ".").styled(style -> style.withColor(Formatting.GREEN));
    }
}
