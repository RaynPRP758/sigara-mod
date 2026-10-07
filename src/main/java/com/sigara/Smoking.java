package com.sigara;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/** Duman efektleri ve çakmak yardımcıları. */
public final class Smoking {
    private Smoking() {}

    /** Envanterdeki çakmağı bulur (önce sonsuz olanları tercih eder). */
    public static ItemStack findLighter(Player player) {
        var inv = player.getInventory();
        ItemStack disposable = ItemStack.EMPTY;
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack s = inv.getItem(i);
            if (s.getItem() instanceof LighterItem) {
                if (!s.isDamageableItem()) return s;
                if (disposable.isEmpty()) disposable = s;
            }
        }
        return disposable;
    }

    public static Vec3 mouth(LivingEntity e) {
        return e.getEyePosition().add(e.getLookAngle().scale(0.35)).subtract(0, 0.12, 0);
    }

    /** Çakmak sesi + alev, tek kullanımlıksa aşındırır. Sadece sunucuda çağır. */
    public static void flick(Level level, Player player, ItemStack lighter) {
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.FLINTANDSTEEL_USE, SoundSource.PLAYERS, 0.7F, 1.6F);
        if (level instanceof ServerLevel sl) {
            Vec3 p = player.getEyePosition().add(player.getLookAngle().scale(0.6)).subtract(0, 0.35, 0);
            sl.sendParticles(ParticleTypes.FLAME, p.x, p.y, p.z, 3, 0.01, 0.01, 0.01, 0.003);
            sl.sendParticles(ParticleTypes.SMOKE, p.x, p.y + 0.05, p.z, 2, 0.01, 0.01, 0.01, 0.003);
        }
        if (!lighter.isEmpty() && lighter.isDamageableItem() && !player.getAbilities().instabuild) {
            int d = lighter.getDamageValue() + 1;
            if (d >= lighter.getMaxDamage()) lighter.shrink(1);
            else lighter.setDamageValue(d);
        }
    }

    /** İçerken her tick (istemcide) çağrılır: ince duman + periyodik nefes verme. */
    public static void tickEffects(Level level, LivingEntity e, int elapsed) {
        if (!level.isClientSide()) return;
        RandomSource r = level.getRandom();
        Vec3 look = e.getLookAngle();
        Vec3 mouth = mouth(e);

        // ucundan yükselen ince duman
        if (elapsed % 3 == 0) {
            Vec3 t = mouth.add(look.scale(0.28)).subtract(0, 0.08, 0);
            level.addParticle(ParticleTypes.SMOKE, t.x, t.y, t.z, 0, 0.012, 0);
        }
        // her ~2.5 sn'de bir duman ver
        int phase = elapsed % 50;
        if (phase >= 28 && phase < 42 && phase % 2 == 0) {
            double vx = look.x * 0.06 + (r.nextDouble() - 0.5) * 0.015;
            double vy = look.y * 0.06 + 0.012;
            double vz = look.z * 0.06 + (r.nextDouble() - 0.5) * 0.015;
            level.addParticle(ParticleTypes.LARGE_SMOKE, mouth.x, mouth.y, mouth.z, vx, vy, vz);
            if (phase % 4 == 0) {
                level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, mouth.x, mouth.y, mouth.z, vx * 0.5, 0.004, vz * 0.5);
            }
        }
    }

    /** Bitince büyük bir duman bulutu (sunucu). */
    public static void bigExhale(Level level, LivingEntity e) {
        Vec3 m = mouth(e);
        level.playSound(null, e.getX(), e.getY(), e.getZ(),
                SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 0.25F, 1.8F);
        if (level instanceof ServerLevel sl) {
            sl.sendParticles(ParticleTypes.LARGE_SMOKE, m.x, m.y, m.z, 12, 0.08, 0.04, 0.08, 0.02);
            sl.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, m.x, m.y, m.z, 4, 0.05, 0.02, 0.05, 0.01);
        }
    }
}
