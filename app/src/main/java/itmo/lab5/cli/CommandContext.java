package itmo.lab5.cli;

import java.util.HashMap;

/**
 * This class provides a context for storing and retrieving key-value pairs 
 */
public class CommandContext {
  private HashMap<String, Object> data = new HashMap<>();

  /**
   * Stores a value associated with the specified key in the context.
   *
   * @param key the key under which the value is to be stored
   * @param value the value to be stored in the context
   */
  public void set(String key, Object value) {
    this.data.put(key, value);
  }
  
  /**
    * Retrieves the value associated with the specified key from the context.
    *
    * @param name the key associated with value
    * @return the value associated with the specified key, or {@code null}
    * if the key does not exist
    */
  public Object get(String name) {
    return this.data.get(name);
  }
}
