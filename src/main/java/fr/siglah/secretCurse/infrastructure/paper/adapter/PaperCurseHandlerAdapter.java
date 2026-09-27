package fr.siglah.secretCurse.infrastructure.paper.adapter;

import fr.siglah.secretCurse.domain.port.CurseHandlerPort;
import fr.siglah.secretCurse.domain.CurseType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class PaperCurseHandlerAdapter implements CurseHandlerPort {
    private final JavaPlugin plugin;

    public PaperCurseHandlerAdapter(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void enableCurse(CurseType type, UUID hiderId) {
        // TODO: Plus tard, c'est ici qu'on instanciera et activera les Listeners spécifiques (ex: dégâts si on sprinte pas)
        plugin.getLogger().info("Malédiction " + type.name() + " activée pour le joueur " + hiderId);
    }

    @Override
    public void disableCurse() {
        // TODO: Plus tard, c'est ici qu'on désactivera le Listener actif
        plugin.getLogger().info("Malédiction désactivée.");
    }

    @Override
    public void sendPrivateBriefing(UUID hiderId, CurseType type) {
        Player hider = Bukkit.getPlayer(hiderId);
        if (hider != null) {
            // Message privé rouge envoyé uniquement au cacheur
            hider.sendMessage(Component.text("🤫 Défi Secret : " + type.name(), NamedTextColor.RED));
        }
    }

    @Override
    public void announceSuccess(UUID hiderId, CurseType type) {
        Component mainTitle = Component.text("DÉFI TROUVÉ !", NamedTextColor.GREEN);
        Component subTitle = Component.text("La malédiction était : " + type.name(), NamedTextColor.YELLOW);
        Title title = Title.title(mainTitle, subTitle);

        // Affichage du grand titre au centre de l'écran pour tous les joueurs
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.showTitle(title);
        }
    }

    @Override
    public void announceFailure(UUID hiderId, CurseType type) {
        Component mainTitle = Component.text("ÉCHEC DU CHERCHEUR", NamedTextColor.RED);
        Component subTitle = Component.text("Le secret était : " + type.name(), NamedTextColor.GRAY);
        Title title = Title.title(mainTitle, subTitle);

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.showTitle(title);
        }
    }
}
