package net.atticus.combat_utilities.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.atticus.combat_utilities.config.ModConfigs;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.Map;

public class ModCommands {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                                CommandRegistryAccess registryAccess,
                                CommandManager.RegistrationEnvironment environment) {

        LiteralArgumentBuilder<ServerCommandSource> argumentBuilder = CommandManager.literal("combatUtil")
                .requires(commandSourceStack -> commandSourceStack.hasPermissionLevel(4))
                .then(CommandManager.literal("listall").executes(ModCommands::listAll));

        LiteralArgumentBuilder<ServerCommandSource> set = CommandManager.literal("set");
        LiteralArgumentBuilder<ServerCommandSource> reset = CommandManager.literal("reset");

        for (String key : ModConfigs.getBoolKeys()) {
            set.then(CommandManager.literal(key)
                    .then(CommandManager.argument("value", BoolArgumentType.bool())
                            .executes(context -> setBool(context, key))));
            reset.then(CommandManager.literal(key).executes(context -> resetBool(context, key)));
        }
        for (String key : ModConfigs.getFloatKeys()) {
            set.then(CommandManager.literal(key)
                    .then(CommandManager.argument("value", FloatArgumentType.floatArg())
                            .executes(context -> setFloat(context, key))));
            reset.then(CommandManager.literal(key).executes(context -> resetFloat(context, key)));
        }

        argumentBuilder.then(set).then(reset);
        dispatcher.register(argumentBuilder);
    }

    private static int resetFloat(CommandContext<ServerCommandSource> context, String key) {
        ModConfigs.put(key, ModConfigs.defaultFloats.get(key));
        context.getSource().sendMessage(Text.of("§e" + key + "§r reset to " + colored(ModConfigs.defaultFloats.get(key))));
        ModConfigs.writeToConfig();
        return 0;
    }

    private static int resetBool(CommandContext<ServerCommandSource> context, String key) {
        ModConfigs.put(key, ModConfigs.defaultBools.get(key));
        context.getSource().sendMessage(Text.of("§e" + key + "§r reset to " + colored(ModConfigs.defaultBools.get(key))));
        ModConfigs.writeToConfig();
        return 0;
    }

    private static int setBool(CommandContext<ServerCommandSource> context, String key) {
        boolean value = context.getArgument("value", Boolean.class);
        ModConfigs.put(key, value);
        context.getSource().sendMessage(Text.of("§e" + key + "§r set to " + colored(value)));
        ModConfigs.writeToConfig();
        return 0;
    }

    private static int setFloat(CommandContext<ServerCommandSource> context, String key) {
        float value = context.getArgument("value", Float.class);
        ModConfigs.put(key, value);
        context.getSource().sendMessage(Text.of("§e" + key + "§r set to " + colored(value)));
        ModConfigs.writeToConfig();
        return 0;
    }

    private static int listAll(CommandContext<ServerCommandSource> context) {
        StringBuilder sb = new StringBuilder();
        appendMap(sb, ModConfigs.bools);
        appendMap(sb, ModConfigs.floats);
        // Delete final '\n' character
        sb.deleteCharAt(sb.length() - 1);
        context.getSource().sendMessage(Text.of(sb.toString()));
        return 0;
    }

    private static <K, V> void appendMap(StringBuilder sb, Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            sb.append("§e").append(entry.getKey()).append(" ").append(colored(entry.getValue())).append("\n");
        }
    }

    private static <T> String colored(T value) {
        if (value instanceof Boolean) {
            if ((Boolean) value) {
                return "§atrue";
            }
            return "§cfalse";
        }
        return "§b" + value.toString();
    }
}