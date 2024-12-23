package dev.lesroseaux.geocraft.models.location;

import dev.lesroseaux.geocraft.data.dao.RoadDao;
import java.util.ArrayList;

/**
 * Represents a district in GeoCraft, which is a playable zone.
 */
public class District implements PlayableZone {
  private int districtId;
  private String districtName;
  private int cityId;

  /**
   * Constructs a District with the specified district ID, name, and city ID.
   *
   * @param districtId The ID of the district.
   * @param districtName The name of the district.
   * @param cityId The ID of the city the district belongs to.
   */
  public District(int districtId, String districtName, int cityId) {
    this.districtId = districtId;
    this.districtName = districtName;
    this.cityId = cityId;
  }

  /**
   * Gets the ID of the district.
   *
   * @return The district ID.
   */
  public int getDistrictId() {
    return districtId;
  }

  /**
   * Sets the ID of the district.
   *
   * @param districtId The district ID.
   */
  public void setDistrictId(int districtId) {
    this.districtId = districtId;
  }

  /**
   * Gets the name of the district.
   *
   * @return The district name.
   */
  public String getDistrictName() {
    return districtName;
  }

  /**
   * Sets the name of the district.
   *
   * @param districtName The district name.
   */
  public void setDistrictName(String districtName) {
    this.districtName = districtName;
  }

  /**
   * Gets the ID of the city the district belongs to.
   *
   * @return The city ID.
   */
  public int getCityId() {
    return cityId;
  }

  /**
   * Sets the ID of the city the district belongs to.
   *
   * @param cityId The city ID.
   */
  public void setCityId(int cityId) {
    this.cityId = cityId;
  }

  /**
   * Gets the zones (roads) within the district.
   *
   * @return A list of roads in the district.
   */
  @Override
  public ArrayList<Road> getZones() {
    RoadDao roadDao = new RoadDao();
    return roadDao.getZonesByDistrictId(districtId);
  }

  /**
   * Gets the ID of the district.
   *
   * @return The district ID.
   */
  @Override
  public int getId() {
    return districtId;
  }
}