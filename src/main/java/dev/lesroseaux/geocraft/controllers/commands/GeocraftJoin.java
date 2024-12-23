package dev.lesroseaux.geocraft.controllers.commands;

import dev.lesroseaux.geocraft.models.game.Game;
import dev.lesroseaux.geocraft.models.game.GameManager;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import java.util.Collection;
import java.util.HashMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Command class for handling the "join" commands in GeoCraft.
 */
public class GeocraftJoin implements BasicCommand {

  /**
   * Executes the command with the given {@link CommandSourceStack} and arguments.
   *
   * @param commandSourceStack the commandSourceStack of the command
   * @param args               the arguments of the command ignoring repeated spaces
   */
  @Override
  public void execute(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] args) {
    // Update instance of the game
    GameManager gameManager = GameManager.getInstance();

    if (commandSourceStack.getSender() instanceof Player player) {
      if (!gameManager.isGameStarted()) {
        if (gameManager.isPlayerInGame(player)) {
          player.sendMessage("You have already joined the game.");
        } else {
          gameManager.addPlayer(player);
          player.sendMessage("You joined the game.");
        }
      } else {
        player.sendMessage("The game has already started.");
      }
    } else {
      commandSourceStack.getSender().sendMessage("Only players can join the game.");
    }
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