package dev.lesroseaux.geocraft.models.game;

import dev.lesroseaux.geocraft.models.location.PlayableZone;
import dev.lesroseaux.geocraft.utils.Banner;
import java.util.List;
import org.bukkit.entity.Player;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class Game {
  private PlayableZone playableZone;
  private final UUID gameUuid;
  private final ArrayList<GeocraftPlayer> players;
  private Duration gameDuration;
  private GeocraftMap geocraftMap;
  private Random random;
  private long startTime;

  public Game() {
    this.gameDuration = Duration.ZERO;
    this.playableZone = null;
    this.players = new ArrayList<>();
    this.gameUuid = UUID.randomUUID();
    this.random = new Random();
  }

  public Duration getGameDuration() {
    return gameDuration;
  }

  public PlayableZone getPlayableZone() {
    return playableZone;
  }

  public UUID getGameUuid() {
    return gameUuid;
  }

  public ArrayList<GeocraftPlayer> getPlayers() {
    return players;
  }

  public void setGameDuration(Duration gameDuration) {
    this.gameDuration = gameDuration;
  }

  public void setPlayableZone(PlayableZone playableZone) {
    this.playableZone = playableZone;
  }

  public void setMap(GeocraftMap geocraftMap) {
    this.geocraftMap = geocraftMap;
  }

  public void addPlayer(Player player) {
    List<Banner> banners = Banner.bannersList;
    for (GeocraftPlayer geocraftPlayer : players) {
      banners.remove(geocraftPlayer.getBanner());
    }
    Banner banner = banners.get(random.nextInt(banners.size()));
    players.add(new GeocraftPlayer(player, banner));
  }

  public void removePlayer(Player player) {
    players.removeIf(geocraftPlayer -> geocraftPlayer.getPlayer().equals(player));
  }

  public void clearPlayers() {
    players.clear();
  }

  public boolean isPlayerInGame(Player player) {
    return players.stream().map(GeocraftPlayer::getPlayer).toList().contains(player);
  }

  public GeocraftPlayer getPlayer(Player player) {
    return players.stream().filter(geocraftPlayer -> geocraftPlayer.getPlayer().equals(player)).findFirst().orElse(null);
  }

  public boolean isGameEmpty() {
    return players.isEmpty();
  }

  public GeocraftMap getMap() {
    return geocraftMap;
  }

  public void start() {
    startTime = System.currentTimeMillis();
  }

  public long getStartTime() {
    return startTime;
  }
}