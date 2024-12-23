package dev.lesroseaux.geocraft.models.score;

/**
 * Represents a score in GeoCraft.
 */
public class Score {
  private int score;

  /**
   * Constructs a Score with an initial value of 0.
   */
  public Score() {
    this.score = 0;
  }

  /**
   * Gets the current score.
   *
   * @return The current score.
   */
  public int getScore() {
    return score;
  }

  /**
   * Sets the score to the specified value.
   *
   * @param score The new score value.
   */
  public void setScore(int score) {
    this.score = score;
  }

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