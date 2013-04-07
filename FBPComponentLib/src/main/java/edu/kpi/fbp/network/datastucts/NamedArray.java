package edu.kpi.fbp.network.datastucts;

import java.util.List;

public interface NamedArray<E> extends List<E> {
  /**
   * @return the list name
   */
  String getName();
}
