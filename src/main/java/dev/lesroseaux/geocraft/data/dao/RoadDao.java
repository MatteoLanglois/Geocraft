package dev.lesroseaux.geocraft.data.dao;

import dev.lesroseaux.geocraft.models.location.GeoCraftWorld;
import dev.lesroseaux.geocraft.models.location.Road;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import org.bukkit.Location;

/**
 * Data Access Object (DAO) for the Road entity.
 */
public class RoadDao extends AbstractDao<Road> {

  /**
   * Inserts a Road object into the database.
   *
   * @param obj The Road object to insert.
   * @return The ID of the inserted Road.
   */
  @Override
  public int insert(Road obj) {
    String preparedStatement = "INSERT INTO road (zone_name, district_id, zone_point1_x, "
        + "zone_point1_y, zone_point1_z, zone_point2_x, zone_point2_y, zone_point2_z)"
        + " VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
    try (PreparedStatement statement = connection.prepareStatement(preparedStatement,
        PreparedStatement.RETURN_GENERATED_KEYS)) {
      connection.setAutoCommit(false);
      statement.setString(1, obj.getZoneName());
      statement.setInt(2, obj.getDistrictId());
      statement.setDouble(3, obj.getZonePoint1().getX());
      statement.setDouble(4, obj.getZonePoint1().getY());
      statement.setDouble(5, obj.getZonePoint1().getZ());
      statement.setDouble(6, obj.getZonePoint2().getX());
      statement.setDouble(7, obj.getZonePoint2().getY());
      statement.setDouble(8, obj.getZonePoint2().getZ());
      int result = statement.executeUpdate();
      connection.commit();
      return result;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Updates a Road object in the database.
   *
   * @param obj The Road object to update.
   */
  @Override
  public void update(Road obj) {
    String preparedStatement = "UPDATE road SET zone_name = ?, zone_point1_x = ?,"
        + " zone_point1_y = ?, zone_point1_z = ?, zone_point2_x = ?, zone_point2_y = ?,"
        + " zone_point2_z = ? WHERE zone_id = ?;";
    try {
      PreparedStatement statement = connection.prepareStatement(preparedStatement);
      connection.setAutoCommit(false);
      statement.setString(1, obj.getZoneName());
      statement.setDouble(2, obj.getZonePoint1().getX());
      statement.setDouble(3, obj.getZonePoint1().getY());
      statement.setDouble(4, obj.getZonePoint1().getZ());
      statement.setDouble(5, obj.getZonePoint2().getX());
      statement.setDouble(6, obj.getZonePoint2().getY());
      statement.setDouble(7, obj.getZonePoint2().getZ());
      statement.setInt(8, obj.getZoneId());
      statement.executeUpdate();
      connection.commit();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Deletes a Road object from the database.
   *
   * @param obj The Road object to delete.
   */
  @Override
  public void delete(Road obj) {
    String preparedStatement = "DELETE FROM road WHERE zone_id = ?;";
    try {
      PreparedStatement statement = connection.prepareStatement(preparedStatement);
      connection.setAutoCommit(false);
      statement.setInt(1, obj.getZoneId());
      statement.executeUpdate();
      connection.commit();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Retrieves a Road object by its ID.
   *
   * @param id The ID of the Road to retrieve.
   * @return The Road object with the specified ID.
   */
  @Override
  public Road getById(int id) {
    String query = "SELECT * FROM road WHERE zone_id = ?;";
    try {
      PreparedStatement statement = connection.prepareStatement(query);
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
   * Retrieves all Road objects from the database.
   *
   * @return A list of all Road objects.
   */
  @Override
  public ArrayList<Road> getAll() {
    String query = "SELECT * FROM road;";
    ArrayList<Road> roads = new ArrayList<>();
    try {
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery(query);
      while (resultSet.next()) {
        roads.add(parseResult(resultSet));
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return roads;
  }

  /**
   * Retrieves all Road objects by their district ID.
   *
   * @param districtId The district ID to filter by.
   * @return A list of Road objects in the specified district.
   */
  public ArrayList<Road> getZonesByDistrictId(int districtId) {
    String query = "SELECT * FROM road WHERE district_id = ?;";
    ArrayList<Road> roads = new ArrayList<>();
    try {
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setInt(1, districtId);
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        roads.add(parseResult(resultSet));
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return roads;
  }

  /**
   * Parses a ResultSet into a Road object.
   *
   * @param resultSet The ResultSet to parse.
   * @return The parsed Road object.
   */
  private Road parseResult(ResultSet resultSet) {
    try {
      WorldDao worldDao = new WorldDao();
      GeoCraftWorld world = worldDao.getWorldByZoneId(resultSet.getInt("zone_id"));
      Location point1 = new Location(world.getWorld(), resultSet.getDouble("zone_point1_x"),
          resultSet.getDouble("zone_point1_y"), resultSet.getDouble("zone_point1_z"));
      Location point2 = new Location(world.getWorld(), resultSet.getDouble("zone_point2_x"),
          resultSet.getDouble("zone_point2_y"), resultSet.getDouble("zone_point2_z"));
      return new Road(point1, point2, resultSet.getString("zone_name"),
          resultSet.getInt("district_id"));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Gets the SQL query for creating the road table.
   *
   * @return The SQL query for creating the road table.
   */
  public String getTableCreationQuery() {
    return "CREATE TABLE IF NOT EXISTS road ("
        + "zone_id INT AUTO_INCREMENT PRIMARY KEY,"
        + "zone_name VARCHAR(255) NOT NULL,"
        + "district_id INT NOT NULL,"
        + "zone_point1_x DOUBLE NOT NULL,"
        + "zone_point1_y DOUBLE NOT NULL,"
        + "zone_point1_z DOUBLE NOT NULL,"
        + "zone_point2_x DOUBLE NOT NULL,"
        + "zone_point2_y DOUBLE NOT NULL,"
        + "zone_point2_z DOUBLE NOT NULL,"
        + "FOREIGN KEY (district_id) REFERENCES districts(district_id)"
        + ")";
  }
}