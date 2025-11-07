package protection_enchant_overhaul.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import protection_enchant_overhaul.config.PEOConfig;

import java.util.ArrayList;
import java.util.List;

@Mixin(EnchantmentHelper.class)
public abstract class EqualizeProtWeightsMixin {

    @Inject(
        method = "getPossibleEntries(ILnet/minecraft/item/ItemStack;Z)Ljava/util/List;",
        at = @At("RETURN"), cancellable = true
    )
    private static void peo$equalizeProtWeights(int power, ItemStack stack, boolean treasureAllowed,
                                                CallbackInfoReturnable<List<EnchantmentLevelEntry>> cir) {
        if (!PEOConfig.equalizeProtWeights) return;

        List<EnchantmentLevelEntry> original = cir.getReturnValue();
        if (original == null || original.isEmpty()) return;

        List<EnchantmentLevelEntry> out = new ArrayList<>(original.size() + 8);

        for (EnchantmentLevelEntry e : original) {
            Enchantment ench = ((EnchantmentLevelEntryAccessor)(Object)e).peo$getEnchantment();

            int dup = 1; // default: keep as-is
            if (ench == Enchantments.PROTECTION) {
                dup = 1;                          
            } else if (ench == Enchantments.FIRE_PROTECTION) {
                dup = 2;                        
            } else if (ench == Enchantments.BLAST_PROTECTION) {
                dup = 5;                         
            } else if (ench == Enchantments.PROJECTILE_PROTECTION) {
                dup = 2;                           
            }

            if (dup == 1) {
                out.add(e);
            } else {
                int level = ((EnchantmentLevelEntryAccessor)(Object)e).peo$getLevel();
                for (int i = 0; i < dup; i++) {
                    out.add(new EnchantmentLevelEntry(ench, level));
                }
            }
        }

        cir.setReturnValue(out);
    }
}
