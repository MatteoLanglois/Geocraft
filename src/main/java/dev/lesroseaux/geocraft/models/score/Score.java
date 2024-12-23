package dev.lesroseaux.geocraft.models.score;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a score in GeoCraft.
 */
@Setter
@Getter
@NoArgsConstructor
public class Score {
  private int score;

  /**
   * Adds the specified value to the current score.
   *
   * @param score The value to add to the current score.
   */
  public void addScore(int score) {
    this.score += score;
  }

  /**
   * Resets the score to 0.
   */
  public void resetScore() {
    this.score = 0;
  }
}