package itmo.lab5.cli.helpers;

import java.util.ArrayDeque;
import java.util.Deque;

public class History {
  private static final int MAX_SIZE = 8;
  private final Deque<String> history = new ArrayDeque<String>(MAX_SIZE);

  public void add(String command) {
    if (history.size() >= MAX_SIZE)
      history.removeFirst();

    history.add(command);
  }

  public String get(int x) {
    if (x >= MAX_SIZE)
      return "";

    return (String) history.toArray()[x];
  }

  public String get() {
    return history.toString();
  }

  public String toString() {
    var builder = new StringBuilder("History: \n");
    history.forEach(command -> builder.append(" - " + command + "\n"));
    return builder.toString();
  }
}
