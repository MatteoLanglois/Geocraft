package dev.lesroseaux.geocraft.models.location;

import java.util.ArrayList;

/**
 * Interface representing a playable zone in GeoCraft.
 */
public interface PlayableZone {

  /**
   * Gets the zones (roads) within the playable zone.
   *
   * @return A list of roads in the playable zone.
   */
  ArrayList<Road> getZones();

  /**
   * Gets the ID of the playable zone.
   *
   * @return The ID of the playable zone.
   */
  int getId();
}