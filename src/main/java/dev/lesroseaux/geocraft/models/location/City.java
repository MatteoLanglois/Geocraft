package dev.lesroseaux.geocraft.models.location;

import dev.lesroseaux.geocraft.data.dao.DistrictDao;
import dev.lesroseaux.geocraft.data.dao.RoadDao;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a city in GeoCraft, which is a playable zone.
 */
@Setter
@Getter
public class City implements PlayableZone {
  private int cityId;
  private String cityName;
  private int regionId;

  /**
   * Constructs a City with the specified city ID, name, and region ID.
   *
   * @param cityId The ID of the city.
   * @param cityName The name of the city.
   * @param regionId The ID of the region the city belongs to.
   */
  public City(int cityId, String cityName, int regionId) {
    this.cityId = cityId;
    this.cityName = cityName;
    this.regionId = regionId;
  }

  /**
   * Constructs a City with the specified name and region ID.
   * The city ID is set to 0.
   *
   * @param cityName The name of the city.
   * @param regionId The ID of the region the city belongs to.
   */
  public City(String cityName, int regionId) {
    this(0, cityName, regionId);
  }

  /**
   * Gets the zones (roads) within the city.
   *
   * @return A list of roads in the city.
   */
  @Override
  public ArrayList<Road> getZones() {
    DistrictDao districtDao = new DistrictDao();
    ArrayList<District> districts = districtDao.getAllDistrictsByCityId(cityId);
    ArrayList<Road> roads = new ArrayList<>();
    RoadDao roadDao = new RoadDao();
    for (District district : districts) {
      roads.addAll(roadDao.getZonesByDistrictId(district.getDistrictId()));
    }
    return roads;
  }

  /**
   * Gets the ID of the city.
   *
   * @return The city ID.
   */
  @Override
  public int getId() {
    return cityId;
  }

}