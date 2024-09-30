package dev.lesroseaux.geocraft.models.Location;

import dev.lesroseaux.geocraft.data.dao.CityDao;
import java.util.ArrayList;
import java.util.UUID;

public class Region implements PlayableZone {
  private int region_id;
  private String region_name;
  private UUID world_id;

  public Region(int region_id, String region_name, UUID world_id) {
    this.region_id = region_id;
    this.region_name = region_name;
    this.world_id = world_id;
  }

  public Region(String region_name, UUID world_id) {
    this(0, region_name, world_id);
  }

  public int getRegion_id() {
    return region_id;
  }

  public void setRegion_id(int region_id) {
    this.region_id = region_id;
  }

  public UUID getWorld_id() {
    return world_id;
  }

  public void setWorld_id(UUID world_id) {
    this.world_id = world_id;
  }

  public String getRegion_name() {
    return region_name;
  }

  public void setRegion_name(String region_name) {
    this.region_name = region_name;
  }

  @Override
  public ArrayList<Road> getZones() {
    CityDao cityDao = new CityDao();
    ArrayList<City> cities = cityDao.getAllCitiesByRegionId(region_id);
    ArrayList<Road> roads = new ArrayList<>();
    for (City city : cities) {
      roads.addAll(city.getZones());
    }
    return roads;
  }
}
