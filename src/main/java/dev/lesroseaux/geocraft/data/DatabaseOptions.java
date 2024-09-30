package dev.lesroseaux.geocraft.data;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

public class DatabaseOptions implements ConfigurationSerializable {
  private String host = "localhost";
  private int port = 3306;
  private String name = "geocraft";
  private String username = "geocraft";
  private String password = "password";

  public DatabaseOptions(String host, int port, String name, String username, String password) {
    this.host = host;
    this.port = port;
    this.name = name;
    this.username = username;
    this.password = password;
  }

  public static DatabaseOptions loadFromConfig(@NotNull FileConfiguration config) {
    return new DatabaseOptions(config.getString("database.host"), config.getInt("database.port"),
        config.getString("database.database"),
        config.getString("database.username"), config.getString("database.password"));
  }

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

  public static DatabaseOptions deserialize(Map<String, Object> data) {
    return new DatabaseOptions((String) data.get("host"), (int) data.get("port"),
        (String) data.get("database"),
        (String) data.get("username"),
        (String) data.get("password"));
  }

  public String getHost() {
    return host;
  }

  public int getPort() {
    return port;
  }

  public String getName() {
    return name;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String toString() {
    return "DatabaseOptions{" +
        "host='" + host + '\'' +
        ", port=" + port +
        ", name='" + name + '\'' +
        ", username='" + username + '\'' +
        ", password='" + password + '\'' +
        '}';
  }
}
