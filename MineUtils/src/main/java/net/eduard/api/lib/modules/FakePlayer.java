package net.eduard.api.lib.modules;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * API de criação de Jogador Offline melhor para uso de Scoreboard
 *
 * @author Eduard
 * @version 1.2
 */
@SuppressWarnings("unused")
public class FakePlayer implements OfflinePlayer {


    private String name;
    private UUID id;
    private transient Player playerCache = null;


    public void setIdByName() {
        this.id = UUID.nameUUIDFromBytes(("OfflinePlayer:" + name)
                .getBytes(StandardCharsets.UTF_8));
    }


    public void setIdByNameLowerCase() {
        this.id = UUID.nameUUIDFromBytes(("OfflinePlayer:" +
                name.toLowerCase()).getBytes(StandardCharsets.UTF_8));
    }

    public void fixUUID() {
        Player player = getPlayer();
        if (player != null) {
            this.id = player.getUniqueId();
        } else {
            setIdByName();
        }
    }

    public void sendMessage(String message) {
        consume(p -> p.sendMessage(message));
    }

    public void consume(Consumer<Player> playerConsume) {
        Player player = getPlayer();
        if (player != null) {
            playerConsume.accept(player);
        }
    }


    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "FakePlayer{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }

    public FakePlayer(String name) {
        setName(name);
        fixUUID();
    }

    public FakePlayer(String name, UUID id) {
        setName(name);
        if (id != null) {
            setId(id);
        } else {
            setIdByName();
        }
    }


    public FakePlayer(OfflinePlayer player) {
        this(player.getName(), player.getUniqueId());
    }

    public FakePlayer() {
        this("Eduard");
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        FakePlayer otherPlayer = (FakePlayer) other;
        return name.equalsIgnoreCase(otherPlayer.name);
    }

    @Override
    public int hashCode() {
        return name.toLowerCase().hashCode();
    }

    @Override
    public boolean isOp() {
        return false;
    }

    @Override
    public void setOp(boolean op) {

    }

    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> mapaNovo = new HashMap<>();
        mapaNovo.put("name", this.name);
        mapaNovo.put("uuid", this.getUniqueId());
        return mapaNovo;
    }

    @Override
    public Location getBedSpawnLocation() {
        return null;
    }

    @Override
    public long getFirstPlayed() {
        return 0;
    }

    @Override
    public long getLastPlayed() {
        return 0;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Player getPlayer() {
        if (playerCache != null && playerCache.isOnline()) {
            return playerCache;
        }

        if (id != null)
            playerCache = Bukkit.getPlayer(id);
        if (playerCache == null && name != null)
            playerCache = Bukkit.getPlayer(name);

        return playerCache;
    }


    public UUID getUniqueId() {
        return this.id;
    }


    public boolean hasPlayedBefore() {
        return true;
    }


    public boolean isBanned() {
        return false;
    }


    public boolean isOnline() {
        return getPlayer() != null;
    }


    public boolean isWhitelisted() {

        return true;
    }


    public void setBanned(boolean flag) {


    }


    public void setWhitelisted(boolean flag) {


    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

}
