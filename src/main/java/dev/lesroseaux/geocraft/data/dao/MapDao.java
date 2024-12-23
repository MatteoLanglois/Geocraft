package dev.lesroseaux.geocraft.data.dao;

import dev.lesroseaux.geocraft.models.LocationToMap;
import dev.lesroseaux.geocraft.models.game.GeocraftMap;
import dev.lesroseaux.geocraft.models.location.City;
import dev.lesroseaux.geocraft.models.location.District;
import dev.lesroseaux.geocraft.models.location.GeoCraftWorld;
import dev.lesroseaux.geocraft.models.location.PlayableZone;
import dev.lesroseaux.geocraft.models.location.Region;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Data Access Object (DAO) for the GeocraftMap entity.
 */
public class MapDao extends AbstractDao<GeocraftMap> {

  /**
   * Inserts a GeocraftMap object into the database.
   *
   * @param obj The GeocraftMap object to insert.
   * @return The ID of the inserted object.
   */
  @Override
  public int insert(GeocraftMap obj) {
    String sql = "INSERT INTO Map (min_x, min_z, max_x, max_z, scale, location_id) "
        + "VALUES (?, ?, ?, ?, ?, ?)";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setInt(1, obj.getMinX());
      stmt.setInt(2, obj.getMinZ());
      stmt.setInt(3, obj.getMaxX());
      stmt.setInt(4, obj.getMaxZ());
      stmt.setInt(5, obj.getScale());
      stmt.setInt(6, obj.getLocationDatabaseId());
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return 0;
  }

  /**
   * Updates a GeocraftMap object in the database.
   *
   * @param obj The GeocraftMap object to update.
   */
  @Override
  public void update(GeocraftMap obj) {
    String sql = "UPDATE Map SET min_x = ?, min_z = ?, max_x = ?, max_z = ?, scale = ?, location_id = ? WHERE id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setInt(1, obj.getMinX());
      stmt.setInt(2, obj.getMinZ());
      stmt.setInt(3, obj.getMaxX());
      stmt.setInt(4, obj.getMaxZ());
      stmt.setInt(5, obj.getScale());
      stmt.setInt(6, obj.getLocationDatabaseId());
      stmt.setInt(7, obj.getId());
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Deletes a GeocraftMap object from the database.
   *
   * @param obj The GeocraftMap object to delete.
   */
  @Override
  public void delete(GeocraftMap obj) {
    String sql = "DELETE FROM Map WHERE id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setInt(1, obj.getId());
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Retrieves a GeocraftMap object by its ID.
   *
   * @param id The ID of the GeocraftMap object.
   * @return The GeocraftMap object, or null if not found.
   */
  @Override
  public GeocraftMap getById(int id) {
    String sql = "SELECT * FROM Map WHERE id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        return parseResult(rs);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return null;
  }

  /**
   * Retrieves a GeocraftMap object by its associated PlayableZone.
   *
   * @param zone The PlayableZone associated with the GeocraftMap.
   * @return The GeocraftMap object, or null if not found.
   */
  public GeocraftMap getByPlayableZone(PlayableZone zone) {
    String sql = "SELECT * FROM Map WHERE location_id = (SELECT id from location_to_map " +
        "WHERE world_id = ? AND region_id = ? AND city_id = ? AND district_id = ?)";
    try {
      PreparedStatement statement = connection.prepareStatement(sql);
      if (zone instanceof GeoCraftWorld) {
        statement.setString(1, ((GeoCraftWorld) zone).getWorldId().toString());
        statement.setNull(2, java.sql.Types.INTEGER);
        statement.setNull(3, java.sql.Types.INTEGER);
        statement.setNull(4, java.sql.Types.INTEGER);
      } else if (zone instanceof Region) {
        statement.setNull(1, java.sql.Types.VARCHAR);
        statement.setObject(2, zone.getId(), java.sql.Types.INTEGER);
        statement.setNull(3, java.sql.Types.INTEGER);
        statement.setNull(4, java.sql.Types.INTEGER);
      } else if (zone instanceof City) {
        statement.setNull(1, java.sql.Types.VARCHAR);
        statement.setNull(2, java.sql.Types.INTEGER);
        statement.setObject(3, zone.getId(), java.sql.Types.INTEGER);
        statement.setNull(4, java.sql.Types.INTEGER);
      } else if (zone instanceof District) {
        statement.setNull(1, java.sql.Types.VARCHAR);
        statement.setNull(2, java.sql.Types.INTEGER);
        statement.setNull(3, java.sql.Types.INTEGER);
        statement.setObject(4, zone.getId(), java.sql.Types.INTEGER);
      }

      ResultSet rs = statement.executeQuery();
      if (rs.next()) {
        return parseResult(rs);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return null;
  }

  /**
   * Retrieves a GeocraftMap object by its associated LocationToMap ID.
   *
   * @param locationToMap The LocationToMap associated with the GeocraftMap.
   * @return The GeocraftMap object, or null if not found.
   */
  public GeocraftMap getByLocationToMapId(LocationToMap locationToMap) {
    String sql = "SELECT * FROM Map WHERE location_id = ?";
    try {
      PreparedStatement statement = connection.prepareStatement(sql);
      connection.setAutoCommit(false);
      statement.setInt(1, locationToMap.getDatabaseId());
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        return parseResult(resultSet);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return null;
  }

  /**
   * Parses a ResultSet into a GeocraftMap object.
   *
   * @param rs The ResultSet to parse.
   * @return The parsed GeocraftMap object.
   */
  private GeocraftMap parseResult(ResultSet rs) {
    GeocraftMap geocraftMap = new GeocraftMap();
    try {
      if (rs.getInt("id") != 0) {
        geocraftMap.setId(rs.getInt("id"));
      }
      geocraftMap.setMinX(rs.getInt("min_x"));
      geocraftMap.setMinZ(rs.getInt("min_z"));
      geocraftMap.setMaxX(rs.getInt("max_x"));
      geocraftMap.setMaxZ(rs.getInt("max_z"));
      if (rs.getInt("scale") != 0) {
        geocraftMap.setScale(rs.getInt("scale"));
      }
      if (rs.getInt("location_id") != 0) {
        LocationToMap locationToMap = new LocationToMapDao().getById(rs.getInt("location_id"));
        if (locationToMap != null) {
          geocraftMap.setLocationToMap(locationToMap);
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return geocraftMap;
  }

  /**
   * Retrieves all GeocraftMap objects from the database.
   *
   * @return A list of all GeocraftMap objects.
   */
  @Override
  public ArrayList<GeocraftMap> getAll() {
    String sql = "SELECT * FROM Map";
    ArrayList<GeocraftMap> geocraftMaps = new ArrayList<>();
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        geocraftMaps.add(parseResult(rs));
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return geocraftMaps;
  }

  /**
   * Returns the SQL query for creating the Map table.
   *
   * @return The SQL query for creating the Map table.
   */
  public String getTableCreationQuery() {
    return "CREATE TABLE IF NOT EXISTS Map ("
        + "    id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,"
        + "    min_x INT NOT NULL,"
        + "    min_z INT NOT NULL,"
        + "    max_x INT NOT NULL,"
        + "    max_z INT NOT NULL,"
        + "    scale INT NOT NULL,"
        + "    location_id INT NOT NULL,"
        + "    FOREIGN KEY (location_id) REFERENCES location_to_map(id) ON DELETE CASCADE"
        + ");";
  }
}