package protection_enchant_overhaul;

import net.fabricmc.api.ModInitializer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import protection_enchant_overhaul.config.PEOConfig;
import protection_enchant_overhaul.enchant.MagicProtectionEnchantment;

public class ProtectionEnchantOverhaul implements ModInitializer {
    public static final String MODID = "protection_enchant_overhaul";

    public static Enchantment MAGIC_PROTECTION; 

    @Override
    public void onInitialize() {
        PEOConfig.load();

        if (PEOConfig.enableMagicEnchant) {
            MAGIC_PROTECTION = Registry.register(
                    Registries.ENCHANTMENT,
                    new Identifier(MODID, "magic_protection"),
                    new MagicProtectionEnchantment()
            );
        } else {
            MAGIC_PROTECTION = null;
        }
    }
}
