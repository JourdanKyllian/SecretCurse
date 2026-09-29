package fr.siglah.secretCurse.infrastructure.paper.listener.deathrace;

import fr.siglah.secretCurse.domain.DeathCondition;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Contrairement aux CurseBehavior (ciblés sur un seul joueur), ce listener
 * surveille TOUS les joueurs en ligne : le premier qui meurt de la façon
 * désignée par la roue remporte la manche. Se désenregistre automatiquement
 * dès qu'un vainqueur est déclaré.
 */
public class DeathRaceListener implements Listener {
    private final JavaPlugin plugin;
    private final DeathCondition condition;
    private boolean finished = false;

    public DeathRaceListener(JavaPlugin plugin, DeathCondition condition) {
        this.plugin = plugin;
        this.condition = condition;
    }

    public void start() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void stop() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (finished) return;

        Player victim = event.getEntity();
        EntityDamageEvent lastDamage = victim.getLastDamageCause();
        if (lastDamage == null) return;

        if (DeathCauseMapper.matches(condition, lastDamage)) {
            finished = true;
            announceWinner(victim);
            stop();
        }
    }

    private void announceWinner(Player winner) {
        Title title = Title.title(
                Component.text("🏆 " + winner.getName(), NamedTextColor.GOLD),
                Component.text("est mort de la mort désignée : " + condition.getDisplayName(), NamedTextColor.YELLOW)
        );
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.showTitle(title);
        }
    }
}
