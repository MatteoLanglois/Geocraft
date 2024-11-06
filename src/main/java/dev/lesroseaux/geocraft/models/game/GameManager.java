package dev.lesroseaux.geocraft.models.game;

import dev.lesroseaux.geocraft.controllers.MapBuilder;
import dev.lesroseaux.geocraft.models.location.GeoCraftWorld;
import dev.lesroseaux.geocraft.models.location.PlayableZone;
import dev.lesroseaux.geocraft.models.location.Road;
import dev.lesroseaux.geocraft.models.score.ScoreManager;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
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

public class GameManager {
  private static GameManager instance;
  private final Game game;
  private boolean gameStarted;
  private MapBuilder mapBuilder;
  private final ScoreManager scoreManager;
  private Plugin plugin;
  private ArrayList<Road> roads;
  private BossBar bossBar;

  private GameManager() {
    game = new Game();
    gameStarted = false;
    scoreManager = new ScoreManager();
  }

  public static GameManager getInstance() {
    if (instance == null) {
      instance = new GameManager();
    }
    return instance;
  }

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

  private void initBossBar() {
    bossBar = Bukkit.createBossBar("Time left", BarColor.GREEN, org.bukkit.boss.BarStyle.SOLID);
    bossBar.setProgress(1);
    bossBar.setVisible(true);
    game.getPlayers().forEach(player -> bossBar.addPlayer(player.getPlayer()));
  }

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
    scheduler.scheduleSyncRepeatingTask(plugin, this::updateBossBar, 0, 1);
  }

  private void teleportPlayer(GeocraftPlayer player, ArrayList<Road> roads) {
    Road road = roads.get(new Random().nextInt(roads.size()));
    World world = player.getPlayer().getWorld();
    double t = new Random().nextDouble();
    int x = (int) ((1 - t) * road.getPoint1().getX() + t * road.getPoint2().getX());
    int z = (int) ((1 - t) * road.getPoint1().getZ() + t * road.getPoint2().getZ());
    // Check if the position is possible
    int y = player.getPlayer().getLocation().getBlockY();
    while (world.getBlockAt(x, y, z).getType() != Material.AIR) {
      y++;
    }
    Location teleportLocation  = new Location(world, x, y, z);
    Bukkit.getScheduler().runTask(plugin, () -> player.teleportToRandom(teleportLocation));
  }

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

  public void setGameDuration(Duration duration) {
    game.setGameDuration(duration);
  }

  public void setPlayableZone(PlayableZone playableZone) {
    game.setPlayableZone(playableZone);
  }

  public void addPlayer(Player player) {
    game.addPlayer(player);
    scoreManager.addPlayer(game.getPlayer(player));
  }

  public void removePlayer(Player player) {
    game.removePlayer(player);
    scoreManager.removePlayer(game.getPlayer(player));
  }

  public boolean isPlayerInGame(Player player) {
    return game.getPlayers().stream().anyMatch(p -> p.getPlayer().equals(player));
  }

  public GeocraftPlayer getPlayer(Player player) {
    return game.getPlayers().stream().filter(p -> p.getPlayer().equals(player)).findFirst().orElse(null);
  }

  public void setMap(GeocraftMap geocraftMap) {
    game.setMap(geocraftMap);
  }

  public boolean isGameStarted() {
    return gameStarted;
  }

  public void teleportPlayerToGuessMap(Player sender) {
    // TODO : Teleport the player to the guess map
    GeocraftPlayer player = game.getPlayer(sender);
    player.teleportToGuess();
  }
}