package dev.lesroseaux.geocraft.data.dao;

import dev.lesroseaux.geocraft.data.connection.DatabaseConnection;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Optional;

public abstract class AbstractDao<T> {
  protected Connection connection;

  public AbstractDao() {

    this.connection = DatabaseConnection.getInstance(Optional.empty()).getConnection();
  }
  public abstract int insert(T obj);
  public abstract void update(T obj);
  public abstract void delete(T obj);
  public abstract T getById(int id);
  public abstract ArrayList<T> getAll();

  public static String getTableCreationQuery() {
    return null;
  }
  public abstract void createTable();
}
