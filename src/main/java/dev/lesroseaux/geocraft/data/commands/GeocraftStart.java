package dev.lesroseaux.geocraft.data.commands;

import dev.lesroseaux.geocraft.data.dao.CityDao;
import dev.lesroseaux.geocraft.data.dao.DistrictDao;
import dev.lesroseaux.geocraft.data.dao.PlayerDao;
import dev.lesroseaux.geocraft.data.dao.RegionDao;
import dev.lesroseaux.geocraft.models.Game.Game;
import dev.lesroseaux.geocraft.models.Location.PlayableZone;
import dev.lesroseaux.geocraft.models.Location.Road;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GeocraftStart implements BasicCommand {
  private HashMap<Player, Boolean> alreadyStarted;
  private Game game = Game.getInstance();

  public GeocraftStart() {
    this.alreadyStarted = new HashMap<>();
  }

  @Override
  public void execute(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] strings) {
    Player player = (Player) commandSourceStack.getSender();
    PlayableZone playableZone = null;
    // Update instance of the game
    game = Game.getInstance();

    if (!alreadyStarted.containsKey(player)) {
      alreadyStarted.put(player, false);
    }
    if (!alreadyStarted.get(player)) {
      if (strings.length == 3) {
        commandSourceStack.getSender().sendMessage("Geocraft configuration started.");
        switch (strings[0]) {
          case "region" -> playableZone = new RegionDao().getRegionByName(strings[1]);
          case "city" -> playableZone = new CityDao().getCityByName(strings[1]);
          case "district" -> playableZone = new DistrictDao().getDistrictByName(strings[1]);
          default -> player.sendMessage("You must specify a valid type of playable area.");
        }
        player.sendMessage("Re-execute the command to start the game.");
        alreadyStarted.put(player, true);

        game.setGameDuration(Duration.ofMinutes(Long.parseLong(strings[2])));
        game.setPlayableZone(playableZone);
        game.addPlayer(player);

        player.getServer().sendMessage(Component.text("A Geocraft game is starting, created by : " + player.getName()));
        player.getServer().sendMessage(Component.text("Join the game by typing /geocraft-join"));

      } else {
        player.sendMessage("You must specify a type of playable area and the playable area name.");
      }
    } else {
      alreadyStarted.put(player, false);
      game.StartGame();
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
