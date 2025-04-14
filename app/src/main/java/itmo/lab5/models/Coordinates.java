package itmo.lab5.models;

/**
 * Represents a point in the provided task.
 */
public class Coordinates {
  private int x;
  private Long y; // Поле не может быть null

  public Coordinates(int x, Long y) {
    this.x = x;
    this.y = y;
  }

  public void setX(int x) {
    this.x = x;
  }

  public void setY(Long y) {
    this.y = y;
  }

  public int getX() {
    return this.x;
  }

  public Long getY() {
    return this.y;
  }

  public String toString() {
    return "Point(x = " + Integer.toString(x) + ", y = " + Long.toString(y) + ")";
  }
}
