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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Étape 2 du mode "Malédiction Cachée" : liste des contraintes appartenant
 * à une catégorie précise. Une seule contrainte est sélectionnable et mène
 * à l'écran de confirmation.
 */
public class CurseListMenu implements Menu {

    private static final int SIZE = 45;
    private static final int SLOT_BACK = 40;
    // Emplacements "utiles" (on évite la bordure) sur les 3 lignes centrales.
    private static final int[] CONTENT_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25
    };

    private final GuiContext ctx;
    private final CurseCategory category;
    private final Inventory inventory;
    private final Map<Integer, CurseType> slotToCurse = new HashMap<>();

    public CurseListMenu(GuiContext ctx, CurseCategory category) {
        this.ctx = ctx;
        this.category = category;
        this.inventory = Bukkit.createInventory(null, SIZE,
                Component.text(category.getDisplayName(), NamedTextColor.AQUA));
        build();
    }

    private void build() {
        GuiUtils.fillBorder(inventory, Material.GRAY_STAINED_GLASS_PANE);

        int slotIndex = 0;
        for (CurseType curse : CurseType.values()) {
            if (curse.getCategory() != category) {
                continue;
            }
            if (slotIndex >= CONTENT_SLOTS.length) {
                break; // Sécurité si une catégorie dépasse un jour la capacité de la grille
            }

            int slot = CONTENT_SLOTS[slotIndex++];
            slotToCurse.put(slot, curse);

            inventory.setItem(slot, new ItemBuilder(GuiIcons.iconFor(curse))
                    .name(curse.getDisplayName(), NamedTextColor.YELLOW)
                    .lore(List.of(
                            Component.text(curse.getDescription(), NamedTextColor.GRAY),
                            Component.text(""),
                            Component.text("▶ Cliquer pour sélectionner", NamedTextColor.GREEN)
                    ))
                    .build());
        }

        inventory.setItem(SLOT_BACK, new ItemBuilder(Material.ARROW)
                .name("Retour", NamedTextColor.RED)
                .build());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void handleClick(InventoryClickEvent event, Player player) {
        int slot = event.getRawSlot();

        if (slot == SLOT_BACK) {
            ctx.guiManager().open(player, new CurseCategoryMenu(ctx));
            return;
        }

        CurseType selected = slotToCurse.get(slot);
        if (selected != null) {
            ctx.guiManager().open(player, new ConfirmCurseMenu(ctx, selected, false, category));
        }
    }
}
