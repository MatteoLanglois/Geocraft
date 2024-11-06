package dev.lesroseaux.geocraft.models.other;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

public class InventorySaver implements InventoryHolder {
  private final PlayerInventory inventory;

  public InventorySaver(PlayerInventory inventory) {
    this.inventory = inventory;
  }


  /**
   * Get the object's inventory.
   *
   * @return The inventory.
   */
  @Override
  public @NotNull Inventory getInventory() {
    return null;
  }
}
