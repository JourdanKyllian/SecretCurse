package fr.siglah.secretCurse.presentation.gui;

import fr.siglah.secretCurse.application.GameManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Regroupe les dépendances dont un menu peut avoir besoin :
 * - guiManager : pour naviguer vers un autre menu (ouvrir/fermer).
 * - gameManager : pour déclencher les actions de jeu (démarrer une malédiction, etc.).
 * - plugin : nécessaire pour planifier des tâches (ex : l'animation de la roulette).
 */
public record GuiContext(GuiManager guiManager, GameManager gameManager, JavaPlugin plugin) {
}
