package dev.lesroseaux.geocraft.models.location;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;

public class Road implements PlayableZone {
  private final int zoneId;
  private String zoneName;
  private Location zonePoint1;
  private Location zonePoint2;
  private int districtId;

  public Road(int zoneId, Location point1, Location point2, String name, int districtId) {
    this.zoneId = zoneId;
    this.zonePoint1 = point1;
    this.zonePoint2 = point2;
    this.zoneName = name;
    this.districtId = districtId;
  }

  public Road(Location point1, Location point2, String name, int districtId) {
    this(0, point1, point2, name, districtId);
  }

  public Road(Location tempPoint1, Location tempPoint2, String string) {
    this(0, tempPoint1, tempPoint2, string, 0);
  }

  public int getZoneId() {
    return zoneId;
  }

  public Location getPoint1() {
    return zonePoint1;
  }

  public Location getPoint2() {
    return zonePoint2;
  }

  public String getZoneName() {
    return zoneName;
  }

  public int getDistrictId() {
    return districtId;
  }

  public void setPoint1(Location point1) {
    this.zonePoint1 = point1;
  }

  public void setPoint2(Location point2) {
    this.zonePoint2 = point2;
  }

  public void setZoneName(String name) {
    this.zoneName = name;
  }

  public void setDistrictId(int districtId) {
    this.districtId = districtId;
  }

  public static String printAllZones(ArrayList<Road> roads) {
    StringBuilder sb = new StringBuilder();
    for (Road road : roads) {
      sb.append("Zone name : ").append(road.getZoneName())
        .append("\n\tPoint 1: ").append(road.getPoint1())
        .append("\n\tPoint 2: ").append(road.getPoint2())
        .append("\n");
    }
    return sb.toString();
  }

  @Override
  public ArrayList<Road> getZones() {
    return new ArrayList<>(List.of(this));
  }

  @Override
  public int getId() {
    return zoneId;
  }
}