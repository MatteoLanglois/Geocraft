package dev.lesroseaux.geocraft.models.Location;

import dev.lesroseaux.geocraft.data.dao.RoadDAO;
import java.util.ArrayList;

public class District implements PlayableZone {
  private int district_id;
  private String district_name;
  private int city_id;

  public District(int district_id, String district_name, int city_id) {
    this.district_id = district_id;
    this.district_name = district_name;
    this.city_id = city_id;
  }

  public int getDistrict_id() {
    return district_id;
  }

  public void setDistrict_id(int district_id) {
    this.district_id = district_id;
  }

  public String getDistrict_name() {
    return district_name;
  }

  public void setDistrict_name(String district_name) {
    this.district_name = district_name;
  }

  public int getCity_id() {
    return city_id;
  }

  public void setCity_id(int city_id) {
    this.city_id = city_id;
  }


  @Override
  public ArrayList<Road> getZones() {
    RoadDAO roadDAO = new RoadDAO();
    return roadDAO.getZonesByDistrictId(district_id);
  }
}
