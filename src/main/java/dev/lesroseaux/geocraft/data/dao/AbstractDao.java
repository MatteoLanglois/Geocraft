package dev.lesroseaux.geocraft.data.dao;

import dev.lesroseaux.geocraft.data.connection.DatabaseConnection;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Abstract Data Access Object (DAO) class that provides basic CRUD operations.
 *
 * @param <T> The type of the object that this DAO handles.
 */
public abstract class AbstractDao<T> {
  protected Connection connection;

  /**
   * Constructs an AbstractDao and initializes the database connection.
   */
  public AbstractDao() {
    this.connection = DatabaseConnection.getInstance(Optional.empty()).getConnection();
  }

  /**
   * Inserts an object into the database.
   *
   * @param obj The object to insert.
   * @return The ID of the inserted object.
   */
  public abstract int insert(T obj);

  /**
   * Updates an object in the database.
   *
   * @param obj The object to update.
   */
  public abstract void update(T obj);

  /**
   * Deletes an object from the database.
   *
   * @param obj The object to delete.
   */
  public abstract void delete(T obj);

  /**
   * Retrieves an object by its ID.
   *
   * @param id The ID of the object to retrieve.
   * @return The object with the specified ID.
   */
  public abstract T getById(int id);

  /**
   * Retrieves all objects from the database.
   *
   * @return A list of all objects.
   */
  public abstract ArrayList<T> getAll();

  /**
   * Gets the SQL query for creating the table.
   *
   * @return The SQL query for creating the table.
   */
  public abstract String getTableCreationQuery();

  /**
   * Creates the table in the database using the table creation query.
   */
  public void createTable() {
    String query = getTableCreationQuery();
    try {
      connection.prepareStatement(query).executeUpdate();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}