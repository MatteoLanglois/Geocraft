package dev.lesroseaux.geocraft.models.Game;

import dev.lesroseaux.geocraft.models.Location.PlayableZone;
import dev.lesroseaux.geocraft.models.Location.Road;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Game {
  private PlayableZone playableZone;
  private final UUID game_uuid;
  private ArrayList<Player> players;
  private Duration game_duration;
  private static Game instance;

  private Game() {
    this.game_duration = Duration.ZERO;
    this.playableZone = null;
    this.players = new ArrayList<>();
    this.game_uuid = UUID.randomUUID();
  }

  public static Game getInstance() {
    if (instance == null) {
      instance = new Game();
    }
    return instance;
  }

  public void StartGame() {
    ArrayList<Road> roads = playableZone.getZones();
    for (Player player : players) {
      teleportPlayer(player, roads);
    }
  }

  private void teleportPlayer(Player player, ArrayList<Road> roads) {
    Road road = roads.get(new Random().nextInt(roads.size()));
    int minX = Math.min(road.getPoint1().getBlockX(), road.getPoint2().getBlockX());
    int maxX = Math.max(road.getPoint1().getBlockX(), road.getPoint2().getBlockX());
    int minZ = Math.min(road.getPoint1().getBlockZ(), road.getPoint2().getBlockZ());
    int maxZ = Math.max(road.getPoint1().getBlockZ(), road.getPoint2().getBlockZ());
    int choseX = new Random().nextInt(minX, maxX);
    int chosedZ = new Random().nextInt(minZ, maxZ);
    // Check if the position is possible
    int y = player.getLocation().getBlockY();
    while (player.getWorld().getBlockAt(choseX, y, chosedZ).getType() != Material.AIR) {
      y++;
    }
    player.teleport(new Location(player.getWorld(), choseX, y, chosedZ));
  }

  public Duration getGameDuration() {
    return game_duration;
  }

  public PlayableZone getPlayableZone() {
    return playableZone;
  }

  public UUID getGameUuid() {
    return game_uuid;
  }

  public ArrayList<Player> getPlayers() {
    return players;
  }

  public void setGameDuration(Duration game_duration) {
    this.game_duration = game_duration;
  }

  public void setPlayableZone(PlayableZone playableZone) {
    this.playableZone = playableZone;
  }

  public void addPlayer(Player player) {
    players.add(player);
  }

  public void removePlayer(Player player) {
    players.remove(player);
  }

  public void clearPlayers() {
    players.clear();
  }

  public boolean isPlayerInGame(Player player) {
    return players.contains(player);
  }

  public boolean isGameEmpty() {
    return players.isEmpty();
  }
}
