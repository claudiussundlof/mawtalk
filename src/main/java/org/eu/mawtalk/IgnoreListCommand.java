package org.eu.mawtalk;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

public class IgnoreListCommand implements CommandExecutor {

    private final MawTalk plugin;

    public IgnoreListCommand(MawTalk plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("Only players may use that command.");
            return true;
        }

        Set<UUID> set = plugin.getIgnored().get(p.getUniqueId());
        if (set == null || set.isEmpty()) {
            p.sendMessage(Component.text("You are not ignoring anyone.", NamedTextColor.YELLOW));
            return true;
        }

        StringBuilder sb = new StringBuilder();
        for (UUID u : set) {
            @SuppressWarnings("deprecation")
            OfflinePlayer op = plugin.getServer().getOfflinePlayer(u);
            String name = (op.getName() != null) ? op.getName() : u.toString();
            if (!sb.isEmpty()) sb.append(", ");
            sb.append(name);
        }

        p.sendMessage(
                Component.text("Ignored players: ", NamedTextColor.GRAY)
                        .append(Component.text(sb.toString(), NamedTextColor.WHITE))
        );
        return true;
    }
}
