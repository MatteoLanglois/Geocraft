package dev.lesroseaux.geocraft.models.score;

import dev.lesroseaux.geocraft.models.game.GeocraftMap;
import dev.lesroseaux.geocraft.models.game.GeocraftPlayer;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Location;

/**
 * Manages the scores of players in GeoCraft.
 */
public class ScoreManager {
  private final Map<GeocraftPlayer, Score> scores;

  /**
   * Constructs a ScoreManager with an empty score map.
   */
  public ScoreManager() {
    this.scores = new HashMap<>();
  }

  /**
   * Adds a player to the score manager.
   * If the player is already present, no action is taken.
   *
   * @param player The player to add.
   */
  public void addPlayer(GeocraftPlayer player) {
    if (!scores.containsKey(player)) {
      scores.put(player, new Score());
    }
  }

  /**
   * Removes a player from the score manager.
   *
   * @param player The player to remove.
   */
  public void removePlayer(GeocraftPlayer player) {
    scores.remove(player);
  }

  /**
   * Adds a score to the specified player.
   * If the player is not present, no action is taken.
   *
   * @param player The player to add the score to.
   * @param score The score to add.
   */
  public void addScore(GeocraftPlayer player, int score) {
    if (scores.containsKey(player)) {
      scores.get(player).addScore(score);
    }
  }

  /**
   * Gets the score of the specified player.
   * If the player is not present, returns 0.
   *
   * @param player The player whose score to get.
   * @return The score of the player.
   */
  public int getScore(GeocraftPlayer player) {
    if (scores.containsKey(player)) {
      return scores.get(player).getScore();
    }
    return 0;
  }

  /**
   * Resets the score of the specified player to 0.
   * If the player is not present, no action is taken.
   *
   * @param player The player whose score to reset.
   */
  public void resetScore(GeocraftPlayer player) {
    if (scores.containsKey(player)) {
      scores.get(player).resetScore();
    }
  }

  /**
   * Gets the player with the highest score.
   * If no players are present, returns null.
   *
   * @return The player with the highest score, or null if no players are present.
   */
  public GeocraftPlayer getWinner() {
    return scores.entrySet().stream()
        .max(Map.Entry.comparingByValue(Comparator.comparingInt(Score::getScore)))
        .map(Map.Entry::getKey)
        .orElse(null);
  }

  /**
   * Calculates the score for each player based on their guessed location and the goal location.
   *
   * @param map The GeoCraft map.
   * @param mapStart The starting location of the map.
   */
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

  /**
   * Transforms a guessed location to the corresponding location on the map.
   *
   * @param guessLocation The guessed location.
   * @param map The GeoCraft map.
   * @param guessMapStart The starting location of the guessed map.
   * @return The transformed location.
   */
  private Location transformLocation(Location guessLocation, GeocraftMap map, Location guessMapStart) {
    double newX = guessLocation.getX() * map.getScale() + map.getMinX() + guessMapStart.getX();
    double newZ = guessLocation.getZ() * map.getScale() + map.getMinZ() + guessMapStart.getZ();
    return new Location(guessLocation.getWorld(), newX, guessLocation.getY(), newZ);
  }
}