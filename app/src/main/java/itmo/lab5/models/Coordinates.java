package itmo.lab5.models;

/**
 * Represents a point in the provided task.
 */
public class Coordinates {
  private int x;
  private Double y; // Поле не может быть null

  public Coordinates(int x, Double y) {
    this.x = x;
    this.y = y;
  }

  public void setX(int x) {
    this.x = x;
  }

  public void setY(Double y) {
    this.y = y;
  }

  public int getX() {
    return this.x;
  }

  public Double getY() {
    return this.y;
  }

  public String toString() {
    return "Point(x = " + Integer.toString(x) + ", y = " + Double.toString(y) + ")";
  }
}
