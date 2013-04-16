package edu.kpi.fbp.network.components;

import com.jpmorrsn.fbp.engine.Component;
import com.jpmorrsn.fbp.engine.InPort;
import com.jpmorrsn.fbp.engine.InPorts;
import com.jpmorrsn.fbp.engine.InputPort;
import com.jpmorrsn.fbp.engine.OutPort;
import com.jpmorrsn.fbp.engine.OutputPort;
import com.jpmorrsn.fbp.engine.Packet;

import edu.kpi.fbp.network.datastucts.Column;
import edu.kpi.fbp.network.datastucts.NamedArray;
import edu.kpi.fbp.network.datastucts.NamedValue;

/**
 * Component which normalizes array  ((arr[i] - average) / standard deviation).
 *
 * @author Pustovit Michael, pustovitm@gmail.com
 */
@InPorts({
  @InPort(value = ArrayNormalizer.PORT_IN_ARRAY, type = NamedArray.class),
  @InPort(value = ArrayNormalizer.PORT_IN_AVERAGE, type = NamedValue.class),
  @InPort(value = ArrayNormalizer.PORT_IN_DEVIATION, type = NamedValue.class)
})
@OutPort(value = ArrayNormalizer.PORT_OUT, type = NamedArray.class)
public class ArrayNormalizer extends Component {
  public static final String PORT_IN_ARRAY = "ARRAY";
  public static final String PORT_IN_AVERAGE = "AVERAGE";
  public static final String PORT_IN_DEVIATION = "DEVIATION";

  public static final String PORT_OUT = "OUT";

  private InputPort inPortArray;
  private InputPort inPortAverage;
  private InputPort inPortDeviation;

  private OutputPort outPort;

  @SuppressWarnings("unchecked")
  @Override
  protected void execute() throws Exception {
    Packet<NamedArray<Float>> arrayPack;
    while ((arrayPack = inPortArray.receive()) != null) {
      final Packet<NamedValue<Float>> averagePack = inPortAverage.receive();
      final float average = averagePack.getContent().getValue();
      drop(averagePack);

      final Packet<NamedValue<Float>> deviationPack = inPortDeviation.receive();
      final float deviation = deviationPack.getContent().getValue();
      drop(deviationPack);

      final NamedArray<Float> array = arrayPack.getContent();
      drop(arrayPack);

      final Column col = new Column(array.getName());

      for (final Float f : array) {
        col.add((f - average) / deviation);
      }

      outPort.send(create(col));
    }

    inPortArray.close();
    inPortAverage.close();
    inPortDeviation.close();
    outPort.close();
  }
  @Override
  protected void openPorts() {
    inPortArray = openInput(PORT_IN_ARRAY);
    inPortAverage = openInput(PORT_IN_AVERAGE);
    inPortDeviation = openInput(PORT_IN_DEVIATION);

    outPort = openOutput(PORT_OUT);
  }

}
