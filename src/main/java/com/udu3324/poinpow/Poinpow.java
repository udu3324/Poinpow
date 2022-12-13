package com.udu3324.poinpow;

import com.mojang.brigadier.CommandDispatcher;
import com.udu3324.poinpow.commands.PoinpowHelp;
import com.udu3324.poinpow.utils.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Poinpow implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("poinpow");
	public static Boolean onMinehut = false;
	@Override
	public void onInitialize() {
		LOGGER.info("udu3324 was here!!! | poinpow v" + Config.version);

		// register the commands
		ClientCommandRegistrationCallback.EVENT.register(Poinpow::registerCommands);

		// create a config
		Config.create();
	}

	public static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
		PoinpowHelp.register(dispatcher);

		RemoveLobbyRanks.register(dispatcher);

		AutoSkipBarrier.register(dispatcher);

		BlockLobbyWelcome.register(dispatcher);
		BlockLobbyAds.register(dispatcher);
		BlockMinehutAds.register(dispatcher);
		BlockFreeCredits.register(dispatcher);
	}
}
