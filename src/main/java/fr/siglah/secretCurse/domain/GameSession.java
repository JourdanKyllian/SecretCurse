package fr.siglah.secretCurse.domain;

import java.util.UUID;

public class GameSession {
    private final UUID hiderId; // L'identifiant du joueur qui cache la malédiction
    private CurseType activeCurse;
    private boolean isRunning;

    public GameSession(UUID hiderId) {
        this.hiderId = hiderId;
        this.isRunning = false;
    }

    public void startCurse(CurseType curse) {
        this.activeCurse = curse;
        this.isRunning = true;
    }

    public void stopCurse() {
        this.isRunning = false;
    }

    public UUID getHiderId() {
        return hiderId;
    }

    public CurseType getActiveCurse() {
        return activeCurse;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
