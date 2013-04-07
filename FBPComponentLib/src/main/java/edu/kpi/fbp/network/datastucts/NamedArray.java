package edu.kpi.fbp.network.datastucts;

import java.util.List;

/**
 * Array with name.
 *
 * @param <E> list element type
 * @author Pustovit Michael, pustovitm@gmail.com
 */
public interface NamedArray<E> extends List<E> {
  /**
   * @return the list name
   */
  String getName();
}
