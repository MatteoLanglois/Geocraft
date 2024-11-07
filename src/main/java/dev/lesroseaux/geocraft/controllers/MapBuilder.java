package dev.lesroseaux.geocraft.controllers;

import dev.lesroseaux.geocraft.models.game.GeocraftMap;
import dev.lesroseaux.geocraft.models.location.GeoCraftWorld;
import dev.lesroseaux.geocraft.models.location.PlayableZone;
import dev.lesroseaux.geocraft.models.location.Road;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class MapBuilder {
  private final GeocraftMap geocraftMap;
  private final Location start;
  private final Plugin plugin;
  private final Runnable callback;
  private final BossBar bossBar;

  public MapBuilder(GeocraftMap geocraftMap, Location start, Plugin plugin, Runnable callback) {
    this.geocraftMap = geocraftMap;
    this.start = start;
    this.plugin = plugin;
    this.callback = callback;
    this.bossBar = Bukkit.createBossBar("Building Map", BarColor.BLUE, BarStyle.SOLID);
  }

  public void build() {
    if (geocraftMap.getMap() == null) {
      throw new RuntimeException("Map is null");
    }
    World world = getWorld();
    plugin.getServer().getConsoleSender().sendMessage("Building map... This may take a while.");
    bossBar.setVisible(true);
    Bukkit.getOnlinePlayers().forEach(bossBar::addPlayer);
    CompletableFuture.runAsync(() -> processAndPlaceBlocks(world))
      .thenRun(() -> {
        bossBar.setVisible(false);
        callback.run();
      })
      .exceptionally(ex -> {
        plugin.getServer().getConsoleSender().sendMessage("Error while building map: " + ex.getMessage());
        bossBar.setVisible(false);
        return null;
      });
  }

  private FileConfiguration loadConfig() {
    File configFile = new File(plugin.getDataFolder(), "config.yml");
    if (!configFile.exists()) {
      plugin.saveResource("config.yml", false);
    }
    return YamlConfiguration.loadConfiguration(configFile);
  }

  private void processAndPlaceBlocks(World world) {
    int newX = this.start.getBlockX();
    int newZ = this.start.getBlockZ();
    int scale = geocraftMap.getScale();
    int totalBlocks = (geocraftMap.getMaxX() - geocraftMap.getMinX()) * (geocraftMap.getMaxZ() - geocraftMap.getMinZ());
    int processedBlocks = 0;

    for (int x = geocraftMap.getMinX(); x < geocraftMap.getMaxX(); x += scale) {
      for (int z = geocraftMap.getMinZ(); z < geocraftMap.getMaxZ(); z += scale) {
        HashMap<Material, Integer> blockCount = new HashMap<>();
        for (int xx = 0; xx < scale; xx++) {
          for (int zz = 0; zz < scale; zz++) {
            int y;
            if (this.start.getX() < geocraftMap.getMaxX() && this.start.getX() > geocraftMap.getMinX()
                && this.start.getZ() < geocraftMap.getMaxZ() && this.start.getZ() > geocraftMap.getMinZ()) {
              y = this.start.getBlockY() - 1;
              while (world.getBlockAt(x + xx, y, z + zz).getType() == Material.AIR) {
                y--;
              }
            } else {
              y = world.getHighestBlockYAt(x + xx, z + zz);
            }

            Block block = world.getBlockAt(x + xx, y, z + zz);
            Material material = block.getType();
            blockCount.put(material, blockCount.getOrDefault(material, 0) + 1);
          }
        }
        Material mostFrequentBlock = getMostFrequentBlock(blockCount);
        Location location = new Location(world, newX, start.getBlockY(), newZ);
        newZ++;
        world.getChunkAtAsync(location).thenAccept(chunk -> {
          Bukkit.getScheduler().runTask(plugin, () -> {
            Block block = world.getBlockAt(location);
            block.setType(getMaterialForBlock(mostFrequentBlock));
          });
        });

        processedBlocks += scale * scale;
        double progress = (double) processedBlocks / totalBlocks;
        bossBar.setProgress(progress);
      }
      newX++;
      newZ = this.start.getBlockZ();
    }
    plugin.getServer().getConsoleSender().sendMessage("Map built.");
  }

  private Material getMaterialForBlock(Material mostFrequentBlock) {
    if (isMaterialInList(mostFrequentBlock, "materials.grass")) {
      return geocraftMap.getGrassMaterial();
    } else if (isMaterialInList(mostFrequentBlock, "materials.water")) {
      return geocraftMap.getWaterMaterial();
    } else if (isMaterialInList(mostFrequentBlock, "materials.building")) {
      return geocraftMap.getBuildingMaterial();
    } else if (isMaterialInList(mostFrequentBlock, "materials.road")) {
      return geocraftMap.getRoadMaterial();
    } else if (isMaterialInList(mostFrequentBlock, "materials.sand")) {
      return geocraftMap.getSandMaterial();
    } else {
      return mostFrequentBlock;
    }
  }

  private boolean isMaterialInList(Material material, String path) {
    FileConfiguration config = loadConfig();
    List<String> materials = config.getStringList(path);
    return materials.contains(material.name());
  }

  private @NotNull World getWorld() {
    World world = null;
    if (geocraftMap.getMap() instanceof GeoCraftWorld) {
      PlayableZone mapZone = this.geocraftMap.getMap();
      world = ((GeoCraftWorld) mapZone).getWorld();
    } else {
      ArrayList<Road> roads = geocraftMap.getMap().getZones();
      if (roads != null && !roads.isEmpty()) {
        world = roads.getFirst().getPoint1().getWorld();
      }
    }
    if (world == null) {
      throw new RuntimeException("World is null for map: " + geocraftMap);
    }
    return world;
  }

  private Material getMostFrequentBlock(HashMap<Material, Integer> blockCount) {
    return blockCount.entrySet().stream()
      .max(java.util.Map.Entry.comparingByValue())
      .map(HashMap.Entry::getKey)
      .orElse(Material.PINK_CONCRETE);
  }

  public Location getCenterOfGuessMap() {
    double x_map_length = geocraftMap.getMaxX() - geocraftMap.getMinX();
    double z_map_length = geocraftMap.getMaxZ() - geocraftMap.getMinZ();
    double x = this.start.getX() + (x_map_length / this.geocraftMap.getScale()) / 2;
    double z = this.start.getZ() + (z_map_length / this.geocraftMap.getScale()) / 2;
    return new Location(getWorld(), x, start.getBlockY() + 1, z);
  }

  public void removeMap() {
    World world = getWorld();
    double x_map_length = (double) (geocraftMap.getMaxX() - geocraftMap.getMinX()) / geocraftMap.getScale();
    double z_map_length = (double) (geocraftMap.getMaxZ() - geocraftMap.getMinZ()) / geocraftMap.getScale();
    for (int x = geocraftMap.getMinX(); x < x_map_length; x++) {
      for (int z = geocraftMap.getMinZ(); z < z_map_length; z++) {
        Location location = new Location(world, x, start.getBlockY(), z);
        world.getChunkAtAsync(location).thenAccept(chunk -> {
          Bukkit.getScheduler().runTask(plugin, () -> {
            Block block = world.getBlockAt(location);
            block.setType(Material.AIR);
          });
        });
      }
    }
  }

  public Location getStart() {
    return start;
  }
}