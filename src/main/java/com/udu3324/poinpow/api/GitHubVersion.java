package com.udu3324.poinpow.api;

import com.udu3324.poinpow.Config;
import com.udu3324.poinpow.Poinpow;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

public class GitHubVersion {
    //this checks the current and latest version of poinpow to then send an alert to update
    private static boolean canSend = true;

    //this gets the latest version of poinpow released
    private static String get() {
        try {
            URL obj = new URI("https://api.github.com/repos/udu3324/poinpow/tags").toURL();

            //request for player's friends using uuid url
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            Poinpow.log.info("Request Type: {} | Response Code: {} | URL Requested {}", con.getRequestMethod(), responseCode, obj);

            //return only if response is not 200 (ok)
            if (responseCode != 200) return null;

            //turn response into a string
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;

            ArrayList<String> response = new ArrayList<>();
            while ((inputLine = in.readLine()) != null) {
                response.add(inputLine);
            }
            in.close();

            String r = response.toString();
            if (r.contains("\"name\":\"")) {
                int start = r.indexOf("\"name\":\"") + 8;
                int end = r.indexOf("\",\"", start);
                return r.substring(start, end).replace("v", "");
            } else {
                return null;
            }
        } catch (Exception e) {
            Poinpow.log.info("Problem while contacting github api!!! {}", e.getLocalizedMessage());
        }

        return null;
    }

    public static void check() {
        String latestVersion = get();

        if (latestVersion == null) return;

        Version latest = new Version(get());
        Version current = new Version(Config.version);

        if (latest.compareTo(current) > 0 && canSend) {
            canSend = false;
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal("Poinpow v" + latestVersion + " is now available. (click)").styled(style -> style
                    .withClickEvent(new ClickEvent.OpenUrl(URI.create("https://modrinth.com/mod/poinpow/versions")))
                    .withColor(Formatting.DARK_GRAY)
                    .withUnderline(true)));
        }
    }
}
