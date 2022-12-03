package com.udu3324.poinpow;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static net.minecraft.server.command.CommandManager.*;

public class Poinpow implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("poinpow");

	@Override
	public void onInitialize() {
		LOGGER.info("udu3324 was here!!!");

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("foo")
				.executes(context -> {
					// For versions below 1.19, replace "Text.literal" with "new LiteralText".
					context.getSource().sendMessage(Text.literal("Called /foo with no arguments"));

					return 1;
				})));
	}
}
