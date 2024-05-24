package net.atticus.combat_utilities.mixin;

import net.atticus.combat_utilities.CombatUtilities;
import net.atticus.combat_utilities.config.ModConfigs;
import net.atticus.combat_utilities.enchantment.ModEnchantmentHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin {

    @Redirect(method = "tryAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getAttackDamage(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/EntityType;)F"))
    private float modifySharpness(ItemStack stack, @Nullable EntityType<?> entityType) {
        if (ModConfigs.getBool(ModConfigs.MODIFY_NON_PLAYER_ARMOR)) {
            MutableFloat mutableFloat = new MutableFloat();
            ModEnchantmentHelper.forEachEnchantment((enchantment, level) -> {
                mutableFloat.add(ModEnchantmentHelper.getAttackDamage(enchantment, level , entityType));
            }, stack);
            return mutableFloat.floatValue();
        }
        return EnchantmentHelper.getAttackDamage(stack, entityType);
    }

    @Redirect(method = "tryAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/MobEntity;getAttributeValue(Lnet/minecraft/registry/entry/RegistryEntry;)D"))
    private double modifyAttackAttributes(MobEntity entity, RegistryEntry<EntityAttribute> registryEntry) {
        if (ModConfigs.getBool(ModConfigs.MODIFY_NON_PLAYER_ATTACK)) {
            if (registryEntry.equals(EntityAttributes.GENERIC_ATTACK_DAMAGE)) {
                return entity.getAttributeValue(registryEntry) * ModConfigs.getFloat(ModConfigs.ATTACK_DAMAGE_MODIFIER);
            } else if (registryEntry.equals(EntityAttributes.GENERIC_ATTACK_KNOCKBACK)) {
                return entity.getAttributeValue(registryEntry) * ModConfigs.getFloat(ModConfigs.KNOCKBACK_MODIFIER);
            }
        }
        return entity.getAttributeValue(registryEntry);
    }
}
