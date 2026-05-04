package org.eu.mawtalk;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.Set;
import java.util.UUID;

public class ChatListener implements Listener {

    private final MawTalk plugin;

    public ChatListener(MawTalk plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onChat(AsyncChatEvent event) {
        final UUID senderId = event.getPlayer().getUniqueId();
        final PartyManager pm = plugin.getPartyManager();

        // If player has party chat toggled, route message only to party members
        if (pm.isChatEnabled(senderId)) {
            event.setCancelled(true);
            Party party = pm.getPartyByPlayer(senderId);
            if (party == null) return;

            Player sender = event.getPlayer();
            String rawMsg = PlainTextComponentSerializer.plainText().serialize(event.message());

            Component formatted = Component.text("[Party] ", NamedTextColor.GREEN)
                    .append(Component.text(sender.getName(), NamedTextColor.GRAY))
                    .append(Component.text(": ", NamedTextColor.GRAY))
                    .append(Component.text(rawMsg, NamedTextColor.LIGHT_PURPLE));

            for (UUID memberId : party.getMembers()) {
                Player member = plugin.getServer().getPlayer(memberId);
                if (member == null || !member.isOnline()) continue;
                Set<UUID> ignored = plugin.getIgnored().get(member.getUniqueId());
                if (ignored != null && ignored.contains(senderId)) continue;
                member.sendMessage(formatted);
            }
            return;
        }

        // Remove recipients who have ignored the sender
        event.viewers().removeIf(viewer -> {
            if (!(viewer instanceof Player recipient)) return false;
            Set<UUID> ignored = plugin.getIgnored().get(recipient.getUniqueId());
            return ignored != null && ignored.contains(senderId);
        });
    }
}
