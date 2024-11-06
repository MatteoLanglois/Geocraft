package dev.lesroseaux.geocraft.models;

import dev.lesroseaux.geocraft.models.location.City;
import dev.lesroseaux.geocraft.models.location.District;
import dev.lesroseaux.geocraft.models.location.GeoCraftWorld;
import dev.lesroseaux.geocraft.models.location.PlayableZone;
import dev.lesroseaux.geocraft.models.location.Region;

public class LocationToMap {
  private int databaseId;
  private GeoCraftWorld world;
  private Region region;
  private City city;
  private District district;

  public LocationToMap() {
    this.databaseId = 0;
  }

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

  public int getDatabaseId() {
    return this.databaseId;
  }

  public void setDatabaseId(int id) {
    this.databaseId = id;
  }
  
}
