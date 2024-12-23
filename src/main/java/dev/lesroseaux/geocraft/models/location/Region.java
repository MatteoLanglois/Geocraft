package dev.lesroseaux.geocraft.models.location;

import dev.lesroseaux.geocraft.data.dao.CityDao;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Represents a region in GeoCraft, which is a playable zone.
 */
public class Region implements PlayableZone {
  private int regionId;
  private String regionName;
  private UUID worldId;

  /**
   * Constructs a Region with the specified region ID, name, and world ID.
   *
   * @param regionId The ID of the region.
   * @param regionName The name of the region.
   * @param worldId The UUID of the world the region belongs to.
   */
  public Region(int regionId, String regionName, UUID worldId) {
    this.regionId = regionId;
    this.regionName = regionName;
    this.worldId = worldId;
  }

  /**
   * Constructs a Region with the specified name and world ID.
   * The region ID is set to 0.
   *
   * @param regionName The name of the region.
   * @param worldId The UUID of the world the region belongs to.
   */
  public Region(String regionName, UUID worldId) {
    this(0, regionName, worldId);
  }

  /**
   * Gets the ID of the region.
   *
   * @return The region ID.
   */
  public int getRegionId() {
    return regionId;
  }

  /**
   * Sets the ID of the region.
   *
   * @param regionId The region ID.
   */
  public void setRegionId(int regionId) {
    this.regionId = regionId;
  }

  /**
   * Gets the UUID of the world the region belongs to.
   *
   * @return The UUID of the world.
   */
  public UUID getWorldId() {
    return worldId;
  }

  /**
   * Sets the UUID of the world the region belongs to.
   *
   * @param worldId The UUID of the world.
   */
  public void setWorldId(UUID worldId) {
    this.worldId = worldId;
  }

  /**
   * Gets the name of the region.
   *
   * @return The region name.
   */
  public String getRegionName() {
    return regionName;
  }

  /**
   * Sets the name of the region.
   *
   * @param regionName The region name.
   */
  public void setRegionName(String regionName) {
    this.regionName = regionName;
  }

  /**
   * Gets the zones (roads) within the region.
   *
   * @return A list of roads in the region.
   */
  @Override
  public ArrayList<Road> getZones() {
    CityDao cityDao = new CityDao();
    ArrayList<City> cities = cityDao.getAllCitiesByRegionId(regionId);
    ArrayList<Road> roads = new ArrayList<>();
    for (City city : cities) {
      roads.addAll(city.getZones());
    }
    return roads;
  }

  /**
   * Gets the ID of the region.
   *
   * @return The region ID.
   */
  @Override
  public int getId() {
    return regionId;
  }
}