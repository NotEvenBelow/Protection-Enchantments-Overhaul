package protection_enchant_overhaul.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Disables vanilla Protection (ALL) reduction.
 * Other Protection types (Fire/Blast/Projectile/Fall) behave vanilla.
 */
@Mixin(ProtectionEnchantment.class)
public abstract class ProtectionEnchantmentMixin {

    @Inject(method = "getProtectionAmount(ILnet/minecraft/entity/damage/DamageSource;)I",
            at = @At("HEAD"), cancellable = true)
    private void peo$disableVanillaProt(int level, DamageSource source, CallbackInfoReturnable<Integer> cir) {
        Enchantment self = (Enchantment)(Object)this;
        if (self == Enchantments.PROTECTION) {
            // always return 0 for generic Protection
            cir.setReturnValue(0);
        }
    }
}
