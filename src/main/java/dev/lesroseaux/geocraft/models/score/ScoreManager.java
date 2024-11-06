package dev.lesroseaux.geocraft.models.score;

import dev.lesroseaux.geocraft.models.game.GeocraftMap;
import dev.lesroseaux.geocraft.models.game.GeocraftPlayer;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Location;

public class ScoreManager {
  private final Map<GeocraftPlayer, Score> scores;

  public ScoreManager() {
    this.scores = new HashMap<>();
  }

  public void addPlayer(GeocraftPlayer player) {
    if (!scores.containsKey(player)) {
      scores.put(player, new Score());
    }
  }

  public void removePlayer(GeocraftPlayer player) {
    scores.remove(player);
  }

  public void addScore(GeocraftPlayer player, int score) {
    if (scores.containsKey(player)) {
      scores.get(player).addScore(score);
    }
  }

  public int getScore(GeocraftPlayer player) {
    if (scores.containsKey(player)) {
      return scores.get(player).getScore();
    }
    return 0;
  }

  public void resetScore(GeocraftPlayer player) {
    if (scores.containsKey(player)) {
      scores.get(player).resetScore();
    }
  }

  public GeocraftPlayer getWinner() {
    return scores.entrySet().stream()
        .max(Map.Entry.comparingByValue(Comparator.comparingInt(Score::getScore)))
        .map(Map.Entry::getKey)
        .orElse(null);
  }

  public void calculateScore(GeocraftMap map, Location mapStart) {
    scores.forEach((player, score) -> {
      // Calculate score
      Location goal = player.getTpLocation();
      Location guess = player.getGuessedLocation();

      if (goal != null && guess != null) {
        Location transformedGuess = transformLocation(guess, map, mapStart);
        transformedGuess.setY(goal.getY());
        double distance = goal.distance(transformedGuess);
        double maxDistance = Math.sqrt(Math.pow(map.getMaxX() - map.getMinX(), 2)
            + Math.pow(map.getMaxZ() - map.getMinZ(), 2));
        int scoreValue = (int) ((1 - distance / maxDistance) * 5000);
        score.addScore(scoreValue);
      }
    });
  }

  private Location transformLocation(Location guessLocation, GeocraftMap map, Location guessMapStart) {
    double newX = guessLocation.getX() * map.getScale() + map.getMinX() + guessMapStart.getX();
    double newZ = guessLocation.getZ() * map.getScale() + map.getMinZ() + guessMapStart.getZ();
    return new Location(guessLocation.getWorld(), newX, guessLocation.getY(), newZ);
  }
}
