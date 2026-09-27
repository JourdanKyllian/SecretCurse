package fr.siglah.secretCurse.presentation.gui;

import fr.siglah.secretCurse.presentation.gui.curse.CurseCategoryMenu;
import fr.siglah.secretCurse.presentation.gui.death.DeathRaceConfirmMenu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.List;

/**
 * Tableau de bord principal : ouvert par /defi menu.
 * Permet de choisir entre les deux modes de jeu.
 */
public class MainMenu implements Menu {

    private static final int SIZE = 27;
    private static final int SLOT_HIDDEN_CURSE = 11;
    private static final int SLOT_DEATH_RACE = 15;

    private final GuiContext ctx;
    private final Inventory inventory;

    public MainMenu(GuiContext ctx) {
        this.ctx = ctx;
        this.inventory = Bukkit.createInventory(null, SIZE,
                Component.text("SecretCurse - Menu Principal", NamedTextColor.DARK_PURPLE));
        build();
    }

    private void build() {
        GuiUtils.fillBorder(inventory, Material.PURPLE_STAINED_GLASS_PANE);

        inventory.setItem(SLOT_HIDDEN_CURSE, new ItemBuilder(Material.ENDER_EYE)
                .name("Malédiction Cachée", NamedTextColor.LIGHT_PURPLE)
                .lore(List.of(
                        Component.text("Mode cache-cache asymétrique.", NamedTextColor.GRAY),
                        Component.text("L'un cherche, l'autre cache la contrainte.", NamedTextColor.GRAY),
                        Component.text(""),
                        Component.text("▶ Cliquer pour configurer", NamedTextColor.YELLOW)
                ))
                .build());

        inventory.setItem(SLOT_DEATH_RACE, new ItemBuilder(Material.TOTEM_OF_UNDYING)
                .name("Qui Mourra Le Premier ?", NamedTextColor.RED)
                .lore(List.of(
                        Component.text("La roue du destin désigne", NamedTextColor.GRAY),
                        Component.text("une mort précise à réaliser.", NamedTextColor.GRAY),
                        Component.text(""),
                        Component.text("▶ Cliquer pour lancer", NamedTextColor.YELLOW)
                ))
                .build());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void handleClick(InventoryClickEvent event, Player player) {
        int slot = event.getRawSlot();

        if (slot == SLOT_HIDDEN_CURSE) {
            ctx.guiManager().open(player, new CurseCategoryMenu(ctx));
        } else if (slot == SLOT_DEATH_RACE) {
            ctx.guiManager().open(player, new DeathRaceConfirmMenu(ctx));
        }
    }
}
