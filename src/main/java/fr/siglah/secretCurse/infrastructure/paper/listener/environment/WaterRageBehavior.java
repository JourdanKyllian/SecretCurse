package fr.siglah.secretCurse.infrastructure.paper.listener.environment;

import fr.siglah.secretCurse.infrastructure.paper.listener.CurseBehavior;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

/**
 * Rage Hydrique : tout contact avec l'eau (immersion totale OU partielle —
 * les pieds dans une rivière suffisent) déclenche une suffocation continue
 * tant que le contact dure.
 * <p>
 * Volontairement implémenté en forçant l'air restant à une valeur négative
 * plutôt qu'en infligeant du Poison : ça déclenche le VRAI mécanisme de
 * noyade de Minecraft, donc la cause de dégâts est authentiquement
 * DROWNING — le message de mort dans le chat affiche "s'est noyé" et pas
 * "a été empoisonné", ce qui était l'objectif.
 */
public class WaterRageBehavior implements CurseBehavior {
    private final JavaPlugin plugin;
    private final UUID targetId;
    private BukkitTask task;

    public WaterRageBehavior(JavaPlugin plugin, UUID targetId) {
        this.plugin = plugin;
        this.targetId = targetId;
    }

    @Override
    public void onStart(Player player) {
        // Vérification toutes les demi-secondes (10 ticks)
        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            Player p = Bukkit.getPlayer(targetId);
            if (p == null || !p.isOnline() || p.isDead()) return;

            if (p.isInWater()) {
                // Air négatif maintenu en continu : le tick vanilla applique
                // les dégâts de noyade à intervalle régulier tant que c'est négatif,
                // indépendamment du fait que la tête soit réellement immergée ou non.
                p.setRemainingAir(-20);
            } else {
                // Restauration immédiate dès la sortie de l'eau : la contrainte
                // punit le CONTACT, pas un compteur d'air qui traînerait après coup.
                if (p.getRemainingAir() < p.getMaximumAir()) {
                    p.setRemainingAir(p.getMaximumAir());
                }
            }
        }, 10L, 10L);
    }

    @Override
    public void onStop(Player player) {
        if (task != null) task.cancel();
        if (player != null && player.isOnline()) {
            player.setRemainingAir(player.getMaximumAir());
        }
    }
}
