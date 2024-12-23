package dev.lesroseaux.geocraft.models.game;

import dev.lesroseaux.geocraft.models.location.PlayableZone;
import dev.lesroseaux.geocraft.utils.Banner;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

/**
 * Represents a game in GeoCraft.
 */
@Getter
@Setter
public class Game {
  private PlayableZone playableZone;
  private final UUID gameUuid;
  private final ArrayList<GeocraftPlayer> players;
  private Duration gameDuration;
  private GeocraftMap geocraftMap;
  private final Random random;
  private long startTime;

  /**
   * Constructs a new Game instance.
   */
  public Game() {
    this.gameDuration = Duration.ZERO;
    this.playableZone = null;
    this.players = new ArrayList<>();
    this.gameUuid = UUID.randomUUID();
    this.random = new Random();
    this.geocraftMap = null; // Initialize geocraftMap to null
  }

  /**
   * Sets the map of the game.
   *
   * @param geocraftMap The GeocraftMap object.
   */
  public void setMap(GeocraftMap geocraftMap) {
    this.geocraftMap = geocraftMap;
  }

  /**
   * Adds a player to the game.
   *
   * @param player The player to add.
   */
  public void addPlayer(Player player) {
    List<Banner> banners = new ArrayList<>(Banner.bannersList);
    for (GeocraftPlayer geocraftPlayer : players) {
      banners.remove(geocraftPlayer.getBanner());
    }
    Banner banner = banners.get(random.nextInt(banners.size()));
    players.add(new GeocraftPlayer(player, banner));
  }

  /**
   * Removes a player from the game.
   *
   * @param player The player to remove.
   */
  public void removePlayer(Player player) {
    players.removeIf(geocraftPlayer -> geocraftPlayer.getPlayer().equals(player));
  }

  /**
   * Clears all players from the game.
   */
  public void clearPlayers() {
    players.clear();
  }

  /**
   * Checks if a player is in the game.
   *
   * @param player The player to check.
   * @return True if the player is in the game, false otherwise.
   */
  public boolean isPlayerInGame(Player player) {
    return players.stream().map(GeocraftPlayer::getPlayer).toList().contains(player);
  }

  /**
   * Gets a GeocraftPlayer object for a given player.
   *
   * @param player The player to get.
   * @return The GeocraftPlayer object, or null if not found.
   */
  public GeocraftPlayer getPlayer(Player player) {
    return players.stream().filter(geocraftPlayer -> geocraftPlayer.getPlayer().equals(player)).findFirst().orElse(null);
  }

  /**
   * Checks if the game has no players.
   *
   * @return True if the game is empty, false otherwise.
   */
  public boolean isGameEmpty() {
    return players.isEmpty();
  }

  /**
   * Gets the map of the game.
   *
   * @return The GeocraftMap object.
   */
  public GeocraftMap getMap() {
    return geocraftMap;
  }

  /**
   * Starts the game.
   */
  public void start() {
    startTime = System.currentTimeMillis();
  }

}