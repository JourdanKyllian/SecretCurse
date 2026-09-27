package fr.siglah.secretCurse.application;

import fr.siglah.secretCurse.domain.DeathCondition;
import fr.siglah.secretCurse.domain.port.CurseHandlerPort;
import fr.siglah.secretCurse.domain.CurseType;
import fr.siglah.secretCurse.domain.GameSession;

import java.util.Random;
import java.util.UUID;

public class GameManager {
    private GameSession currentSession;
    private final CurseHandlerPort curseHandler;
    private final Random random;

    // Le GameManager reçoit son Port via le constructeur (Injection de dépendance)
    public GameManager(CurseHandlerPort curseHandler) {
        this.curseHandler = curseHandler;
        this.random = new Random();
    }

    public boolean startGame(UUID hiderId, CurseType specificCurse) {
        if (currentSession != null && currentSession.isRunning()) {
            return false; // Une partie est déjà en cours
        }

        // On instancie la session
        currentSession = new GameSession(hiderId);

        // Si aucun défi n'est précisé, on en pioche un au hasard
        CurseType curseToPlay = (specificCurse != null) ? specificCurse : pickRandomCurse();
        currentSession.startCurse(curseToPlay);

        // On délègue l'affichage et l'activation à l'infrastructure via le port
        curseHandler.sendPrivateBriefing(hiderId, curseToPlay);
        curseHandler.enableCurse(curseToPlay, hiderId);

        return true;
    }

    public void stopGame(boolean guessedCorrectly) {
        if (currentSession == null || !currentSession.isRunning()) {
            return;
        }

        CurseType activeCurse = currentSession.getActiveCurse();
        UUID hiderId = currentSession.getHiderId();

        // On arrête la logique
        currentSession.stopCurse();
        curseHandler.disableCurse();

        // On annonce le résultat au serveur
        if (guessedCorrectly) {
            curseHandler.announceSuccess(hiderId, activeCurse);
        } else {
            curseHandler.announceFailure(hiderId, activeCurse);
        }

        currentSession = null;
    }

    /**
     * Point d'entrée du mode "Qui mourra le premier ?" : appelé une fois que
     * la roue s'est arrêtée sur une DeathCondition.
     * <p>
     * TODO : brancher ici un Listener (ex: EntityDamageEvent / PlayerDeathEvent,
     * côté infrastructure.paper, sur le même modèle que PaperCurseHandlerAdapter)
     * qui détecte quel joueur meurt le premier de la façon désignée, et déclare
     * le vainqueur. Pour l'instant, ce point d'entrée est fonctionnel mais
     * n'active pas encore la détection en jeu.
     */
    public void startDeathRaceChallenge(DeathCondition condition) {
        // Volontairement minimal pour le moment (cf. TODO ci-dessus).
    }

    private CurseType pickRandomCurse() {
        CurseType[] curses = CurseType.values();
        return curses[random.nextInt(curses.length)];
    }

    public GameSession getCurrentSession() {
        return currentSession;
    }
}
