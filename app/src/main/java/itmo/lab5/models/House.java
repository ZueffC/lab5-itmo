package itmo.lab5.models;

public class House {
  private String name; // Поле не может быть null
  private int year; // Максимальное значение поля: 959, Значение поля должно быть больше 0
  private long numberOfFloors; // Максимальное значение поля: 77, Значение поля должно быть больше 0

  public House(String name, int year, long numberOfFloors) {
    this.name = name;
    this.year = year;
    this.numberOfFloors = numberOfFloors;
  }

  public String getName() {
    return this.name;
  }

  public int getYear() {
    return this.year;
  }

  public long getNumberOfFloors() {
    return this.numberOfFloors;
  }

  public String toString() {
    return "House(" +
        "name = '" + name +
        "', year = " + Integer.toString(year) +
        ", numberOfFloors = " + Long.toString(numberOfFloors) +
        ")";
  }
}
