package dev.lesroseaux.geocraft.data.connection;

import dev.lesroseaux.geocraft.data.DatabaseOptions;
import dev.lesroseaux.geocraft.data.dao.CityDao;
import dev.lesroseaux.geocraft.data.dao.DistrictDao;
import dev.lesroseaux.geocraft.data.dao.GameDao;
import dev.lesroseaux.geocraft.data.dao.RegionDao;
import dev.lesroseaux.geocraft.data.dao.WorldDao;
import dev.lesroseaux.geocraft.data.dao.RoadDAO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

public class DatabaseConnection {
  private static DatabaseConnection instance;
  private Connection connection;
  private final DatabaseOptions options;

  private DatabaseConnection(DatabaseOptions databaseOptions) {
    this.options = databaseOptions;
    try {
      connection = DriverManager.getConnection("jdbc:mysql://" + options.getHost() + ":"
          + options.getPort() + "/" + options.getName(), options.getUsername(), options.getPassword());
      // Create tables

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static DatabaseConnection getInstance(Optional<DatabaseOptions> databaseOptions) {
    if (databaseOptions.isEmpty()) {
      if (instance != null) {
        return instance;
      } else {
        throw new IllegalArgumentException("Database options are required");
      }
    }
    if (instance == null || !instance.options.equals(databaseOptions.get())) {
      instance = new DatabaseConnection(databaseOptions.get());
      new WorldDao().createTable();
      new RegionDao().createTable();
      new CityDao().createTable();
      new DistrictDao().createTable();
      new RoadDAO().createTable();
      new GameDao().createTable();
    }
    return instance;
  }

  public Connection getConnection() {
    return connection;
  }

  public void closeConnection() {
    try {
      connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void openConnection() {
    try {
      connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/geocraft", "geocraft", "password");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
