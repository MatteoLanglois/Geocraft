package dev.lesroseaux.geocraft.data.dao;

import java.util.ArrayList;
import org.bukkit.entity.Player;

public class PlayerDao extends AbstractDao<Player> {
  @Override
  public int insert(Player obj) {
    return 0;
  }

  @Override
  public void update(Player obj) {

  }

  @Override
  public void delete(Player obj) {

  }

  @Override
  public Player getById(int id) {
    return null;
  }

  @Override
  public ArrayList<Player> getAll() {
    return null;
  }

  @Override
  public void createTable() {

  }
}
