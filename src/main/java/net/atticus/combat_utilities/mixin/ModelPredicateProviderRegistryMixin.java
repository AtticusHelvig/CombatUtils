package net.atticus.combat_utilities.mixin;

import net.atticus.combat_utilities.config.ModConfigs;
import net.minecraft.client.item.ClampedModelPredicateProvider;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ModelPredicateProviderRegistry.class)
public abstract class ModelPredicateProviderRegistryMixin {

    @Redirect(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/item/ModelPredicateProviderRegistry;register(Lnet/minecraft/item/Item;Lnet/minecraft/util/Identifier;Lnet/minecraft/client/item/ClampedModelPredicateProvider;)V"))
    private static void modifyBowDrawAnimationSpeed(Item item, Identifier id, ClampedModelPredicateProvider provider) {
        if (item.equals(Items.BOW)) {
            if (id.equals(new Identifier(("pull")))) {
                ModelPredicateProviderRegistry.register(item, id, (stack, world, entity, seed) -> {
                    if (entity == null) {
                        return 0.0f;
                    }
                    if (entity.getActiveItem() != stack) {
                        return 0.0f;
                    }
                    if (ModConfigs.getFloat(ModConfigs.BOW_DRAW_TIME_MODIFIER) <= 0.0001f) {
                        return Float.MAX_VALUE;
                    }
                    return (float) (stack.getMaxUseTime() - entity.getItemUseTimeLeft()) / (20.0f * ModConfigs.getFloat(ModConfigs.BOW_DRAW_TIME_MODIFIER));
                });
                return;
            }
        }
        ModelPredicateProviderRegistry.register(item, id, provider);
    }
}
