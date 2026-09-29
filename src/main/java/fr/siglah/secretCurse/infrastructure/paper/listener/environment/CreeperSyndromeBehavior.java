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
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

/**
 * Syndrome du Creeper : si le joueur reste immobile pendant 4 secondes, il explose.
 * La rotation de la caméra compte comme un mouvement.
 * Intègre une BossBar pour visualiser le temps restant avant l'explosion.
 */
public class CreeperSyndromeBehavior implements CurseBehavior {
    private static final double STILL_THRESHOLD_SQUARED = 0.0001; // Sensibilité extrême pour les mouvements
    private static final float CAMERA_THRESHOLD = 0.5f; // Sensibilité de la caméra (0.5 degré)

    // On vérifie toutes les 0.1s (2 ticks) pour que la barre d'animation soit ultra fluide
    private static final long CHECK_INTERVAL = 2L;
    private static final int TICKS_TO_TRIGGER = 40; // 40 x 0.1s = 4.0 secondes avant explosion
    private static final int HISS_TICK = 25; // Le sifflement démarre quand il reste 1.5s
    private static final long COOLDOWN_AFTER_EXPLOSION_TICKS = 60L; // 3 secondes de répit après explosion

    private final JavaPlugin plugin;
    private final UUID targetId;
    private BukkitTask task;
    private BossBar timerBar; // La barre visuelle

    private Location lastLocation;
    private int stillTicks = 0;
    private long cooldownRemaining = 0;

    public CreeperSyndromeBehavior(JavaPlugin plugin, UUID targetId) {
        this.plugin = plugin;
        this.targetId = targetId;
    }

    @Override
    public void onStart(Player player) {
        lastLocation = player.getLocation().clone();

        // Création de la barre de timer (invisible au départ)
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

            // 1. Déplacement physique (X, Y, Z)
            if (lastLocation != null && current.distanceSquared(lastLocation) > STILL_THRESHOLD_SQUARED) {
                isImmobile = false;
            }

            // 2. Mouvement de caméra (Yaw et Pitch)
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

                // Déclenche le son du creeper quand il ne reste plus que 1.5 seconde
                if (stillTicks == HISS_TICK) {
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_CREEPER_PRIMED, SoundCategory.HOSTILE, 1.0f, 1.0f);
                }

                // Boom !
                if (stillTicks >= TICKS_TO_TRIGGER) {
                    stillTicks = 0;
                    hideBar();
                    triggerExplosion(p);
                }
            } else {
                stillTicks = 0;
                hideBar(); // Retire instantanément la barre dès qu'il bouge
            }

            lastLocation = current.clone();

        }, CHECK_INTERVAL, CHECK_INTERVAL);
    }

    @Override
    public void onStop(Player player) {
        if (task != null) task.cancel();
        if (timerBar != null) timerBar.removeAll(); // Sécurité pour retirer la barre quand le jeu s'arrête
    }

    private void showBar(Player player, int ticks) {
        if (!timerBar.getPlayers().contains(player)) {
            timerBar.addPlayer(player);
        }
        // Calcule le temps restant et ajuste la barre
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
        // breakBlocks à true : fait des dégâts au joueur ET casse le décor
        player.getWorld().createExplosion(player.getLocation(), 3.0f, false, true, player);
    }
}