package protection_enchant_overhaul.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EnchantmentLevelEntry.class)
public interface EnchantmentLevelEntryAccessor {
    @Accessor("enchantment") Enchantment peo$getEnchantment();
    @Accessor("level") int peo$getLevel();
}
