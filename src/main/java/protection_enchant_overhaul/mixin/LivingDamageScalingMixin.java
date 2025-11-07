package protection_enchant_overhaul.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.DamageTypeTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import protection_enchant_overhaul.ProtectionEnchantOverhaul;
import protection_enchant_overhaul.config.PEOConfig;

@Mixin(LivingEntity.class)
public abstract class LivingDamageScalingMixin {

    @ModifyVariable(method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z",
            at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float peo$applyEnchantReductions(float amount, DamageSource source) {
        LivingEntity self = (LivingEntity)(Object)this;

        // MAGIC
        if (PEOConfig.enableMagicEnchant
                && ProtectionEnchantOverhaul.MAGIC_PROTECTION != null
                && (source.isOf(DamageTypes.MAGIC) || source.isOf(DamageTypes.INDIRECT_MAGIC))) {
            int lv = levels(self, ProtectionEnchantOverhaul.MAGIC_PROTECTION);
            if (lv > 0) return scale(amount, lv * PEOConfig.magicPerLevel, PEOConfig.magicMaxReduction);
            return amount;
        }

        // BLAST
        if (PEOConfig.overrideBlastEnabled && source.isIn(DamageTypeTags.IS_EXPLOSION)) {
            int lv = levels(self, Enchantments.BLAST_PROTECTION);
            if (lv > 0) return scale(amount, lv * PEOConfig.blastPerLevel, PEOConfig.blastMaxReduction);
            return amount;
        }

        // FIRE
        if (PEOConfig.overrideFireEnabled && source.isIn(DamageTypeTags.IS_FIRE)) {
            int lv = levels(self, Enchantments.FIRE_PROTECTION);
            if (lv > 0) return scale(amount, lv * PEOConfig.firePerLevel, PEOConfig.fireMaxReduction);
            return amount;
        }

        // PROJECTILE
        if (source.isIn(DamageTypeTags.IS_PROJECTILE) && PEOConfig.overrideProjectileEnabled) {
            int lv = levels(self, Enchantments.PROJECTILE_PROTECTION);
            if (lv > 0) return scale(amount, lv * PEOConfig.projPerLevel, PEOConfig.projMaxReduction);
            return amount;
        }

        // PHYSICAL
        if (PEOConfig.physicalOverrideEnabled) {
            if (source.isIn(DamageTypeTags.BYPASSES_ARMOR))  return amount;
            if (source.isOf(DamageTypes.MAGIC) || source.isOf(DamageTypes.INDIRECT_MAGIC)) return amount;
            if (source.isIn(DamageTypeTags.IS_FIRE))         return amount;
            if (source.isIn(DamageTypeTags.IS_EXPLOSION))    return amount;
            if (source.isIn(DamageTypeTags.IS_PROJECTILE))   return amount;

            int lv = levels(self, Enchantments.PROTECTION);
            if (lv > 0) return scale(amount, lv * PEOConfig.perLevel, PEOConfig.maxReduction);
        }

        return amount;
    }

    private static int levels(LivingEntity self, net.minecraft.enchantment.Enchantment ench) {
        int total = 0;
        for (EquipmentSlot slot : new EquipmentSlot[] {
                EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
        }) {
            ItemStack stack = self.getEquippedStack(slot);
            if (!stack.isEmpty()) total += EnchantmentHelper.getLevel(ench, stack);
        }
        return total;
    }

    private static float scale(float amount, float raw, float cap) {
        float r = raw > cap ? cap : raw;
        return amount * (1.0f - r);
    }
}
