package dev.lesroseaux.geocraft.models.location;

import dev.lesroseaux.geocraft.data.dao.CityDao;
import java.util.ArrayList;
import java.util.UUID;

public class Region implements PlayableZone {
  private int regionId;
  private String regionName;
  private UUID worldId;

  public Region(int regionId, String regionName, UUID worldId) {
    this.regionId = regionId;
    this.regionName = regionName;
    this.worldId = worldId;
  }

  public Region(String regionName, UUID worldId) {
    this(0, regionName, worldId);
  }

  public int getRegionId() {
    return regionId;
  }

  public void setRegionId(int regionId) {
    this.regionId = regionId;
  }

  public UUID getWorldId() {
    return worldId;
  }

  public void setWorldId(UUID worldId) {
    this.worldId = worldId;
  }

  public String getRegionName() {
    return regionName;
  }

  public void setRegionName(String regionName) {
    this.regionName = regionName;
  }

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

  @Override
  public int getId() {
    return regionId;
  }
}
