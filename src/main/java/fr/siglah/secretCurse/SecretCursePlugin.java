package fr.siglah.secretCurse;

import fr.siglah.secretCurse.application.GameManager;
import fr.siglah.secretCurse.infrastructure.paper.adapter.PaperCurseHandlerAdapter;
import fr.siglah.secretCurse.presentation.command.DefiCommand;
import fr.siglah.secretCurse.presentation.command.DefiHelpCommand;
import fr.siglah.secretCurse.presentation.gui.GuiClickListener;
import fr.siglah.secretCurse.presentation.gui.GuiManager;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class SecretCursePlugin extends JavaPlugin {

    private GameManager gameManager;
    private GuiManager guiManager;

    @Override
    public void onEnable() {
        PaperCurseHandlerAdapter curseHandler = new PaperCurseHandlerAdapter(this);
        this.gameManager = new GameManager(curseHandler);
        this.guiManager = new GuiManager();

        // Écoute globale des clics/fermetures d'inventaires pour tous les menus SecretCurse
        getServer().getPluginManager().registerEvents(new GuiClickListener(guiManager), this);

        // Enregistrement des commandes (Best Practice : Fail-Fast)
        DefiCommand defiExecutor = new DefiCommand(this.gameManager, this.guiManager, this);

        Objects.requireNonNull(getCommand("defi"), "CRITIQUE : La commande 'defi' n'est pas déclarée dans le plugin.yml !")
                .setExecutor(defiExecutor);

        // Raccourcis : mêmes handlers que /defi, mais enregistrés comme commandes à part
        // entière (le nom exact tapé - le "label" - permet à DefiCommand de savoir lequel a été utilisé)
        Objects.requireNonNull(getCommand("dm"), "CRITIQUE : La commande 'dm' n'est pas déclarée dans le plugin.yml !")
                .setExecutor(defiExecutor);
        Objects.requireNonNull(getCommand("dg"), "CRITIQUE : La commande 'dg' n'est pas déclarée dans le plugin.yml !")
                .setExecutor(defiExecutor);
        Objects.requireNonNull(getCommand("dp"), "CRITIQUE : La commande 'dp' n'est pas déclarée dans le plugin.yml !")
                .setExecutor(defiExecutor);

        PluginCommand defiHelpCmd = getCommand("defihelp");
        Objects.requireNonNull(defiHelpCmd, "CRITIQUE : La commande 'defihelp' n'est pas déclarée dans le plugin.yml !")
                .setExecutor(new DefiHelpCommand());

        getLogger().info("Le plugin SecretCurse a démarré avec succès !");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public GuiManager getGuiManager() {
        return guiManager;
    }
}
