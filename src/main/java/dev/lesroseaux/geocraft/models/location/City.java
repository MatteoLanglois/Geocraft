package dev.lesroseaux.geocraft.models.location;

import dev.lesroseaux.geocraft.data.dao.DistrictDao;
import dev.lesroseaux.geocraft.data.dao.RoadDao;
import java.util.ArrayList;

/**
 * Represents a city in GeoCraft, which is a playable zone.
 */
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

  /**
   * Gets the ID of the city.
   *
   * @return The city ID.
   */
  public int getCityId() {
    return cityId;
  }

  /**
   * Sets the ID of the city.
   *
   * @param cityId The city ID.
   */
  public void setCityId(int cityId) {
    this.cityId = cityId;
  }

  /**
   * Gets the name of the city.
   *
   * @return The city name.
   */
  public String getCityName() {
    return cityName;
  }

  /**
   * Sets the name of the city.
   *
   * @param cityName The city name.
   */
  public void setCityName(String cityName) {
    this.cityName = cityName;
  }

  /**
   * Gets the ID of the region the city belongs to.
   *
   * @return The region ID.
   */
  public int getRegionId() {
    return regionId;
  }

  /**
   * Sets the ID of the region the city belongs to.
   *
   * @param regionId The region ID.
   */
  public void setRegionId(int regionId) {
    this.regionId = regionId;
  }
}