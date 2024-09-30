package dev.lesroseaux.geocraft.data.dao;

import dev.lesroseaux.geocraft.models.Location.GeoCraftWorld;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

public class WorldDao extends AbstractDao<GeoCraftWorld> {
  @Override
  public int insert(GeoCraftWorld obj) {
    String preparedQuery = "INSERT INTO worlds (world_id, world_name) VALUES (?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(preparedQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
      connection.setAutoCommit(false);
      statement.setString(1, obj.getWorldId().toString());
      statement.setString(2, obj.getWorldName());
      int result = statement.executeUpdate();
      connection.commit();
      return result;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void update(GeoCraftWorld obj) {
    String preparedQuery = "UPDATE worlds SET world_name = ? WHERE world_id = ?";
    try {
      PreparedStatement statement = connection.prepareStatement(preparedQuery);
      connection.setAutoCommit(false);
      statement.setString(1, obj.getWorldName());
      statement.setString(2, obj.getWorldId().toString());
      statement.executeUpdate();
      connection.commit();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void delete(GeoCraftWorld obj) {
    String preparedQuery = "DELETE FROM worlds WHERE world_id = ?";
    try {
      PreparedStatement statement = connection.prepareStatement(preparedQuery);
      connection.setAutoCommit(false);
      statement.setString(1, obj.getWorldId().toString());
      statement.executeUpdate();
      connection.commit();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public GeoCraftWorld getById(int id) {
    throw new UnsupportedOperationException("Does not support getById(int) operation");
  }

  public GeoCraftWorld getByUUID(UUID id) {
    String preparedQuery = "SELECT * FROM worlds WHERE world_id = ?";
    try (PreparedStatement statement = connection.prepareStatement(preparedQuery)) {
      statement.setString(1, id.toString());
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          GeoCraftWorld world = new GeoCraftWorld();
          world.setWorldId(UUID.fromString(resultSet.getString("world_id")));
          world.setWorldName(resultSet.getString("world_name"));
          return world;
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return null;
  }

  @Override
  public ArrayList<GeoCraftWorld> getAll() {
    throw new UnsupportedOperationException("Does not support getAll operation");
  }

  public GeoCraftWorld getWorldByZoneId(int zoneId) {
    String preparedQuery = "SELECT * FROM worlds WHERE world_id = (SELECT world_id FROM road WHERE zone_id = ?)";
    try {
      PreparedStatement statement = connection.prepareStatement(preparedQuery);
      statement.setInt(1, zoneId);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        GeoCraftWorld world = new GeoCraftWorld();
        world.setWorldId(UUID.fromString(resultSet.getString("world_id")));
        world.setWorldName(resultSet.getString("world_name"));
        return world;
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return null;
  }

  public static String getTableCreationQuery() {
    return "CREATE TABLE IF NOT EXISTS worlds ("
        + "world_id VARCHAR(36) PRIMARY KEY,"
        + "world_name VARCHAR(255) NOT NULL"
        + ")";
  }

  @Override
  public void createTable() {
    String query = getTableCreationQuery();
    try {
      connection.prepareStatement(query).executeUpdate();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
