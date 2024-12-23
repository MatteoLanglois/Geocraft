package dev.lesroseaux.geocraft.models.game;

import dev.lesroseaux.geocraft.utils.Banner;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * Listener class for handling guessing events in GeoCraft.
 */
public class GuessChecker implements Listener {

  /**
   * Event handler for block placement during guessing.
   *
   * @param event The BlockPlaceEvent triggered when a block is placed.
   */
  @EventHandler
  public void onGuessing(BlockPlaceEvent event) {
    if (event.isCancelled()
        || Banner.bannersList.stream().noneMatch(
            banner -> banner.getMaterial() == event.getBlockPlaced().getType())) {
      return;
    }
    Player player = event.getPlayer();
    GameManager gameManager = GameManager.getInstance();
    if (!gameManager.isGameStarted() || !gameManager.isPlayerInGame(player)) {
      return;
    }
    GeocraftPlayer geocraftPlayer = gameManager.getPlayer(player);
    if (geocraftPlayer != null && event.getBlockPlaced().getType() == geocraftPlayer.getBanner().getMaterial()) {
      if (geocraftPlayer.isAtGuessMap()) {
        if (geocraftPlayer.getGuessedLocation() != null) {
          geocraftPlayer.getGuessedLocation().getBlock().setType(Material.AIR);
        }
        geocraftPlayer.setGuessedLocation(event.getBlockPlaced().getLocation());
      } else {
        event.setCancelled(true);
        player.sendMessage("You can only place your banner at the guess map.");
      }
    } else {
      event.setCancelled(true);
      player.sendMessage("You can only place your banner.");
    }
  }
}