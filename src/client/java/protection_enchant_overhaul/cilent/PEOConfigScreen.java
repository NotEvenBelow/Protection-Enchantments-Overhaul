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

        // Save JSON when pressing Done
        builder.setSavingRunnable(PEOConfig::save);

        ConfigEntryBuilder eb = builder.entryBuilder();

        // =======================
        // PHYSICAL
        // =======================
        ConfigCategory physical = builder.getOrCreateCategory(Text.literal("Physical"));
        physical.addEntry(
                eb.startBooleanToggle(Text.literal("Use custom Physical Protection (disable vanilla)"),
                                PEOConfig.physicalOverrideEnabled)
                  .setTooltip(
                      Text.literal("ON: Uses custom Physical scaling and disables vanilla Protection."),
                      Text.literal("OFF: Restores vanilla Protection. Custom Physical scaling is not applied.")
                  )
                  .setSaveConsumer(v -> PEOConfig.physicalOverrideEnabled = v)
                  .setDefaultValue(true)
                  .build()
        );
        physical.addEntry(
                eb.startFloatField(Text.literal("Per-level reduction"), PEOConfig.perLevel)
                  .setMin(0.0f).setMax(1.0f)
                  .setTooltip(
                      Text.literal("Damage reduced per Protection level on physical hits."),
                      Text.literal("Default: 0.0525 (5.25%).")
                  )
                  .setSaveConsumer(v -> PEOConfig.perLevel = clamp(v, 0.0f, 1.0f))
                  .setDefaultValue(0.0525f)
                  .build()
        );
        physical.addEntry(
                eb.startFloatField(Text.literal("Max total reduction"), PEOConfig.maxReduction)
                  .setMin(0.0f).setMax(0.95f)
                  .setTooltip(
                      Text.literal("Hard cap for custom Physical reduction."),
                      Text.literal("Default: 0.75 (75%).")
                  )
                  .setSaveConsumer(v -> PEOConfig.maxReduction = clamp(v, 0.0f, 0.95f))
                  .setDefaultValue(0.75f)
                  .build()
        );

        // =======================
        // MAGIC
        // =======================
        ConfigCategory magic = builder.getOrCreateCategory(Text.literal("Magic"));
        magic.addEntry(
                eb.startBooleanToggle(Text.literal("Enable Magic Protection enchant"),
                                PEOConfig.enableMagicEnchant)
                  .setTooltip(
                      Text.literal("ON: Magic Protection is active and reduces MAGIC damage."),
                      Text.literal("OFF: It does nothing immediately; after restart it is unregistered.")
                  )
                  .setSaveConsumer(v -> PEOConfig.enableMagicEnchant = v)
                  .setDefaultValue(true)
                  .build()
        );
        magic.addEntry(
                eb.startFloatField(Text.literal("Per-level reduction"), PEOConfig.magicPerLevel)
                  .setMin(0.0f).setMax(1.0f)
                  .setTooltip(
                      Text.literal("Damage reduced per Magic Protection level on magic hits."),
                      Text.literal("Default: 0.06 (6%).")
                  )
                  .setSaveConsumer(v -> PEOConfig.magicPerLevel = clamp(v, 0.0f, 1.0f))
                  .setDefaultValue(0.06f)
                  .build()
        );
        magic.addEntry(
                eb.startFloatField(Text.literal("Max total reduction"), PEOConfig.magicMaxReduction)
                  .setMin(0.0f).setMax(0.95f)
                  .setTooltip(
                      Text.literal("Hard cap for Magic Protection."),
                      Text.literal("Default: 0.8 (80%).")
                  )
                  .setSaveConsumer(v -> PEOConfig.magicMaxReduction = clamp(v, 0.0f, 0.95f))
                  .setDefaultValue(0.8f)
                  .build()
        );

        // =======================
        // FIRE
        // =======================
        ConfigCategory fire = builder.getOrCreateCategory(Text.literal("Fire"));
        fire.addEntry(
                eb.startBooleanToggle(Text.literal("Override Fire Protection (disable vanilla)"),
                                PEOConfig.overrideFireEnabled)
                  .setTooltip(
                      Text.literal("ON: Uses custom Fire scaling and disables vanilla Fire Prot."),
                      Text.literal("OFF: Vanilla Fire Protection works; custom Fire scaling is not applied.")
                  )
                  .setSaveConsumer(v -> PEOConfig.overrideFireEnabled = v)
                  .setDefaultValue(true)
                  .build()
        );
        fire.addEntry(
                eb.startFloatField(Text.literal("Per-level reduction"), PEOConfig.firePerLevel)
                  .setMin(0.0f).setMax(1.0f)
                  .setTooltip(
                      Text.literal("Damage reduced per level on fire/lava/burning hits."),
                      Text.literal("Default: 0.0625 (6.25%).")
                  )
                  .setSaveConsumer(v -> PEOConfig.firePerLevel = clamp(v, 0.0f, 1.0f))
                  .setDefaultValue(0.0625f)
                  .build()
        );
        fire.addEntry(
                eb.startFloatField(Text.literal("Max total reduction"), PEOConfig.fireMaxReduction)
                  .setMin(0.0f).setMax(0.95f)
                  .setTooltip(
                      Text.literal("Hard cap for custom Fire reduction."),
                      Text.literal("Default: 0.87 (87%).")
                  )
                  .setSaveConsumer(v -> PEOConfig.fireMaxReduction = clamp(v, 0.0f, 0.95f))
                  .setDefaultValue(0.87f)
                  .build()
        );

        // =======================
        // BLAST
        // =======================
        ConfigCategory blast = builder.getOrCreateCategory(Text.literal("Blast"));
        blast.addEntry(
                eb.startBooleanToggle(Text.literal("Override Blast Protection (disable vanilla)"),
                                PEOConfig.overrideBlastEnabled)
                  .setTooltip(
                      Text.literal("ON: Uses custom Blast scaling and disables vanilla Blast Prot."),
                      Text.literal("OFF: Vanilla Blast Protection works; custom Blast scaling is not applied.")
                  )
                  .setSaveConsumer(v -> PEOConfig.overrideBlastEnabled = v)
                  .setDefaultValue(true)
                  .build()
        );
        blast.addEntry(
                eb.startFloatField(Text.literal("Per-level reduction"), PEOConfig.blastPerLevel)
                  .setMin(0.0f).setMax(1.0f)
                  .setTooltip(
                      Text.literal("Damage reduced per level on explosions."),
                      Text.literal("Default: 0.06 (6%).")
                  )
                  .setSaveConsumer(v -> PEOConfig.blastPerLevel = clamp(v, 0.0f, 1.0f))
                  .setDefaultValue(0.06f)
                  .build()
        );
        blast.addEntry(
                eb.startFloatField(Text.literal("Max total reduction"), PEOConfig.blastMaxReduction)
                  .setMin(0.0f).setMax(0.95f)
                  .setTooltip(
                      Text.literal("Hard cap for custom Blast reduction."),
                      Text.literal("Default: 0.85 (85%).")
                  )
                  .setSaveConsumer(v -> PEOConfig.blastMaxReduction = clamp(v, 0.0f, 0.95f))
                  .setDefaultValue(0.85f)
                  .build()
        );

        // =======================
        // PROJECTILE
        // =======================
        ConfigCategory projectile = builder.getOrCreateCategory(Text.literal("Projectile"));
        projectile.addEntry(
                eb.startBooleanToggle(Text.literal("Override Projectile Protection (disable vanilla)"),
                                PEOConfig.overrideProjectileEnabled)
                  .setTooltip(
                      Text.literal("ON: Uses custom Projectile scaling and disables vanilla Projectile Prot."),
                      Text.literal("OFF: Vanilla Projectile Protection works; custom Projectile scaling is not applied.")
                  )
                  .setSaveConsumer(v -> PEOConfig.overrideProjectileEnabled = v)
                  .setDefaultValue(true)
                  .build()
        );
        projectile.addEntry(
                eb.startFloatField(Text.literal("Per-level reduction"), PEOConfig.projPerLevel)
                  .setMin(0.0f).setMax(1.0f)
                  .setTooltip(
                      Text.literal("Damage reduced per level on projectile hits."),
                      Text.literal("Default: 0.055 (5.5%).")
                  )
                  .setSaveConsumer(v -> PEOConfig.projPerLevel = clamp(v, 0.0f, 1.0f))
                  .setDefaultValue(0.055f)
                  .build()
        );
        projectile.addEntry(
                eb.startFloatField(Text.literal("Max total reduction"), PEOConfig.projMaxReduction)
                  .setMin(0.0f).setMax(0.95f)
                  .setTooltip(
                      Text.literal("Hard cap for custom Projectile reduction."),
                      Text.literal("Default: 0.78 (78%).")
                  )
                  .setSaveConsumer(v -> PEOConfig.projMaxReduction = clamp(v, 0.0f, 0.95f))
                  .setDefaultValue(0.78f)
                  .build()
        );

        return builder.build();
    }

    private static float clamp(float v, float min, float max) {
        if (Float.isNaN(v) || Float.isInfinite(v)) return min;
        return Math.max(min, Math.min(max, v));
    }
}
