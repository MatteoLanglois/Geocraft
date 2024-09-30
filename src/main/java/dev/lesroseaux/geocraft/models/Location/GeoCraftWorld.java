package dev.lesroseaux.geocraft.models.Location;

import dev.lesroseaux.geocraft.data.dao.RegionDao;
import java.util.ArrayList;
import java.util.UUID;
import org.bukkit.World;

public class GeoCraftWorld implements PlayableZone {
  private World world;
  private UUID world_id;
  private String world_name;

  public GeoCraftWorld() {
  }

  private GeoCraftWorld(World world, UUID world_id, String world_name) {
    this.world = world;
    this.world_id = world_id;
    this.world_name = world_name;
  }

  public GeoCraftWorld(World world) {
    this(world, world.getUID(), world.getName());
  }

  @Override
  public ArrayList<Road> getZones() {
    RegionDao regionDao = new RegionDao();
    ArrayList<Region> regions = regionDao.getAllRegionsByWorldId(this.world_id);
    ArrayList<Road> roads = new ArrayList<>();
    for (Region region : regions) {
      roads.addAll(region.getZones());
    }
    return roads;
  }

  public World getWorld() {
    return world;
  }

  public UUID getWorldId() {
    return world_id;
  }

  public String getWorldName() {
    return world_name;
  }

  public void setWorld(World world) {
    this.world = world;
  }

  public void setWorldId(UUID world_id) {
    this.world_id = world_id;
  }

  public void setWorldName(String world_name) {
    this.world_name = world_name;
  }
}
