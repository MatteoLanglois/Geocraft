package dev.lesroseaux.geocraft.data;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

/**
 * Class representing the database options for GeoCraft.
 * Implements ConfigurationSerializable for Bukkit configuration serialization.
 */
public class DatabaseOptions implements ConfigurationSerializable {
  private String host = "localhost";
  private int port = 3306;
  private String name = "geocraft";
  private String username = "geocraft";
  private String password = "password";

  /**
   * Constructor for DatabaseOptions.
   *
   * @param host     The database host.
   * @param port     The database port.
   * @param name     The database name.
   * @param username The database username.
   * @param password The database password.
   */
  public DatabaseOptions(String host, int port, String name, String username, String password) {
    this.host = host;
    this.port = port;
    this.name = name;
    this.username = username;
    this.password = password;
  }

  /**
   * Loads DatabaseOptions from a FileConfiguration.
   *
   * @param config The FileConfiguration to load from.
   * @return The loaded DatabaseOptions.
   */
  public static DatabaseOptions loadFromConfig(@NotNull FileConfiguration config) {
    return new DatabaseOptions(config.getString("database.host"), config.getInt("database.port"),
        config.getString("database.database"),
        config.getString("database.username"), config.getString("database.password"));
  }

  /**
   * Serializes the DatabaseOptions to a Map.
   *
   * @return The serialized Map.
   */
  @Override
  public @NotNull Map<String, Object> serialize() {
    Map<String, Object> data = new HashMap<>();
    data.put("host", host);
    data.put("port", port);
    data.put("database", name);
    data.put("username", username);
    data.put("password", password);

    return data;
  }

  /**
   * Deserializes DatabaseOptions from a Map.
   *
   * @param data The Map to deserialize from.
   * @return The deserialized DatabaseOptions.
   */
  public static DatabaseOptions deserialize(Map<String, Object> data) {
    return new DatabaseOptions((String) data.get("host"), (int) data.get("port"),
        (String) data.get("database"),
        (String) data.get("username"),
        (String) data.get("password"));
  }

  /**
   * Gets the database host.
   *
   * @return The database host.
   */
  public String getHost() {
    return host;
  }

  /**
   * Gets the database port.
   *
   * @return The database port.
   */
  public int getPort() {
    return port;
  }

  /**
   * Gets the database name.
   *
   * @return The database name.
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the database username.
   *
   * @return The database username.
   */
  public String getUsername() {
    return username;
  }

  /**
   * Gets the database password.
   *
   * @return The database password.
   */
  public String getPassword() {
    return password;
  }

  /**
   * Returns a string representation of the DatabaseOptions.
   *
   * @return A string representation of the DatabaseOptions.
   */
  public String toString() {
    return "DatabaseOptions{"
        + "host='" + host + '\''
        + ", port=" + port
        + ", name='" + name + '\''
        + ", username='" + username + '\''
        + ", password='" + password + '\''
        + '}';
  }
}