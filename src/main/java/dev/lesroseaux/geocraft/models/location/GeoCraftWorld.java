package dev.lesroseaux.geocraft.models.location;

import dev.lesroseaux.geocraft.data.dao.RegionDao;
import java.util.ArrayList;
import java.util.UUID;
import org.bukkit.World;

public class GeoCraftWorld implements PlayableZone {
  private World world;
  private UUID worldId;
  private String worldName;

  public GeoCraftWorld() {
  }

  private GeoCraftWorld(World world, UUID worldId, String worldName) {
    this.world = world;
    this.worldId = worldId;
    this.worldName = worldName;
  }

  public GeoCraftWorld(World world) {
    this(world, world.getUID(), world.getName());
  }

  @Override
  public ArrayList<Road> getZones() {
    ArrayList<Region> regions = new RegionDao().getAllRegionsByWorldId(this.worldId);
    ArrayList<Road> roads = new ArrayList<>();
    for (Region region : regions) {
      roads.addAll(region.getZones());
    }
    return roads;
  }

  @Override
  public int getId() {
    return 0;
  }

  public World getWorld() {
    return world;
  }

  public UUID getWorldId() {
    return worldId;
  }

  public String getWorldName() {
    return worldName;
  }

  public void setWorld(World world) {
    this.world = world;
  }

  public void setWorldId(UUID worldId) {
    this.worldId = worldId;
  }

  public void setWorldName(String worldName) {
    this.worldName = worldName;
  }
}
