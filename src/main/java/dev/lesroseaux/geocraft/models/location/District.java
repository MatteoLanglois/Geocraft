package dev.lesroseaux.geocraft.models.location;

import dev.lesroseaux.geocraft.data.dao.RoadDao;
import java.util.ArrayList;

public class District implements PlayableZone {
  private int districtId;
  private String districtName;
  private int cityId;

  public District(int districtId, String districtName, int cityId) {
    this.districtId = districtId;
    this.districtName = districtName;
    this.cityId = cityId;
  }

  public int getDistrictId() {
    return districtId;
  }

  public void setDistrictId(int districtId) {
    this.districtId = districtId;
  }

  public String getDistrictName() {
    return districtName;
  }

  public void setDistrictName(String districtName) {
    this.districtName = districtName;
  }

  public int getCityId() {
    return cityId;
  }

  public void setCityId(int cityId) {
    this.cityId = cityId;
  }


  @Override
  public ArrayList<Road> getZones() {
    RoadDao roadDao = new RoadDao();
    return roadDao.getZonesByDistrictId(districtId);
  }

  @Override
  public int getId() {
    return districtId;
  }
}
