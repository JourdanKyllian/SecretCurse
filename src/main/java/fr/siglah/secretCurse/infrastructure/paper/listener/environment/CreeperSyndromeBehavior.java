package fr.siglah.secretCurse.infrastructure.paper.listener.environment;

import fr.siglah.secretCurse.infrastructure.paper.listener.CurseBehavior;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

/**
 * Syndrome du Creeper : si le joueur reste immobile pendant 4 secondes
 * (déplacement OU rotation caméra), il explose. Volontairement one-shot :
 * l'explosion tue toujours le porteur de la malédiction, quels que soient
 * son armure ou ses effets de résistance, exactement comme un vrai creeper
 * tuerait n'importe qui à bout portant. Les dégâts de blocs (breakBlocks)
 * sont assumés : ce plugin est pensé pour de petites parties entre amis, pas
 * pour un serveur public.
 */
public class CreeperSyndromeBehavior implements CurseBehavior {
    private static final double STILL_THRESHOLD_SQUARED = 0.0001;
    private static final float CAMERA_THRESHOLD = 0.5f;

    private static final long CHECK_INTERVAL = 2L;
    private static final int TICKS_TO_TRIGGER = 40; // 4.0 secondes
    private static final int HISS_TICK = 25; // Sifflement à 1,5s de l'explosion
    private static final long COOLDOWN_AFTER_EXPLOSION_TICKS = 60L;

    private final JavaPlugin plugin;
    private final UUID targetId;
    private BukkitTask task;
    private BossBar timerBar;

    private Location lastLocation;
    private int stillTicks = 0;
    private long cooldownRemaining = 0;
    private boolean expectingLethalExplosion = false;

    public CreeperSyndromeBehavior(JavaPlugin plugin, UUID targetId) {
        this.plugin = plugin;
        this.targetId = targetId;
    }

    @Override
    public void onStart(Player player) {
        lastLocation = player.getLocation().clone();
        timerBar = Bukkit.createBossBar("§c§l💥 Explosion imminente...", BarColor.RED, BarStyle.SOLID);

        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            Player p = Bukkit.getPlayer(targetId);
            if (p == null || !p.isOnline() || p.isDead()) {
                hideBar();
                return;
            }

            if (cooldownRemaining > 0) {
                cooldownRemaining -= CHECK_INTERVAL;
                lastLocation = p.getLocation().clone();
                hideBar();
                return;
            }

            Location current = p.getLocation();
            boolean isImmobile = true;

            if (lastLocation != null && current.distanceSquared(lastLocation) > STILL_THRESHOLD_SQUARED) {
                isImmobile = false;
            }
            if (lastLocation != null) {
                float yawDiff = Math.abs(current.getYaw() - lastLocation.getYaw());
                float pitchDiff = Math.abs(current.getPitch() - lastLocation.getPitch());
                if (yawDiff > CAMERA_THRESHOLD || pitchDiff > CAMERA_THRESHOLD) {
                    isImmobile = false;
                }
            }

            if (isImmobile) {
                stillTicks++;
                showBar(p, stillTicks);

                if (stillTicks == HISS_TICK) {
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_CREEPER_PRIMED, SoundCategory.HOSTILE, 1.0f, 1.0f);
                }
                if (stillTicks >= TICKS_TO_TRIGGER) {
                    stillTicks = 0;
                    hideBar();
                    triggerExplosion(p);
                }
            } else {
                stillTicks = 0;
                hideBar();
            }

            lastLocation = current.clone();
        }, CHECK_INTERVAL, CHECK_INTERVAL);
    }

    @Override
    public void onStop(Player player) {
        if (task != null) task.cancel();
        if (timerBar != null) timerBar.removeAll();
    }

    private void showBar(Player player, int ticks) {
        if (!timerBar.getPlayers().contains(player)) {
            timerBar.addPlayer(player);
        }
        double timeLeft = 4.0 - (ticks * 0.1);
        timerBar.setTitle("§c§l Explosion dans : " + String.format("%.1f", Math.max(0.0, timeLeft)) + "s");
        timerBar.setProgress(1.0 - ((double) ticks / TICKS_TO_TRIGGER));
    }

    private void hideBar() {
        if (timerBar != null) {
            timerBar.removeAll();
        }
    }

    private void triggerExplosion(Player player) {
        cooldownRemaining = COOLDOWN_AFTER_EXPLOSION_TICKS;
        expectingLethalExplosion = true;
        // breakBlocks à true (assumé) : dégâts au joueur ET au décor
        player.getWorld().createExplosion(player.getLocation(), 3.0f, false, true, player);
        expectingLethalExplosion = false;
    }

    /**
     * Garantit le one-shot : quels que soient l'armure, la Résistance ou
     * l'Absorption du joueur, l'explosion de SA PROPRE malédiction le tue
     * toujours. On ne force le kill que sur les dégâts causés par NOTRE
     * explosion (le flag expectingLethalExplosion est vrai uniquement le temps
     * de l'appel à createExplosion), pas sur un vrai creeper croisé par hasard.
     */
    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!player.getUniqueId().equals(targetId)) return;
        if (!expectingLethalExplosion) return;

        boolean isExplosion = event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION
                || event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION;
        if (isExplosion) {
            event.setDamage(1000.0);
        }
    }
}
