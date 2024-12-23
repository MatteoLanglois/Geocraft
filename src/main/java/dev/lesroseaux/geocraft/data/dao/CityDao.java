package dev.lesroseaux.geocraft.data.dao;

import dev.lesroseaux.geocraft.models.location.City;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * Data Access Object (DAO) for the City entity.
 */
public class CityDao extends AbstractDao<City> {

  /**
   * Inserts a City object into the database.
   *
   * @param obj The City object to insert.
   * @return The ID of the inserted City.
   */
  @Override
  public int insert(City obj) {
    String query = "INSERT INTO cities (name, region_id) VALUES (?, ?);";
    try (PreparedStatement statement = connection.prepareStatement(query,
        PreparedStatement.RETURN_GENERATED_KEYS)) {
      connection.setAutoCommit(false);
      statement.setString(1, obj.getCityName());
      statement.setInt(2, obj.getRegionId());
      int result = statement.executeUpdate();
      connection.commit();
      return result;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Updates a City object in the database.
   *
   * @param obj The City object to update.
   */
  @Override
  public void update(City obj) {
    String query = "UPDATE cities SET name = ? WHERE city_id = ?;";
    try {
      PreparedStatement statement = connection.prepareStatement(query);
      connection.setAutoCommit(false);
      statement.setString(1, obj.getCityName());
      statement.setInt(2, obj.getCityId());
      statement.executeUpdate();
      connection.commit();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Deletes a City object from the database.
   *
   * @param obj The City object to delete.
   */
  @Override
  public void delete(City obj) {
    String query = "DELETE FROM cities WHERE city_id = ?;";
    try {
      PreparedStatement statement = connection.prepareStatement(query);
      connection.setAutoCommit(false);
      statement.setInt(1, obj.getCityId());
      statement.executeUpdate();
      connection.commit();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Retrieves a City object by its ID.
   *
   * @param id The ID of the City to retrieve.
   * @return The City object with the specified ID.
   */
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

  /**
   * Retrieves all City objects from the database.
   *
   * @return A list of all City objects.
   */
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

  /**
   * Retrieves all City objects by their region ID.
   *
   * @param regionId The region ID to filter by.
   * @return A list of City objects in the specified region.
   */
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

  /**
   * Gets the SQL query for creating the cities table.
   *
   * @return The SQL query for creating the cities table.
   */
  public String getTableCreationQuery() {
    return "CREATE TABLE IF NOT EXISTS cities ("
        + "city_id INT AUTO_INCREMENT PRIMARY KEY,"
        + "name VARCHAR(255) NOT NULL,"
        + "region_id INT NOT NULL,"
        + "FOREIGN KEY (region_id) REFERENCES regions(region_id)"
        + ")";
  }

  /**
   * Retrieves a City object by its name.
   *
   * @param cityName The name of the City to retrieve.
   * @return The City object with the specified name.
   */
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

  /**
   * Parses a ResultSet into a City object.
   *
   * @param resultSet The ResultSet to parse.
   * @return The parsed City object.
   */
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