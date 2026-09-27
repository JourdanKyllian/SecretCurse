package fr.siglah.secretCurse.presentation.command;

import fr.siglah.secretCurse.application.GameManager;
import fr.siglah.secretCurse.presentation.gui.GuiContext;
import fr.siglah.secretCurse.presentation.gui.GuiManager;
import fr.siglah.secretCurse.presentation.gui.MainMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class DefiCommand implements CommandExecutor {
    private final GameManager gameManager;
    private final GuiManager guiManager;
    private final JavaPlugin plugin;

    // On injecte les dépendances pour que la commande puisse contrôler le jeu et ouvrir le GUI
    public DefiCommand(GameManager gameManager, GuiManager guiManager, JavaPlugin plugin) {
        this.gameManager = gameManager;
        this.guiManager = guiManager;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // Sécurité : on vérifie que c'est bien un joueur qui tape la commande
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Seul un joueur en jeu peut utiliser cette commande.");
            return true;
        }

        // Raccourcis : /dm, /dg, /dp sont enregistrés comme des commandes à part entière
        // dans plugin.yml, mais partagent ce même executor. On regarde le label utilisé
        // (celui tapé par le joueur) pour savoir si on doit court-circuiter la logique
        // habituelle basée sur args[0].
        if (label.equalsIgnoreCase("dm")) {
            guiManager.open(player, new MainMenu(new GuiContext(guiManager, gameManager, plugin)));
            return true;
        }
        if (label.equalsIgnoreCase("dg")) {
            gameManager.stopGame(true);
            return true;
        }
        if (label.equalsIgnoreCase("dp")) {
            gameManager.stopGame(false);
            return true;
        }

        // Commande : /defi menu (ouvre le tableau de bord du Maître du Jeu)
        if (args.length > 0 && args[0].equalsIgnoreCase("menu")) {
            guiManager.open(player, new MainMenu(new GuiContext(guiManager, gameManager, plugin)));
            return true;
        }

        // Commande de base : /defi (raccourci historique : lance un défi aléatoire directement, sans passer par le GUI)
        if (args.length == 0) {
            boolean started = gameManager.startGame(player.getUniqueId(), null);
            if (!started) {
                player.sendMessage("§cUne malédiction est déjà en cours ! Utilisez /defi gg ou /defi perdu pour l'arrêter.");
            }
            return true;
        }

        // Commande : /defi gg (Le chercheur a trouvé la bonne malédiction)
        if (args[0].equalsIgnoreCase("gg")) {
            gameManager.stopGame(true);
            return true;
        }

        // Commande : /defi perdu (Le chercheur s'est trompé sur son unique essai)
        if (args[0].equalsIgnoreCase("perdu")) {
            gameManager.stopGame(false);
            return true;
        }

        return false;
    }
}
