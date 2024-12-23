package dev.lesroseaux.geocraft.controllers.commands;

import dev.lesroseaux.geocraft.models.game.GameManager;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import java.util.Collection;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;

/**
 * Command class for handling the "guess" commands in GeoCraft.
 */
public class GeocraftGuess implements BasicCommand {

  /**
   * Executes the command with the given {@link CommandSourceStack} and arguments.
   *
   * @param commandSourceStack the commandSourceStack of the command
   * @param args               the arguments of the command ignoring repeated spaces
   */
  @Override
  public void execute(CommandSourceStack commandSourceStack, String[] args) {
    if (args.length < 1) {
      commandSourceStack.getSender().sendMessage("Invalid subcommand.");
      return;
    }
    switch (args[0]) {
      case "tp":
        // Teleport to the guess location or return to the game map
        commandSourceStack.getSender().sendMessage("Teleporting to the guess location.");
        GameManager gameManager = GameManager.getInstance();
        if (!gameManager.isGameStarted()) {
          commandSourceStack.getSender().sendMessage("The game is not started.");
          return;
        }
        if (gameManager.isPlayerInGame((Player) commandSourceStack.getSender())) {
          gameManager.teleportPlayerToGuessMap((Player) commandSourceStack.getSender());
        } else {
          commandSourceStack.getSender().sendMessage("You are not in a game.");
        }
        break;
      case "guess":
        // Finish the game early
        commandSourceStack.getSender().sendMessage("Guessing the location.");
        break;
      default:
        commandSourceStack.getSender().sendMessage("Invalid subcommand.");
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
  public Collection<String> suggest(CommandSourceStack commandSourceStack, String[] args) {
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
  public boolean canUse(CommandSender sender) {
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