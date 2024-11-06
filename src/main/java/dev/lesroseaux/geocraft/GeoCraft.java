package dev.lesroseaux.geocraft;

import com.destroystokyo.paper.ParticleBuilder;
import dev.lesroseaux.geocraft.controllers.commands.GeocraftCreate;
import dev.lesroseaux.geocraft.controllers.commands.GeocraftGuess;
import dev.lesroseaux.geocraft.controllers.commands.GeocraftJoin;
import dev.lesroseaux.geocraft.controllers.commands.GeocraftList;
import dev.lesroseaux.geocraft.controllers.commands.GeocraftMap;
import dev.lesroseaux.geocraft.controllers.commands.GeocraftReloadDb;
import dev.lesroseaux.geocraft.controllers.commands.GeocraftStart;
import dev.lesroseaux.geocraft.data.DatabaseOptions;
import dev.lesroseaux.geocraft.data.connection.DatabaseConnection;
import dev.lesroseaux.geocraft.models.game.GuessChecker;
import dev.lesroseaux.geocraft.models.game.MapMaterials;
import dev.lesroseaux.geocraft.models.location.City;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import java.util.Objects;
import java.util.Optional;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class GeoCraft extends JavaPlugin implements Listener {
  private Location tempPoint1;
  private Location tempPoint2;
  @Override
  public void onEnable() {
    ConfigurationSerialization.registerClass(DatabaseOptions.class, "database");
    setConfig();
    FileConfiguration config = getConfig();

    DatabaseOptions databaseOptions = DatabaseOptions.loadFromConfig(getConfig());
    DatabaseConnection.getInstance(Optional.of(databaseOptions));

    Bukkit.getPluginManager().registerEvents(this, this);
    Bukkit.getPluginManager().registerEvents(new GuessChecker(), this);
    LifecycleEventManager<Plugin> manager = this.getLifecycleManager();
    manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
      final Commands commands = event.registrar();
      commands.register("geocraft-create", "Create a playable zone", new GeocraftCreate(this));
      commands.register("geocraft-guess", "Guess the location", new GeocraftGuess());
      commands.register("geocraft-info", "Get information about a playable zone",
          new GeocraftList());
      commands.register("geocraft-join", "Join the game", new GeocraftJoin());
      commands.register("geocraft-list", "List all playable zones", new GeocraftList());
      commands.register("geocraft-map", "Create a map of the playable zone", new GeocraftMap(this));
      commands.register("geocraft-reloaddb", "Reload the database", new GeocraftReloadDb(this));
      commands.register("geocraft-start", "Start the game", new GeocraftStart(this));
    });
    this.getLogger().info("GeoCraft plugin enabled.");
    this.getLogger().info("Size required :" + City.class);
  }
  private void setConfig() {
    FileConfiguration config = getConfig();
    config.addDefault("database.host", "localhost");
    config.addDefault("database.port", 3306);
    config.addDefault("database.database", "geocraft");
    config.addDefault("database.username", "geocraft");
    config.addDefault("database.password", "password");
    config.addDefault("mapLocation.x", 0);
    config.addDefault("mapLocation.y", 255);
    config.addDefault("mapLocation.z", 0);
    config.addDefault("tools.selection", Material.PAPER.name());
    MapMaterials.saveDefaultMaterials(this);
    config.options().copyDefaults(true);
    saveResource("config.yml", false);
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    event.getPlayer().sendMessage(Component.text("Hello, " + event.getPlayer().getName() + "!"));
  }

  private boolean isHoldingSelectionTool(Player player) {
    FileConfiguration config = getConfig();
    return player.getInventory().getItemInMainHand().getType() == Material
        .valueOf(config.getString("tools.selection"));
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    Player player = event.getPlayer();
    if (isHoldingSelectionTool(player) && event.hasBlock()) {
      if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
        tempPoint1 = Objects.requireNonNull(event.getClickedBlock()).getLocation();
        player.sendMessage(Component.text("First point set to " + locationToString(tempPoint1)));
      } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
        if (tempPoint1 == null) {
          player.sendMessage(Component.text("You must set the first point first."));
        } else {
          tempPoint2 = Objects.requireNonNull(event.getClickedBlock()).getLocation();
          player.sendMessage(Component.text("Second point set to " + locationToString(tempPoint2)));
          displayRoad(tempPoint1, tempPoint2);
        }
      }
      event.setCancelled(true);
    }
  }

  private void displayRoad(Location point1, Location point2) {
    ParticleBuilder particleBuilder = new ParticleBuilder(Particle.BUBBLE);

    // Draw the line between the two points for 20 seconds
    Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
      private int ticks = 0;

      @Override
      public void run() {
        if (ticks >= 20 * 20) { // 20 seconds * 20 ticks per second
          Bukkit.getScheduler().cancelTask(ticks);
          return;
        }
        if (point1 != null && point2 != null) {
          drawLine(particleBuilder, point1, point2);
        }
        ticks++;
      }
    }, 0L, 1L); // Run every tick (1L)
  }

  private void drawLine(ParticleBuilder particleBuilder, Location start, Location end) {
    double distance = start.distance(end);
    if (distance == 0 || distance > 600) {
      return;
    }
    for (double i = 0; i < distance; i += 0.1) {
      double t = i / distance;
      double x = (1 - t) * start.getX() + t * end.getX();
      double y = (1 - t) * start.getY() + t * end.getY();
      double z = (1 - t) * start.getZ() + t * end.getZ();
      particleBuilder.location(new Location(start.getWorld(), x, y + 1, z)).spawn();
    }
  }

  private String locationToString(Location location) {
    return "(" + location.getBlockX() + ", " + location.getBlockY() + ", "
        + location.getBlockZ() + ")";
  }

  public Location getTempPoint1() {
    return tempPoint1;
  }

  public Location getTempPoint2() {
    return tempPoint2;
  }
}