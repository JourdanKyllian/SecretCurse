package fr.siglah.secretCurse.infrastructure.paper.listener.camera;

import fr.siglah.secretCurse.infrastructure.paper.listener.CurseBehavior;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.UUID;

/**
 * Empreinte de l'Araignée : quand le joueur est accroupi et collé contre un
 * mur solide (dans la direction où il regarde), il grimpe le long de ce mur
 * comme une araignée. Les dégâts de chute sont annulés pendant l'ascension.
 */
public class SpiderImprintBehavior implements CurseBehavior {
    private static final double CLIMB_SPEED = 0.18;
    private static final double WALL_CHECK_DISTANCE = 0.4;

    private final JavaPlugin plugin;
    private final UUID targetId;
    private BukkitTask task;
    private boolean currentlyClimbing = false;

    public SpiderImprintBehavior(JavaPlugin plugin, UUID targetId) {
        this.plugin = plugin;
        this.targetId = targetId;
    }

    @Override
    public void onStart(Player player) {
        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            Player p = Bukkit.getPlayer(targetId);
            if (p == null || !p.isOnline() || p.isDead()) {
                currentlyClimbing = false;
                return;
            }

            if (p.isSneaking() && isFacingWall(p)) {
                Vector velocity = p.getVelocity();
                p.setVelocity(new Vector(velocity.getX(), CLIMB_SPEED, velocity.getZ()));
                p.setFallDistance(0f);
                currentlyClimbing = true;
            } else {
                currentlyClimbing = false;
            }
        }, 2L, 2L);
    }

    @Override
    public void onStop(Player player) {
        if (task != null) task.cancel();
        currentlyClimbing = false;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!player.getUniqueId().equals(targetId)) return;
        if (currentlyClimbing && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
        }
    }

    private boolean isFacingWall(Player player) {
        Location eye = player.getEyeLocation();
        Vector direction = eye.getDirection().setY(0).normalize();
        Location checkPoint = eye.clone().add(direction.multiply(WALL_CHECK_DISTANCE));
        Block block = checkPoint.getBlock();
        return block.getType().isSolid();
    }
}
