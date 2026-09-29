package fr.siglah.secretCurse.infrastructure.paper.listener.mining;

import fr.siglah.secretCurse.infrastructure.paper.listener.CurseBehavior;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

/**
 * Fournaise Intérieure : tout minerai brut et toute nourriture crue obtenus
 * (minés ou ramassés) sont instantanément convertis en leur version
 * cuite/raffinée. Le raccroc au ramassage (plutôt qu'au minage) couvre à la
 * fois les blocs minés ET la viande crue lâchée par un animal tué.
 */
public class InnerFurnaceBehavior implements CurseBehavior {
    private final UUID targetId;

    private static final Map<Material, Material> RAW_TO_REFINED = new EnumMap<>(Material.class);

    static {
        RAW_TO_REFINED.put(Material.RAW_IRON, Material.IRON_INGOT);
        RAW_TO_REFINED.put(Material.RAW_GOLD, Material.GOLD_INGOT);
        RAW_TO_REFINED.put(Material.RAW_COPPER, Material.COPPER_INGOT);

        RAW_TO_REFINED.put(Material.BEEF, Material.COOKED_BEEF);
        RAW_TO_REFINED.put(Material.PORKCHOP, Material.COOKED_PORKCHOP);
        RAW_TO_REFINED.put(Material.CHICKEN, Material.COOKED_CHICKEN);
        RAW_TO_REFINED.put(Material.MUTTON, Material.COOKED_MUTTON);
        RAW_TO_REFINED.put(Material.RABBIT, Material.COOKED_RABBIT);
        RAW_TO_REFINED.put(Material.COD, Material.COOKED_COD);
        RAW_TO_REFINED.put(Material.SALMON, Material.COOKED_SALMON);
        RAW_TO_REFINED.put(Material.POTATO, Material.BAKED_POTATO);
    }

    public InnerFurnaceBehavior(JavaPlugin plugin, UUID targetId) {
        this.targetId = targetId;
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!player.getUniqueId().equals(targetId)) return;

        ItemStack stack = event.getItem().getItemStack();
        Material refined = RAW_TO_REFINED.get(stack.getType());
        if (refined != null) {
            stack.setType(refined);
        }
    }
}
