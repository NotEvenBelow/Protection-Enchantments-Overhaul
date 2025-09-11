package protection_enchant_overhaul.enchant;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.EquipmentSlot;

public class MagicProtectionEnchantment extends Enchantment {
    public MagicProtectionEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentTarget.ARMOR,
                new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET});
    }

    @Override
    public int getMinPower(int level) {
        return level * 11 - 10;
    }

    @Override
    public int getMaxPower(int level) {
        return this.getMinPower(level) + 11;
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    public boolean isTreasure() {
        return false; // so it can show up in normal enchanting / loot
    }

    @Override
    public boolean isAvailableForRandomSelection() {
        return true;  // enchanting table + loot rolls
    }

    @Override
    public boolean isAvailableForEnchantedBookOffer() {
        return true;  // librarian trades
    }

    @Override
    protected boolean canAccept(Enchantment other) {
        // prevent dupes + conflict with vanilla Protection
        return other != this && !(other instanceof ProtectionEnchantment) && super.canAccept(other);
    }
}
