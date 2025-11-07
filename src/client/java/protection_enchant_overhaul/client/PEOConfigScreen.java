// file: src/main/java/protection_enchant_overhaul/client/PEOConfigScreen.java
package protection_enchant_overhaul.client;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import protection_enchant_overhaul.config.PEOConfig;

public final class PEOConfigScreen {
    private PEOConfigScreen() {}

    public static Screen create(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.literal("Protection Enchant Overhaul"));

        builder.setSavingRunnable(PEOConfig::save);
        ConfigEntryBuilder eb = builder.entryBuilder();

        // ===== PHYSICAL =====
        ConfigCategory physical = builder.getOrCreateCategory(Text.literal("Physical"));
        physical.addEntry(
                eb.startBooleanToggle(Text.literal("Use custom Physical Protection (disable vanilla)"),
                                PEOConfig.physicalOverrideEnabled)
                        .setSaveConsumer(v -> PEOConfig.physicalOverrideEnabled = v)
                        .setDefaultValue(true)
                        .build()
        );
        physical.addEntry(
                eb.startFloatField(Text.literal("Per-level reduction"), PEOConfig.perLevel)
                        .setMin(0f).setMax(1f)
                        .setSaveConsumer(v -> PEOConfig.perLevel = clamp(v, 0f, 1f))
                        .setDefaultValue(0.0525f)
                        .build()
        );
        physical.addEntry(
                eb.startFloatField(Text.literal("Max total reduction"), PEOConfig.maxReduction)
                        .setMin(0f).setMax(0.95f)
                        .setSaveConsumer(v -> PEOConfig.maxReduction = clamp(v, 0f, 0.95f))
                        .setDefaultValue(0.75f)
                        .build()
        );

        // ===== MAGIC =====
        ConfigCategory magic = builder.getOrCreateCategory(Text.literal("Magic"));
        magic.addEntry(
                eb.startBooleanToggle(Text.literal("Enable Magic Protection enchant"),
                                PEOConfig.enableMagicEnchant)
                        .setSaveConsumer(v -> PEOConfig.enableMagicEnchant = v)
                        .setDefaultValue(true)
                        .build()
        );
        magic.addEntry(
                eb.startFloatField(Text.literal("Per-level reduction"), PEOConfig.magicPerLevel)
                        .setMin(0f).setMax(1f)
                        .setSaveConsumer(v -> PEOConfig.magicPerLevel = clamp(v, 0f, 1f))
                        .setDefaultValue(0.06f)
                        .build()
        );
        magic.addEntry(
                eb.startFloatField(Text.literal("Max total reduction"), PEOConfig.magicMaxReduction)
                        .setMin(0f).setMax(0.95f)
                        .setSaveConsumer(v -> PEOConfig.magicMaxReduction = clamp(v, 0f, 0.95f))
                        .setDefaultValue(0.8f)
                        .build()
        );

        // ===== FIRE =====
        ConfigCategory fire = builder.getOrCreateCategory(Text.literal("Fire"));
        fire.addEntry(
                eb.startBooleanToggle(Text.literal("Override Fire Protection (disable vanilla)"),
                                PEOConfig.overrideFireEnabled)
                        .setSaveConsumer(v -> PEOConfig.overrideFireEnabled = v)
                        .setDefaultValue(true)
                        .build()
        );
        fire.addEntry(
                eb.startFloatField(Text.literal("Per-level reduction"), PEOConfig.firePerLevel)
                        .setMin(0f).setMax(1f)
                        .setSaveConsumer(v -> PEOConfig.firePerLevel = clamp(v, 0f, 1f))
                        .setDefaultValue(0.0625f)
                        .build()
        );
        fire.addEntry(
                eb.startFloatField(Text.literal("Max total reduction"), PEOConfig.fireMaxReduction)
                        .setMin(0f).setMax(0.95f)
                        .setSaveConsumer(v -> PEOConfig.fireMaxReduction = clamp(v, 0f, 0.95f))
                        .setDefaultValue(0.87f)
                        .build()
        );

        // ===== BLAST =====
        ConfigCategory blast = builder.getOrCreateCategory(Text.literal("Blast"));
        blast.addEntry(
                eb.startBooleanToggle(Text.literal("Override Blast Protection (disable vanilla)"),
                                PEOConfig.overrideBlastEnabled)
                        .setSaveConsumer(v -> PEOConfig.overrideBlastEnabled = v)
                        .setDefaultValue(true)
                        .build()
        );
        blast.addEntry(
                eb.startFloatField(Text.literal("Per-level reduction"), PEOConfig.blastPerLevel)
                        .setMin(0f).setMax(1f)
                        .setSaveConsumer(v -> PEOConfig.blastPerLevel = clamp(v, 0f, 1f))
                        .setDefaultValue(0.06f)
                        .build()
        );
        blast.addEntry(
                eb.startFloatField(Text.literal("Max total reduction"), PEOConfig.blastMaxReduction)
                        .setMin(0f).setMax(0.95f)
                        .setSaveConsumer(v -> PEOConfig.blastMaxReduction = clamp(v, 0f, 0.95f))
                        .setDefaultValue(0.85f)
                        .build()
        );

        // ===== PROJECTILE =====
        ConfigCategory projectile = builder.getOrCreateCategory(Text.literal("Projectile"));

        // --- Custom reduction FIRST ---
        projectile.addEntry(
                eb.startBooleanToggle(Text.literal("Override Projectile Protection (custom reduction)"),
                                PEOConfig.overrideProjectileEnabled)
                        .setSaveConsumer(v -> PEOConfig.overrideProjectileEnabled = v)
                        .setDefaultValue(false)
                        .build()
        );
        projectile.addEntry(
                eb.startFloatField(Text.literal("Per-level reduction"), PEOConfig.projPerLevel)
                        .setMin(0f).setMax(1f)
                        .setSaveConsumer(v -> PEOConfig.projPerLevel = clamp(v, 0f, 1f))
                        .setDefaultValue(0.055f)
                        .build()
        );
        projectile.addEntry(
                eb.startFloatField(Text.literal("Max total reduction"), PEOConfig.projMaxReduction)
                        .setMin(0f).setMax(0.95f)
                        .setSaveConsumer(v -> PEOConfig.projMaxReduction = clamp(v, 0f, 0.95f))
                        .setDefaultValue(0.78f)
                        .build()
        );

        // --- Reflection / Ricochet AFTER reduction ---
        projectile.addEntry(
                eb.startBooleanToggle(Text.literal("Enable Reflection (ricochet)"),
                                PEOConfig.projectileReflectionEnabled)
                        .setSaveConsumer(v -> PEOConfig.projectileReflectionEnabled = v)
                        .setDefaultValue(true)
                        .build()
        );
        projectile.addEntry(
                eb.startFloatField(Text.literal("Ricochet range (blocks)"),
                                PEOConfig.projectileReflectionRangeBlocks)
                        .setMin(0.5f).setMax(10f)
                        .setSaveConsumer(v -> PEOConfig.projectileReflectionRangeBlocks = clamp(v, 0.5f, 10f))
                        .setDefaultValue(4.0f)
                        .build()
        );
        projectile.addEntry(
                eb.startFloatField(Text.literal("Ricochet bonus dmg per level > 1"),
                                PEOConfig.projectileReflectionBonusDamagePerLevelAbove1)
                        .setMin(0f).setMax(5f)
                        .setSaveConsumer(v -> PEOConfig.projectileReflectionBonusDamagePerLevelAbove1 = clamp(v, 0f, 5f))
                        .setDefaultValue(0.25f)
                        .build()
        );

        // ===== ENCHANTING =====
        ConfigCategory ench = builder.getOrCreateCategory(Text.literal("Enchanting"));
        ench.addEntry(
                eb.startBooleanToggle(Text.literal("Equalize Protection weights (25% each)"),
                                PEOConfig.equalizeProtWeights)
                        .setSaveConsumer(v -> PEOConfig.equalizeProtWeights = v)
                        .setDefaultValue(true)
                        .build()
        );

        return builder.build();
    }

    private static float clamp(float v, float min, float max) {
        if (Float.isNaN(v) || Float.isInfinite(v)) return min;
        return Math.max(min, Math.min(max, v));
    }
}
