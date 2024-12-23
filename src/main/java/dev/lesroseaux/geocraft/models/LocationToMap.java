package dev.lesroseaux.geocraft.models;

import dev.lesroseaux.geocraft.models.location.City;
import dev.lesroseaux.geocraft.models.location.District;
import dev.lesroseaux.geocraft.models.location.GeoCraftWorld;
import dev.lesroseaux.geocraft.models.location.PlayableZone;
import dev.lesroseaux.geocraft.models.location.Region;

/**
 * Represents a mapping of a location to a GeoCraft map.
 */
public class LocationToMap {
  private int databaseId;
  private GeoCraftWorld world;
  private Region region;
  private City city;
  private District district;

  /**
   * Constructs a LocationToMap with a default database ID of 0.
   */
  public LocationToMap() {
    this.databaseId = 0;
  }

  /**
   * Constructs a LocationToMap with the specified playable zone.
   * The playable zone can be a GeoCraftWorld, Region, City, or District.
   *
   * @param playableZone The playable zone to map.
   */
  public LocationToMap(PlayableZone playableZone) {
    if (playableZone instanceof GeoCraftWorld) {
      this.world = (GeoCraftWorld) playableZone;
    } else if (playableZone instanceof Region) {
      this.region = (Region) playableZone;
    } else if (playableZone instanceof City) {
      this.city = (City) playableZone;
    } else if (playableZone instanceof District) {
      this.district = (District) playableZone;
    }
    this.databaseId = 0;
  }

  /**
   * Gets the current location as a PlayableZone.
   *
   * @return The current location as a PlayableZone, or null if no location is set.
   */
  public PlayableZone getLocation() {
    if (this.world != null) {
      return this.world;
    } else if (this.region != null) {
      return this.region;
    } else if (this.city != null) {
      return this.city;
    } else if (this.district != null) {
      return this.district;
    }
    return null;
  }

  /**
   * Sets the location to the specified PlayableZone.
   * The playable zone can be a GeoCraftWorld, Region, City, or District.
   *
   * @param zone The playable zone to set as the location.
   */
  public void setLocation(PlayableZone zone) {
    if (zone instanceof GeoCraftWorld) {
      this.world = (GeoCraftWorld) zone;
    } else if (zone instanceof Region) {
      this.region = (Region) zone;
    } else if (zone instanceof City) {
      this.city = (City) zone;
    } else if (zone instanceof District) {
      this.district = (District) zone;
    }
  }

  /**
   * Gets the ID of the current location.
   * The ID can be the world ID, region ID, city ID, or district ID.
   *
   * @return The ID of the current location, or null if no location is set.
   */
  public Object getId() {
    if (this.world != null) {
      return this.world.getWorldId();
    } else if (this.region != null) {
      return this.region.getRegionId();
    } else if (this.city != null) {
      return this.city.getCityId();
    } else if (this.district != null) {
      return this.district.getDistrictId();
    }
    return null;
  }

  /**
   * Gets the database ID.
   *
   * @return The database ID.
   */
  public int getDatabaseId() {
    return this.databaseId;
  }

  /**
   * Sets the database ID to the specified value.
   *
   * @param id The new database ID.
   */
  public void setDatabaseId(int id) {
    this.databaseId = id;
  }
}