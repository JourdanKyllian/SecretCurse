package fr.siglah.secretCurse.presentation.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class GuiUtils {

    private GuiUtils() {
    }

    /** Remplit le contour d'un inventaire (première/dernière ligne + colonnes de bord) avec un item décoratif. */
    public static void fillBorder(Inventory inventory, Material material) {
        ItemStack filler = new ItemBuilder(material).name(Component.text(" ")).build();
        int size = inventory.getSize();
        int rows = size / 9;

        for (int i = 0; i < size; i++) {
            int row = i / 9;
            int col = i % 9;
            if (row == 0 || row == rows - 1 || col == 0 || col == 8) {
                inventory.setItem(i, filler);
            }
        }
    }

    /** Remplit toutes les cases encore vides (utile après avoir posé les items "fonctionnels"). */
    public static void fillEmpty(Inventory inventory, Material material) {
        ItemStack filler = new ItemBuilder(material).name(Component.text(" ")).build();
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, filler);
            }
        }
    }
}
