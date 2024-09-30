package dev.lesroseaux.geocraft.models.Location;

import dev.lesroseaux.geocraft.data.dao.DistrictDao;
import dev.lesroseaux.geocraft.data.dao.RoadDAO;
import java.util.ArrayList;

public class City implements PlayableZone {
  private int city_id;
  private String city_name;
  private int region_id;

  public City(int city_id, String city_name, int region_id) {
    this.city_id = city_id;
    this.city_name = city_name;
    this.region_id = region_id;
  }

  public City(String city_name, int region_id) {
    this(0, city_name, region_id);
  }

  @Override
  public ArrayList<Road> getZones() {
    DistrictDao districtDao = new DistrictDao();
    ArrayList<District> districts = districtDao.getAllDistrictsByCityId(city_id);
    ArrayList<Road> roads = new ArrayList<>();
    RoadDAO roadDAO = new RoadDAO();
    for (District district : districts) {
      roads.addAll(roadDAO.getZonesByDistrictId(district.getDistrict_id()));
    }
    return roads;
  }

  public int getCity_id() {
    return city_id;
  }

  public void setCity_id(int city_id) {
    this.city_id = city_id;
  }

  public String getCity_name() {
    return city_name;
  }

  public void setCity_name(String city_name) {
    this.city_name = city_name;
  }

  public int getRegion_id() {
    return region_id;
  }

  public void setRegion_id(int region_id) {
    this.region_id = region_id;
  }
}
