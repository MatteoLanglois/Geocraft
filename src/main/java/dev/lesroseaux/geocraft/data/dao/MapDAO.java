package dev.lesroseaux.geocraft.data.dao;

import dev.lesroseaux.geocraft.models.Game.Map;
import java.util.ArrayList;

public class MapDAO extends AbstractDao<Map> {
  @Override
  public int insert(Map obj) {
    return 0;
  }

  @Override
  public void update(Map obj) {
  }

  @Override
  public void delete(Map obj) {
  }

  @Override
  public Map getById(int id) {
    return null;
  }

  @Override
  public ArrayList<Map> getAll() {
    return null;
  }

  @Override
  public void createTable() {

  }
}
