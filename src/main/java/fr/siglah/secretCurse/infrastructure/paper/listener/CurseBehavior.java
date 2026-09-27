package fr.siglah.secretCurse.infrastructure.paper.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

/**
 * Contrat pour le comportement d'une malédiction.
 * Évite les fuites de mémoire : on peut l'allumer (onStart) et l'éteindre (onStop).
 * Comme elle étend Listener, l'adaptateur pourra l'enregistrer et la désenregistrer de Bukkit à la volée.
 */
public interface CurseBehavior extends Listener {

    /** Appelé quand la malédiction s'active. Permet de donner des effets, etc. */
    default void onStart(Player player) {}

    /** Appelé quand la malédiction se termine. Permet de nettoyer les effets. */
    default void onStop(Player player) {}
}
