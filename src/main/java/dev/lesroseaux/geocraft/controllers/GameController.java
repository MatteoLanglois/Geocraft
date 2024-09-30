package dev.lesroseaux.geocraft.controllers;

import dev.lesroseaux.geocraft.models.Game.Game;
import dev.lesroseaux.geocraft.models.Location.Road;
import java.util.ArrayList;

public class GameController {
  private final Game game;

  public GameController(Game game) {
    this.game = game;
  }

  public void startGame() {
    // Choose game settings
    // Invite players
    // Start game
    ArrayList<Road> roads = game.getPlayableZone().getZones();
  }
}
