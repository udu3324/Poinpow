package com.udu3324.poinpow.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.udu3324.poinpow.Poinpow;
import com.udu3324.poinpow.api.Minehut;
import com.udu3324.poinpow.commands.Commands;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class ServerLookup {
    public static final String name = "lookupServer";
    public static final String description = "Lookup a server on minehut to see detailed information about it. Contributed by BuggyAI on Github.";

    public static void registerCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal(name).executes(context -> usage(context.getSource())).then(
                ClientCommandManager.argument("serverName", StringArgumentType.string()).executes(ctx -> lookup(StringArgumentType.getString(ctx, "serverName")))
        ));
    }

    private static int usage(FabricClientCommandSource source) {
        source.sendFeedback(Text.literal("Usage: " + name + " <serverName>").styled(style -> style
                .withColor(Formatting.RED)));

        return Command.SINGLE_SUCCESS;
    }

    //do in new thread because api req takes some time
    private static int lookup(String serverName) {
        new Thread(() -> {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if (player == null) {
                Poinpow.log.error("Error while looking up server: {} - Player is null", serverName);
                return;
            }

            // send api request to https://api.minehut.com/server/{serverName}?byName=true
            JsonObject response = Minehut.getServer(player, serverName);

            if (response == null) return;

            String status = Formatting.RED + "Offline";
            if (response.get("online").getAsBoolean()) {
                status = Formatting.GREEN + "Online";
            }

            Commands.running = true;

            player.sendMessage(Text.literal(""), false);
            player.sendMessage(Text.literal("this aint a overlay"), false);
            player.sendMessage(Text.literal("this is a overlay woo"), true);
            player.sendMessage(Text.literal(serverName + " is ").styled(style -> style
                            .withColor(Formatting.GOLD))
                    .append(status)
            , false);

            // print out each entry in the response and its value
            Set<Map.Entry<String, JsonElement>> entries = response.entrySet();
            for (Map.Entry<String, JsonElement> entry : entries) {
                String key = entry.getKey();

                // remove annoying information
                if (key.equals("motd") || key.equals("purchased_icons") ||
                        key.contains("name") || key.equals("active_icon") ||
                        key.contains("default_banner") || key.equals("online") ||
                        key.contains("rawPlan") || key.contains("server_plan")
                ) {
                    continue;
                }

                JsonElement value = entry.getValue();
                if (key.equals("creation") || key.equals("last_online")) { // format dates
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy 'at' h:mm a");
                    player.sendMessage(generateStat(key, dateFormat.format(value.getAsLong())), false);
                    continue;
                } else if (key.equals("activeServerPlan")) { // format server plan
                    String rawPlan = response.get("rawPlan").getAsString();
                    if (!rawPlan.isEmpty()) {
                        player.sendMessage(generateStat(key, value.getAsString() + " (" + rawPlan + ")"), false);
                        continue;
                    }
                }

                if (value.isJsonPrimitive()) {
                    player.sendMessage(generateStat(key, value.getAsString()), false);
                } else if (value.isJsonArray()) {
                    JsonArray array = value.getAsJsonArray();
                    if (array.isEmpty()) {
                        player.sendMessage(generateStat(key, "None!"), false);
                    } else {
                        AtomicReference<String> arrayString = new AtomicReference<>("");
                        array.forEach(element -> arrayString.set(arrayString + element.getAsString() + ", "));
                        player.sendMessage(generateStat(key, arrayString.get()), false);
                    }
                }
            }

            player.sendMessage(Text.literal(""), false);

            Commands.running = false;
        }).start();

        return Command.SINGLE_SUCCESS;
    }

    private static MutableText generateStat(String display, Object value) {
        return Text.literal(display + ": ").styled(style -> style
                .withColor(Formatting.GRAY)
        ).append(Text.literal(value.toString()).styled(style -> style
                .withColor(Formatting.WHITE)
        ));
    }
}
