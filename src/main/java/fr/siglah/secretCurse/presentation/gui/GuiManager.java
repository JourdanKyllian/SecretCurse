package fr.siglah.secretCurse.presentation.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tient le registre "quel joueur a quel menu SecretCurse ouvert".
 * Sert à la fois à ouvrir/naviguer entre menus et à router les clics
 * depuis le GuiClickListener vers le bon Menu.
 */
public class GuiManager {

    private final Map<UUID, Menu> openMenus = new ConcurrentHashMap<>();

    /**
     * Ouvre un menu pour un joueur. Si un autre menu SecretCurse était déjà
     * ouvert, on le remplace directement : Bukkit fermera l'ancien inventaire
     * et ouvrira le nouveau. Comme la map est mise à jour AVANT l'appel à
     * openInventory(), le InventoryCloseEvent généré pour l'ancien inventaire
     * ne correspondra plus à ce qui est tracké : on ne perd donc pas l'état
     * pendant une navigation (voir GuiManager#handleClose).
     */
    public void open(Player player, Menu menu) {
        openMenus.put(player.getUniqueId(), menu);
        player.openInventory(menu.getInventory());
    }

    public Menu getOpenMenu(UUID playerId) {
        return openMenus.get(playerId);
    }

    /** Retire manuellement un joueur du registre (ex : après validation d'une action qui ferme le menu). */
    public void forget(UUID playerId) {
        openMenus.remove(playerId);
    }

    /**
     * À appeler depuis InventoryCloseEvent. Ne retire le joueur du registre
     * que si l'inventaire fermé est bien celui actuellement suivi pour lui —
     * ce qui exclut les fermetures "techniques" dues à une navigation interne.
     */
    public void handleClose(Player player, Inventory closedInventory) {
        Menu current = openMenus.get(player.getUniqueId());
        if (current != null && current.getInventory().equals(closedInventory)) {
            current.onClose(player);
            openMenus.remove(player.getUniqueId());
        }
    }
}
