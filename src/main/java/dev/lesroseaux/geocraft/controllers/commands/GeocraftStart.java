package dev.lesroseaux.geocraft.controllers.commands;

import dev.lesroseaux.geocraft.data.dao.CityDao;
import dev.lesroseaux.geocraft.data.dao.DistrictDao;
import dev.lesroseaux.geocraft.data.dao.LocationToMapDao;
import dev.lesroseaux.geocraft.data.dao.MapDao;
import dev.lesroseaux.geocraft.data.dao.RegionDao;
import dev.lesroseaux.geocraft.models.LocationToMap;
import dev.lesroseaux.geocraft.models.game.GameManager;
import dev.lesroseaux.geocraft.models.game.GeocraftMap;
import dev.lesroseaux.geocraft.models.location.GeoCraftWorld;
import dev.lesroseaux.geocraft.models.location.PlayableZone;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GeocraftStart implements BasicCommand {
  private final HashMap<Player, Boolean> alreadyStarted;
  private GameManager gameManager = GameManager.getInstance();
  private final BukkitScheduler scheduler;
  private final Plugin plugin;

  public GeocraftStart(@NotNull Plugin plugin) {
    this.alreadyStarted = new HashMap<>();
    this.plugin = plugin;
    this.scheduler = plugin.getServer().getScheduler();
  }

  @Override
  public void execute(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] strings) {
    Player player = (Player) commandSourceStack.getSender();
    PlayableZone playableZone = null;
    // Update instance of the game
    gameManager = GameManager.getInstance();

    if (!alreadyStarted.containsKey(player)) {
      alreadyStarted.put(player, false);
    }
    if (!alreadyStarted.get(player)) {
      if (strings.length == 3) {
        // /geoCraft-start <region/city/district/world> <name> <duration>
        commandSourceStack.getSender().sendMessage("Geocraft configuration started.");
        switch (strings[0].toLowerCase(Locale.ROOT)) {
          case "region" -> playableZone = new RegionDao().getRegionByName(strings[1]);
          case "city" -> playableZone = new CityDao().getCityByName(strings[1]);
          case "district" -> playableZone = new DistrictDao().getDistrictByName(strings[1]);
          case "world" -> playableZone = new GeoCraftWorld(player.getWorld());
          default -> player.sendMessage("You must specify a valid type of playable area.");
        }
        LocationToMapDao locationToMapDao = new LocationToMapDao();
        if (locationToMapDao.getByPlayableZone(new LocationToMap(playableZone)) == null
            && locationToMapDao.getParentMap(new LocationToMap(playableZone)) == null) {
          player.sendMessage("The playable zone must be associated with a Map");
        }

        player.sendMessage("Re-execute the command to start the game.");
        alreadyStarted.put(player, true);

        plugin.getLogger().info("Duration : " + Long.parseLong(strings[2]));
        gameManager.setGameDuration(Duration.ofSeconds(Long.parseLong(strings[2])));
        gameManager.setPlayableZone(playableZone);
        gameManager.addPlayer(player);
        LocationToMap location = locationToMapDao.getParentMap(new LocationToMap(playableZone));
        if (location == null) {
          throw new RuntimeException("Location not found");
        } else {
          plugin.getLogger().info("Location found : " + location.getDatabaseId());
        }
        GeocraftMap geocraftMap = new MapDao().getByLocationToMapId(location);
        if (geocraftMap == null) {
          throw new RuntimeException("Map not found");
        }
        gameManager.setMap(geocraftMap);

        player.getServer().sendMessage(Component.text("A Geocraft game is starting, "
            + "created by : " + player.getName()));
        player.getServer().sendMessage(Component.text("Join the game by typing /geocraft-join"));

      } else {
        player.sendMessage("You must specify a type of playable area and the playable area name.");
      }
    } else {
      alreadyStarted.put(player, false);
      try {
        gameManager.initGame(plugin);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

  }

  @Override
  public @NotNull Collection<String> suggest(@NotNull CommandSourceStack commandSourceStack,
                                             @NotNull String[] args) {
    return BasicCommand.super.suggest(commandSourceStack, args);
  }

  @Override
  public boolean canUse(@NotNull CommandSender sender) {
    return BasicCommand.super.canUse(sender);
  }

  @Override
  public @Nullable String permission() {
    return BasicCommand.super.permission();
  }
}