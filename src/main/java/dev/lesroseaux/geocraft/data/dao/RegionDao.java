package dev.lesroseaux.geocraft.data.dao;

import dev.lesroseaux.geocraft.models.location.Region;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

/**
 * Data Access Object (DAO) for the Region entity.
 */
public class RegionDao extends AbstractDao<Region> {

  /**
   * Inserts a Region object into the database.
   *
   * @param obj The Region object to insert.
   * @return The ID of the inserted Region.
   */
  @Override
  public int insert(Region obj) {
    String preparedStatement = "INSERT INTO regions (region_name, world_id) VALUES (?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(preparedStatement,
        PreparedStatement.RETURN_GENERATED_KEYS)) {
      connection.setAutoCommit(false);
      statement.setString(1, obj.getRegionName());
      statement.setString(2, obj.getWorldId().toString());
      int result = statement.executeUpdate();
      connection.commit();
      return result;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Updates a Region object in the database.
   *
   * @param obj The Region object to update.
   */
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

  /**
   * Deletes a Region object from the database.
   *
   * @param obj The Region object to delete.
   */
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

  /**
   * Retrieves a Region object by its ID.
   *
   * @param id The ID of the Region to retrieve.
   * @return The Region object with the specified ID.
   */
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

  /**
   * Retrieves all Region objects from the database.
   *
   * @return A list of all Region objects.
   */
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

  /**
   * Retrieves all Region objects by their world ID.
   *
   * @param worldId The world ID to filter by.
   * @return A list of Region objects in the specified world.
   */
  public ArrayList<Region> getAllRegionsByWorldId(UUID worldId) {
    return null;
  }

  /**
   * Parses a ResultSet into a Region object.
   *
   * @param result The ResultSet to parse.
   * @return The parsed Region object.
   */
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

  /**
   * Gets the SQL query for creating the regions table.
   *
   * @return The SQL query for creating the regions table.
   */
  public String getTableCreationQuery() {
    return "CREATE TABLE IF NOT EXISTS regions ("
        + "region_id INT AUTO_INCREMENT PRIMARY KEY,"
        + "region_name VARCHAR(255) NOT NULL,"
        + "world_id VARCHAR(36) NOT NULL,"
        + "FOREIGN KEY (world_id) REFERENCES worlds(world_id)"
        + ")";
  }

  /**
   * Retrieves a Region object by its name.
   *
   * @param regionName The name of the Region to retrieve.
   * @return The Region object with the specified name.
   */
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