package org.eu.mawtalk;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PartyCommand implements CommandExecutor {

    private final MawTalk plugin;

    public PartyCommand(MawTalk plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("Only players may use that command.");
            return true;
        }

        UUID uuid = p.getUniqueId();

        if (args.length == 0) {
            p.sendMessage(Component.text("Party commands:", NamedTextColor.LIGHT_PURPLE));
            p.sendMessage(Component.text("/party create", NamedTextColor.GRAY));
            p.sendMessage(Component.text("/party invite <player>", NamedTextColor.GRAY));
            p.sendMessage(Component.text("/party accept", NamedTextColor.GRAY));
            p.sendMessage(Component.text("/party leave", NamedTextColor.GRAY));
            p.sendMessage(Component.text("/party chat  (toggle party chat)", NamedTextColor.GRAY));
            return true;
        }

        PartyManager pm = plugin.getPartyManager();
        String sub = args[0].toLowerCase();

        switch (sub) {
            case "create" -> {
                if (pm.getPartyByPlayer(uuid) != null) {
                    p.sendMessage(Component.text("You are already in a party.", NamedTextColor.RED));
                    return true;
                }
                Party created = pm.createParty(uuid);
                if (created != null) {
                    p.sendMessage(Component.text("Party created. Use /party invite <player> to invite.", NamedTextColor.GREEN));
                } else {
                    p.sendMessage(Component.text("Could not create party.", NamedTextColor.RED));
                }
            }

            case "invite" -> {
                if (args.length < 2) {
                    p.sendMessage(Component.text("Usage: /party invite <player>", NamedTextColor.RED));
                    return true;
                }
                Party party = pm.getPartyByPlayer(uuid);
                if (party == null) {
                    p.sendMessage(Component.text("You are not in a party. Create one with /party create.", NamedTextColor.RED));
                    return true;
                }
                if (!party.getLeader().equals(uuid)) {
                    p.sendMessage(Component.text("Only the party leader may invite.", NamedTextColor.RED));
                    return true;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null || !target.isOnline()) {
                    p.sendMessage(Component.text("Player not found or not online.", NamedTextColor.RED));
                    return true;
                }
                if (pm.invite(party.getId(), target.getUniqueId())) {
                    p.sendMessage(Component.text("Invitation sent to " + target.getName(), NamedTextColor.GREEN));
                    target.sendMessage(
                            Component.text("You have been invited to a party by ", NamedTextColor.GRAY)
                                    .append(Component.text(p.getName(), NamedTextColor.WHITE))
                    );
                    target.sendMessage(
                            Component.text("Use ", NamedTextColor.GRAY)
                                    .append(Component.text("/party accept", NamedTextColor.WHITE))
                                    .append(Component.text(" to join.", NamedTextColor.GRAY))
                    );
                } else {
                    p.sendMessage(Component.text("Could not invite that player (maybe already in a party).", NamedTextColor.RED));
                }
            }

            case "accept" -> {
                if (pm.getInvitedParty(uuid) == null) {
                    p.sendMessage(Component.text("You have no pending party invites.", NamedTextColor.RED));
                    return true;
                }
                Party joined = pm.acceptInvite(uuid);
                if (joined == null) {
                    p.sendMessage(Component.text("Could not join party.", NamedTextColor.RED));
                    return true;
                }
                for (UUID memberId : joined.getMembers()) {
                    Player member = Bukkit.getPlayer(memberId);
                    if (member != null && member.isOnline()) {
                        member.sendMessage(Component.text(p.getName() + " has joined the party.", NamedTextColor.GREEN));
                    }
                }
            }

            case "leave" -> {
                Party cur = pm.getPartyByPlayer(uuid);
                if (cur == null) {
                    p.sendMessage(Component.text("You are not in a party.", NamedTextColor.RED));
                    return true;
                }
                boolean wasLeader = cur.getLeader().equals(uuid);
                // snapshot members before leaving so we can notify them
                UUID[] snapshot = cur.getMembers().toArray(new UUID[0]);
                pm.leave(uuid);
                if (wasLeader) {
                    for (UUID memberId : snapshot) {
                        if (memberId.equals(uuid)) continue;
                        Player member = Bukkit.getPlayer(memberId);
                        if (member != null && member.isOnline()) {
                            member.sendMessage(Component.text("Leader has left the party.", NamedTextColor.YELLOW));
                        }
                    }
                }
                p.sendMessage(Component.text("You left the party.", NamedTextColor.GRAY));
            }

            case "chat" -> {
                Party my = pm.getPartyByPlayer(uuid);
                if (my == null) {
                    p.sendMessage(Component.text("You are not in a party.", NamedTextColor.RED));
                    return true;
                }
                boolean enabled = pm.toggleChat(uuid);
                if (enabled) {
                    p.sendMessage(Component.text("Party chat enabled. Your messages will go to the party.", NamedTextColor.GREEN));
                } else {
                    p.sendMessage(Component.text("Party chat disabled. Your messages will be public.", NamedTextColor.YELLOW));
                }
            }

            default -> p.sendMessage(Component.text("Unknown subcommand. Use /party for help.", NamedTextColor.RED));
        }

        return true;
    }
}
