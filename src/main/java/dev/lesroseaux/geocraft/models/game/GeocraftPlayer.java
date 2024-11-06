package dev.lesroseaux.geocraft.models.game;

import dev.lesroseaux.geocraft.utils.Banner;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.jetbrains.annotations.NotNull;

public class GeocraftPlayer {
  private final Player player;
  private final Banner banner;
  private final Location lastPos;
  private Location tpLocation;
  private Location guessedLocation;
  private boolean isAtGuessMap;
  private Location mapCenter;

  public GeocraftPlayer(Player player, Banner banner) {
    this.player = player;
    this.banner = banner;
    this.lastPos = player.getLocation();
    this.tpLocation = null;
    this.guessedLocation = null;
    this.isAtGuessMap = false;
    this.mapCenter = null;
  }

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

  public void resetInventory() {
    // TODO : Reset the player's inventory with the previous one
  }

  public Player getPlayer() {
    return player;
  }

  public Banner getBanner() {
    return banner;
  }

  public Location getLastPos() {
    return lastPos;
  }

  public void teleportToRandom(Location location) {
    this.tpLocation = location;
    player.teleport(location);
  }

  public Location getTpLocation() {
    return tpLocation;
  }

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

  public boolean isAtGuessMap() {
    return isAtGuessMap;
  }

  public void setGuessedLocation(@NotNull Location location) {
    guessedLocation = location;
  }

  public Location getGuessedLocation() {
    return guessedLocation;
  }

  public void setMapCenter(Location center) {
    this.mapCenter = center;
  }
}
