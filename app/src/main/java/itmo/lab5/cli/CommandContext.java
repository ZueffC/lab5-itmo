package itmo.lab5.cli;

import java.util.HashMap;

public class CommandContext {
  private HashMap<String, Object> data = new HashMap<>();

  public void set(String key, Object value) {
    this.data.put(key, value);
  }

  public Object get(String name) {
    return this.data.get(name);
  }
}
