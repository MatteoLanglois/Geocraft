package dev.lesroseaux.geocraft.models.Game;

import org.bukkit.Material;

public class Map {
  private int minX;
  private int minZ;

  private int maxX;
  private int maxZ;

  private int scale;

  private final Material roadMaterial = Material.WHITE_CONCRETE;
  private final Material buildingMaterial = Material.GRAY_CONCRETE;
  private final Material waterMaterial = Material.BLUE_CONCRETE;
  private final Material grassMaterial = Material.GREEN_CONCRETE;
}
