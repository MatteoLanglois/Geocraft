package dev.lesroseaux.geocraft.data.dao;

import dev.lesroseaux.geocraft.models.Location.Region;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public class RegionDao extends AbstractDao<Region> {
  @Override
  public int insert(Region obj) {
    String preparedStatement = "INSERT INTO regions (region_name, world_id) VALUES (?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(preparedStatement, PreparedStatement.RETURN_GENERATED_KEYS)) {
      connection.setAutoCommit(false);
      statement.setString(1, obj.getRegion_name());
      statement.setString(2, obj.getWorld_id().toString());
      int result = statement.executeUpdate();
      connection.commit();
      return result;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void update(Region obj) {
    String preparedStatement = "UPDATE regions SET region_name = ? WHERE region_id = ?";
    try {
      connection.setAutoCommit(false);
      connection.prepareStatement(preparedStatement).executeUpdate();
      connection.commit();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void delete(Region obj) {
    String preparedStatement = "DELETE FROM regions WHERE region_id = ?";
    try {
      connection.setAutoCommit(false);
      connection.prepareStatement(preparedStatement).executeUpdate();
      connection.commit();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Region getById(int id) {
    String preparedStatement = "SELECT * FROM regions WHERE region_id = ?";
    try {
      PreparedStatement statement = connection.prepareStatement(preparedStatement);
      statement.setInt(1, id);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        return parseResult(resultSet);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return null;
  }

  @Override
  public ArrayList<Region> getAll() {
    String preparedStatement = "SELECT * FROM regions";
    try {
      PreparedStatement statement = connection.prepareStatement(preparedStatement);
      ResultSet resultSet = statement.executeQuery();
      ArrayList<Region> regions = new ArrayList<>();
      while (resultSet.next()) {
        regions.add(parseResult(resultSet));
      }
      return regions;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public ArrayList<Region> getAllRegionsByWorldId(UUID worldId) {
    return null;
  }

  private Region parseResult(ResultSet result) {
    Region r;
    try {
      r = new Region(result.getInt("region_id"),
          result.getString("region_name"),
          UUID.fromString(result.getString("world_id")));
    } catch (SQLException e) {
      r = null;
    }
    return r;
  }

  public static String getTableCreationQuery() {
    return "CREATE TABLE IF NOT EXISTS regions ("
        + "region_id INT AUTO_INCREMENT PRIMARY KEY,"
        + "region_name VARCHAR(255) NOT NULL,"
        + "world_id VARCHAR(36) NOT NULL,"
        + "FOREIGN KEY (world_id) REFERENCES worlds(world_id)"
        + ")";
  }

  public void createTable() {
    String query = getTableCreationQuery();
    try {
      connection.prepareStatement(query).executeUpdate();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public Region getRegionByName(@NotNull String regionName) {
    String preparedStatement = "SELECT * FROM regions WHERE LOWER(region_name) = LOWER(?)";
    try (PreparedStatement statement = connection.prepareStatement(preparedStatement)) {
      statement.setString(1, regionName);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return parseResult(resultSet);
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return null;
  }
}
