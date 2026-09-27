package fr.siglah.secretCurse.presentation.gui.death;

import fr.siglah.secretCurse.presentation.gui.GuiContext;
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

/**
 * Écran de confirmation du mode "Qui mourra le premier ?" avant de lancer
 * l'animation de la roue.
 */
public class DeathRaceConfirmMenu implements Menu {
    private static final int SIZE = 27;
    private static final int SLOT_ACCEPT = 11;
    private static final int SLOT_INFO = 13;
    private static final int SLOT_REFUSE = 15;

    private final GuiContext ctx;
    private final Inventory inventory;

    public DeathRaceConfirmMenu(GuiContext ctx) {
        this.ctx = ctx;
        this.inventory = Bukkit.createInventory(null, SIZE,
                Component.text("Qui Mourra Le Premier ?", NamedTextColor.RED));
        build();
    }

    private void build() {
        GuiUtils.fillBorder(inventory, Material.RED_STAINED_GLASS_PANE);

        inventory.setItem(SLOT_INFO, new ItemBuilder(Material.TOTEM_OF_UNDYING)
                .name("La roue va désigner une mort", NamedTextColor.YELLOW)
                .lore(List.of(
                        Component.text("Le premier joueur qui meurt de", NamedTextColor.GRAY),
                        Component.text("cette façon précise remporte la manche.", NamedTextColor.GRAY)
                ))
                .build());

        inventory.setItem(SLOT_ACCEPT, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE)
                .name("✔ Lancer la roue", NamedTextColor.GREEN)
                .build());

        inventory.setItem(SLOT_REFUSE, new ItemBuilder(Material.RED_STAINED_GLASS_PANE)
                .name("✘ Annuler", NamedTextColor.RED)
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
            DeathRouletteMenu roulette = new DeathRouletteMenu(ctx);
            ctx.guiManager().open(player, roulette);
            roulette.startSpin();
            return;
        }

        if (slot == SLOT_REFUSE) {
            ctx.guiManager().open(player, new MainMenu(ctx));
        }
    }
}
