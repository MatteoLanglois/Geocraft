package dev.lesroseaux.geocraft.models.game;

import dev.lesroseaux.geocraft.controllers.MapBuilder;
import dev.lesroseaux.geocraft.models.location.GeoCraftWorld;
import dev.lesroseaux.geocraft.models.location.PlayableZone;
import dev.lesroseaux.geocraft.models.location.Road;
import dev.lesroseaux.geocraft.models.score.ScoreManager;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.Random;

/**
 * Manages the GeoCraft game, including initialization, player management, and game state.
 */
public class GameManager {
  private static GameManager instance;
  private final Game game;
  private boolean gameStarted;
  private MapBuilder mapBuilder;
  private final ScoreManager scoreManager;
  private Plugin plugin;
  private ArrayList<Road> roads;
  private BossBar bossBar;

  /**
   * Private constructor for GameManager.
   * Initializes the game and score manager.
   */
  private GameManager() {
    game = new Game();
    gameStarted = false;
    scoreManager = new ScoreManager();
  }

  /**
   * Returns the singleton instance of GameManager.
   *
   * @return The singleton instance of GameManager.
   */
  public static GameManager getInstance() {
    if (instance == null) {
      instance = new GameManager();
    }
    return instance;
  }

  /**
   * Initializes the game with the given plugin.
   *
   * @param plugin The plugin instance.
   */
  public void initGame(Plugin plugin) {
    this.plugin = plugin;
    roads = game.getPlayableZone().getZones();

    game.getPlayers().forEach(scoreManager::addPlayer);

    FileConfiguration config = plugin.getConfig();
    Location startLocation = new Location(roads.getFirst().getPoint1().getWorld(),
        config.getInt("mapLocation.x"),
        config.getInt("mapLocation.y"),
        config.getInt("mapLocation.z"));

    try {
      if (game.getMap().getMap() instanceof GeoCraftWorld) {
        GeoCraftWorld world = new GeoCraftWorld(game.getPlayers().getFirst().getPlayer().getWorld());
        game.getMap().setMap(world);
      }
    } catch (Exception e) {
      plugin.getLogger().warning("Error while setting the map");
    }
    game.getPlayers().forEach(player -> player.getPlayer().sendMessage("The game will start when the map will be built."));
    mapBuilder = new MapBuilder(game.getMap(), startLocation, plugin, this::startGame);
    mapBuilder.build();
  }

  /**
   * Initializes the boss bar for displaying the remaining game time.
   */
  private void initBossBar() {
    bossBar = Bukkit.createBossBar("Time left", BarColor.GREEN, org.bukkit.boss.BarStyle.SOLID);
    bossBar.setProgress(1);
    bossBar.setVisible(true);
    game.getPlayers().forEach(player -> bossBar.addPlayer(player.getPlayer()));
  }

  /**
   * Updates the boss bar with the remaining game time.
   */
  private void updateBossBar() {
    long remainingTime = game.getGameDuration().toMillis() - (System.currentTimeMillis() - game.getStartTime());
    double progress = (double) remainingTime / game.getGameDuration().toMillis();
    progress = Math.max(0.0, Math.min(1.0, progress)); // Clamp the progress value
    bossBar.setProgress(progress);
    bossBar.setTitle("Time left: " + remainingTime / 1000 + " seconds");
    if (remainingTime / 1000 <= 10) {
      bossBar.setColor(BarColor.YELLOW);
    } else if (remainingTime / 1000 <= 5) {
      bossBar.setColor(BarColor.RED);
    }
  }

  /**
   * Starts the game, initializes the boss bar, and schedules the end of the game.
   */
  public void startGame() {
    BukkitScheduler scheduler = plugin.getServer().getScheduler();
    gameStarted = true;
    Location center = mapBuilder.getCenterOfGuessMap();
    this.game.getPlayers().forEach(player -> player.setMapCenter(center));

    scheduler.scheduleSyncDelayedTask(plugin, this::endGame, game.getGameDuration().toMillis() / 50);
    for (GeocraftPlayer player : game.getPlayers()) {
      teleportPlayer(player, roads);
      player.setInventory();
    }
    game.getPlayers().forEach(player -> player.getPlayer().sendMessage("The game is starting, you have "
        + game.getGameDuration().toSeconds() + " seconds to play."));
    game.getPlayers().forEach(GeocraftPlayer::setInventory);
    game.start();
    initBossBar();
    scheduler.scheduleSyncRepeatingTask(plugin, this::updateBossBar, 0, 20);
  }

  /**
   * Teleports a player to a random location on a road.
   *
   * @param player The player to teleport.
   * @param roads  The list of roads.
   */
  private void teleportPlayer(GeocraftPlayer player, ArrayList<Road> roads) {
    Road road = roads.get(new Random().nextInt(roads.size()));
    World world = player.getPlayer().getWorld();
    double t = new Random().nextDouble();
    int x = (int) ((1 - t) * road.getPoint1().getX() + t * road.getPoint2().getX());
    int z = (int) ((1 - t) * road.getPoint1().getZ() + t * road.getPoint2().getZ());
    Location teleportLocation  = new Location(world, x, (road.getPoint1().y() + road.getPoint2().y()) / 2, z);
    Bukkit.getScheduler().runTask(plugin, () -> player.teleportToRandom(teleportLocation));
  }

  /**
   * Ends the game, calculates scores, announces the winner, and resets player states.
   */
  private void endGame() {
    scoreManager.calculateScore(game.getMap(), mapBuilder.getStart());
    GeocraftPlayer winner = scoreManager.getWinner();
    game.getPlayers().forEach(player -> player.getPlayer().sendMessage("The game is over, the winner is "
        + winner.getPlayer().getName() + " with a score of " + scoreManager.getScore(winner)));
    game.getPlayers().forEach(player -> player.getPlayer().sendMessage("Your score is " + scoreManager.getScore(player)));
    game.getPlayers().forEach(GeocraftPlayer::resetInventory);
    game.getPlayers().forEach(player -> player.getPlayer().teleport(player.getLastPos()));
    game.getPlayers().forEach(player -> player.getPlayer().sendMessage("You have been teleported back to your initial position."));
    game.getPlayers().forEach(player -> player.getPlayer().sendMessage("Removing the guess map..."));
    CompletableFuture.runAsync(() -> {
      mapBuilder.removeMap();
    });
    game.clearPlayers();
    gameStarted = false;

    if (bossBar != null) {
      bossBar.removeAll();
    }
  }

  /**
   * Sets the duration of the game.
   *
   * @param duration The game duration.
   */
  public void setGameDuration(Duration duration) {
    game.setGameDuration(duration);
  }

  /**
   * Sets the playable zone of the game.
   *
   * @param playableZone The playable zone.
   */
  public void setPlayableZone(PlayableZone playableZone) {
    game.setPlayableZone(playableZone);
  }

  /**
   * Adds a player to the game.
   *
   * @param player The player to add.
   */
  public void addPlayer(Player player) {
    game.addPlayer(player);
    Component message = Component.text("You joined the game.").color(TextColor.color(0x00FF00));
    player.sendMessage(message);
    Component messageToOthers = Component.text(player.getName()).color(TextColor.color(0x00FF00))
        .append(Component.text(" joined the game."));
    game.getPlayers().forEach(gamePlayer -> gamePlayer.getPlayer().sendMessage(messageToOthers));
    scoreManager.addPlayer(game.getPlayer(player));
  }

  /**
   * Removes a player from the game.
   *
   * @param player The player to remove.
   */
  public void removePlayer(Player player) {
    game.removePlayer(player);
    player.sendMessage("You left the game.");
    Component message = Component.text(player.getName()).color(TextColor.color(0xFF0000))
        .append(Component.text(" left the game."));
    game.getPlayers().forEach(gamePlayer -> gamePlayer.getPlayer().sendMessage(message));
    scoreManager.removePlayer(game.getPlayer(player));
  }

  /**
   * Checks if a player is in the game.
   *
   * @param player The player to check.
   * @return True if the player is in the game, false otherwise.
   */
  public boolean isPlayerInGame(Player player) {
    return game.getPlayers().stream().anyMatch(p -> p.getPlayer().equals(player));
  }

  /**
   * Gets the GeocraftPlayer object for a given player.
   *
   * @param player The player to get.
   * @return The GeocraftPlayer object, or null if not found.
   */
  public GeocraftPlayer getPlayer(Player player) {
    return game.getPlayers().stream().filter(p -> p.getPlayer().equals(player)).findFirst().orElse(null);
  }

  /**
   * Sets the map of the game.
   *
   * @param geocraftMap The GeocraftMap object.
   */
  public void setMap(GeocraftMap geocraftMap) {
    game.setMap(geocraftMap);
  }

  /**
   * Checks if the game has started.
   *
   * @return True if the game has started, false otherwise.
   */
  public boolean isGameStarted() {
    return gameStarted;
  }

  /**
   * Teleports a player to the guess map.
   *
   * @param sender The player to teleport.
   */
  public void teleportPlayerToGuessMap(Player sender) {
    GeocraftPlayer player = game.getPlayer(sender);
    player.teleportToGuess();
  }
}