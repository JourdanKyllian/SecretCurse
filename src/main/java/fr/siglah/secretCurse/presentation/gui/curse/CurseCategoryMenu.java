package fr.siglah.secretCurse.presentation.gui.curse;

import fr.siglah.secretCurse.domain.CurseCategory;
import fr.siglah.secretCurse.domain.CurseType;
import fr.siglah.secretCurse.presentation.gui.GuiContext;
import fr.siglah.secretCurse.presentation.gui.GuiIcons;
import fr.siglah.secretCurse.presentation.gui.GuiUtils;
import fr.siglah.secretCurse.presentation.gui.ItemBuilder;
import fr.siglah.secretCurse.presentation.gui.MainMenu;
import fr.siglah.secretCurse.presentation.gui.Menu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.Random;

/**
 * Étape 1 du mode "Malédiction Cachée" : choisir une catégorie de contraintes,
 * ou tirer une contrainte au sort parmi toutes les catégories.
 */
public class CurseCategoryMenu implements Menu {

    private static final int SIZE = 27;
    private static final int SLOT_RANDOM = 8;
    private static final int SLOT_BACK = 22;

    private static final CurseCategory[] CATEGORIES = CurseCategory.values();
    // Ajoute des slots ici si CurseCategory gagne de nouvelles valeurs.
    private static final int[] CATEGORY_SLOTS = {11, 12, 13, 14};

    private final GuiContext ctx;
    private final Inventory inventory;
    private final Random random = new Random();

    public CurseCategoryMenu(GuiContext ctx) {
        this.ctx = ctx;
        this.inventory = Bukkit.createInventory(null, SIZE,
                Component.text("Malédiction Cachée - Catégories", NamedTextColor.LIGHT_PURPLE));
        build();
    }

    private void build() {
        GuiUtils.fillBorder(inventory, Material.PURPLE_STAINED_GLASS_PANE);

        for (int i = 0; i < CATEGORIES.length && i < CATEGORY_SLOTS.length; i++) {
            CurseCategory category = CATEGORIES[i];
            inventory.setItem(CATEGORY_SLOTS[i], new ItemBuilder(GuiIcons.iconFor(category))
                    .name(category.getDisplayName(), NamedTextColor.AQUA)
                    .lore(List.of(Component.text("▶ Cliquer pour voir les contraintes", NamedTextColor.YELLOW)))
                    .build());
        }

        inventory.setItem(SLOT_RANDOM, new ItemBuilder(Material.NETHER_STAR)
                .name("Choix Aléatoire", NamedTextColor.GOLD)
                .lore(List.of(
                        Component.text("Une contrainte est tirée au sort", NamedTextColor.GRAY),
                        Component.text("parmi toutes les catégories.", NamedTextColor.GRAY)
                ))
                .build());

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
            ctx.guiManager().open(player, new MainMenu(ctx));
            return;
        }

        if (slot == SLOT_RANDOM) {
            CurseType randomCurse = pickRandomCurse();
            ctx.guiManager().open(player, new ConfirmCurseMenu(ctx, randomCurse, true, null));
            return;
        }

        for (int i = 0; i < CATEGORY_SLOTS.length; i++) {
            if (CATEGORY_SLOTS[i] == slot) {
                ctx.guiManager().open(player, new CurseListMenu(ctx, CATEGORIES[i]));
                return;
            }
        }
    }

    private CurseType pickRandomCurse() {
        CurseType[] all = CurseType.values();
        return all[random.nextInt(all.length)];
    }
}
