package edu.kpi.fbp.network.components;

import com.jpmorrsn.fbp.engine.InPort;
import com.jpmorrsn.fbp.engine.OutPort;

import edu.kpi.fbp.network.datastucts.NamedArray;
import edu.kpi.fbp.network.datastucts.NamedValue;

@InPort(value = SingleNumberStatComponent.PORT_IN, type = NamedArray.class)
@OutPort(value = SingleNumberStatComponent.PORT_OUT, type = NamedValue.class)
public class StandartDeviation extends SingleNumberStatComponent<NamedArray<Double>, NamedValue<Double>> {

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
    final int count = column.size() - nullValues;
    final Double aver = new Double(sum / count);

    double deviSum = 0;
    for (final Double d : column) {
      if (d != null) {
        deviSum += (d - aver) * (d - aver);
      }
    }
    final double devi = Math.sqrt(deviSum / count);

    return new NamedValue<Double>() {
      public String getName() {
        return columnName;
      }

      public Double getValue() {
        return devi;
      }

      @Override
      public String toString() {
        return getName() + " " + getValue();
      }
    };
  }

}
