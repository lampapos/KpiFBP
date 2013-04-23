package edu.kpi.fbp.network.components;

import com.jpmorrsn.fbp.engine.Component;
import com.jpmorrsn.fbp.engine.ComponentDescription;
import com.jpmorrsn.fbp.engine.InPort;
import com.jpmorrsn.fbp.engine.InputPort;
import com.jpmorrsn.fbp.engine.OutPort;
import com.jpmorrsn.fbp.engine.OutputPort;
import com.jpmorrsn.fbp.engine.Packet;

/**
 * Component which has single input port and array of output ports. It just copy value from input port to the
 * all out ports.
 *
 * @author Pustovit Michael, pustovitm@gmail.com
 */
@InPort(Multiplicator.PORT_IN)
@OutPort(value = Multiplicator.PORT_OUT, arrayPort = true)
@ComponentDescription(
    "Component which has single input port and array of output ports. It just copy value from input port to the\n"
  + "all out ports.")
public class Multiplicator extends Component {

  public static final String PORT_IN = "IN";
  public static final String PORT_OUT = "OUT";

  private InputPort inPort;
  private OutputPort[] outPorts;

  @Override
  protected void execute() throws Exception {
    Packet<?> pack;
    while ((pack = inPort.receive()) != null) {
      final Object o = pack.getContent();
      for (final OutputPort p : outPorts) {
        p.send(create(o));
      }
      drop(pack);
    }

    inPort.close();
    for (final OutputPort p : outPorts) {
      p.close();
    }
  }

  @Override
  protected void openPorts() {
    inPort = openInput(PORT_IN);
    outPorts = openOutputArray(PORT_OUT);
  }

}
