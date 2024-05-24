package net.atticus.combat_utilities.mixin;

import net.atticus.combat_utilities.config.ModConfigs;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EnderPearlEntity.class)
public abstract class EnderPearlEntityMixin {

    @ModifyArg(method = "onCollision", index = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private float modifyPearlDamage(float amount) {
        return ModConfigs.getFloat(ModConfigs.BASE_PEARL_DAMAGE);
    }
}
