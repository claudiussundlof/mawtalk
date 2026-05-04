package org.eu.mawtalk;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MawTalk extends JavaPlugin {

    private final Map<UUID, UUID> lastMessager = new ConcurrentHashMap<>();
    private final Map<UUID, Set<UUID>> ignored = new ConcurrentHashMap<>();
    private final PartyManager partyManager = new PartyManager();

    @Override
    public void onEnable() {
        Objects.requireNonNull(getCommand("msg")).setExecutor(new MsgCommand(this));
        Objects.requireNonNull(getCommand("reply")).setExecutor(new ReplyCommand(this));
        Objects.requireNonNull(getCommand("ignore")).setExecutor(new IgnoreCommand(this));
        Objects.requireNonNull(getCommand("unignore")).setExecutor(new UnignoreCommand(this));
        Objects.requireNonNull(getCommand("ignorelist")).setExecutor(new IgnoreListCommand(this));
        Objects.requireNonNull(getCommand("mawtalk")).setExecutor(new HelpCommand(this));
        Objects.requireNonNull(getCommand("party")).setExecutor(new PartyCommand(this));

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new ChatListener(this), this);

        getLogger().info("MawTalk enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("MawTalk disabled.");
    }

    public Map<UUID, UUID> getLastMessager() {
        return lastMessager;
    }

    public Map<UUID, Set<UUID>> getIgnored() {
        return ignored;
    }

    public PartyManager getPartyManager() {
        return partyManager;
    }
}
