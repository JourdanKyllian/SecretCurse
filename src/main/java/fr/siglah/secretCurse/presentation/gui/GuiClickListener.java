package fr.siglah.secretCurse.presentation.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

/**
 * Écoute globale des inventaires : dès qu'un joueur a un Menu SecretCurse
 * ouvert (selon GuiManager), on annule systématiquement le clic (empêche
 * tout vol/déplacement d'item), puis on délègue au Menu concerné si le clic
 * a bien eu lieu dans l'inventaire du haut (celui du menu, pas celui du joueur).
 */
public class GuiClickListener implements Listener {

    private final GuiManager guiManager;

    public GuiClickListener(GuiManager guiManager) {
        this.guiManager = guiManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        Menu menu = guiManager.getOpenMenu(player.getUniqueId());
        if (menu == null) {
            return; // Le joueur clique dans un inventaire qui n'est pas géré par SecretCurse
        }

        event.setCancelled(true);

        int topInventorySize = event.getView().getTopInventory().getSize();
        if (event.getRawSlot() >= 0 && event.getRawSlot() < topInventorySize) {
            menu.handleClick(event, player);
        }
        // Si le rawSlot dépasse la taille du top inventory, le clic a eu lieu
        // dans l'inventaire du joueur : on l'a déjà annulé, rien d'autre à faire.
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) {
            return;
        }
        guiManager.handleClose(player, event.getInventory());
    }
}
