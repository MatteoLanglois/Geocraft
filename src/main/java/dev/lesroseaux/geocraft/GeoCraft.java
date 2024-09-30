package dev.lesroseaux.geocraft;

import dev.lesroseaux.geocraft.data.DatabaseOptions;
import dev.lesroseaux.geocraft.data.commands.GeocraftCreate;
import dev.lesroseaux.geocraft.data.commands.GeocraftList;
import dev.lesroseaux.geocraft.data.commands.GeocraftReloadDb;
import dev.lesroseaux.geocraft.data.commands.GeocraftStart;
import dev.lesroseaux.geocraft.data.connection.DatabaseConnection;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEvent;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
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

public class GeoCraft extends JavaPlugin implements Listener {
  private Location tempPoint1;
  private Location tempPoint2;

  @Override
  public void onEnable() {
    ConfigurationSerialization.registerClass(DatabaseOptions.class, "database");
    FileConfiguration config = getConfig();
    config.addDefault("database.host", "localhost");
    config.addDefault("database.port", 3306);
    config.addDefault("database.database", "geocraft");
    config.addDefault("database.username", "geocraft");
    config.addDefault("database.password", "password");
    config.options().copyDefaults(true);
    saveResource("config.yml", false);

    DatabaseOptions databaseOptions = DatabaseOptions.loadFromConfig(getConfig());
    DatabaseConnection.getInstance(Optional.of(databaseOptions));

    Bukkit.getPluginManager().registerEvents(this, this);
    LifecycleEventManager<Plugin> manager = this.getLifecycleManager();
    manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
      final Commands commands = event.registrar();
      commands.register("geocraft-create", "Create a playable zone", new GeocraftCreate(this));
      commands.register("geocraft-list", "List all playable zones", new GeocraftList());
      commands.register("geocraft-start", "Start the game", new GeocraftStart());
      commands.register("geocraft-reloaddb", "Reload the database", new GeocraftReloadDb(this));
    });
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    event.getPlayer().sendMessage(Component.text("Hello, " + event.getPlayer().getName() + "!"));
  }

  private boolean isHoldingSelectionTool(Player player) {
    return player.getInventory().getItemInMainHand().getType() == Material.COMPASS;
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    Player player = event.getPlayer();
    if (isHoldingSelectionTool(player) && event.hasBlock()) {
      if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
        tempPoint1 = Objects.requireNonNull(event.getClickedBlock()).getLocation();
        player.sendMessage(Component.text("First point set to " + locationToString(tempPoint1)));
      } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
        tempPoint2 = Objects.requireNonNull(event.getClickedBlock()).getLocation();
        player.sendMessage(Component.text("Second point set to " + locationToString(tempPoint2)));
      }
    }
  }

  private String locationToString(Location location) {
    return "(" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + ")";
  }

  public Location getTempPoint1() {
    return tempPoint1;
  }

  public Location getTempPoint2() {
    return tempPoint2;
  }

  public void reloadDatabase() {
    DatabaseOptions databaseOptions = DatabaseOptions.loadFromConfig(getConfig());
    DatabaseConnection db = DatabaseConnection.getInstance(Optional.of(databaseOptions));
  }
}