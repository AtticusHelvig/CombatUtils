package net.atticus.combat_utilities.mixin;

import net.atticus.combat_utilities.config.ModConfigs;
import net.atticus.combat_utilities.enchantment.ModEnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(method = "attack(Lnet/minecraft/entity/Entity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getAttackDamage(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/EntityType;)F"))
    private float modifySharpness(ItemStack stack, @Nullable EntityType<?> entityType) {
        MutableFloat mutableFloat = new MutableFloat();
        ModEnchantmentHelper.forEachEnchantment((enchantment, level) -> {
            mutableFloat.add(ModEnchantmentHelper.getAttackDamage(enchantment, level, entityType));
        }, stack);
        return mutableFloat.floatValue();
    }

    @ModifyConstant(method = "getAttackCooldownProgressPerTick", constant = @Constant(doubleValue = 20.0))
    private double modifyAttackCooldown(double value) {
        return value * ModConfigs.getFloat(ModConfigs.ATTACK_COOLDOWN_MODIFIER);
    }

    @ModifyConstant(method = "attack", constant = @Constant(floatValue = 1.5f))
    private float modifyCritMultiplier(float value) {
        return ModConfigs.getFloat(ModConfigs.CRIT_DAMAGE_MULTIPLIER);
    }

    @ModifyConstant(method = "disableShield", constant = @Constant(intValue = 100))
    private int modifyShieldDisableTime(int value) {
        return (int) (ModConfigs.getFloat(ModConfigs.SHIELD_DISABLE_TIME) * 20);
    }
}
