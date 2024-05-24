package net.atticus.combat_utilities.mixin;

import net.atticus.combat_utilities.config.ModConfigs;
import net.minecraft.item.BowItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(BowItem.class)
public abstract class BowItemMixin {

    @ModifyConstant(method = "getPullProgress", constant = @Constant(floatValue = 20.0f))
    private static float modifyBowDrawSpeed(float value) {
        if (ModConfigs.getFloat(ModConfigs.BOW_DRAW_TIME_MODIFIER) <= 0.0001f) {
            return Float.MIN_VALUE;
        }
        return value * ModConfigs.getFloat(ModConfigs.BOW_DRAW_TIME_MODIFIER);
    }
}
