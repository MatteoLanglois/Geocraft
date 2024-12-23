package dev.lesroseaux.geocraft.data.connection;

import dev.lesroseaux.geocraft.data.DatabaseOptions;
import dev.lesroseaux.geocraft.data.dao.CityDao;
import dev.lesroseaux.geocraft.data.dao.DistrictDao;
import dev.lesroseaux.geocraft.data.dao.LocationToMapDao;
import dev.lesroseaux.geocraft.data.dao.MapDao;
import dev.lesroseaux.geocraft.data.dao.RegionDao;
import dev.lesroseaux.geocraft.data.dao.RoadDao;
import dev.lesroseaux.geocraft.data.dao.WorldDao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Singleton class for managing the database connection.
 */
public class DatabaseConnection {
  private static DatabaseConnection instance;
  private Connection connection;
  private final DatabaseOptions options;

  /**
   * Private constructor for DatabaseConnection.
   *
   * @param databaseOptions The database options.
   */
  private DatabaseConnection(DatabaseOptions databaseOptions) {
    this.options = databaseOptions;
    try {
      connection = DriverManager.getConnection("jdbc:mysql://" + options.getHost() + ":"
          + options.getPort() + "/" + options.getName() + "?autoReconnect=true",
          options.getUsername(),
          options.getPassword());
      // Create tables
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Returns the singleton instance of DatabaseConnection.
   *
   * @param databaseOptions The optional database options.
   * @return The singleton instance of DatabaseConnection.
   */
  public static DatabaseConnection getInstance(Optional<DatabaseOptions> databaseOptions) {
    if (databaseOptions.isEmpty()) {
      if (instance != null) {
        return instance;
      } else {
        throw new IllegalArgumentException("Database options are required");
      }
    }
    if (instance == null || !instance.options.equals(databaseOptions.get()) || !instance.isValidConnection()) {
      instance = new DatabaseConnection(databaseOptions.get());
      new WorldDao().createTable();
      new RegionDao().createTable();
      new CityDao().createTable();
      new DistrictDao().createTable();
      new RoadDao().createTable();
      new LocationToMapDao().createTable();
      new MapDao().createTable();
    }
    return instance;
  }

  /**
   * Returns the current database connection.
   *
   * @return The current database connection.
   */
  public Connection getConnection() {
    return connection;
  }

  /**
   * Closes the current database connection.
   */
  public void closeConnection() {
    if (connection != null) {
      try {
        connection.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Opens a new database connection.
   */
  public void openConnection() {
    try {
      connection = DriverManager.getConnection("jdbc:mysql://" + options.getHost() + ":"
          + options.getPort() + "/" + options.getName() + "?autoReconnect=true",
          options.getUsername(),
          options.getPassword());
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Checks if the current database connection is valid.
   *
   * @return True if the connection is valid, false otherwise.
   */
  public boolean isValidConnection() {
    try {
      return connection != null && !connection.isClosed() && connection.isValid(2);
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }
}