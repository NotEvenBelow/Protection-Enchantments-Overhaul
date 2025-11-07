package protection_enchant_overhaul.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import protection_enchant_overhaul.config.PEOConfig;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin {

    @Inject(method = "onEntityHit(Lnet/minecraft/util/hit/EntityHitResult;)V", at = @At("TAIL"))
    private void peo$ricochetAfterHit(EntityHitResult hit, CallbackInfo ci) {
        if (!PEOConfig.projectileReflectionEnabled) return;

        final Entity tgt = hit.getEntity();
        if (!(tgt instanceof LivingEntity living)) return;

        final PersistentProjectileEntity self = (PersistentProjectileEntity)(Object)this;
        final World world = self.getWorld();
        if (world.isClient) return;

        // need some Projectile Protection to ricochet
        int level = 0;
        for (ItemStack armor : living.getArmorItems()) {
            level += EnchantmentHelper.getLevel(Enchantments.PROJECTILE_PROTECTION, armor);
        }
        if (level <= 0) return;

        // sfx
        world.playSound(null, living.getX(), living.getY(), living.getZ(),
                SoundEvents.ITEM_SHIELD_BLOCK, SoundCategory.PLAYERS, 1.0F,
                0.8F + world.random.nextFloat() * 0.4F);

        Vec3d incoming = self.getVelocity();
        if (incoming.lengthSquared() == 0) return;

        // start with straight-back vector
        Vec3d away = incoming.normalize().multiply(-1);

        // never aim near the shooter
        Entity shooter = self.getOwner();
        if (shooter != null) {
            Vec3d toShooter = shooter.getPos().subtract(self.getPos()).normalize();
            if (away.dotProduct(toShooter) > 0.5) { // stricter check
                away = rotateAroundAxis(away, new Vec3d(0, 1, 0), Math.toRadians(70)); // bigger sidestep
            }
        }


        Vec3d axis = away.crossProduct(new Vec3d(0, 1, 0));
        if (axis.lengthSquared() < 1.0e-6) axis = new Vec3d(1, 0, 0);
        Vec3d dir = rotateAroundAxis(away, axis.normalize(), Math.toRadians(28)); // steeper


        float jitter = (world.random.nextFloat() * 12f - 6f); // -6..+6 deg
        dir = rotateAroundAxis(dir, new Vec3d(0, 1, 0), Math.toRadians(jitter)).normalize();


        final double SPEED_PER_BLOCK = 0.07;
        double speed = MathHelper.clamp(PEOConfig.projectileReflectionRangeBlocks * SPEED_PER_BLOCK, 0.12, 0.35);


        Entity clone = self.getType().create(world);
        if (!(clone instanceof PersistentProjectileEntity ric)) return;


        double baseDmg = self.getDamage();
        double bonus = Math.max(0, level - 1) * PEOConfig.projectileReflectionBonusDamagePerLevelAbove1 * baseDmg;
        ric.setDamage(baseDmg + bonus);


        ric.setOwner(living);


        Vec3d start = self.getPos().add(dir.multiply(0.1));
        ric.refreshPositionAndAngles(start.x, start.y, start.z, self.getYaw(), self.getPitch());
        ric.setVelocity(dir.multiply(speed));


        ric.setCritical(false);
        ric.setPunch(0);

        world.spawnEntity(ric);
    }


    private static Vec3d rotateAroundAxis(Vec3d v, Vec3d axis, double angleRad) {
        axis = axis.normalize();
        double c = MathHelper.cos((float) angleRad);
        double s = MathHelper.sin((float) angleRad);
        double kx = axis.x, ky = axis.y, kz = axis.z;

        double dot = v.x * kx + v.y * ky + v.z * kz;
        Vec3d cross = new Vec3d(
                ky * v.z - kz * v.y,
                kz * v.x - kx * v.z,
                kx * v.y - ky * v.x
        );

        return new Vec3d(
                v.x * c + cross.x * s + kx * dot * (1 - c),
                v.y * c + cross.y * s + ky * dot * (1 - c),
                v.z * c + cross.z * s + kz * dot * (1 - c)
        );
    }
}
