package org.eu.mawtalk;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PartyManager {

    private final Map<UUID, Party> parties = new ConcurrentHashMap<>();
    private final Map<UUID, UUID> memberToParty = new ConcurrentHashMap<>(); // player -> partyId
    private final Map<UUID, UUID> invitations = new ConcurrentHashMap<>();   // target -> partyId
    private final Set<UUID> chatToggled = ConcurrentHashMap.newKeySet();

    public Party createParty(UUID leader) {
        if (memberToParty.containsKey(leader)) return null;
        UUID id = UUID.randomUUID();
        Party p = new Party(id, leader);
        parties.put(id, p);
        memberToParty.put(leader, id);
        return p;
    }

    public boolean invite(UUID partyId, UUID target) {
        Party p = parties.get(partyId);
        if (p == null) return false;
        if (memberToParty.containsKey(target)) return false;
        invitations.put(target, partyId);
        return true;
    }

    public Party acceptInvite(UUID target) {
        UUID partyId = invitations.remove(target);
        if (partyId == null) return null;
        Party p = parties.get(partyId);
        if (p == null) return null;
        p.addMember(target);
        memberToParty.put(target, partyId);
        return p;
    }

    public Party getPartyByPlayer(UUID player) {
        UUID id = memberToParty.get(player);
        if (id == null) return null;
        return parties.get(id);
    }

    public boolean leave(UUID player) {
        UUID id = memberToParty.remove(player);
        if (id == null) return false;
        Party p = parties.get(id);
        if (p == null) return false;
        p.removeMember(player);
        chatToggled.remove(player);
        if (p.getMembers().isEmpty()) {
            parties.remove(id);
        } else if (player.equals(p.getLeader())) {
            UUID newLeader = p.getMembers().iterator().next();
            p.setLeader(newLeader);
        }
        return true;
    }

    public boolean disband(UUID partyId) {
        Party p = parties.remove(partyId);
        if (p == null) return false;
        for (UUID member : p.getMembers()) {
            memberToParty.remove(member);
            chatToggled.remove(member);
        }
        return true;
    }

    public boolean toggleChat(UUID player) {
        if (chatToggled.contains(player)) {
            chatToggled.remove(player);
            return false;
        } else {
            chatToggled.add(player);
            return true;
        }
    }

    public boolean isChatEnabled(UUID player) {
        return chatToggled.contains(player);
    }

    public UUID getInvitedParty(UUID target) {
        return invitations.get(target);
    }
}
