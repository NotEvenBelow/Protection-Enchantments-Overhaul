package protection_enchant_overhaul.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import protection_enchant_overhaul.config.PEOConfig;

/** Conditionally zero vanilla Protection buckets we override. */
@Mixin(ProtectionEnchantment.class)
public abstract class DisableVanillaProtMixin {
    @Inject(method = "getProtectionAmount(ILnet/minecraft/entity/damage/DamageSource;)I",
            at = @At("HEAD"), cancellable = true)
    private void peo$conditionalDisable(int level, DamageSource source, CallbackInfoReturnable<Integer> cir) {
        Enchantment self = (Enchantment)(Object)this;

        // ALL (our "Physical")
        if (PEOConfig.physicalOverrideEnabled && self == Enchantments.PROTECTION) {
            cir.setReturnValue(0);
            return;
        }
        // FIRE
        if (PEOConfig.overrideFireEnabled && self == Enchantments.FIRE_PROTECTION) {
            cir.setReturnValue(0);
            return;
        }
        // BLAST
        if (PEOConfig.overrideBlastEnabled && self == Enchantments.BLAST_PROTECTION) {
            cir.setReturnValue(0);
            return;
        }
        // PROJECTILE
        if (PEOConfig.overrideProjectileEnabled && self == Enchantments.PROJECTILE_PROTECTION) {
            cir.setReturnValue(0);
        }
        // else: let vanilla run unchanged
    }
}
