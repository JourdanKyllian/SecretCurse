package fr.siglah.secretCurse.domain.port;

import fr.siglah.secretCurse.domain.CurseType;

import java.util.UUID;

public interface CurseHandlerPort {
    // Gère l'enregistrement/désenregistrement des événements Paper
    void enableCurse(CurseType type, UUID hiderId);
    void disableCurse();

    // Gère l'affichage en jeu (Chat, Titres, Sons)
    void sendPrivateBriefing(UUID hiderId, CurseType type);
    void announceSuccess(UUID hiderId, CurseType type);
    void announceFailure(UUID hiderId, CurseType type);
}
