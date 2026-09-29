package fr.siglah.secretCurse.infrastructure.paper.listener.deathrace;

import fr.siglah.secretCurse.domain.DeathCondition;
import org.bukkit.entity.AbstractSkeleton;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Détermine si les derniers dégâts subis par un joueur correspondent
 * précisément à une {@link DeathCondition} donnée. Centralise toute la
 * logique Bukkit (DamageCause, type d'entité attaquante...) pour garder le
 * domaine indépendant de l'API Paper — même principe que GuiIcons pour les
 * Material.
 * <p>
 * LIMITES CONNUES : CACTUS s'appuie sur la cause générique CONTACT (qui
 * couvre aussi les buissons de baies sucrées) ; on ne peut pas la distinguer
 * à 100% sans inspecter le bloc exact au moment du dégât.
 */
public final class DeathCauseMapper {

    private DeathCauseMapper() {
    }

    public static boolean matches(DeathCondition condition, EntityDamageEvent lastDamage) {
        EntityDamageEvent.DamageCause cause = lastDamage.getCause();

        return switch (condition) {
            case LAVA -> cause == EntityDamageEvent.DamageCause.LAVA;
            case FALL_DAMAGE -> cause == EntityDamageEvent.DamageCause.FALL;
            case DROWNING -> cause == EntityDamageEvent.DamageCause.DROWNING;
            case FIRE -> cause == EntityDamageEvent.DamageCause.FIRE
                    || cause == EntityDamageEvent.DamageCause.FIRE_TICK;
            case STARVATION -> cause == EntityDamageEvent.DamageCause.STARVATION;
            case SUFFOCATION -> cause == EntityDamageEvent.DamageCause.SUFFOCATION;
            case CACTUS -> cause == EntityDamageEvent.DamageCause.CONTACT;

            case STALACTITE -> lastDamage instanceof EntityDamageByEntityEvent e
                    && e.getDamager() instanceof FallingBlock fb
                    && fb.getBlockData().getMaterial().name().equals("POINTED_DRIPSTONE");

            case ZOMBIE -> cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK
                    && lastDamage instanceof EntityDamageByEntityEvent e
                    && e.getDamager() instanceof Zombie;

            case SKELETON_ARROW -> lastDamage instanceof EntityDamageByEntityEvent e
                    && e.getDamager() instanceof Arrow arrow
                    && arrow.getShooter() instanceof AbstractSkeleton;

            case ANVIL -> lastDamage instanceof EntityDamageByEntityEvent e
                    && e.getDamager() instanceof FallingBlock fb
                    && fb.getBlockData().getMaterial().name().contains("ANVIL");

            // Un vrai creeper attaque via EntityDamageByEntityEvent avec lui-même comme
            // "damager". Ça exclut naturellement l'explosion de notre propre curse
            // Syndrome du Creeper, dont le damager est le joueur lui-même, pas un Creeper.
            case CREEPER_EXPLOSION -> (cause == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION
                    || cause == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)
                    && lastDamage instanceof EntityDamageByEntityEvent e
                    && e.getDamager() instanceof Creeper;
        };
    }
}
