package com.udu3324.poinpow.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.udu3324.poinpow.Poinpow;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Minehut {
    public static Map<String, String> playerRank = new HashMap<>();

    public static Boolean inLobby() {
        if (MinecraftClient.getInstance().player == null) return null;

        // check for scoreboard
        Scoreboard scoreboard = MinecraftClient.getInstance().player.getScoreboard();
        ArrayList<String> scores = new ArrayList<>();
        for (ScoreboardObjective objective : scoreboard.getObjectives()) {
            scores.add(objective.getDisplayName().toString());
        }

        return scores.toString().toLowerCase().contains("minehut");
    }

    //(BuggyAl) this gets information about a server in json
    public static JsonObject getServer(ClientPlayerEntity player, String serverName) {
        try {
            URL apiURL = new URL("https://api.minehut.com/server/" + serverName + "?byName=true");
            HttpURLConnection connection = (HttpURLConnection) apiURL.openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == 500) {
                player.sendMessage(Text.literal("Server not found! (" + connection.getResponseCode() + ")").styled(style -> style
                        .withColor(Formatting.RED)), false);
                return null;
            } else if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) { // bruh
                player.sendMessage(Text.literal("Server not found! API may be down. (" + connection.getResponseCode() + ")").styled(style -> style
                        .withColor(Formatting.RED)), false);
                return null;
            }

            JsonObject obj = JsonParser.parseReader(new InputStreamReader(connection.getInputStream())).getAsJsonObject().get("server").getAsJsonObject();

            connection.disconnect();

            return obj;
        } catch (Exception e) {
            Poinpow.log.error("Error while looking up server: {} - {}", serverName, e);
            player.sendMessage(Text.literal("Response to minehut api was unsuccessful. Server name: " + serverName), false);
            return null;
        }
    }

    //this gets a player's mh rank through their uuid, has to have dashes in
    public static String getRank(String uuid) {
        try {
            //check if rank of player already has been cached
            if (playerRank.containsKey(uuid)) {
                //Poinpow.log.info("uuid {} cached with rank {}", uuid, playerRank.get(uuid));
                return playerRank.get(uuid);
            }

            URL apiURL = new URL("https://api.minehut.com/cosmetics/profile/" + uuid);
            HttpURLConnection connection = (HttpURLConnection) apiURL.openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Poinpow.log.error("Player ({}) not found! ({})", uuid, connection.getResponseCode());
                return null;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String json;
            json = in.readLine();

            in.close();

            connection.disconnect();

            //parse rank
            int start = json.indexOf("rank\":\"") + 7;
            int end = json.indexOf("\",\"", start);

            String rank = json.substring(start, end);
            playerRank.put(uuid, rank);

            return rank;
        } catch (Exception e) {
            Poinpow.log.error("Response to minehut api was unsuccessful. Player uuid: {} {}", uuid, e);
            return null;
        }
    }
}
