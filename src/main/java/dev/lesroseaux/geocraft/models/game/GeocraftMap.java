package dev.lesroseaux.geocraft.models.game;

import dev.lesroseaux.geocraft.models.LocationToMap;
import dev.lesroseaux.geocraft.models.location.City;
import dev.lesroseaux.geocraft.models.location.District;
import dev.lesroseaux.geocraft.models.location.GeoCraftWorld;
import dev.lesroseaux.geocraft.models.location.PlayableZone;
import dev.lesroseaux.geocraft.models.location.Region;
import org.bukkit.Material;

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

  public GeocraftMap() {

  }

  public GeocraftMap(int minX, int minZ, int maxX, int maxZ, int scale, PlayableZone map) {
    this.id = 0;
    this.minX = minX;
    this.minZ = minZ;
    this.maxX = maxX;
    this.maxZ = maxZ;
    this.scale = scale;
    this.map = new LocationToMap(map);
  }

  public GeocraftMap(int minX, int minZ, int maxX, int maxZ, int scale, LocationToMap map) {
    this.id = 0;
    this.minX = minX;
    this.minZ = minZ;
    this.maxX = maxX;
    this.maxZ = maxZ;
    this.scale = scale;
    this.map = map;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getMinX() {
    return minX;
  }

  public void setMinX(int minX) {
    this.minX = minX;
  }

  public int getMinZ() {
    return minZ;
  }

  public void setMinZ(int minZ) {
    this.minZ = minZ;
  }

  public int getMaxX() {
    return maxX;
  }

  public void setMaxX(int maxX) {
    this.maxX = maxX;
  }

  public int getMaxZ() {
    return maxZ;
  }

  public void setMaxZ(int maxZ) {
    this.maxZ = maxZ;
  }

  public int getScale() {
    return scale;
  }

  public void setScale(int scale) {
    this.scale = scale;
  }

  public PlayableZone getMap() {
    return map.getLocation();
  }

  public LocationToMap getLocationToMap() {
    return map;
  }

  public void setMap(PlayableZone world) {
    this.map = new LocationToMap(world);
  }

  public void setLocationToMap(LocationToMap map) {
    this.map = map;
  }

  public Material getRoadMaterial() {
    return roadMaterial;
  }

  public Material getBuildingMaterial() {
    return buildingMaterial;
  }

  public Material getWaterMaterial() {
    return waterMaterial;
  }

  public Material getGrassMaterial() {
    return grassMaterial;
  }

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

  public int getLocationDatabaseId() {
    return map.getDatabaseId();
  }

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

  public Material getSandMaterial() {
    return sandMaterial;
  }

}
