package com.udu3324.poinpow;

import com.mojang.brigadier.CommandDispatcher;
import com.udu3324.poinpow.commands.Commands;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Poinpow implements ModInitializer {
	public static final Logger log = LoggerFactory.getLogger("poinpow");
	public static Boolean onMinehut = false;
	@Override
	public void onInitialize() {
		log.info("udu3324 was here!!! | poinpow v" + Config.version);

		// register the commands
		ClientCommandRegistrationCallback.EVENT.register(Poinpow::registerCommands);

		// create a config
		Config.create();
	}

	public static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
		Commands.register(dispatcher);
	}
}
