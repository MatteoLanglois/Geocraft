package dev.lesroseaux.geocraft.models.location;

import dev.lesroseaux.geocraft.data.dao.DistrictDao;
import dev.lesroseaux.geocraft.data.dao.RoadDao;
import java.util.ArrayList;

public class City implements PlayableZone {
  private int cityId;
  private String cityName;
  private int regionId;

  public City(int cityId, String cityName, int regionId) {
    this.cityId = cityId;
    this.cityName = cityName;
    this.regionId = regionId;
  }

  public City(String cityName, int regionId) {
    this(0, cityName, regionId);
  }

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

  @Override
  public int getId() {
    return cityId;
  }

  public int getCityId() {
    return cityId;
  }

  public void setCityId(int cityId) {
    this.cityId = cityId;
  }

  public String getCityName() {
    return cityName;
  }

  public void setCityName(String cityName) {
    this.cityName = cityName;
  }

  public int getRegionId() {
    return regionId;
  }

  public void setRegionId(int regionId) {
    this.regionId = regionId;
  }
}
