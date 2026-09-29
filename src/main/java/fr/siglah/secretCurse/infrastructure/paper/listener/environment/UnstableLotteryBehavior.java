package fr.siglah.secretCurse.infrastructure.paper.listener.environment;

import fr.siglah.secretCurse.infrastructure.paper.listener.CurseBehavior;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

/**
 * Loterie Instable : toutes les 30 secondes, un nouvel effet de potion
 * aléatoire remplace le précédent (SANS AUCUNE PARTICULE VISIBLE).
 * Le suicide purge et immunise le joueur si un effet handicapant était actif.
 */
public class UnstableLotteryBehavior implements CurseBehavior {
    private static final long CYCLE_TICKS = 30L * 20L; // 30 secondes

    private static final List<PotionEffectType> EFFECT_POOL = List.of(
            PotionEffectType.SPEED, PotionEffectType.SLOWNESS, PotionEffectType.HASTE,
            PotionEffectType.MINING_FATIGUE, PotionEffectType.STRENGTH, PotionEffectType.JUMP_BOOST,
            PotionEffectType.NAUSEA, PotionEffectType.REGENERATION, PotionEffectType.RESISTANCE,
            PotionEffectType.FIRE_RESISTANCE, PotionEffectType.WATER_BREATHING, PotionEffectType.INVISIBILITY,
            PotionEffectType.BLINDNESS, PotionEffectType.NIGHT_VISION, PotionEffectType.HUNGER,
            PotionEffectType.WEAKNESS, PotionEffectType.POISON, PotionEffectType.WITHER,
            PotionEffectType.HEALTH_BOOST, PotionEffectType.ABSORPTION, PotionEffectType.GLOWING,
            PotionEffectType.LEVITATION, PotionEffectType.LUCK, PotionEffectType.UNLUCK,
            PotionEffectType.SLOW_FALLING, PotionEffectType.CONDUIT_POWER, PotionEffectType.DOLPHINS_GRACE,
            PotionEffectType.BAD_OMEN, PotionEffectType.HERO_OF_THE_VILLAGE, PotionEffectType.DARKNESS,
            PotionEffectType.WIND_CHARGED, PotionEffectType.WEAVING, PotionEffectType.OOZING, PotionEffectType.INFESTED
    );

    // Effets "marquants" qui ruinent le jeu et justifient un suicide stratégique
    private static final Set<PotionEffectType> MARKED_EFFECTS = Set.of(
            PotionEffectType.GLOWING, PotionEffectType.POISON, PotionEffectType.WITHER,
            PotionEffectType.WEAKNESS, PotionEffectType.LEVITATION, PotionEffectType.SLOW_FALLING,
            PotionEffectType.INVISIBILITY, PotionEffectType.BLINDNESS, PotionEffectType.NAUSEA,
            PotionEffectType.WIND_CHARGED, PotionEffectType.WEAVING, PotionEffectType.OOZING,
            PotionEffectType.INFESTED
    );

    private final JavaPlugin plugin;
    private final UUID targetId;
    private final Random random = new Random();
    private BukkitTask task;

    private PotionEffectType currentEffect;
    private boolean immune = false;

    public UnstableLotteryBehavior(JavaPlugin plugin, UUID targetId) {
        this.plugin = plugin;
        this.targetId = targetId;
    }

    @Override
    public void onStart(Player player) {
        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            Player p = Bukkit.getPlayer(targetId);
            if (p == null || !p.isOnline()) return;
            if (immune) return;

            rollNewEffect(p);
        }, 0L, CYCLE_TICKS);
    }

    @Override
    public void onStop(Player player) {
        if (task != null) task.cancel();
        clearCurrentEffect(player);
    }

    private void rollNewEffect(Player player) {
        clearCurrentEffect(player);

        currentEffect = EFFECT_POOL.get(random.nextInt(EFFECT_POOL.size()));

        // Explication des booléens :
        // 1er false : Ambient (désactive la translucidité type "balise")
        // 2e false  : Particles (DÉSACTIVE TOTALEMENT LES TOURBILLONS VISIBLES)
        // 3e true   : Icon (garde l'icône en haut à droite de l'écran pour le joueur maudit)
        PotionEffect effect = new PotionEffect(currentEffect, (int) CYCLE_TICKS + 20, 1, false, false, true);

        // Le 'true' final force l'application et écrase toute ancienne potion récalcitrante
        player.addPotionEffect(effect, true);
    }

    private void clearCurrentEffect(Player player) {
        if (currentEffect != null && player != null) {
            player.removePotionEffect(currentEffect);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        if (!victim.getUniqueId().equals(targetId)) return;
        if (immune) return;

        if (currentEffect != null && MARKED_EFFECTS.contains(currentEffect)) {
            immune = true;
            currentEffect = null;
        }
    }
}
