package dev.lesroseaux.geocraft.models.location;

import dev.lesroseaux.geocraft.data.dao.CityDao;
import java.util.ArrayList;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a region in GeoCraft, which is a playable zone.
 */
@Getter
@Setter
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
   * Gets the ID of the playable zone.
   *
   * @return The ID of the playable zone.
   */
  @Override
  public int getId() {
    return regionId;
  }
}