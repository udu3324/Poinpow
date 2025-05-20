package com.udu3324.poinpow.utils;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.udu3324.poinpow.Config;
import com.udu3324.poinpow.api.Minehut;
import com.udu3324.poinpow.commands.Commands;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class MuteLobbyChat {
    public static String name = "muteLobbyChat";
    public static String description = "This mutes the lobby chat if you don't want to see it.";

    public static boolean toggled = false;
    public static boolean canSendCheck = false;

    public static void registerCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal(name).executes(ctx -> toggle(ctx.getSource())));
    }

    private static int toggle(FabricClientCommandSource source) {
        Commands.running = true;

        //don't allow check as they already know
        canSendCheck = false;

        if (toggled) {
            source.sendFeedback(Text.literal("Lobby chat has been unmuted.").styled(style -> style
                    .withColor(Formatting.RED)));

            toggled = false;
        } else {
            source.sendFeedback(Text.literal("Lobby chat has been muted.").styled(style -> style
                    .withColor(Formatting.GREEN)));

            toggled = true;
        }

        Config.setValueFromConfig(name, String.valueOf(toggled));

        Commands.running = false;
        return Command.SINGLE_SUCCESS;
    }

    public static boolean isAllowed(String chat) {
        if (MinecraftClient.getInstance().player == null || MinecraftClient.getInstance().player.getName().getLiteralString() == null)
            return false;

        //it is a /msg from someone
        if (chat.startsWith("To") || chat.startsWith("From")) { //it is a /msg from someone
            return true;
        } else { //messages with the player's ign
            if (!chat.contains(":")) { //misc message should be allowed
                return true;
            } else {
                return chat.toLowerCase().contains(MinecraftClient.getInstance().player.getName().getLiteralString().toLowerCase());
            }
        }
    }

    //this is run when joining back into the lobby while it is muted.
    public static void check() {
        if (!canSendCheck) return;

        if (!toggled) return;

        Boolean inLobby = Minehut.inLobby();
        if (inLobby != null && inLobby) {
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal("Lobby is currently muted by you. (click)").styled(style -> style
                    .withClickEvent(new ClickEvent.RunCommand("/" + MuteLobbyChat.name))
                    .withColor(Formatting.DARK_GRAY)));
        }

        canSendCheck = false;
    }
}
