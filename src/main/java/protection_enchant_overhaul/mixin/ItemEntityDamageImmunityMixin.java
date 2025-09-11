package protection_enchant_overhaul.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.DamageTypeTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Prevent destroying enchanted armor items:
 * - Any item stack with vanilla PROTECTION cannot be destroyed by CACTUS.
 * - Any item stack with FIRE_PROTECTION cannot be destroyed by FIRE/LAVA/etc.
 * - Any item stack with BLAST_PROTECTION cannot be destroyed by explosion damage.
 *
 * Notes:
 * - This affects the item entity while dropped on the ground.
 * - We don't touch projectile here (you didn't ask for projectile immunity).
 */
@Mixin(ItemEntity.class)
public abstract class ItemEntityDamageImmunityMixin {
    @Shadow public abstract ItemStack getStack();

    @Inject(method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z",
            at = @At("HEAD"), cancellable = true)
    private void peo$armorItemImmunity(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = this.getStack();
        if (stack.isEmpty()) return;

        // Physical -> cactus immunity
        if (source.isOf(DamageTypes.CACTUS)
                && EnchantmentHelper.getLevel(Enchantments.PROTECTION, stack) > 0) {
            cir.setReturnValue(false); // ignore damage -> don't despawn
            return;
        }

        // Fire -> fire immunity (in_fire, on_fire, lava, etc. all tagged as IS_FIRE)
        if (source.isIn(DamageTypeTags.IS_FIRE)
                && EnchantmentHelper.getLevel(Enchantments.FIRE_PROTECTION, stack) > 0) {
            cir.setReturnValue(false);
            return;
        }

        // Blast -> explosion immunity
        if (source.isIn(DamageTypeTags.IS_EXPLOSION)
                && EnchantmentHelper.getLevel(Enchantments.BLAST_PROTECTION, stack) > 0) {
            cir.setReturnValue(false);
        }
    }
}
