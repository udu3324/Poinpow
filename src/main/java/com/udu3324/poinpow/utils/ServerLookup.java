package com.udu3324.poinpow.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.udu3324.poinpow.Poinpow;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class ServerLookup {

    public static void registerCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("lookupserver").executes(context -> {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            player.sendMessage(Text.literal("Usage: /lookupserver <serverName>").styled(style -> style
                    .withColor(Formatting.RED)));
            return 1;
        }).then(
                ClientCommandManager.argument("serverName", StringArgumentType.string()).executes(context -> {
                    lookupServer(StringArgumentType.getString(context, "serverName"));
                    return 1;
                })
        ));
    }

    public static void lookupServer(String serverName) {

        String errorInLookup = "Error while looking up server: " + serverName + " - ";

        // send api request to https://api.minehut.com/server/{serverName}?byName=true
        JsonObject response = null;
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) {
            Poinpow.log.error(errorInLookup + "Player is null");
            return;
        }

        try {
            URL apiURL = new URL("https://api.minehut.com/server/" + serverName + "?byName=true");
            HttpURLConnection connection = (HttpURLConnection) apiURL.openConnection();
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                player.sendMessage(Text.literal("Server not found! (" + connection.getResponseCode() + ")").styled(style -> style
                        .withColor(Formatting.RED)));
                return;
            }
            response = JsonParser.parseReader(new InputStreamReader(connection.getInputStream())).getAsJsonObject().get("server").getAsJsonObject();
            connection.disconnect();
        } catch (Exception e) {
            Poinpow.log.error(errorInLookup + e);
        }

        if (response == null) {
            player.sendMessage(Text.literal(errorInLookup + "Response is null"));
            return;
        }

        String status = Formatting.RED + "Offline";
        if (response.get("online").getAsBoolean()) {
            status = Formatting.GREEN + "Online";
        }

        player.sendMessage(Text.literal(""));
        player.sendMessage(Text.literal(serverName + " is ").styled(style -> style
                        .withColor(Formatting.GOLD))
                .append(status)
        );

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
                player.sendMessage(generateStat(key, dateFormat.format(value.getAsLong())));
                continue;
            } else if (key.equals("activeServerPlan")) { // format server plan
                String rawPlan = response.get("rawPlan").getAsString();
                if (!rawPlan.isEmpty()) {
                    player.sendMessage(generateStat(key, value.getAsString() + " (" + rawPlan + ")"));
                    continue;
                }
            }

            if (value.isJsonPrimitive()) {
                player.sendMessage(generateStat(key, value.getAsString()));
            } else if (value.isJsonArray()) {
                JsonArray array = value.getAsJsonArray();
                if (array.isEmpty()) {
                    player.sendMessage(generateStat(key, "None!"));
                } else {
                    AtomicReference<String> arrayString = new AtomicReference<>("");
                    array.forEach(element -> arrayString.set(arrayString + element.getAsString() + ", "));
                    player.sendMessage(generateStat(key, arrayString.get()));
                }
            }
        }

        player.sendMessage(Text.literal(""));

    }

    private static MutableText generateStat(String display, Object value) {
        return Text.literal(display + ": ").styled(style -> style
                .withColor(Formatting.GRAY)
        ).append(Text.literal(value.toString()).styled(style -> style
                .withColor(Formatting.WHITE)
        ));
    }

}
