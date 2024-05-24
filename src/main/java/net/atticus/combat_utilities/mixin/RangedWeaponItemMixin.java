package net.atticus.combat_utilities.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.atticus.combat_utilities.config.ModConfigs;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RangedWeaponItem.class)
public abstract class RangedWeaponItemMixin {

    @Inject(method = "createArrowEntity", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/ArrowItem;createArrow(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/entity/projectile/PersistentProjectileEntity;", shift = At.Shift.AFTER))
    private void modifyArrowDamage(World world, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack, boolean critical, CallbackInfoReturnable<ProjectileEntity> cir, @Local PersistentProjectileEntity persistentProjectileEntity) {
        if (shooter instanceof PlayerEntity || ModConfigs.getBool(ModConfigs.MODIFY_NON_PLAYER_ATTACK)) {
            Item item = weaponStack.getItem();
            if (item.equals(Items.CROSSBOW)) {
                persistentProjectileEntity.setDamage(persistentProjectileEntity.getDamage() * ModConfigs.getFloat(ModConfigs.CROSSBOW_DAMAGE_MODIFIER));
            } else if (item.equals(Items.BOW)) {
                persistentProjectileEntity.setDamage(persistentProjectileEntity.getDamage() * ModConfigs.getFloat(ModConfigs.BOW_DAMAGE_MODIFIER));
            }
        }
    }

    @ModifyArg(method = "createArrowEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;setCritical(Z)V"))
    private boolean modifyRangedCrits(boolean critical, @Local(argsOnly = true) LivingEntity shooter, @Local(argsOnly = true, ordinal = 0) ItemStack weaponStack, @Local PersistentProjectileEntity persistentProjectileEntity) {
        if (shooter instanceof PlayerEntity || ModConfigs.getBool(ModConfigs.MODIFY_NON_PLAYER_ATTACK)) {
            if (weaponStack.getItem().equals(Items.BOW)) {
                return ModConfigs.getBool(ModConfigs.BOW_CRITS);
            }
            if (weaponStack.getItem().equals(Items.CROSSBOW)) {
                return ModConfigs.getBool(ModConfigs.CROSSBOW_CRITS);
            }
        }
        return critical;
    }

    @ModifyArg(method = "createArrowEntity", index = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;setDamage(D)V"))
    private double modifyPower(double damage, @Local(argsOnly = true) LivingEntity shooter, @Local int level, @Local PersistentProjectileEntity persistentProjectileEntity) {
        if (shooter instanceof PlayerEntity || ModConfigs.getBool(ModConfigs.MODIFY_NON_PLAYER_ATTACK)) {
            return persistentProjectileEntity.getDamage() + ((double) level + 1) * ModConfigs.getFloat(ModConfigs.POWER_DAMAGE_PER_LEVEL);
        }
        return damage;
    }
}
