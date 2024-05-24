package net.atticus.combat_utilities.enchantment;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.atticus.combat_utilities.config.ModConfigs;
import net.atticus.combat_utilities.util.Consumer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.registry.tag.TagKey;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ModEnchantmentHelper {

    public static void forEachEnchantment(Consumer<Enchantment, Integer> consumer, ItemStack stack) {
        ItemEnchantmentsComponent itemEnchantmentsComponent = stack
                .getOrDefault(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT);

        for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : itemEnchantmentsComponent.getEnchantmentsMap()) {
            consumer.accept(entry.getKey().value(), entry.getIntValue());
        }
    }

    public static void forEachEnchantment(Consumer<Enchantment, Integer> consumer, Iterable<ItemStack> stacks) {

        for (ItemStack itemStack : stacks) {
            forEachEnchantment(consumer, itemStack);
        }

    }

    public static float getProtectionAmount(Enchantment enchantment, int level, DamageSource source) {
        if (!(enchantment instanceof ProtectionEnchantment prot)) {
            return 0;
        }

        if (source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return 0;
        }

        return switch (prot.protectionType) {
            case ProtectionEnchantment.Type.ALL -> level * ModConfigs.getFloat(ModConfigs.PROTECTION_PER_LEVEL);
            case ProtectionEnchantment.Type.FIRE -> level * ModConfigs.getFloat(ModConfigs.FIRE_PROTECTION_PER_LEVEL);
            case ProtectionEnchantment.Type.FALL -> level * ModConfigs.getFloat(ModConfigs.FEATHER_FALLING_PER_LEVEL);
            case ProtectionEnchantment.Type.EXPLOSION -> level * ModConfigs.getFloat(ModConfigs.BLAST_PROTECTION_PER_LEVEL);
            case ProtectionEnchantment.Type.PROJECTILE -> level * ModConfigs.getFloat(ModConfigs.PROJECTILE_PROTECTION_PER_LEVEL);
        };
    }

    public static float getAttackDamage(Enchantment enchantment, int level, @Nullable EntityType<?> entityType) {
        Optional<TagKey<EntityType<?>>> applicableEntities = Optional.empty();
        if (enchantment.equals(Enchantments.SMITE)) {
            applicableEntities = Optional.of(EntityTypeTags.SENSITIVE_TO_SMITE);
        } else if (enchantment.equals(Enchantments.BANE_OF_ARTHROPODS)) {
            applicableEntities = Optional.of(EntityTypeTags.SENSITIVE_TO_BANE_OF_ARTHROPODS);
        }

        if (applicableEntities.isEmpty()) {
            return ModConfigs.getFloat(ModConfigs.SHARPNESS_DAMAGE_PER_LEVEL) * (level + 1);
        }
        if (entityType != null && entityType.isIn(applicableEntities.get())) {
            if (enchantment.equals(Enchantments.SMITE)) {
                return level * ModConfigs.getFloat(ModConfigs.SMITE_DAMAGE_PER_LEVEL);
            }
            if (enchantment.equals(Enchantments.BANE_OF_ARTHROPODS)) {
                return level * ModConfigs.getFloat(ModConfigs.BANE_OF_ARTHROPODS_DAMAGE_PER_LEVEL);
            }
        }
        return 0.0f;
    }
}
