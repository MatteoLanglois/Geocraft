package dev.lesroseaux.geocraft.data.dao;

import dev.lesroseaux.geocraft.models.location.District;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

public class DistrictDao extends AbstractDao<District> {
  @Override
  public int insert(District obj) {
    String preparedStatement = "INSERT INTO districts (district_name, city_id) VALUES (?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(preparedStatement,
        PreparedStatement.RETURN_GENERATED_KEYS)) {
      connection.setAutoCommit(false);
      statement.setString(1, obj.getDistrictName());
      statement.setInt(2, obj.getCityId());
      int result = statement.executeUpdate();
      connection.commit();
      return result;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void update(District obj) {
    String preparedStatement = "UPDATE districts SET district_name = ? WHERE district_id = ?";
    try {
      PreparedStatement statement = connection.prepareStatement(preparedStatement);
      connection.setAutoCommit(false);
      statement.setString(1, obj.getDistrictName());
      statement.setInt(2, obj.getDistrictId());
      statement.executeUpdate();
      connection.commit();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void delete(District obj) {
    String preparedStatement = "DELETE FROM districts WHERE district_id = ?";
    try {
      PreparedStatement statement = connection.prepareStatement(preparedStatement);
      connection.setAutoCommit(false);
      statement.setInt(1, obj.getDistrictId());
      statement.executeUpdate();
      connection.commit();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public District getById(int id) {
    String preparedStatement = "SELECT * FROM districts WHERE district_id = ?";
    try {
      PreparedStatement statement = connection.prepareStatement(preparedStatement);
      statement.setInt(1, id);
      ResultSet result = statement.executeQuery();
      District district = new District(result.getInt("district_id"),
          result.getString("district_name"),
          result.getInt("city_id"));
      return district;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public ArrayList<District> getAll() {
    String preparedStatement = "SELECT * FROM districts";
    try {
      PreparedStatement statement = connection.prepareStatement(preparedStatement);
      ResultSet result = statement.executeQuery();
      ArrayList<District> districts = new ArrayList<>();
      while (result.next()) {
        District district = new District(result.getInt("district_id"),
            result.getString("district_name"),
            result.getInt("city_id"));
        districts.add(district);
      }
      return districts;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public ArrayList<District> getAllDistrictsByCityId(int cityId) {
    String preparedStatement = "SELECT * FROM districts WHERE city_id = ?";
    try {
      PreparedStatement statement = connection.prepareStatement(preparedStatement);
      statement.setInt(1, cityId);
      ResultSet result = statement.executeQuery();
      ArrayList<District> districts = new ArrayList<>();
      while (result.next()) {
        District district = new District(result.getInt("district_id"),
            result.getString("district_name"),
            result.getInt("city_id"));
        districts.add(district);
      }
      return districts;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public String getTableCreationQuery() {
    return "CREATE TABLE IF NOT EXISTS districts ("
        + "district_id INT AUTO_INCREMENT PRIMARY KEY,"
        + "district_name VARCHAR(255) NOT NULL,"
        + "city_id INT NOT NULL,"
        + "FOREIGN KEY (city_id) REFERENCES cities(city_id)"
        + ")";
  }

  public District getDistrictByName(@NotNull String districtName) {
    String preparedStatement = "SELECT * FROM districts WHERE LOWER(district_name) = LOWER(?)";
    try (PreparedStatement statement = connection.prepareStatement(preparedStatement)) {
      statement.setString(1, districtName);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return new District(resultSet.getInt("district_id"),
              resultSet.getString("district_name"),
              resultSet.getInt("city_id"));
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return null;
  }
}
