package dev.lesroseaux.geocraft.controllers.commands;

import dev.lesroseaux.geocraft.GeoCraft;
import dev.lesroseaux.geocraft.data.dao.CityDao;
import dev.lesroseaux.geocraft.data.dao.DistrictDao;
import dev.lesroseaux.geocraft.data.dao.RegionDao;
import dev.lesroseaux.geocraft.data.dao.RoadDao;
import dev.lesroseaux.geocraft.data.dao.WorldDao;
import dev.lesroseaux.geocraft.models.location.City;
import dev.lesroseaux.geocraft.models.location.District;
import dev.lesroseaux.geocraft.models.location.GeoCraftWorld;
import dev.lesroseaux.geocraft.models.location.Region;
import dev.lesroseaux.geocraft.models.location.Road;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Command class for creating various geographical entities in GeoCraft.
 */
public class GeocraftCreate implements BasicCommand {
  private final GeoCraft plugin;

  /**
   * Constructor for GeocraftCreate.
   *
   * @param plugin The GeoCraft plugin instance.
   */
  public GeocraftCreate(GeoCraft plugin) {
    this.plugin = plugin;
  }

  /**
   * Executes the command based on the provided arguments.
   *
   * @param commandSourceStack The source of the command.
   * @param strings The command arguments.
   */
  @Override
  public void execute(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] strings) {
    Player sender = (Player) commandSourceStack.getSender();
    switch (strings[0]) {
      case "region" -> {
        if (strings.length > 1) {
          // Check if the world is already in the database
          if (new WorldDao().getByUuid(sender.getWorld().getUID()) == null) {
            new WorldDao().insert(new GeoCraftWorld(sender.getWorld()));
          }
          new RegionDao().insert(
              new Region(strings[1], sender.getWorld().getUID()));
          sender.sendMessage(Component.text("Region created."));
        } else {
          sender.sendMessage(Component.text("You must specify a region name."));
        }
      }
      case "city" -> {
        if (strings.length > 2) {
          Region region = new RegionDao().getRegionByName(strings[2]);
          if (region == null) {
            sender.sendMessage(Component.text("Region " + strings[2] + " not found."));
            return;
          }
          new CityDao().insert(new City(strings[1], region.getRegionId()));
          sender.sendMessage(Component.text("City created."));
        } else {
          sender.sendMessage(Component.text("You must specify a city name and a region name."));
        }
      }
      case "district" -> {
        if (strings.length > 2) {
          City city = new CityDao().getCityByName(strings[2]);
          if (city != null) {
            new DistrictDao().insert(new District(0, strings[1], city.getCityId()));
            sender.sendMessage(Component.text("District created."));
          } else {
            sender.sendMessage(Component.text("City " + strings[2] + " + not found."));
          }
        } else {
          sender.sendMessage(
              Component.text("You must specify a city name and a district name."));
        }
      }
      case "road" -> {
        // /geocraft create road <name> <district>
        if (plugin.getTempPoint1() != null && plugin.getTempPoint2() != null
            && strings.length > 1) {
          District district = new DistrictDao().getDistrictByName(strings[2]);
          new RoadDao().insert(
              new Road(plugin.getTempPoint1(), plugin.getTempPoint2(), strings[1],
                  district.getDistrictId()));
          sender.sendMessage(Component.text("Zone created."));
        } else if (plugin.getTempPoint1() == null || plugin.getTempPoint2() == null) {
          sender.sendMessage(
              Component.text("You must select two points first. Use a compass "
                  + "to select points."));
        } else {
          sender.sendMessage(Component.text("You must specify a zone name."));
        }
      }
      default -> sender.sendMessage(Component.text("Invalid zone type."));
    }
  }

  /**
   * Provides command suggestions based on the current input.
   *
   * @param commandSourceStack The source of the command.
   * @param args The current command arguments.
   * @return A collection of suggested command completions.
   */
  @Override
  public @NotNull Collection<String> suggest(@NotNull CommandSourceStack commandSourceStack,
                                             @NotNull String @NotNull [] args) {
    ArrayList<String> suggests = new ArrayList<>();
    if (args.length == 0) {
      return List.of(new String[] {"region", "city", "district", "road"});
    } else if (args.length == 3) {
      return switch (args[0]) {
        case "city" -> {
          for (Region r : new RegionDao().getAllRegionsByWorldId(
              ((Player) commandSourceStack.getSender()).getWorld().getUID())) {
            suggests.add(r.getRegionName());
          }
          yield suggests;
        }
        case "district" -> {
          for (City r : new CityDao().getAll()) {
            suggests.add(r.getCityName());
          }
          yield suggests;
        }
        case "road" -> {
          for (District r : new DistrictDao().getAll()) {
            suggests.add(r.getDistrictName());
          }
          yield suggests;
        }
        default -> BasicCommand.super.suggest(commandSourceStack, args);
      };
    }
    return BasicCommand.super.suggest(commandSourceStack, args);
  }

  /**
   * Checks if the sender can use the command.
   *
   * @param sender The command sender.
   * @return True if the sender can use the command, false otherwise.
   */
  @Override
  public boolean canUse(@NotNull CommandSender sender) {
    return BasicCommand.super.canUse(sender);
  }

  /**
   * Returns the permission required to use the command.
   *
   * @return The required permission, or null if none is required.
   */
  @Override
  public @Nullable String permission() {
    return BasicCommand.super.permission();
  }
}