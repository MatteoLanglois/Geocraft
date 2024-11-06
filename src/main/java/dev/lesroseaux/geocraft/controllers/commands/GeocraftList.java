package dev.lesroseaux.geocraft.controllers.commands;

import dev.lesroseaux.geocraft.data.dao.RoadDao;
import dev.lesroseaux.geocraft.models.location.Road;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import java.util.Collection;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GeocraftList implements BasicCommand {
  @Override
  public void execute(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] strings) {
    commandSourceStack.getSender().sendMessage(Road.printAllZones(new RoadDao().getAll()));
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
