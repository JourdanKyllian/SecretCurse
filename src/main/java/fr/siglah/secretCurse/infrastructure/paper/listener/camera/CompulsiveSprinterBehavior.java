package fr.siglah.secretCurse.infrastructure.paper.listener.camera;

import fr.siglah.secretCurse.infrastructure.paper.listener.CurseBehavior;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.UUID;

/**
 * Sprinteur Compulsif : force le joueur à avancer en continu dans la direction
 * où il regarde, à la vitesse du sprint (sans toucher au clavier).
 * S'accroupir (Sneak) permet de mettre le mouvement en pause temporairement.
 */
public class CompulsiveSprinterBehavior implements CurseBehavior {
    // 0.28 blocs par tick correspond à la vitesse de sprint vanilla de Minecraft
    private static final double SPRINT_SPEED = 0.28;

    private final JavaPlugin plugin;
    private final UUID targetId;
    private BukkitTask task;

    public CompulsiveSprinterBehavior(JavaPlugin plugin, UUID targetId) {
        this.plugin = plugin;
        this.targetId = targetId;
    }

    @Override
    public void onStart(Player player) {
        // Exécuté à chaque tick (toutes les 50ms) pour une fluidité de mouvement parfaite
        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            Player p = Bukkit.getPlayer(targetId);
            if (p == null || !p.isOnline() || p.isDead()) return;

            // On autorise le joueur à s'accroupir pour bloquer la course
            if (p.isSneaking()) return;

            // Force l'état visuel du sprint (particules aux pieds, FOV)
            if (!p.isSprinting()) {
                p.setSprinting(true);
            }

            // Calcule la direction vers l'avant sur le plan horizontal
            Vector direction = p.getLocation().getDirection().setY(0);

            // Sécurité : si le joueur regarde exactement à la verticale, le vecteur horizontal est nul
            if (direction.lengthSquared() > 0) {
                direction.normalize().multiply(SPRINT_SPEED);

                // On conserve la vitesse verticale existante pour que le joueur puisse sauter et subir la gravité
                direction.setY(p.getVelocity().getY());

                // On applique la propulsion physique
                p.setVelocity(direction);
            }
        }, 1L, 1L);
    }

    @Override
    public void onStop(Player player) {
        if (task != null) task.cancel();
    }
}
