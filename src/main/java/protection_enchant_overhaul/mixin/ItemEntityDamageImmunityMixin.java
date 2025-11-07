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


@Mixin(ItemEntity.class)
public abstract class ItemEntityDamageImmunityMixin {
    @Shadow public abstract ItemStack getStack();

    @Inject(method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z",
            at = @At("HEAD"), cancellable = true)
    private void peo$armorItemImmunity(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = this.getStack();
        if (stack.isEmpty()) return;


        if (source.isOf(DamageTypes.CACTUS)
                && EnchantmentHelper.getLevel(Enchantments.PROTECTION, stack) > 0) {
            cir.setReturnValue(false); // ignore damage -> don't despawn
            return;
        }


        if (source.isIn(DamageTypeTags.IS_FIRE)
                && EnchantmentHelper.getLevel(Enchantments.FIRE_PROTECTION, stack) > 0) {
            cir.setReturnValue(false);
            return;
        }


        if (source.isIn(DamageTypeTags.IS_EXPLOSION)
                && EnchantmentHelper.getLevel(Enchantments.BLAST_PROTECTION, stack) > 0) {
            cir.setReturnValue(false);
        }
    }
}
