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

public class MapDao extends AbstractDao<GeocraftMap> {
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