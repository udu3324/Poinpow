package com.udu3324.poinpow;

import com.udu3324.poinpow.utils.*;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Config {
    public static File configFile = new File(FabricLoader.getInstance().getConfigDir().toString() + File.separator + "poinpow.cfg");
    private static final ModContainer mod = FabricLoader.getInstance()
            .getModContainer("poinpow")
            .orElseThrow(NullPointerException::new);

    public static final String version = mod.getMetadata().getVersion().getFriendlyString();

    public static Boolean isLatestModVersion() throws IOException {
        BufferedReader brTest = new BufferedReader(new FileReader(configFile));
        String text = brTest.readLine();
        brTest.close();

        return text.contains(version);
    }

    //example: getValueFromConfig("api-key") returns "thisIsTheApiKey"
    public static String getValueFromConfig(String value) {
        String data = null;
        try {
            BufferedReader file = new BufferedReader(new FileReader(configFile));
            String line;

            while ((line = file.readLine()) != null) {
                if (line.contains(value)) {
                    //take the value line and parse it for its data
                    // value: data -> data
                    data = line.replace(value + ": ", "");
                    file.close();
                    return data;
                }
            }

            file.close();
        } catch (Exception e) {
            Poinpow.log.info("Problem reading file.");
        }

        return data;
    }

    //example: setValueFromConfig("api-key", "thisIsTheApiKey")
    public static void setValueFromConfig(String value, String data) {
        try {
            // get the lines in the config (arraylist)
            BufferedReader bufferedReader = new BufferedReader(new FileReader(configFile));

            ArrayList<String> lines = new ArrayList<>();

            String l;
            while ((l = bufferedReader.readLine()) != null) {
                lines.add(l);
            }
            bufferedReader.close();

            // modify the values in the config
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);

                // ignore comments
                if (!line.contains("#") && line.length() > 1 && line.contains(":")) {
                    // if line (value: data) -> substring to : (value) -> equals value
                    if (value.equals(line.substring(0, line.indexOf(":")))) {
                        // it's the line being modified
                        lines.set(i, value + ": " + data);
                    }
                }
            }

            //overwrite the rest
            FileWriter writer = new FileWriter(configFile);

            //write array back to new file
            for (String line : lines) {
                writer.write(line + System.lineSeparator());
            }

            writer.close();
        } catch (Exception e) {
            Poinpow.log.info("Problem writing file. " + e);
        }
    }

    public static void delete() {
        if (configFile.delete())
            Poinpow.log.info("Config file has been successfully deleted.");
        else
            Poinpow.log.info("Error! Config file couldn't be deleted!");
    }

    public static void create() {
        try {
            if (configFile.createNewFile()) {
                FileWriter w = new FileWriter(configFile, true);
                w.write("# Poinpow v" + version + " by udu3324 | Config" + System.lineSeparator());
                w.write("# Hey! I suggest you use the in-game commands instead of editing the config directly." + System.lineSeparator());
                w.write(System.lineSeparator());
                w.write(RemoveLobbyRanks.name + ": true" + System.lineSeparator());
                w.write(AutoSkipBarrier.name + ": true" + System.lineSeparator());
                w.write(BlockLobbyWelcome.name + ": true" + System.lineSeparator());
                w.write(BlockLobbyAds.name + ": true" + System.lineSeparator());
                w.write(BlockMinehutAds.name + ": true" + System.lineSeparator());
                w.write(BlockFreeCredits.name + ": true" + System.lineSeparator());
                w.close();

                Poinpow.log.info("New config created.");
            } else {
                //don't do anything if the config has alr been made
                Poinpow.log.info("Config already exists.");

                //delete config if bad version
                if (!isLatestModVersion()) {
                    Poinpow.log.info("Config is outdated!");

                    //delete and create the new one with the right version
                    delete();
                    create();
                } else {
                    //set values from config since its good
                    RemoveLobbyRanks.toggled = Boolean.valueOf(getValueFromConfig(RemoveLobbyRanks.name));

                    AutoSkipBarrier.toggled = Boolean.valueOf(getValueFromConfig(AutoSkipBarrier.name));

                    BlockLobbyWelcome.toggled = Boolean.valueOf(getValueFromConfig(BlockLobbyWelcome.name));
                    BlockLobbyAds.toggled = Boolean.valueOf(getValueFromConfig(BlockLobbyAds.name));
                    BlockMinehutAds.toggled = Boolean.valueOf(getValueFromConfig(BlockMinehutAds.name));
                    BlockFreeCredits.toggled = Boolean.valueOf(getValueFromConfig(BlockFreeCredits.name));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}