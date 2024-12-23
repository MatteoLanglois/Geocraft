package dev.lesroseaux.geocraft.data.dao;

import dev.lesroseaux.geocraft.models.location.GeoCraftWorld;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Data Access Object (DAO) for the GeoCraftWorld entity.
 */
public class WorldDao extends AbstractDao<GeoCraftWorld> {

  /**
   * Inserts a GeoCraftWorld object into the database.
   *
   * @param obj The GeoCraftWorld object to insert.
   * @return The ID of the inserted GeoCraftWorld.
   */
  @Override
  public int insert(GeoCraftWorld obj) {
    String preparedQuery = "INSERT INTO worlds (world_id, world_name) VALUES (?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(preparedQuery,
        PreparedStatement.RETURN_GENERATED_KEYS)) {
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

  /**
   * Updates a GeoCraftWorld object in the database.
   *
   * @param obj The GeoCraftWorld object to update.
   */
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

  /**
   * Deletes a GeoCraftWorld object from the database.
   *
   * @param obj The GeoCraftWorld object to delete.
   */
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

  /**
   * Retrieves a GeoCraftWorld object by its ID.
   *
   * @param id The ID of the GeoCraftWorld to retrieve.
   * @return The GeoCraftWorld object with the specified ID.
   */
  @Override
  public GeoCraftWorld getById(int id) {
    throw new UnsupportedOperationException("Does not support getById(int) operation");
  }

  /**
   * Retrieves a GeoCraftWorld object by its UUID.
   *
   * @param id The UUID of the GeoCraftWorld to retrieve.
   * @return The GeoCraftWorld object with the specified UUID.
   */
  public GeoCraftWorld getByUuid(UUID id) {
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

  /**
   * Retrieves all GeoCraftWorld objects from the database.
   *
   * @return A list of all GeoCraftWorld objects.
   */
  @Override
  public ArrayList<GeoCraftWorld> getAll() {
    throw new UnsupportedOperationException("Does not support getAll operation");
  }

  /**
   * Retrieves a GeoCraftWorld object by its zone ID.
   *
   * @param zoneId The zone ID to filter by.
   * @return The GeoCraftWorld object with the specified zone ID.
   */
  public GeoCraftWorld getWorldByZoneId(int zoneId) {
    String preparedQuery = "SELECT * FROM worlds WHERE world_id "
        + "= (SELECT world_id FROM road WHERE zone_id = ?)";
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

  /**
   * Gets the SQL query for creating the worlds table.
   *
   * @return The SQL query for creating the worlds table.
   */
  public String getTableCreationQuery() {
    return "CREATE TABLE IF NOT EXISTS worlds ("
        + "world_uuid VARCHAR(36) PRIMARY KEY,"
        + "world_name VARCHAR(255) NOT NULL"
        + ")";
  }
}