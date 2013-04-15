package edu.kpi.fbp.network.components;

import com.jpmorrsn.fbp.engine.InPort;
import com.jpmorrsn.fbp.engine.InputPort;
import com.jpmorrsn.fbp.engine.OutPort;
import com.jpmorrsn.fbp.engine.OutputPort;
import com.jpmorrsn.fbp.engine.Packet;

import edu.kpi.fbp.javafbp.ParameterizedComponent;
import edu.kpi.fbp.network.datastucts.NamedArray;
import edu.kpi.fbp.network.datastucts.NamedValue;

/**
 * Base class for components that have single input port and produce single number as result.
 *
 * @param <InType> input package type
 * @param <OutType> output package type
 * @author Pustovit Michael, pustovitm@gmail.com
 */
@InPort(value = SingleNumberStatComponent.PORT_IN, type = NamedArray.class)
@OutPort(value = SingleNumberStatComponent.PORT_OUT, type = NamedValue.class)
public abstract class SingleNumberStatComponent<InType, OutType> extends ParameterizedComponent {
  public static final String PORT_IN = "IN";
  public static final String PORT_OUT = "OUT";

  private InputPort inPort;
  private OutputPort outPort;

  /**
   * @param in the input package
   * @return packet processing result
   */
  protected abstract OutType strategy(InType in);

  @Override
  @SuppressWarnings("unchecked")
  protected void execute() throws Exception {
    Packet<InType> inPack;
    while ((inPack = inPort.receive()) != null) {
      final InType column = inPack.getContent();
      drop(inPack);

      final OutType res = strategy(column);
      outPort.send(create(res));
    }

    outPort.close();
  }

  @Override
  protected void openPorts() {
    inPort = openInput(PORT_IN);
    outPort = openOutput(PORT_OUT);
  }
}
