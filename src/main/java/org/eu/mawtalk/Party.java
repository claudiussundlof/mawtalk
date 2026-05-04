package org.eu.mawtalk;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Party {

    private final UUID id;
    private UUID leader;
    private final Set<UUID> members = ConcurrentHashMap.newKeySet();

    public Party(UUID id, UUID leader) {
        this.id = id;
        this.leader = leader;
        members.add(leader);
    }

    public UUID getId() {
        return id;
    }

    public UUID getLeader() {
        return leader;
    }

    public void setLeader(UUID newLeader) {
        this.leader = newLeader;
    }

    public Set<UUID> getMembers() {
        return members;
    }

    public boolean addMember(UUID player) {
        return members.add(player);
    }

    public boolean removeMember(UUID player) {
        return members.remove(player);
    }

    public boolean isMember(UUID player) {
        return members.contains(player);
    }
}
