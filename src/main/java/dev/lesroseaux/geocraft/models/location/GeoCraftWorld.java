package dev.lesroseaux.geocraft.models.location;

import dev.lesroseaux.geocraft.data.dao.RegionDao;
import java.util.ArrayList;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.World;

/**
 * Represents a GeoCraft world, which is a playable zone.
 */
@Setter
@Getter
public class GeoCraftWorld implements PlayableZone {
  private World world;
  private UUID worldId;
  private String worldName;

  /**
   * Default constructor for GeoCraftWorld.
   */
  public GeoCraftWorld() {
  }

  /**
   * Constructs a GeoCraftWorld with the specified world, world ID, and world name.
   *
   * @param world The Bukkit world.
   * @param worldId The UUID of the world.
   * @param worldName The name of the world.
   */
  private GeoCraftWorld(World world, UUID worldId, String worldName) {
    this.world = world;
    this.worldId = worldId;
    this.worldName = worldName;
  }

  /**
   * Constructs a GeoCraftWorld with the specified world.
   * The world ID and name are derived from the world.
   *
   * @param world The Bukkit world.
   */
  public GeoCraftWorld(World world) {
    this(world, world.getUID(), world.getName());
  }

  /**
   * Gets the zones (roads) within the world.
   *
   * @return A list of roads in the world.
   */
  @Override
  public ArrayList<Road> getZones() {
    ArrayList<Region> regions = new RegionDao().getAllRegionsByWorldId(this.worldId);
    ArrayList<Road> roads = new ArrayList<>();
    for (Region region : regions) {
      roads.addAll(region.getZones());
    }
    return roads;
  }

  /**
   * Gets the ID of the world.
   *
   * @return The world ID.
   */
  @Override
  public int getId() {
    return 0;
  }

}