package fr.siglah.secretCurse.presentation.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

/**
 * Contrat commun à tous les écrans du GUI SecretCurse.
 * Chaque implémentation construit son propre {@link Inventory} et sait
 * réagir à un clic dans cet inventaire (navigation, action de jeu, etc.).
 */
public interface Menu {

    Inventory getInventory();

    void handleClick(InventoryClickEvent event, Player player);

    /**
     * Appelé quand le joueur ferme réellement ce menu (Échap, sans naviguer
     * vers un autre menu SecretCurse). Optionnel : à surcharger si un menu
     * a besoin de nettoyer une tâche (ex : arrêter une animation).
     */
    default void onClose(Player player) {
    }
}
