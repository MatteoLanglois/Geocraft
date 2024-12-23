package dev.lesroseaux.geocraft.models.game;

import dev.lesroseaux.geocraft.models.LocationToMap;
import dev.lesroseaux.geocraft.models.location.City;
import dev.lesroseaux.geocraft.models.location.District;
import dev.lesroseaux.geocraft.models.location.GeoCraftWorld;
import dev.lesroseaux.geocraft.models.location.PlayableZone;
import dev.lesroseaux.geocraft.models.location.Region;
import org.bukkit.Material;

/**
 * Represents a GeoCraft map with various properties and materials.
 */
public class GeocraftMap {
  private int id;
  private int minX;
  private int minZ;
  private int maxX;
  private int maxZ;
  private int scale;
  private LocationToMap map;

  private final Material roadMaterial = Material.WHITE_CONCRETE;
  private final Material buildingMaterial = Material.GRAY_CONCRETE;
  private final Material waterMaterial = Material.BLUE_CONCRETE;
  private final Material grassMaterial = Material.GREEN_CONCRETE;
  private final Material sandMaterial = Material.YELLOW_CONCRETE;

  /**
   * Default constructor for GeocraftMap.
   */
  public GeocraftMap() {
  }

  /**
   * Constructs a GeocraftMap with specified parameters.
   *
   * @param minX  The minimum X coordinate.
   * @param minZ  The minimum Z coordinate.
   * @param maxX  The maximum X coordinate.
   * @param maxZ  The maximum Z coordinate.
   * @param scale The scale of the map.
   * @param map   The playable zone of the map.
   */
  public GeocraftMap(int minX, int minZ, int maxX, int maxZ, int scale, PlayableZone map) {
    this.id = 0;
    this.minX = minX;
    this.minZ = minZ;
    this.maxX = maxX;
    this.maxZ = maxZ;
    this.scale = scale;
    this.map = new LocationToMap(map);
  }

  /**
   * Constructs a GeocraftMap with specified parameters.
   *
   * @param minX  The minimum X coordinate.
   * @param minZ  The minimum Z coordinate.
   * @param maxX  The maximum X coordinate.
   * @param maxZ  The maximum Z coordinate.
   * @param scale The scale of the map.
   * @param map   The location to map.
   */
  public GeocraftMap(int minX, int minZ, int maxX, int maxZ, int scale, LocationToMap map) {
    this.id = 0;
    this.minX = minX;
    this.minZ = minZ;
    this.maxX = maxX;
    this.maxZ = maxZ;
    this.scale = scale;
    this.map = map;
  }

  /**
   * Gets the ID of the map.
   *
   * @return The map ID.
   */
  public int getId() {
    return id;
  }

  /**
   * Sets the ID of the map.
   *
   * @param id The map ID.
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Gets the minimum X coordinate.
   *
   * @return The minimum X coordinate.
   */
  public int getMinX() {
    return minX;
  }

  /**
   * Sets the minimum X coordinate.
   *
   * @param minX The minimum X coordinate.
   */
  public void setMinX(int minX) {
    this.minX = minX;
  }

  /**
   * Gets the minimum Z coordinate.
   *
   * @return The minimum Z coordinate.
   */
  public int getMinZ() {
    return minZ;
  }

  /**
   * Sets the minimum Z coordinate.
   *
   * @param minZ The minimum Z coordinate.
   */
  public void setMinZ(int minZ) {
    this.minZ = minZ;
  }

  /**
   * Gets the maximum X coordinate.
   *
   * @return The maximum X coordinate.
   */
  public int getMaxX() {
    return maxX;
  }

  /**
   * Sets the maximum X coordinate.
   *
   * @param maxX The maximum X coordinate.
   */
  public void setMaxX(int maxX) {
    this.maxX = maxX;
  }

  /**
   * Gets the maximum Z coordinate.
   *
   * @return The maximum Z coordinate.
   */
  public int getMaxZ() {
    return maxZ;
  }

  /**
   * Sets the maximum Z coordinate.
   *
   * @param maxZ The maximum Z coordinate.
   */
  public void setMaxZ(int maxZ) {
    this.maxZ = maxZ;
  }

  /**
   * Gets the scale of the map.
   *
   * @return The map scale.
   */
  public int getScale() {
    return scale;
  }

  /**
   * Sets the scale of the map.
   *
   * @param scale The map scale.
   */
  public void setScale(int scale) {
    this.scale = scale;
  }

  /**
   * Gets the playable zone of the map.
   *
   * @return The playable zone.
   */
  public PlayableZone getMap() {
    return map.getLocation();
  }

  /**
   * Gets the location to map.
   *
   * @return The location to map.
   */
  public LocationToMap getLocationToMap() {
    return map;
  }

  /**
   * Sets the playable zone of the map.
   *
   * @param world The playable zone.
   */
  public void setMap(PlayableZone world) {
    this.map = new LocationToMap(world);
  }

  /**
   * Sets the location to map.
   *
   * @param map The location to map.
   */
  public void setLocationToMap(LocationToMap map) {
    this.map = map;
  }

  /**
   * Gets the material used for roads.
   *
   * @return The road material.
   */
  public Material getRoadMaterial() {
    return roadMaterial;
  }

  /**
   * Gets the material used for buildings.
   *
   * @return The building material.
   */
  public Material getBuildingMaterial() {
    return buildingMaterial;
  }

  /**
   * Gets the material used for water.
   *
   * @return The water material.
   */
  public Material getWaterMaterial() {
    return waterMaterial;
  }

  /**
   * Gets the material used for grass.
   *
   * @return The grass material.
   */
  public Material getGrassMaterial() {
    return grassMaterial;
  }

  /**
   * Gets the location ID based on the type of location.
   *
   * @return The location ID.
   */
  public String getLocationId() {
    if (map.getLocation() instanceof GeoCraftWorld) {
      return map.getId().toString();
    } else if (map.getLocation() instanceof Region) {
      return (String) map.getId();
    } else if (map.getLocation() instanceof City) {
      return (String) map.getId();
    } else if (map.getLocation() instanceof District) {
      return (String) map.getId();
    }
    return null;
  }

  /**
   * Gets the database ID of the location.
   *
   * @return The location database ID.
   */
  public int getLocationDatabaseId() {
    return map.getDatabaseId();
  }

  /**
   * Returns a string representation of the map.
   *
   * @return A string representation of the map.
   */
  @Override
  public String toString() {
    return "Map{" +
        "id=" + id +
        ", minX=" + minX +
        ", minZ=" + minZ +
        ", maxX=" + maxX +
        ", maxZ=" + maxZ +
        ", scale=" + scale +
        ", map=" + map +
        '}';
  }

  /**
   * Gets the material used for sand.
   *
   * @return The sand material.
   */
  public Material getSandMaterial() {
    return sandMaterial;
  }
}