package protection_enchant_overhaul.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * JSON config for all buckets.
 *  - Physical: perLevel/maxReduction + physicalOverrideEnabled (return to vanilla when false)
 *  - Magic:    magicPerLevel/magicMaxReduction + enableMagicEnchant
 *  - Fire:     firePerLevel/fireMaxReduction + overrideFireEnabled (zero vanilla FIRE prot when true)
 *  - Blast:    blastPerLevel/blastMaxReduction + overrideBlastEnabled
 *  - Projectile: projPerLevel/projMaxReduction + overrideProjectileEnabled
 */
public final class PEOConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File FILE = new File("config/protection_enchant_overhaul.json");

    // Physical
    public static float perLevel = 0.0525f;
    public static float maxReduction = 0.75f;
    public static boolean physicalOverrideEnabled = true;

    // Magic
    public static float magicPerLevel = 0.06f;
    public static float magicMaxReduction = 0.80f;
    public static boolean enableMagicEnchant = true;

    // Fire (requested: 6.25% per level, cap 87%)
    public static float firePerLevel = 0.0625f;
    public static float fireMaxReduction = 0.87f;
    public static boolean overrideFireEnabled = true;

    // Blast (requested: 6% per level, cap 85%)
    public static float blastPerLevel = 0.06f;
    public static float blastMaxReduction = 0.85f;
    public static boolean overrideBlastEnabled = true;

    // Projectile (requested: 5.5% per level, cap 78%)
    public static float projPerLevel = 0.055f;
    public static float projMaxReduction = 0.78f;
    public static boolean overrideProjectileEnabled = true;

    private PEOConfig() {}

    public static void load() {
        if (!FILE.exists()) { save(); return; }
        try (FileReader fr = new FileReader(FILE); JsonReader jr = new JsonReader(fr)) {
            jr.setLenient(true);
            Data d = GSON.fromJson(jr, Data.class);
            if (d == null) return;

            if (d.perLevel != null)                 perLevel = clamp(d.perLevel, 0f, 1f);
            if (d.maxReduction != null)             maxReduction = clamp(d.maxReduction, 0f, 0.95f);
            if (d.physicalOverrideEnabled != null)  physicalOverrideEnabled = d.physicalOverrideEnabled;

            if (d.magicPerLevel != null)            magicPerLevel = clamp(d.magicPerLevel, 0f, 1f);
            if (d.magicMaxReduction != null)        magicMaxReduction = clamp(d.magicMaxReduction, 0f, 0.95f);
            if (d.enableMagicEnchant != null)       enableMagicEnchant = d.enableMagicEnchant;

            if (d.firePerLevel != null)             firePerLevel = clamp(d.firePerLevel, 0f, 1f);
            if (d.fireMaxReduction != null)         fireMaxReduction = clamp(d.fireMaxReduction, 0f, 0.95f);
            if (d.overrideFireEnabled != null)      overrideFireEnabled = d.overrideFireEnabled;

            if (d.blastPerLevel != null)            blastPerLevel = clamp(d.blastPerLevel, 0f, 1f);
            if (d.blastMaxReduction != null)        blastMaxReduction = clamp(d.blastMaxReduction, 0f, 0.95f);
            if (d.overrideBlastEnabled != null)     overrideBlastEnabled = d.overrideBlastEnabled;

            if (d.projPerLevel != null)             projPerLevel = clamp(d.projPerLevel, 0f, 1f);
            if (d.projMaxReduction != null)         projMaxReduction = clamp(d.projMaxReduction, 0f, 0.95f);
            if (d.overrideProjectileEnabled != null) overrideProjectileEnabled = d.overrideProjectileEnabled;
        } catch (IOException e) {
            e.printStackTrace();
            save();
        }
    }

    public static void save() {
        try {
            FILE.getParentFile().mkdirs();
            try (FileWriter w = new FileWriter(FILE)) {
                GSON.toJson(new Data(
                        perLevel, maxReduction, magicPerLevel, magicMaxReduction,
                        physicalOverrideEnabled, enableMagicEnchant,
                        firePerLevel, fireMaxReduction, overrideFireEnabled,
                        blastPerLevel, blastMaxReduction, overrideBlastEnabled,
                        projPerLevel, projMaxReduction, overrideProjectileEnabled
                ), w);
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    private static float clamp(float v, float min, float max) {
        if (Float.isNaN(v) || Float.isInfinite(v)) return min;
        return Math.max(min, Math.min(max, v));
    }

    // Boxed types -> missing fields won't overwrite defaults.
    private static class Data {
        Float perLevel, maxReduction, magicPerLevel, magicMaxReduction;
        Boolean physicalOverrideEnabled, enableMagicEnchant;
        Float firePerLevel, fireMaxReduction;
        Boolean overrideFireEnabled;
        Float blastPerLevel, blastMaxReduction;
        Boolean overrideBlastEnabled;
        Float projPerLevel, projMaxReduction;
        Boolean overrideProjectileEnabled;

        Data(float perLevel, float maxReduction, float magicPerLevel, float magicMaxReduction,
             boolean physicalOverrideEnabled, boolean enableMagicEnchant,
             float firePerLevel, float fireMaxReduction, boolean overrideFireEnabled,
             float blastPerLevel, float blastMaxReduction, boolean overrideBlastEnabled,
             float projPerLevel, float projMaxReduction, boolean overrideProjectileEnabled) {
            this.perLevel = perLevel; this.maxReduction = maxReduction;
            this.magicPerLevel = magicPerLevel; this.magicMaxReduction = magicMaxReduction;
            this.physicalOverrideEnabled = physicalOverrideEnabled; this.enableMagicEnchant = enableMagicEnchant;
            this.firePerLevel = firePerLevel; this.fireMaxReduction = fireMaxReduction; this.overrideFireEnabled = overrideFireEnabled;
            this.blastPerLevel = blastPerLevel; this.blastMaxReduction = blastMaxReduction; this.overrideBlastEnabled = overrideBlastEnabled;
            this.projPerLevel = projPerLevel; this.projMaxReduction = projMaxReduction; this.overrideProjectileEnabled = overrideProjectileEnabled;
        }
    }
}
