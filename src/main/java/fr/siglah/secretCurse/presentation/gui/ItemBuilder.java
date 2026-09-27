package fr.siglah.secretCurse.presentation.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Petit builder pour éviter de répéter la même mécanique fastidieuse
 * (récupérer l'ItemMeta, désactiver l'italique par défaut d'Adventure sur
 * les noms/lores, la réappliquer sur l'item...) dans chaque menu.
 */
public final class ItemBuilder {

    private final ItemStack item;
    private final ItemMeta meta;

    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
        this.meta = item.getItemMeta();
    }

    public ItemBuilder name(Component name) {
        meta.displayName(stripItalic(name));
        return this;
    }

    public ItemBuilder name(String plainName, NamedTextColor color) {
        return name(Component.text(plainName, color));
    }

    public ItemBuilder lore(List<Component> lines) {
        List<Component> cleaned = new ArrayList<>(lines.size());
        for (Component line : lines) {
            cleaned.add(stripItalic(line));
        }
        meta.lore(cleaned);
        return this;
    }

    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }

    /** Adventure met les noms/lores en italique par défaut sur les items ; on l'annule pour un rendu propre. */
    private static Component stripItalic(Component component) {
        return component.decoration(TextDecoration.ITALIC, false);
    }
}
