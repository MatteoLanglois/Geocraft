package dev.lesroseaux.geocraft.data.dao;

import dev.lesroseaux.geocraft.models.LocationToMap;
import dev.lesroseaux.geocraft.models.location.City;
import dev.lesroseaux.geocraft.models.location.District;
import dev.lesroseaux.geocraft.models.location.GeoCraftWorld;
import dev.lesroseaux.geocraft.models.location.PlayableZone;
import dev.lesroseaux.geocraft.models.location.Region;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Data Access Object (DAO) for the LocationToMap entity.
 */
public class LocationToMapDao extends AbstractDao<LocationToMap> {

  /**
   * Inserts a LocationToMap object into the database.
   *
   * @param obj The LocationToMap object to insert.
   * @return The ID of the inserted LocationToMap.
   */
  @Override
  public int insert(LocationToMap obj) {
    String sql = "INSERT INTO location_to_map (world_id, region_id, city_id, district_id) "
        + "VALUES (?, ?, ?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(sql,
        PreparedStatement.RETURN_GENERATED_KEYS)) {
      if (obj.getLocation() instanceof GeoCraftWorld) {
        statement.setString(1, obj.getId().toString());
        statement.setNull(2, java.sql.Types.INTEGER);
        statement.setNull(3, java.sql.Types.INTEGER);
        statement.setNull(4, java.sql.Types.INTEGER);
      } else if (obj.getLocation() instanceof Region) {
        statement.setNull(1, Types.VARCHAR);
        statement.setObject(2, obj.getId(), java.sql.Types.INTEGER);
        statement.setNull(3, java.sql.Types.INTEGER);
        statement.setNull(4, java.sql.Types.INTEGER);
      } else if (obj.getLocation() instanceof City) {
        statement.setNull(1, Types.VARCHAR);
        statement.setNull(2, java.sql.Types.INTEGER);
        statement.setObject(3, obj.getId(), java.sql.Types.INTEGER);
        statement.setNull(4, java.sql.Types.INTEGER);
      } else if (obj.getLocation() instanceof District) {
        statement.setNull(1, Types.VARCHAR);
        statement.setNull(2, java.sql.Types.INTEGER);
        statement.setNull(3, java.sql.Types.INTEGER);
        statement.setObject(4, obj.getId(), java.sql.Types.INTEGER);
      }
      int affectedRows = statement.executeUpdate();
      if (affectedRows == 0) {
        throw new RuntimeException("Creating location to map failed, no rows affected.");
      }
      try (
          ResultSet generatedKeys = statement.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          return generatedKeys.getInt(1);
        } else {
          throw new RuntimeException("Creating location to map failed, no ID obtained.");
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Updates a LocationToMap object in the database.
   *
   * @param obj The LocationToMap object to update.
   */
  @Override
  public void update(LocationToMap obj) {
    String sql = "UPDATE location_to_map SET world_id = ?, region_id = ?, city_id = ?, district_id = ? WHERE id = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      connection.setAutoCommit(false);
      statement.setString(1, obj.getId() != null ? obj.getId().toString() : null);
      statement.setObject(2, obj.getLocation().getId() != 0 ? obj.getLocation().getId() : 0, java.sql.Types.INTEGER);
      statement.setObject(3, obj.getLocation().getId() != 0 ? obj.getLocation().getId() : 0, java.sql.Types.INTEGER);
      statement.setObject(4, obj.getLocation().getId() != 0 ? obj.getLocation().getId() : 0, java.sql.Types.INTEGER);
      statement.setInt(5, obj.getDatabaseId());
      statement.executeUpdate();
      connection.commit();
    } catch (Exception e) {
      try {
        connection.rollback();
      } catch (Exception rollbackEx) {
        throw new RuntimeException(rollbackEx);
      }
      throw new RuntimeException(e);
    } finally {
      try {
        connection.setAutoCommit(true);
      } catch (Exception autoCommitEx) {
        throw new RuntimeException(autoCommitEx);
      }
    }
  }

  /**
   * Deletes a LocationToMap object from the database.
   *
   * @param obj The LocationToMap object to delete.
   */
  @Override
  public void delete(LocationToMap obj) {
    String sql = "DELETE FROM location_to_map WHERE id = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      connection.setAutoCommit(false);
      statement.setInt(1, obj.getDatabaseId());
      statement.executeUpdate();
      connection.commit();
    } catch (Exception e) {
      try {
        connection.rollback();
      } catch (Exception rollbackEx) {
        throw new RuntimeException(rollbackEx);
      }
      throw new RuntimeException(e);
    }
  }

  /**
   * Retrieves a LocationToMap object by its ID.
   *
   * @param id The ID of the LocationToMap to retrieve.
   * @return The LocationToMap object with the specified ID.
   */
  @Override
  public LocationToMap getById(int id) {
    String sql = "SELECT * FROM location_to_map WHERE id = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, id);
      ResultSet rs = statement.executeQuery();
      if (rs.next()) {
        LocationToMap location = parseResult(rs);
        rs.close();
        statement.close();
        return location;
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return null;
  }

  /**
   * Retrieves all LocationToMap objects from the database.
   *
   * @return A list of all LocationToMap objects.
   */
  @Override
  public ArrayList<LocationToMap> getAll() {
    String sql = "SELECT * FROM location_to_map";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      ArrayList<LocationToMap> locationToMaps = new ArrayList<>();
      ResultSet rs = statement.executeQuery();
      while (rs.next()) {
        locationToMaps.add(parseResult(rs));
      }
      return locationToMaps;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Retrieves a LocationToMap object by its playable zone.
   *
   * @param zone The playable zone to filter by.
   * @return The LocationToMap object with the specified playable zone.
   */
  public LocationToMap getByPlayableZone(LocationToMap zone) {
    String sql = "SELECT * FROM location_to_map WHERE world_id = ? OR region_id = ? "
        + "OR city_id = ? OR district_id = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, zone.getLocation() instanceof GeoCraftWorld ? zone.getId().toString() : null);
      statement.setInt(2, zone.getLocation() instanceof Region ? zone.getLocation().getId() : 0);
      statement.setInt(3, zone.getLocation() instanceof City ? zone.getLocation().getId() : 0);
      statement.setInt(4, zone.getLocation() instanceof District ? zone.getLocation().getId() : 0);
      ResultSet rs = statement.executeQuery();
      if (rs.next()) {
        LocationToMap map = parseResult(rs);
        rs.close();
        statement.close();
        return map;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Retrieves the parent map of a LocationToMap object.
   *
   * @param locationToMap The LocationToMap object to retrieve the parent map for.
   * @return The parent LocationToMap object.
   */
  public LocationToMap getParentMap(LocationToMap locationToMap) {
    if (locationToMap == null) {
      return null;
    }
    PlayableZone zone = locationToMap.getLocation();
    if (zone instanceof GeoCraftWorld) {
      return getByPlayableZone(locationToMap);
    } else if (zone instanceof Region) {
      return getByPlayableZone(new LocationToMap(new WorldDao().getByUuid(((Region) zone).getWorldId())));
    } else if (zone instanceof City) {
      return getParentMap(new LocationToMap(new RegionDao().getById(((City) zone).getRegionId())));
    } else if (zone instanceof District) {
      return getParentMap(new LocationToMap(new CityDao().getById(((District) zone).getCityId())));
    }
    return getByPlayableZone(locationToMap);
  }

  /**
   * Parses a ResultSet into a LocationToMap object.
   *
   * @param rs The ResultSet to parse.
   * @return The parsed LocationToMap object.
   */
  private LocationToMap parseResult(ResultSet rs) {
    try {
      LocationToMap locationToMap = new LocationToMap();
      locationToMap.setDatabaseId(rs.getInt("id"));
      if (rs.getString("world_id") != null) {
        locationToMap.setLocation(new WorldDao().getByUuid(
            UUID.fromString(rs.getString("world_id"))));
      } else if (rs.getInt("region_id") != 0) {
        locationToMap.setLocation(new RegionDao().getById(rs.getInt("region_id")));
      } else if (rs.getInt("city_id") != 0) {
        locationToMap.setLocation(new CityDao().getById(rs.getInt("city_id")));
      } else if (rs.getInt("district_id") != 0) {
        locationToMap.setLocation(new DistrictDao().getById(rs.getInt("district_id")));
      }
      return locationToMap;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Gets the SQL query for creating the location\_to\_map table.
   *
   * @return The SQL query for creating the location\_to\_map table.
   */
  @Override
  public String getTableCreationQuery() {
    return "CREATE TABLE IF NOT EXISTS location_to_map ("
        + "id INT AUTO_INCREMENT PRIMARY KEY,"
        + "world_id VARCHAR(36),"
        + "region_id INT,"
        + "city_id INT,"
        + "district_id INT,"
        + "FOREIGN KEY (world_id) REFERENCES worlds(world_id),"
        + "FOREIGN KEY (region_id) REFERENCES regions(region_id),"
        + "FOREIGN KEY (city_id) REFERENCES cities(city_id),"
        + "FOREIGN KEY (district_id) REFERENCES districts(district_id)"
        + ")";
  }
}