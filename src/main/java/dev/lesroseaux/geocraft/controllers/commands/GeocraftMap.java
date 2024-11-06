package dev.lesroseaux.geocraft.controllers.commands;

import dev.lesroseaux.geocraft.controllers.MapBuilder;
import dev.lesroseaux.geocraft.data.dao.CityDao;
import dev.lesroseaux.geocraft.data.dao.DistrictDao;
import dev.lesroseaux.geocraft.data.dao.LocationToMapDao;
import dev.lesroseaux.geocraft.data.dao.MapDao;
import dev.lesroseaux.geocraft.data.dao.RegionDao;
import dev.lesroseaux.geocraft.data.dao.WorldDao;
import dev.lesroseaux.geocraft.models.LocationToMap;
import dev.lesroseaux.geocraft.models.location.GeoCraftWorld;
import dev.lesroseaux.geocraft.models.location.PlayableZone;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GeocraftMap implements BasicCommand {
  private final Plugin plugin;

  public GeocraftMap(Plugin plugin) {
    this.plugin = plugin;
  }

  /**
   * Executes the command with the given {@link CommandSourceStack} and arguments.
   *
   * @param commandSourceStack the commandSourceStack of the command
   * @param args               the arguments of the command ignoring repeated spaces
   */
  @Override
public void execute(@NotNull CommandSourceStack commandSourceStack, String [] args) {
  CompletableFuture.runAsync(() -> {
    Player sender = (Player) commandSourceStack.getSender();
    if (args.length == 0) {
      commandSourceStack.getSender().sendMessage("You must specify a subcommand.");
    } else {
      switch (args[0]) {
        case "create" -> {
          if (args.length < 5) {
            commandSourceStack.getSender().sendMessage(
                "You must specify a min and max coordinates (x and z).");
            return;
          } else if (args.length < 6) {
            commandSourceStack.getSender().sendMessage("You must specify a zone type.");
            return;
          } else if (args.length < 7) {
            commandSourceStack.getSender().sendMessage("You must specify a zone name.");
            return;
          }
          PlayableZone zone;
          switch (args[5]) {
            case "world" -> {
              zone = new GeoCraftWorld(sender.getWorld());
            }
            case "region" -> {
              RegionDao regionDao = new RegionDao();
              zone = regionDao.getRegionByName(args[6]);
              if (zone == null) {
                commandSourceStack.getSender().sendMessage("Region not found.");
                return;
              }
            }
            case "city" -> {
              zone = new CityDao().getCityByName(args[6]);
              if (zone == null) {
                commandSourceStack.getSender().sendMessage("City not found.");
                return;
              }
            }
            case "district" -> {
              zone = new DistrictDao().getDistrictByName(args[6]);
              if (zone == null) {
                commandSourceStack.getSender().sendMessage("District not found.");
                return;
              }
            }
            default -> {
              commandSourceStack.getSender().sendMessage("Invalid zone type.");
              return;
            }
          }
          try {
            MapDao mapDao = new MapDao();
            LocationToMapDao locationToMapDao = new LocationToMapDao();
            LocationToMap locationToMap = new LocationToMap(zone);
            int id = locationToMapDao.insert(locationToMap);
            locationToMap.setDatabaseId(id);
            mapDao.insert(new dev.lesroseaux.geocraft.models.game.GeocraftMap(Integer.parseInt(args[1]), Integer.parseInt(args[2]),
                Integer.parseInt(args[3]), Integer.parseInt(args[4]), 10, locationToMap));
            commandSourceStack.getSender().sendMessage("Map created.");
          } catch (NumberFormatException e) {
            commandSourceStack.getSender().sendMessage("Invalid coordinates.");
          }
        }
        case "edit" -> commandSourceStack.getSender().sendMessage("Map edited.");
        case "remove" -> commandSourceStack.getSender().sendMessage("Map removed.");
        case "info" -> commandSourceStack.getSender().sendMessage("Map info.");
        case "build" -> {
          commandSourceStack.getSender().sendMessage("Building map...");
          LocationToMap location = new LocationToMapDao().getParentMap(
              new LocationToMap(new WorldDao().getByUuid(sender.getWorld().getUID())));
          dev.lesroseaux.geocraft.models.game.GeocraftMap geocraftMap = new MapDao().getByLocationToMapId(location);
          if (geocraftMap == null || geocraftMap.getMap() == null) {
            commandSourceStack.getSender().sendMessage("Map not found.");
            return;
          }
          geocraftMap.setMap(new GeoCraftWorld(sender.getWorld()));
          FileConfiguration config = plugin.getConfig();
          Location startLocation = new Location(sender.getWorld(),
              config.getInt("mapLocation.x"),
              config.getInt("mapLocation.y"),
              config.getInt("mapLocation.z"));
          MapBuilder mapBuilder = new MapBuilder(geocraftMap, startLocation, plugin, null);
          mapBuilder.build();
        }
        default -> commandSourceStack.getSender().sendMessage("Invalid subcommand.");
      }
    }
  }).exceptionally(e -> {
    commandSourceStack.getSender().sendMessage("An error occurred: " + e.getMessage());
    return null;
  });
}

  /**
   * Suggests possible completions for the given command {@link CommandSourceStack} and arguments.
   *
   * @param commandSourceStack the commandSourceStack of the command
   * @param args               the arguments of the command including repeated spaces
   * @return a collection of suggestions
   */
  @Override
  public @NotNull Collection<String> suggest(@NotNull CommandSourceStack commandSourceStack,
                                             @NotNull String[] args) {
    if (args.length == 1) {
      return List.of("create", "edit", "remove", "info");
    }
    return BasicCommand.super.suggest(commandSourceStack, args);
  }

  /**
   * Checks whether a command sender can receive and run the root command.
   *
   * @param sender the command sender trying to execute the command
   * @return whether the command sender fulfills the root command requirement
   * @see #permission()
   */
  @Override
  public boolean canUse(@NotNull CommandSender sender) {
    return BasicCommand.super.canUse(sender);
  }

  /**
   * Returns the permission for the root command used in {@link #canUse(CommandSender)} by default.
   *
   * @return the permission for the root command used in {@link #canUse(CommandSender)}
   */
  @Override
  public @Nullable String permission() {
    return BasicCommand.super.permission();
  }
}
