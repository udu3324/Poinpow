
![Mod Banner](https://github.com/udu3324/Poinpow/blob/master/src/main/resources/assets/poinpow/banner.png?raw=true)

![License](https://img.shields.io/github/license/udu3324/poinpow)    
![Latest Releases](https://img.shields.io/github/v/release/udu3324/Poinpow)    
![Github Downloads](https://img.shields.io/github/downloads/udu3324/poinpow/total)    
![Modrinth Downloads](https://img.shields.io/badge/dynamic/json?color=1bd96a&label=modrinth&query=downloads&suffix=%20downloads&url=https%3A%2F%2Fapi.modrinth.com%2Fv2%2Fproject%2Fpoinpow)    
![Mod loader: Fabric](https://img.shields.io/badge/modloader-Fabric-decea6?style=round)    
![Requires](https://img.shields.io/badge/requires-Fabric%20API-dece5a?style=round)    
[![Discord Server](https://img.shields.io/badge/Official%20Discord%20Server-7289DA?style=round&logo=discord&logoColor=white)](https://discord.gg/NXm9tJvyBT)

# 📘 Features
Poinpow is a fabric mod that adds **amazing features** and **makes the player experience 103% better**. (QoL) Features that Minehut locks behind a paywall (ex: removing ads, muting chat, etc.) are in Poinpow.

| Features             | Command                                                                                                                           |
|:---------------------|:----------------------------------------------------------------------------------------------------------------------------------|
| AutoSkipBarrier      | Automatically skips the transition ads when joining minehut/free servers.                                                         |
| BlockLobbyAds        | Blocks player made ads in the lobby.                                                                                              |
| ToggleSpecificAds    | Extends the functionality of BlockLobbyAds to block certain ads based on ranks.                                                   |
| ChatPhraseFilter     | Filters and blocks messages in chat based on a regex list.                                                                        |
| ~~ServerLookup~~     | ~~A command to see details about a minehut server. Contributed by [BuggyAl](https://github.com/BuggyAl).~~ (use at your own risk) |
| MuteLobbyChat        | A command that mutes Minehut's lobby, only allowing `/msg`, joins, and some other stuff.                                          |
| HubCommandBack       | Adds back the `/hub` command. It redirects the command `/hub` to `/mh` when necessary.                                            |
| BlockChestAds        | Removes the ads inside the compass server listing in hub.                                                                         |
| BlockLobbyMapAds     | Removes the humungous map art that advertises things in lobby. It also mutes the sound played if present.                         |
| BlockLobbyWelcome    | Blocks the lobby join message that sometimes has an ad in it.                                                                     |
| BlockRaids           | Blocks the raid messages in hub.                                                                                                  |
| BlockFreeCredits     | Blocks the vote messages encouraging to vote for free credits.                                                                    |
| BlockMinehutAds      | Blocks ads from Minehut that are sent in free servers.                                                                            |


![screenshot of me using AutoSkipBarrier](https://cdn.modrinth.com/data/zmUzIoT1/images/aaa8cda2723de8979014cde22db46d34c8160553.png)       
Skipping ads **automatically** with AutoSkipBarrier **(extremely fast)**  
![screenshot of me using BlockLobbyAds and BlockLobbyMapAds](https://cdn.modrinth.com/data/zmUzIoT1/images/c49843c5f4e7412df0c53670e94f3434eb4c4238.png)      
Example of **BlockLobbyAds** (blocks ads made by player) and **BlockLobbyMapAds**. It clears up chat a lot and blocks those awful map ads.


![commands of poinpow](https://cdn.modrinth.com/data/zmUzIoT1/images/75745e7d81968d0ad369493ab3174f0d2a605517.png)      
/poinpow - An **intractable/clickable** help command that easily allows the toggling of features. (image may be outdated)

## 💾 How to Install  the Mod
1. Make sure you have **[Fabric](https://fabricmc.net/use/installer/)** installed in your mc launcher
2. Download the latest release of **Poinpow** [here](https://github.com/udu3324/poinpow/releases)
3. Download the latest release of **Fabric API** [here](https://modrinth.com/mod/fabric-api/versions)
4. Move the **two jars** you downloaded into the mods folder **(its usually .minecraft/mods)**
5. Launch Minecraft and **enjoy the mod!**
6. **(optional)** Star the repository to support me :> You can join the [discord server](https://discord.gg/NXm9tJvyBT) to send feedback about the mod too.

## 🛠️ How to Compile the Jar
If you want to **contribute to the code** or **make the jar yourself**, you can follow the steps below.

1. Make sure you have **JDK 17** or above installed.
2. Clone the repository and extract it to a folder
3. Open a terminal and cd to the folder
4. Run `gradlew build` or `.\gradlew build` depending on your system os
5. Done! The jar should be in `/build/libs`
6. (optional) Open up the folder in your IDE (intellij recommended) to look at the code and contribute to it if you want to.  Run `.\gradlew runClient` to run the mod directly in your IDE.

## 🧾 Is it Against the Rules?
**No.** I read the rules, and it doesn't break any. Unless..... "Minehut reserves the right to suspend or refuse service for users and servers at any time at their discretion." bruh

## ❤️ How to Contribute
You can contribute by starring the repo, reporting issues, and creating pull requests. You can also send feedback in the [discord server](https://discord.gg/NXm9tJvyBT) or send me a [ko-fi](https://ko-fi.com/udu3324).
