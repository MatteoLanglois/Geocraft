package dev.lesroseaux.geocraft.models.game;

import java.util.List;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

public class MapMaterials {
  public static void saveDefaultMaterials(Plugin plugin) {
    FileConfiguration config = plugin.getConfig();

    saveGrassMaterial(config);
    saveWaterMaterial(config);
    saveBuildingMaterial(config);
    saveRoadMaterial(config);
    saveSandMaterial(config);

    plugin.saveConfig();
  }

  private static void saveGrassMaterial(FileConfiguration config) {
    config.set("materials.grass", Arrays.asList(
        "GRASS_BLOCK", "OAK_LEAVES", "BIRCH_LEAVES", "SPRUCE_LEAVES", "JUNGLE_LEAVES",
        "ACACIA_LEAVES", "DARK_OAK_LEAVES", "AZALEA_LEAVES", "FARMLAND", "DIRT", "MANGROVE_LEAVES"));
  }

  private static void saveWaterMaterial(FileConfiguration config) {
    config.set("materials.water", List.of("WATER"));
  }

  private static void saveBuildingMaterial(FileConfiguration config) {
    config.set("materials.building", Arrays.asList(
        "STONE", "COBBLESTONE", "DIORITE", "POLISHED_BLACKSTONE_BRICK_STAIRS",
        "POLISHED_BLACKSTONE_BRICK_SLAB", "POLISHED_DEEPSLATE_SLAB", "POLISHED_DEEPSLATE_STAIRS",
        "WHITE_STAINED_GLASS", "WHITE_STAINED_GLASS_PANE", "TINTED_GLASS", "POLISHED_BLACKSTONE_BRICKS",
        "BLACK_TERRACOTTA", "POLISHED_ANDESITE", "POLISHED_ANDESITE_SLAB", "LIGHT_GRAY_TERRACOTTA",
        "ANDESITE", "POLISHED_DEEPSLATE", "GREEN_TERRACOTTA", "RED_TERRACOTTA", "GRAY_TERRACOTTA",
        "QUARTZ_BLOCK", "DEEPSLATE_BRICK_SLAB", "RED_WOOL", "BRICK", "BRICK_SLAB", "BRICK_STAIRS",
        "ANDESITE_SLAB", "BLACK_WOOL", "CALCITE", "LIGHT_GRAY_CONCRETE", "DEEPSLATE_BRICKS",
        "DEEPSLATE_BRICK_STAIRS", "SMOOTH_STONE", "BRICKS"));
  }

  private static void saveRoadMaterial(FileConfiguration config) {
    config.set("materials.road", Arrays.asList(
        "GRAY_CONCRETE", "WHITE_CONCRETE", "BLACK_CONCRETE", "DIRT_PATH", "GRAVEL",
        "MUD_BRICKS", "MUD_BRICK_SLAB", "COBBLED_DEEPSLATE", "POLISHED_DIORITE",
        "DEAD_HORN_CORAL_BLOCK", "COARSE_DIRT", "DEEPSLATE_TILES", "STONE_BRICKS",
        "SMOOTH_STONE_SLAB", "GRAY_WOOL", "BASALT", "QUARTZ_SLAB", "IRON_BARS",
        "PACKED_MUD", "GRANITE"));
  }

  private static void saveSandMaterial(FileConfiguration config) {
    config.set("materials.sand", Arrays.asList(
        "SAND", "SANDSTONE", "RED_SAND", "RED_SANDSTONE", "SMOOTH_SANDSTONE"));
  }
}