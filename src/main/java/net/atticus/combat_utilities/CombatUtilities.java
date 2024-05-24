package net.atticus.combat_utilities;

import net.atticus.combat_utilities.command.ModCommands;
import net.atticus.combat_utilities.config.ModConfigs;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CombatUtilities implements ModInitializer {
	public static final String MOD_ID = "combat_utilities";
	public static final Logger LOGGER = LoggerFactory.getLogger("Combat Utilities");

	@Override
	public void onInitialize() {
		ModConfigs.registerConfigs();
		CommandRegistrationCallback.EVENT.register(ModCommands::register);
	}
}