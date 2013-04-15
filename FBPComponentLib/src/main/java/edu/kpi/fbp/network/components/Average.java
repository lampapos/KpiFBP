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
public class Average extends SingleNumberStatComponent<NamedArray<Float>, NamedValue<Float>> {

  @Override
  protected NamedValue<Float> strategy(final NamedArray<Float> column) {
    Double sum = 0d;
    int nullValues = 0;
    for (final Float d : column) {
      if (d == null) {
        nullValues++;
      } else {
        sum += d;
      }
    }

    final String columnName = column.getName();
    final Float aver = new Float(sum / (column.size() - nullValues));

    return new NamedValue<Float>() {
      public String getName() {
        return columnName;
      }

      public Float getValue() {
        return aver;
      }

      @Override
      public String toString() {
        return getName() + " " + getValue();
      }
    };
  }

}
