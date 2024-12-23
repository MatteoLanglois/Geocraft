package dev.lesroseaux.geocraft.models.location;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

/**
 * Represents a road in GeoCraft, which is a playable zone.
 */
@Getter
@Setter
public class Road implements PlayableZone {
  private final int zoneId;
  private String zoneName;
  private Location zonePoint1;
  private Location zonePoint2;
  private int districtId;

  /**
   * Constructs a Road with the specified zone ID, points, name, and district ID.
   *
   * @param zoneId The ID of the zone.
   * @param point1 The first point of the zone.
   * @param point2 The second point of the zone.
   * @param name The name of the zone.
   * @param districtId The ID of the district the zone belongs to.
   */
  public Road(int zoneId, Location point1, Location point2, String name, int districtId) {
    this.zoneId = zoneId;
    this.zonePoint1 = point1;
    this.zonePoint2 = point2;
    this.zoneName = name;
    this.districtId = districtId;
  }

  /**
   * Constructs a Road with the specified points, name, and district ID.
   * The zone ID is set to 0.
   *
   * @param point1 The first point of the zone.
   * @param point2 The second point of the zone.
   * @param name The name of the zone.
   * @param districtId The ID of the district the zone belongs to.
   */
  public Road(Location point1, Location point2, String name, int districtId) {
    this(0, point1, point2, name, districtId);
  }

  /**
   * Constructs a Road with the specified points and name.
   * The zone ID and district ID are set to 0.
   *
   * @param tempPoint1 The first point of the zone.
   * @param tempPoint2 The second point of the zone.
   * @param string The name of the zone.
   */
  public Road(Location tempPoint1, Location tempPoint2, String string) {
    this(0, tempPoint1, tempPoint2, string, 0);
  }

  /**
   * Prints all zones in the provided list of roads.
   *
   * @param roads The list of roads.
   * @return A string representation of all zones.
   */
  public static String printAllZones(ArrayList<Road> roads) {
    StringBuilder sb = new StringBuilder();
    for (Road road : roads) {
      sb.append("Zone name : ").append(road.getZoneName())
        .append("\n\tPoint 1: ").append(road.getZonePoint1())
        .append("\n\tPoint 2: ").append(road.getZonePoint2())
        .append("\n");
    }
    return sb.toString();
  }

  /**
   * Gets the zones (roads) within the road.
   *
   * @return A list of roads in the road.
   */
  @Override
  public ArrayList<Road> getZones() {
    return new ArrayList<>(List.of(this));
  }

  /**
   * Gets the ID of the road.
   *
   * @return The road ID.
   */
  @Override
  public int getId() {
    return zoneId;
  }
}