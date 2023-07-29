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
import java.util.List;
import java.util.regex.Pattern;

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

        //create a new config if data missing (bad)
        if (data == null) {
            Poinpow.log.info("bad!!! missing " + value);
            delete();
            create();
            return "true";
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
                        break;
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

    //
    public static ArrayList<Pattern> getListOfRegex() {
        try {
            // get the lines in the config (arraylist)
            BufferedReader bufferedReader = new BufferedReader(new FileReader(configFile));

            ArrayList<String> lines = new ArrayList<>();

            String l;
            while ((l = bufferedReader.readLine()) != null) {
                lines.add(l);
            }
            bufferedReader.close();

            ArrayList<Pattern> linesOfRegex = new ArrayList<>();

            //after the # regex comment, put everything else in the array
            int commentLocation = lines.indexOf("regex") + 1;
            for (int i = commentLocation; i < lines.size(); i++) {
                linesOfRegex.add(Pattern.compile(lines.get(i)));
            }

            if (linesOfRegex.size() == 0) return null;

            return linesOfRegex;
        } catch (Exception e) {
            Poinpow.log.info("Problem reading file. " + e);
            return null;
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
                w.write(AutoSkipBarrier.name + ": true" + System.lineSeparator());
                w.write(BlockLobbyWelcome.name + ": true" + System.lineSeparator());
                w.write(BlockLobbyAds.name + ": true" + System.lineSeparator());
                w.write(BlockMinehutAds.name + ": true" + System.lineSeparator());
                w.write(BlockFreeCredits.name + ": true" + System.lineSeparator());
                w.write(BlockLobbyMapAds.name + ": true" + System.lineSeparator());
                w.write(System.lineSeparator());
                w.write("# Each line below is regex for ChatPhraseFilter to use.");
                w.write("/join");
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
                    AutoSkipBarrier.toggled.set(Boolean.parseBoolean(getValueFromConfig(AutoSkipBarrier.name)));
                    BlockLobbyWelcome.toggled.set(Boolean.parseBoolean(getValueFromConfig(BlockLobbyWelcome.name)));
                    BlockLobbyAds.toggled.set(Boolean.parseBoolean(getValueFromConfig(BlockLobbyAds.name)));
                    BlockMinehutAds.toggled.set(Boolean.parseBoolean(getValueFromConfig(BlockMinehutAds.name)));
                    BlockFreeCredits.toggled.set(Boolean.parseBoolean(getValueFromConfig(BlockFreeCredits.name)));
                    BlockLobbyMapAds.toggled.set(Boolean.parseBoolean(getValueFromConfig(BlockLobbyMapAds.name)));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}