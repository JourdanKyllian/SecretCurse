package fr.siglah.secretCurse.presentation.gui.death;

import fr.siglah.secretCurse.domain.DeathCondition;
import fr.siglah.secretCurse.presentation.gui.GuiContext;
import fr.siglah.secretCurse.presentation.gui.GuiIcons;
import fr.siglah.secretCurse.presentation.gui.GuiUtils;
import fr.siglah.secretCurse.presentation.gui.ItemBuilder;
import fr.siglah.secretCurse.presentation.gui.Menu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.Random;

/**
 * Animation façon "machine à sous" : un seul slot change d'item de plus en
 * plus lentement (délais croissants entre chaque tick), jusqu'à se figer sur
 * la DeathCondition tirée au sort. Aucune interaction du joueur n'est
 * possible pendant/après le tirage : le résultat est entièrement automatique.
 */
public class DeathRouletteMenu implements Menu {

    private static final int SIZE = 9;
    private static final int SLOT_SPIN = 4;

    // Délais croissants (en ticks) entre chaque changement d'item : effet de ralentissement progressif.
    private static final int[] SPIN_DELAYS = {2, 2, 2, 2, 3, 3, 3, 4, 4, 5, 6, 7, 9, 11, 14, 18, 23, 28};
    private static final long AUTO_CLOSE_DELAY_TICKS = 60L; // 3 secondes après l'affichage du résultat

    private final GuiContext ctx;
    private final Inventory inventory;
    private final Random random = new Random();
    private DeathCondition result;

    public DeathRouletteMenu(GuiContext ctx) {
        this.ctx = ctx;
        this.inventory = Bukkit.createInventory(null, SIZE,
                Component.text("La Roue du Destin...", NamedTextColor.RED));
        GuiUtils.fillEmpty(inventory, Material.BLACK_STAINED_GLASS_PANE);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void handleClick(InventoryClickEvent event, Player player) {
        // Volontairement vide : le tirage est automatique, aucune action joueur n'est attendue ici.
        // Le clic est de toute façon déjà annulé par le GuiClickListener.
    }

    /** Démarre l'animation. À appeler juste après avoir ouvert ce menu au joueur. */
    public void startSpin() {
        DeathCondition[] all = DeathCondition.values();
        result = all[random.nextInt(all.length)];
        scheduleTick(all, 0);
    }

    private void scheduleTick(DeathCondition[] all, int step) {
        if (step >= SPIN_DELAYS.length) {
            landOnResult();
            return;
        }

        DeathCondition shown = all[random.nextInt(all.length)];
        inventory.setItem(SLOT_SPIN, new ItemBuilder(GuiIcons.iconFor(shown))
                .name(shown.getDisplayName(), NamedTextColor.YELLOW)
                .build());
        playTickSound();

        Bukkit.getScheduler().runTaskLater(ctx.plugin(), () -> scheduleTick(all, step + 1), SPIN_DELAYS[step]);
    }

    private void landOnResult() {
        inventory.setItem(SLOT_SPIN, new ItemBuilder(GuiIcons.iconFor(result))
                .name("☠ " + result.getDisplayName(), NamedTextColor.RED)
                .lore(List.of(Component.text(result.getDescription(), NamedTextColor.GRAY)))
                .build());

        for (HumanEntity viewer : inventory.getViewers()) {
            if (viewer instanceof Player player) {
                player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 0.5f, 1.5f);
                player.sendMessage(Component.text("💀 La mort désignée : ", NamedTextColor.GOLD)
                        .append(Component.text(result.getDisplayName(), NamedTextColor.RED)));
            }
        }

        // Point d'entrée pour brancher la suite du mode de jeu (voir GameManager#startDeathRaceChallenge).
        ctx.gameManager().startDeathRaceChallenge(result);

        // Laisse le temps de lire le résultat avant de refermer automatiquement le menu.
        Bukkit.getScheduler().runTaskLater(ctx.plugin(), () -> {
            for (HumanEntity viewer : List.copyOf(inventory.getViewers())) {
                viewer.closeInventory();
            }
        }, AUTO_CLOSE_DELAY_TICKS);
    }

    private void playTickSound() {
        for (HumanEntity viewer : inventory.getViewers()) {
            if (viewer instanceof Player player) {
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.6f, 1.2f);
            }
        }
    }
}
