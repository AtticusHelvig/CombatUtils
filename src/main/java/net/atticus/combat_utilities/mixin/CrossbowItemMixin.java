package net.atticus.combat_utilities.mixin;

import net.atticus.combat_utilities.config.ModConfigs;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin {
    @Inject(method = "getPullTime", at = @At("RETURN"), cancellable = true)
    private static void modifyCrossbowDrawTime(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue((int) (cir.getReturnValueI() * ModConfigs.getFloat(ModConfigs.CROSSBOW_DRAW_SPEED_MODIFIER)));
    }
}
