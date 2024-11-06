package dev.lesroseaux.geocraft.models.score;

public class Score {
  private int score;

  public Score() {
    this.score = 0;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public void addScore(int score) {
    this.score += score;
  }

  public void resetScore() {
    this.score = 0;
  }
}
