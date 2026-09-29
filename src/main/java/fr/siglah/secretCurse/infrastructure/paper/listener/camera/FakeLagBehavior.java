package fr.siglah.secretCurse.infrastructure.paper.listener.camera;

import fr.siglah.secretCurse.infrastructure.paper.listener.CurseBehavior;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Random;
import java.util.UUID;

/**
 * Faux Lag : toutes les 30 à 60 secondes (aléatoire), le joueur est figé
 * pendant 1,5 à 2 secondes et reste bloqué à sa position de départ du freeze
 * (tout mouvement pendant cette fenêtre est annulé et le renvoie sur place).
 */
public class FakeLagBehavior implements CurseBehavior {
    private final JavaPlugin plugin;
    private final UUID targetId;
    private final Random random = new Random();

    private BukkitTask nextTriggerTask;
    private BukkitTask unfreezeTask;
    private Location frozenLocation;
    private boolean frozen = false;

    public FakeLagBehavior(JavaPlugin plugin, UUID targetId) {
        this.plugin = plugin;
        this.targetId = targetId;
    }

    @Override
    public void onStart(Player player) {
        scheduleNextFreeze();
    }

    @Override
    public void onStop(Player player) {
        if (nextTriggerTask != null) nextTriggerTask.cancel();
        if (unfreezeTask != null) unfreezeTask.cancel();
        frozen = false;
    }

    private void scheduleNextFreeze() {
        long delayTicks = randomTicksBetweenSeconds(30, 60);
        nextTriggerTask = Bukkit.getScheduler().runTaskLater(plugin, this::triggerFreeze, delayTicks);
    }

    private void triggerFreeze() {
        Player player = Bukkit.getPlayer(targetId);
        if (player == null || !player.isOnline() || player.isDead()) {
            scheduleNextFreeze();
            return;
        }

        frozenLocation = player.getLocation().clone();
        frozen = true;

        long freezeDuration = randomTicksBetweenMillis(1500, 2000);
        unfreezeTask = Bukkit.getScheduler().runTaskLater(plugin, () -> {
            frozen = false;
            scheduleNextFreeze();
        }, freezeDuration);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!frozen) return;
        if (!event.getPlayer().getUniqueId().equals(targetId)) return;
        event.setTo(frozenLocation.clone());
    }

    private long randomTicksBetweenSeconds(int minSeconds, int maxSeconds) {
        int seconds = minSeconds + random.nextInt(maxSeconds - minSeconds + 1);
        return seconds * 20L;
    }

    private long randomTicksBetweenMillis(int minMillis, int maxMillis) {
        int millis = minMillis + random.nextInt(maxMillis - minMillis + 1);
        return millis / 50L; // 1 tick = 50ms
    }
}
