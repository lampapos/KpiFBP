package edu.kpi.fbp.network.components;

import com.jpmorrsn.fbp.engine.InPort;
import com.jpmorrsn.fbp.engine.OutPort;

import edu.kpi.fbp.network.datastucts.NamedArray;
import edu.kpi.fbp.network.datastucts.NamedValue;

/**
 * Standard deviation block.
 *
 * @author Pustovit Michael, pustovitm@gmail.com
 */
@InPort(value = SingleNumberStatComponent.PORT_IN, type = NamedArray.class)
@OutPort(value = SingleNumberStatComponent.PORT_OUT, type = NamedValue.class)
public class StandartDeviation extends SingleNumberStatComponent<NamedArray<Float>, NamedValue<Float>> {

  @Override
  protected NamedValue<Float> strategy(final NamedArray<Float> column) {
    Float sum = 0f;
    int nullValues = 0;
    for (final Float d : column) {
      if (d == null) {
        nullValues++;
      } else {
        sum += d;
      }
    }

    final String columnName = column.getName();
    final int count = column.size() - nullValues;
    final Float aver = new Float(sum / count);

    double deviSum = 0;
    for (final Float d : column) {
      if (d != null) {
        deviSum += (d - aver) * (d - aver);
      }
    }
    final float devi = (float) Math.sqrt(deviSum / count);

    return new NamedValue<Float>() {
      public String getName() {
        return columnName;
      }

      public Float getValue() {
        return devi;
      }

      @Override
      public String toString() {
        return getName() + " " + getValue();
      }
    };
  }

}
