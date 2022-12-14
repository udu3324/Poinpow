package com.udu3324.poinpow.api;

import com.udu3324.poinpow.Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GitHubVersion {
    private static String get() {
        try {
            URL obj = new URL("https://api.github.com/repos/udu3324/poinpow/tags");

            //request for player's friends using uuid url
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            System.out.println("Request Type: " + con.getRequestMethod() + " | Response Code: " + responseCode + " | URL Requested " + obj);

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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void check() {
        String latestVersion = get();

        if (latestVersion == null) return;

        if (!latestVersion.equals(Config.version)) {
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal("Poinpow v" + latestVersion + " is now available. (click)").styled(style -> style
                    .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/udu3324/poinpow/releases/latest"))
                    .withColor(Formatting.DARK_GRAY)
                    .withUnderline(true)));
        }
    }
}
