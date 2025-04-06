package itmo.lab5.models;

import itmo.lab5.models.enums.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Flat {
  private int id; // Значение поля должно быть больше 0, Значение этого поля должно быть
                  // уникальным, Значение этого поля должно генерироваться автоматически
  private String name; // Поле не может быть null, Строка не может быть пустой
  private Coordinates coordinates; // Поле не может быть null
  private Date creationDate; // Поле не может быть null, Значение этого поля должно генерироваться
                             // автоматически
  private Double area; // Максимальное значение поля: 626, Значение поля должно быть больше 0
  private int numberOfRooms; // Значение поля должно быть больше 0
  private Furnish furnish; // Поле не может быть null
  private View view; // Поле может быть null
  private Transport transport; // Поле не может быть null
  private House house; // Поле может быть null

  public Flat(int id, String name, Coordinates coordinates, Date creationDate, Double area,
      int numberOfRooms, Furnish furnish, View view, Transport transport, House house) {
    this.id = id;
    this.name = name;
    this.coordinates = coordinates;
    this.creationDate = creationDate;
    this.area = area;
    this.numberOfRooms = numberOfRooms;
    this.furnish = furnish;
    this.view = view;
    this.transport = transport;
    this.house = house;
  }

  public Flat() {

  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Coordinates getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(Coordinates coordinates) {
    this.coordinates = coordinates;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  public Double getArea() {
    return area;
  }

  public void setArea(Double area) {
    this.area = area;
  }

  public int getNumberOfRooms() {
    return numberOfRooms;
  }

  public void setNumberOfRooms(int numberOfRooms) {
    this.numberOfRooms = numberOfRooms;
  }

  public Furnish getFurnish() {
    return furnish;
  }

  public void setFurnish(Furnish furnish) {
    this.furnish = furnish;
  }

  public View getView() {
    return view;
  }

  public void setView(View view) {
    this.view = view;
  }

  public Transport getTransport() {
    return transport;
  }

  public void setTransport(Transport transport) {
    this.transport = transport;
  }

  public House getHouse() {
    return house;
  }

  public void setHouse(House house) {
    this.house = house;
  }

  @Override
  public String toString() {
    var sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    var dateFormat = (creationDate == null) ? "null" : sdf.format(creationDate);
    var builder = new StringBuilder();

    builder
        .append("Flat:\n")
        .append("  ID: ").append(id).append("\n")
        .append("  Name: ").append(name).append("\n")
        .append("  Coordinates: ").append(coordinates != null ? coordinates.toString() : "null").append("\n")
        .append("  Creation Date: ").append(dateFormat).append("\n")
        .append("  Area: ").append(area).append("\n")
        .append("  Number Of Rooms: ").append(numberOfRooms).append("\n")
        .append("  Furnish: ").append(furnish).append("\n")
        .append("  View: ").append(view != null ? view : "null").append("\n")
        .append("  Transport: ").append(transport).append("\n")
        .append("  House: ").append(house != null ? house.toString() : "null").append("\n");

    return builder.toString();
  }
}
