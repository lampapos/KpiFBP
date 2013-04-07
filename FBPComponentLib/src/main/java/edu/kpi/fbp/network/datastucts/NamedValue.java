package edu.kpi.fbp.network.datastucts;

/**
 * Any object with type.
 *
 * @param <T> object type
 * @author Pustovit Michael, pustovitm@gmail.com
 */
public interface NamedValue<T> {
  /**
   * @return the value name
   */
  String getName();

  /**
   * @return the value
   */
  T getValue();
}
