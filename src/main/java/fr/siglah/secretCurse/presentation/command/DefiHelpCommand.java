package fr.siglah.secretCurse.presentation.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class DefiHelpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage(Component.text("=== Aide SecretCurse ===", NamedTextColor.GOLD));
        sender.sendMessage(line("/defi menu", "Ouvre le menu principal (choix du mode de jeu)."));
        sender.sendMessage(line("/defi", "Lance directement une malédiction aléatoire (sans GUI)."));
        sender.sendMessage(line("/defi gg", "Le chercheur a trouvé la malédiction cachée."));
        sender.sendMessage(line("/defi perdu", "Le chercheur a échoué sur son essai."));
        sender.sendMessage(line("/defihelp", "Affiche cette aide."));
        return true;
    }

    private Component line(String command, String description) {
        return Component.text(command, NamedTextColor.YELLOW)
                .append(Component.text(" - " + description, NamedTextColor.GRAY));
    }
}
