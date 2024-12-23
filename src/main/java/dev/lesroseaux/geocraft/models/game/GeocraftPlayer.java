package dev.lesroseaux.geocraft.models.game;

import dev.lesroseaux.geocraft.utils.Banner;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a player in GeoCraft with additional game-related properties.
 */
public class GeocraftPlayer {
  private final Player player;
  private final Banner banner;
  private final Location lastPos;
  private Location tpLocation;
  private Location guessedLocation;
  private boolean isAtGuessMap;
  private Location mapCenter;

  /**
   * Constructs a new GeocraftPlayer instance.
   *
   * @param player The Bukkit player.
   * @param banner The player's banner.
   */
  public GeocraftPlayer(Player player, Banner banner) {
    this.player = player;
    this.banner = banner;
    this.lastPos = player.getLocation();
    this.tpLocation = null;
    this.guessedLocation = null;
    this.isAtGuessMap = false;
    this.mapCenter = null;
  }

  /**
   * Sets the player's inventory with the game-specific items.
   */
  public void setInventory() {
    PlayerInventory inventory = this.player.getInventory();
    inventory.clear();
    inventory.addItem(banner.getItem());
    inventory.addItem(new ItemStack(Material.COMPASS));
    ItemStack helmet = ItemStack.of(Material.LEATHER_HELMET);
    LeatherArmorMeta meta = (LeatherArmorMeta) helmet.getItemMeta();
    meta.setColor(banner.getDyeColor());
    helmet.setItemMeta(meta);
    inventory.setHelmet(helmet);
  }

  /**
   * Resets the player's inventory to the previous state.
   * TODO: Implement the method to reset the player's inventory.
   */
  public void resetInventory() {
    // TODO : Reset the player's inventory with the previous one
  }

  /**
   * Gets the Bukkit player.
   *
   * @return The Bukkit player.
   */
  public Player getPlayer() {
    return player;
  }

  /**
   * Gets the player's banner.
   *
   * @return The player's banner.
   */
  public Banner getBanner() {
    return banner;
  }

  /**
   * Gets the player's last position.
   *
   * @return The player's last position.
   */
  public Location getLastPos() {
    return lastPos;
  }

  /**
   * Teleports the player to a random location.
   *
   * @param location The location to teleport to.
   */
  public void teleportToRandom(Location location) {
    this.tpLocation = location;
    player.teleport(location);
  }

  /**
   * Gets the teleport location.
   *
   * @return The teleport location.
   */
  public Location getTpLocation() {
    return tpLocation;
  }

  /**
   * Teleports the player to the guess map or back to the previous location.
   */
  public void teleportToGuess() {
    if (isAtGuessMap) {
      player.teleport(tpLocation);
      isAtGuessMap = false;
    } else {
      if (guessedLocation == null) {
        player.teleport(mapCenter);
      } else {
        player.teleport(guessedLocation);
      }
      isAtGuessMap = true;
    }
  }

  /**
   * Checks if the player is at the guess map.
   *
   * @return True if the player is at the guess map, false otherwise.
   */
  public boolean isAtGuessMap() {
    return isAtGuessMap;
  }

  /**
   * Sets the guessed location.
   *
   * @param location The guessed location.
   */
  public void setGuessedLocation(@NotNull Location location) {
    guessedLocation = location;
  }

  /**
   * Gets the guessed location.
   *
   * @return The guessed location.
   */
  public Location getGuessedLocation() {
    return guessedLocation;
  }

  /**
   * Sets the map center location.
   *
   * @param center The map center location.
   */
  public void setMapCenter(Location center) {
    this.mapCenter = center;
  }
}