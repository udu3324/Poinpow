package com.udu3324.poinpow.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.udu3324.poinpow.Poinpow;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Minehut {
    //(BuggyAl) this gets information about a server in json
    public static JsonObject getServer(ClientPlayerEntity player, String serverName) {
        try {
            URL apiURL = new URL("https://api.minehut.com/server/" + serverName + "?byName=true");
            HttpURLConnection connection = (HttpURLConnection) apiURL.openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                player.sendMessage(Text.literal("Server not found! (" + connection.getResponseCode() + ")").styled(style -> style
                        .withColor(Formatting.RED)));
                return null;
            }

            JsonObject obj = JsonParser.parseReader(new InputStreamReader(connection.getInputStream())).getAsJsonObject().get("server").getAsJsonObject();

            connection.disconnect();

            return obj;
        } catch (Exception e) {
            Poinpow.log.error("Error while looking up server: {} - {}", serverName, e);
            player.sendMessage(Text.literal("Response to minehut api was unsuccessful. Server name: " + serverName));
            return null;
        }
    }
}
