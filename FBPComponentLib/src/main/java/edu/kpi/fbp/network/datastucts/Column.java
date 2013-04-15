package edu.kpi.fbp.network.datastucts;

import java.util.ArrayList;

/**
 * TSV file column object model.
 *
 * @author Pustovit Michael, pustovitm@gmail.com
 */
public class Column extends ArrayList<Float> implements NamedArray<Float> {
  private static final long serialVersionUID = -2978161943480846484L;

  private final String title;

  /**
   * @param title the column title
   */
  public Column(final String title) {
    this.title = title;
  }

  /**
   * @see edu.kpi.fbp.network.datastucts.NamedArray#getName()
   * @return the list name
   */
  public String getName() {
    return title;
  }

  @Override
  public String toString() {
    final StringBuilder strBuilder = new StringBuilder();

    strBuilder.append(title);
    strBuilder.append("\n");

    for (final Float d : this) {
      strBuilder.append(d);
      strBuilder.append("\n");
    }

    return strBuilder.toString();
  }
}
