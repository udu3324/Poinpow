package com.udu3324.poinpow;

import com.udu3324.poinpow.utils.*;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Pattern;

import static com.udu3324.poinpow.utils.ToggleSpecificAds.*;

public class Config {
    private static final ModContainer mod = FabricLoader.getInstance()
            .getModContainer("poinpow")
            .orElseThrow(NullPointerException::new);
    public static final String version = mod.getMetadata().getVersion().getFriendlyString();
    public static File configFile = new File(FabricLoader.getInstance().getConfigDir().toString() + File.separator + "poinpow.cfg");

    //get the version number stored in the config
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
            ArrayList<String> lines = getConfig();

            if (lines == null) {
                Poinpow.log.info("Problem reading poinpow config!!! Error!!!");
                return;
            }

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

    //this gets the list of regex at the bottom of the config
    public static ArrayList<Pattern> getListOfRegex() {
        try {
            ArrayList<String> lines = getConfig();

            if (lines == null) {
                Poinpow.log.info("Problem reading poinpow config!!! Error!!!");
                return new ArrayList<>();
            }

            //config is out of date! reset
            if (!lines.contains("# Each line below is regex for ChatPhraseFilter to use.")) {
                Poinpow.log.info("bad!!! missing regex for chat phrase filter");
                delete();
                create();
                return new ArrayList<>();
            }

            ArrayList<Pattern> linesOfRegex = new ArrayList<>();

            //after the regex comment, put everything else in the array
            int commentLocation = lines.indexOf("# Each line below is regex for ChatPhraseFilter to use.") + 1;
            for (int i = commentLocation; i < lines.size(); i++) {
                if (!lines.get(i).isEmpty()) {
                    String edit = lines.get(i).replace("\n", "");
                    linesOfRegex.add(Pattern.compile(edit));
                }
            }

            if (linesOfRegex.isEmpty()) return new ArrayList<>();

            return linesOfRegex;
        } catch (Exception e) {
            Poinpow.log.info("Problem reading file. " + e);
            return new ArrayList<>();
        }
    }

    //this adds a line of regex at the bottom of the config
    public static void addRegex(String regex) {
        try {
            FileWriter writer = new FileWriter(configFile, true);

            writer.write("\n" + regex);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //this removes a line of regex at the bottom of the config
    public static void removeRegex(String regex) {
        try {
            ArrayList<String> lines = getConfig();

            if (lines == null) {
                Poinpow.log.info("Problem reading poinpow config!!! Error!!!");
                return;
            }

            // modify the values in the config
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).equals(regex)) {
                    lines.remove(i);
                    break;
                }
            }

            //overwrite the rest
            FileWriter writer = new FileWriter(configFile);

            //write array back to new file
            for (String line : lines) {
                writer.write(line + System.lineSeparator());
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //get the lines in the config
    private static ArrayList<String> getConfig() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(configFile));
            ArrayList<String> lines = new ArrayList<>();

            String l;

            while ((l = bufferedReader.readLine()) != null) {
                lines.add(l);
            }
            bufferedReader.close();

            return lines;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void delete() {
        if (configFile.delete())
            Poinpow.log.info("Config file has been successfully deleted.");
        else
            Poinpow.log.info("Error! Config file couldn't be deleted!");
    }

    //creates a config if possible, does other stuff if it cant
    public static void create() {
        try {
            //try making a new config
            if (configFile.createNewFile()) {
                writeDefaultConfig();
                Poinpow.log.info("New config created.");
                return;
            }

            Poinpow.log.info("Config already exists.");

            //delete config if bad version
            if (!isLatestModVersion()) {
                Poinpow.log.info("Config is outdated!"); // reset config
                delete();
                create();
                return;
            }

            //set values from config since its good
            AutoSkipBarrier.toggled.set(Boolean.parseBoolean(getValueFromConfig(AutoSkipBarrier.name)));
            ChatPhraseFilter.toggled.set(Boolean.parseBoolean(getValueFromConfig(ChatPhraseFilter.name)));
            BlockLobbyWelcome.toggled.set(Boolean.parseBoolean(getValueFromConfig(BlockLobbyWelcome.name)));
            BlockLobbyAds.toggled.set(Boolean.parseBoolean(getValueFromConfig(BlockLobbyAds.name)));
            BlockMinehutAds.toggled.set(Boolean.parseBoolean(getValueFromConfig(BlockMinehutAds.name)));
            BlockFreeCredits.toggled.set(Boolean.parseBoolean(getValueFromConfig(BlockFreeCredits.name)));
            BlockLobbyMapAds.toggled.set(Boolean.parseBoolean(getValueFromConfig(BlockLobbyMapAds.name)));
            HubCommandBack.toggled.set(Boolean.parseBoolean(getValueFromConfig(HubCommandBack.name)));
            BlockRaids.toggled.set(Boolean.parseBoolean(getValueFromConfig(HubCommandBack.name)));
            BlockChestAds.toggled.set(Boolean.parseBoolean(getValueFromConfig(BlockChestAds.name)));

            MuteLobbyChat.toggled = Boolean.parseBoolean(getValueFromConfig(MuteLobbyChat.name));

            defaultRank = Boolean.parseBoolean(getValueFromConfig(name + "_default"));
            vip = Boolean.parseBoolean(getValueFromConfig(name + "_vip"));
            vipPlus = Boolean.parseBoolean(getValueFromConfig(name + "_vipPlus"));
            pro = Boolean.parseBoolean(getValueFromConfig(name + "_pro"));
            legend = Boolean.parseBoolean(getValueFromConfig(name + "_legend"));
            patron = Boolean.parseBoolean(getValueFromConfig(name + "_patron"));

        } catch (IOException e) {
            Poinpow.log.error("Error! Poinpow couldn't create a config! ");
            e.printStackTrace();
        }
    }

    //this writes a new config with all the default values
    private static void writeDefaultConfig() throws IOException {
        FileWriter w = new FileWriter(configFile, true);
        w.write("# Poinpow v" + version + " by udu3324 | Discord: https://discord.gg/NXm9tJvyBT | Config" + System.lineSeparator());
        w.write("# Hey! I suggest you use the in-game commands instead of editing the config directly." + System.lineSeparator());
        w.write(System.lineSeparator());
        w.write(AutoSkipBarrier.name + ": true" + System.lineSeparator());
        w.write(ChatPhraseFilter.name + ": true" + System.lineSeparator());
        w.write(BlockLobbyWelcome.name + ": true" + System.lineSeparator());
        w.write(BlockLobbyAds.name + ": true" + System.lineSeparator());
        w.write(BlockMinehutAds.name + ": true" + System.lineSeparator());
        w.write(BlockFreeCredits.name + ": true" + System.lineSeparator());
        w.write(BlockLobbyMapAds.name + ": true" + System.lineSeparator());
        w.write(HubCommandBack.name + ": true" + System.lineSeparator());
        w.write(BlockRaids.name + ": true" + System.lineSeparator());
        w.write(BlockChestAds.name + ": true" + System.lineSeparator());
        w.write(MuteLobbyChat.name + ": false" + System.lineSeparator());

        w.write(name + "_default: false" + System.lineSeparator());
        w.write(name + "_vip: false" + System.lineSeparator());
        w.write(name + "_vipPlus: false" + System.lineSeparator());
        w.write(name + "_pro: false" + System.lineSeparator());
        w.write(name + "_legend: false" + System.lineSeparator());
        w.write(name + "_patron: false" + System.lineSeparator());
        
        w.write(System.lineSeparator());
        w.write("# Each line below is regex for ChatPhraseFilter to use." + System.lineSeparator());
        w.write("this_is_a_example");
        w.close();
    }
}