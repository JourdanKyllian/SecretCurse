package fr.siglah.secretCurse.presentation.gui.curse;

import fr.siglah.secretCurse.domain.CurseCategory;
import fr.siglah.secretCurse.domain.CurseType;
import fr.siglah.secretCurse.presentation.gui.GuiContext;
import fr.siglah.secretCurse.presentation.gui.GuiIcons;
import fr.siglah.secretCurse.presentation.gui.GuiUtils;
import fr.siglah.secretCurse.presentation.gui.ItemBuilder;
import fr.siglah.secretCurse.presentation.gui.Menu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.List;

/**
 * Étape 3 du mode "Malédiction Cachée" : confirmation finale avant de lancer
 * réellement le défi (que la contrainte vienne d'une sélection manuelle ou
 * du tirage aléatoire).
 *
 * NOTE : pour l'instant, le joueur qui clique "Accepter" devient le "hider"
 * (celui qui cache la malédiction), comme dans le flux /defi existant.
 * Si tu veux qu'un Maître du Jeu choisisse la cible pour un AUTRE joueur,
 * il faudra ajouter une étape de sélection de joueur avant cet écran
 * (ex: un menu listant les joueurs en ligne).
 */
public class ConfirmCurseMenu implements Menu {

    private static final int SIZE = 27;
    private static final int SLOT_ACCEPT = 11;
    private static final int SLOT_CURSE_ITEM = 13;
    private static final int SLOT_REFUSE = 15;

    private final GuiContext ctx;
    private final CurseType curse;
    private final boolean isRandom;
    private final CurseCategory originCategory; // null si on vient du choix aléatoire
    private final Inventory inventory;

    public ConfirmCurseMenu(GuiContext ctx, CurseType curse, boolean isRandom, CurseCategory originCategory) {
        this.ctx = ctx;
        this.curse = curse;
        this.isRandom = isRandom;
        this.originCategory = originCategory;
        this.inventory = Bukkit.createInventory(null, SIZE,
                Component.text("Confirmer la Malédiction", NamedTextColor.LIGHT_PURPLE));
        build();
    }

    private void build() {
        GuiUtils.fillBorder(inventory, Material.PURPLE_STAINED_GLASS_PANE);

        inventory.setItem(SLOT_CURSE_ITEM, new ItemBuilder(GuiIcons.iconFor(curse))
                .name(curse.getDisplayName(), NamedTextColor.YELLOW)
                .lore(List.of(Component.text(curse.getDescription(), NamedTextColor.GRAY)))
                .build());

        inventory.setItem(SLOT_ACCEPT, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE)
                .name("✔ Accepter", NamedTextColor.GREEN)
                .lore(List.of(Component.text("Lance le défi avec cette contrainte.", NamedTextColor.GRAY)))
                .build());

        inventory.setItem(SLOT_REFUSE, new ItemBuilder(Material.RED_STAINED_GLASS_PANE)
                .name("✘ Refuser", NamedTextColor.RED)
                .lore(List.of(Component.text("Retour au choix précédent.", NamedTextColor.GRAY)))
                .build());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void handleClick(InventoryClickEvent event, Player player) {
        int slot = event.getRawSlot();

        if (slot == SLOT_ACCEPT) {
            boolean started = ctx.gameManager().startGame(player.getUniqueId(), curse);
            ctx.guiManager().forget(player.getUniqueId());
            player.closeInventory();

            if (!started) {
                player.sendMessage(Component.text(
                        "Une malédiction est déjà en cours ! Utilisez /defi gg ou /defi perdu pour l'arrêter.",
                        NamedTextColor.RED));
            }
            return;
        }

        if (slot == SLOT_REFUSE) {
            if (isRandom || originCategory == null) {
                ctx.guiManager().open(player, new CurseCategoryMenu(ctx));
            } else {
                ctx.guiManager().open(player, new CurseListMenu(ctx, originCategory));
            }
        }
    }
}
