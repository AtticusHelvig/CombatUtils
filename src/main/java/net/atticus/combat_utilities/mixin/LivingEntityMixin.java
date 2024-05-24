package net.atticus.combat_utilities.mixin;

import net.atticus.combat_utilities.enchantment.ModEnchantmentHelper;
import net.minecraft.entity.damage.DamageTypes;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import net.atticus.combat_utilities.config.ModConfigs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.enchantment.EnchantmentHelper;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow protected abstract boolean tryUseTotem(DamageSource source);

    private static int VANILLA_PROTECTION = 4;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Unique
    private boolean isArmorModified() {
        if (!((Object) this instanceof PlayerEntity)) {
            return ModConfigs.getBool(ModConfigs.MODIFY_NON_PLAYER_ARMOR);
        }
        return true;
    }

    @ModifyArgs(method = "applyArmorToDamage(Lnet/minecraft/entity/damage/DamageSource;F)F", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/DamageUtil;getDamageLeft(FLnet/minecraft/entity/damage/DamageSource;FF)F"))
    private void applyArmorToDamage(Args args) {
        if (isArmorModified()) {
            float armor = args.get(2);
            float armorToughness = args.get(3);
            args.set(2, armor * ModConfigs.getFloat(ModConfigs.ARMOR_MODIFIER));
            args.set(3, armorToughness * ModConfigs.getFloat(ModConfigs.ARMOR_TOUGHNESS_MODIFIER));
        }
    }

    @Redirect(method = "modifyAppliedDamage(Lnet/minecraft/entity/damage/DamageSource;F)F", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getProtectionAmount(Ljava/lang/Iterable;Lnet/minecraft/entity/damage/DamageSource;)I"))
    private int getProtectionAmount(Iterable<ItemStack> equipment, DamageSource source) {
        if (!isArmorModified()) {
            return EnchantmentHelper.getProtectionAmount(equipment, source) * VANILLA_PROTECTION;
        }
        MutableFloat mutableFloat = new MutableFloat();
        ModEnchantmentHelper.forEachEnchantment((enchantment, level) -> {
            mutableFloat.add(ModEnchantmentHelper.getProtectionAmount(enchantment, level, source));
        }, equipment);
        return mutableFloat.intValue();
    }

    @Redirect(method = "modifyAppliedDamage(Lnet/minecraft/entity/damage/DamageSource;F)F", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/DamageUtil;getInflictedDamage(FF)F"))
    private float getInflictedDamage(float damageDealt, float protection) {
        float maxProtectionPercent = isArmorModified() ? ModConfigs.getFloat(ModConfigs.MAX_PROTECTION_PERCENT) : 80.0f;
        float damageReduction = MathHelper.clamp(protection, 0.0f, maxProtectionPercent);
        return damageDealt * (1.0F - damageReduction / 100.0f);
    }

    @Redirect(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;tryUseTotem(Lnet/minecraft/entity/damage/DamageSource;)Z"))
    private boolean modifyTotem(LivingEntity instance, DamageSource source) {
        if (!ModConfigs.getBool(ModConfigs.ENABLE_TOTEMS)) {
            return false;
        }
        if (!ModConfigs.getBool(ModConfigs.PLAYER_KILL_TOTEMS) && (source.getTypeRegistryEntry().equals(DamageTypes.PLAYER_ATTACK) || source.getTypeRegistryEntry().equals(DamageTypes.PLAYER_EXPLOSION)) ) {
            return false;
        }
        return tryUseTotem(source);
    }

    @ModifyConstant(method = "isBlocking", constant = @Constant(intValue = 5))
    private int instantShielding(int value) {
        if (ModConfigs.getBool(ModConfigs.INSTANT_SHIELDING)) {
            return 0;
        }
        return value;
    }

}
