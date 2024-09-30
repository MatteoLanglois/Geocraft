package dev.lesroseaux.geocraft.data.dao;

import dev.lesroseaux.geocraft.models.Location.GeoCraftWorld;
import dev.lesroseaux.geocraft.models.Location.Road;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import org.bukkit.Location;

public class RoadDAO extends AbstractDao<Road> {
  @Override
  public int insert(Road obj) {
    String preparedStatement = "INSERT INTO road (zone_name, district_id, zone_point1_x, zone_point1_y, " +
        "zone_point1_z, zone_point2_x, zone_point2_y, zone_point2_z) VALUES " +
        "(?, ?, ?, ?, ?, ?, ?, ?);";
    try (PreparedStatement statement = connection.prepareStatement(preparedStatement, PreparedStatement.RETURN_GENERATED_KEYS)) {
      connection.setAutoCommit(false);
      statement.setString(1, obj.getZoneName());
      statement.setInt(2, obj.getDistrictId());
      statement.setDouble(3, obj.getPoint1().getX());
      statement.setDouble(4, obj.getPoint1().getY());
      statement.setDouble(5, obj.getPoint1().getZ());
      statement.setDouble(6, obj.getPoint2().getX());
      statement.setDouble(7, obj.getPoint2().getY());
      statement.setDouble(8, obj.getPoint2().getZ());
      int result = statement.executeUpdate();
      connection.commit();
      return result;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void update(Road obj) {
    String preparedStatement = "UPDATE road SET zone_name = ?, zone_point1_x = ?,"
        + " zone_point1_y = ?, zone_point1_z = ?, zone_point2_x = ?, zone_point2_y = ?,"
        + " zone_point2_z = ? WHERE zone_id = ?;";
    try {
      PreparedStatement statement = connection.prepareStatement(preparedStatement);
      connection.setAutoCommit(false);
      statement.setString(1, obj.getZoneName());
      statement.setDouble(2, obj.getPoint1().x());
      statement.setDouble(2, obj.getPoint1().y());
      statement.setDouble(2, obj.getPoint1().z());
      statement.setDouble(2, obj.getPoint2().x());
      statement.setDouble(2, obj.getPoint2().y());
      statement.setDouble(2, obj.getPoint2().z());
      statement.setInt(8, obj.getZoneId());
      statement.executeQuery();
      connection.commit();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void delete(Road obj) {
    String preparedStatement = "DELETE FROM road WHERE zone_id = ?;";
    try {
      PreparedStatement statement = connection.prepareStatement(preparedStatement);
      connection.setAutoCommit(false);
      statement.setInt(1, obj.getZoneId());
      statement.executeQuery();
      connection.commit();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

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

  private Road parseResult(ResultSet resultSet) {
    try {
      WorldDao worldDao = new WorldDao();
      GeoCraftWorld world = worldDao.getWorldByZoneId(resultSet.getInt("zone_id"));
      Location point1 = new Location(world.getWorld(), resultSet.getDouble("zone_point1_x"),
          resultSet.getDouble("zone_point1_y"),
          resultSet.getDouble("zone_point1_z"));
      Location point2 = new Location(world.getWorld(), resultSet.getDouble("zone_point2_x"),
          resultSet.getDouble("zone_point2_y"),
          resultSet.getDouble("zone_point2_z"));
      return new Road(point1, point2, resultSet.getString("zone_name"),
          resultSet.getInt("district_id"));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static String getTableCreationQuery() {
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
