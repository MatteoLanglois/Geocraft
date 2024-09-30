package dev.lesroseaux.geocraft.data.dao;

import dev.lesroseaux.geocraft.models.Location.City;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

public class CityDao extends AbstractDao<City> {
  @Override
  public int insert(City obj) {
    String query = "INSERT INTO cities (name, region_id) VALUES (?, ?);";
    try (PreparedStatement statement = connection.prepareStatement(query,
        PreparedStatement.RETURN_GENERATED_KEYS)) {
      connection.setAutoCommit(false);
      statement.setString(1, obj.getCity_name());
      statement.setInt(2, obj.getRegion_id());
      int result = statement.executeUpdate();
      connection.commit();
      return result;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void update(City obj) {
    String query = "UPDATE cities SET name = ? WHERE city_id = ?;";
    try {
      PreparedStatement statement = connection.prepareStatement(query);
      connection.setAutoCommit(false);
      statement.setString(1, obj.getCity_name());
      statement.setInt(2, obj.getCity_id());
      statement.executeUpdate();
      connection.commit();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

  @Override
  public void delete(City obj) {
    String query = "DELETE FROM cities WHERE city_id = ?;";
    try {
      PreparedStatement statement = connection.prepareStatement(query);
      connection.setAutoCommit(false);
      statement.setInt(1, obj.getCity_id());
      statement.executeUpdate();
      connection.commit();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

  @Override
  public City getById(int id) {
    String query = "SELECT * FROM cities WHERE city_id = ?;";
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
  public ArrayList<City> getAll() {
    String query = "SELECT * FROM cities;";
    try {
      PreparedStatement statement = connection.prepareStatement(query);
      ResultSet resultSet = statement.executeQuery();
      ArrayList<City> cities = new ArrayList<>();
      while (resultSet.next()) {
        cities.add(parseResult(resultSet));
      }
      return cities;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public ArrayList<City> getAllCitiesByRegionId(int regionId) {
    String query = "SELECT * FROM cities WHERE region_id = ?;";
    try {
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setInt(1, regionId);
      ResultSet resultSet = statement.executeQuery();
      ArrayList<City> cities = new ArrayList<>();
      while (resultSet.next()) {
        cities.add(parseResult(resultSet));
      }
      return cities;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static String getTableCreationQuery() {
    return "CREATE TABLE IF NOT EXISTS cities ("
        + "city_id INT AUTO_INCREMENT PRIMARY KEY,"
        + "name VARCHAR(255) NOT NULL,"
        + "region_id INT NOT NULL,"
        + "FOREIGN KEY (region_id) REFERENCES regions(region_id)"
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

  public City getCityByName(@NotNull String cityName) {
    String query = "SELECT * FROM cities WHERE name = ?;";
    try {
      var statement = connection.prepareStatement(query);
      statement.setString(1, cityName);
      var resultSet = statement.executeQuery();
      if (resultSet.next()) {
        return parseResult(resultSet);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return null;
  }

  private City parseResult(ResultSet resultSet) {
    try {
      return new City(resultSet.getInt("city_id"),
          resultSet.getString("name"),
          resultSet.getInt("region_id"));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
