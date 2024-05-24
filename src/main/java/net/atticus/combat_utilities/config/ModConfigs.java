package net.atticus.combat_utilities.config;

import com.mojang.datafixers.util.Pair;

import net.atticus.combat_utilities.CombatUtilities;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ModConfigs {
    public static SimpleConfig CONFIG;
    private static ModConfigProvider configs;

    public static final Map<String, Boolean> defaultBools = new HashMap<String, Boolean>();
    public static final Map<String, Float> defaultFloats = new HashMap<String, Float>();

    public static final Map<String, Boolean> bools = new HashMap<String, Boolean>();
    public static final Map<String, Float> floats = new HashMap<String, Float>();

    public static final String MODIFY_NON_PLAYER_ARMOR = "modifyNonPlayerArmor";
    public static final String ARMOR_TOUGHNESS_MODIFIER = "armorToughnessModifier";
    public static final String ARMOR_MODIFIER = "armorModifier";
    public static final String MAX_PROTECTION_PERCENT = "maxProtectionPercent";
    public static final String PROTECTION_PER_LEVEL = "protectionPerLevel";
    public static final String FIRE_PROTECTION_PER_LEVEL = "fireProtectionPerLevel";
    public static final String BLAST_PROTECTION_PER_LEVEL = "blastProtectionPerLevel";
    public static final String PROJECTILE_PROTECTION_PER_LEVEL = "projectileProtectionPerLevel";
    public static final String FEATHER_FALLING_PER_LEVEL = "featherFallingPerLevel";

    public static final String MODIFY_NON_PLAYER_ATTACK = "modifyNonPlayerAttack";
    public static final String BOW_CRITS = "bowCrits";
    public static final String CROSSBOW_CRITS = "crossbowCrits";
    public static final String ATTACK_DAMAGE_MODIFIER = "attackDamageModifier";
    public static final String KNOCKBACK_MODIFIER = "knockbackModifier";
    public static final String ATTACK_COOLDOWN_MODIFIER = "attackCooldownModifier";
    public static final String CRIT_DAMAGE_MULTIPLIER = "critDamageMultiplier";
    public static final String SHARPNESS_DAMAGE_PER_LEVEL = "sharpnessDamagePerLevel";
    public static final String SMITE_DAMAGE_PER_LEVEL = "smiteDamagePerLevel";
    public static final String BANE_OF_ARTHROPODS_DAMAGE_PER_LEVEL = "baneOfArthropodsDamagePerLevel";
    public static final String BOW_DAMAGE_MODIFIER = "bowDamageModifier";
    public static final String BOW_DRAW_TIME_MODIFIER = "bowDrawTimeModifier";
    public static final String POWER_DAMAGE_PER_LEVEL = "powerDamagePerLevel";
    public static final String CROSSBOW_DAMAGE_MODIFIER = "crossbowDamageModifier";
    public static final String CROSSBOW_DRAW_SPEED_MODIFIER = "crossbowDrawSpeedModifier";

    public static final String ENABLE_TOTEMS = "enableTotems";
    public static final String PLAYER_KILL_TOTEMS = "playerKillTotems";
    public static final String INSTANT_SHIELDING = "instantShielding";
    public static final String PRECISE_ARROWS = "preciseArrows";
    public static final String SHIELD_DISABLE_TIME = "shieldDisableTime";
    public static final String BASE_PEARL_DAMAGE = "basePearlDamage";
    public static final String CRYSTAL_DAMAGE_MODIFIER = "crystalDamageModifier";

    public static void registerConfigs() {
        configs = new ModConfigProvider();
        createConfigs();

        CONFIG = SimpleConfig.of(CombatUtilities.MOD_ID).provider(configs).request();

        assignConfigs();
    }

    public static void writeToConfig() {
        configs.clear();
        for (String key : defaultBools.keySet()) {
            configs.addKeyValuePair(new Pair<>(key, bools.get(key)));
        }
        for (String key : defaultFloats.keySet()) {
            configs.addKeyValuePair(new Pair<>(key, floats.get(key)));
        }
        try {
            CONFIG.updateConfig();
        } catch (IOException e) {
            CombatUtilities.LOGGER.warn("Failed to update config");
        }
    }

    public static boolean getBool(String id) {
        return bools.get(id);
    }

    public static float getFloat(String id) {
        return floats.get(id);
    }

    public static Set<String> getBoolKeys() {
        return defaultBools.keySet();
    }

    public static Set<String> getFloatKeys() {
        return defaultFloats.keySet();
    }

    private static void createConfigs() {
        defaultBools.put(MODIFY_NON_PLAYER_ARMOR, false);
        defaultBools.put(MODIFY_NON_PLAYER_ATTACK, false);
        defaultBools.put(BOW_CRITS, true);
        defaultBools.put(CROSSBOW_CRITS, true);
        defaultBools.put(ENABLE_TOTEMS, true);
        defaultBools.put(PLAYER_KILL_TOTEMS, true);
        defaultBools.put(INSTANT_SHIELDING, false);
        defaultBools.put(PRECISE_ARROWS, false);

        defaultFloats.put(ARMOR_TOUGHNESS_MODIFIER, 1.0f);
        defaultFloats.put(ARMOR_MODIFIER, 1.0f);
        defaultFloats.put(MAX_PROTECTION_PERCENT, 80.0f);
        defaultFloats.put(PROTECTION_PER_LEVEL, 4.0f);
        defaultFloats.put(FIRE_PROTECTION_PER_LEVEL, 8.0f);
        defaultFloats.put(BLAST_PROTECTION_PER_LEVEL, 8.0f);
        defaultFloats.put(PROJECTILE_PROTECTION_PER_LEVEL, 8.0f);
        defaultFloats.put(FEATHER_FALLING_PER_LEVEL, 12.0f);
        defaultFloats.put(ATTACK_DAMAGE_MODIFIER, 1.0f);
        defaultFloats.put(KNOCKBACK_MODIFIER, 1.0f);
        defaultFloats.put(ATTACK_COOLDOWN_MODIFIER, 1.0f);
        defaultFloats.put(CRIT_DAMAGE_MULTIPLIER, 1.5f);
        defaultFloats.put(SHARPNESS_DAMAGE_PER_LEVEL, 0.5f);
        defaultFloats.put(SMITE_DAMAGE_PER_LEVEL, 2.5f);
        defaultFloats.put(BANE_OF_ARTHROPODS_DAMAGE_PER_LEVEL, 2.5f);
        defaultFloats.put(BOW_DAMAGE_MODIFIER, 1.0f);
        defaultFloats.put(BOW_DRAW_TIME_MODIFIER, 1.0f);
        defaultFloats.put(POWER_DAMAGE_PER_LEVEL, 0.5f);
        defaultFloats.put(CROSSBOW_DAMAGE_MODIFIER, 1.0f);
        defaultFloats.put(CROSSBOW_DRAW_SPEED_MODIFIER, 1.0f);
        defaultFloats.put(SHIELD_DISABLE_TIME, 5.0f);
        defaultFloats.put(BASE_PEARL_DAMAGE, 5.0f);
        defaultFloats.put(CRYSTAL_DAMAGE_MODIFIER, 1.0f);

        for (Map.Entry<String, Boolean> entry : defaultBools.entrySet()) {
            configs.addKeyValuePair(new Pair<>(entry.getKey(), entry.getValue()));
        }
        for (Map.Entry<String, Float> entry : defaultFloats.entrySet()) {
            configs.addKeyValuePair(new Pair<>(entry.getKey(), entry.getValue()));
        }
    }

    private static void assignConfigs() {
        for (Map.Entry<String, Boolean> entry : defaultBools.entrySet()) {
            assign(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, Float> entry : defaultFloats.entrySet()) {
            assign(entry.getKey(), entry.getValue());
        }
    }

    private static void assign(String id, boolean def) {
        boolean value = CONFIG.getOrDefault(id, def);
        bools.put(id, value);
    }

    private static void assign(String id, double def) {
        float value = (float) CONFIG.getOrDefault(id, def);
        floats.put(id, value);
    }

    public static void put(String id, boolean value) {
        bools.put(id, value);
    }

    public static void put(String id, float value) {
        floats.put(id, value);
    }
}
