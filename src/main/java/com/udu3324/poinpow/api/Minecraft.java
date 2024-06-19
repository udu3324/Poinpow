package com.udu3324.poinpow.api;

import com.udu3324.poinpow.Poinpow;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Minecraft {
    public static Map<String, String> uuidOfPlayers = new HashMap<>();

    //this gets a player's uuid from their ign
    public static String getUUID(String ign) {
        try {
            //check if player's uuid has already been cached
            if (uuidOfPlayers.containsKey(ign)) {
                //Poinpow.log.info("ign {} cached with uuid {}", uuid, playerRank.get(ign));
                return uuidOfPlayers.get(ign);
            }

            URL apiURL = new URL("https://api.mojang.com/users/profiles/minecraft/" + ign);
            HttpURLConnection connection = (HttpURLConnection) apiURL.openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Poinpow.log.error("Player ({}) not found! ({})", ign, connection.getResponseCode());
                return null;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            String json = response.toString().replaceAll(" ", "");

            connection.disconnect();

            //parse uuid
            int start = json.indexOf("id\":\"") + 5;
            int end = json.indexOf("\",\"", start);

            json = json.substring(start, end);

            uuidOfPlayers.put(ign, json);

            return json;
        } catch (Exception e) {
            Poinpow.log.error("Response to minecraft api was unsuccessful. Player ign: {} - {}", ign, e);
            return null;
        }
    }

    public static String insertUUIDDashes(String uuid) {
        StringBuilder sb = new StringBuilder(uuid);
        sb.insert(8, "-");
        sb.insert(13, "-");
        sb.insert(18, "-");
        sb.insert(23, "-");
        return sb.toString();
    }
}
