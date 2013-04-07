package edu.kpi.fbp.network.components;

import com.jpmorrsn.fbp.engine.InPort;
import com.jpmorrsn.fbp.engine.OutPort;

import edu.kpi.fbp.network.datastucts.NamedArray;
import edu.kpi.fbp.network.datastucts.NamedValue;

/**
 * Average value block.
 *
 * @author Pustovit Michael, pustovitm@gmail.com
 */
@InPort(value = SingleNumberStatComponent.PORT_IN, type = NamedArray.class)
@OutPort(value = SingleNumberStatComponent.PORT_OUT, type = NamedValue.class)
public class Average extends SingleNumberStatComponent<NamedArray<Double>, NamedValue<Double>> {

  @Override
  protected NamedValue<Double> strategy(final NamedArray<Double> column) {
    Double sum = 0d;
    int nullValues = 0;
    for (final Double d : column) {
      if (d == null) {
        nullValues++;
      } else {
        sum += d;
      }
    }

    final String columnName = column.getName();
    final Double aver = new Double(sum / (column.size() - nullValues));

    return new NamedValue<Double>() {
      public String getName() {
        return columnName;
      }

      public Double getValue() {
        return aver;
      }

      @Override
      public String toString() {
        return getName() + " " + getValue();
      }
    };
  }

}
