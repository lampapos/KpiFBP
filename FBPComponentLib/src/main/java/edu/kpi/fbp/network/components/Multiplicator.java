package edu.kpi.fbp.network.components;

import com.jpmorrsn.fbp.engine.InPort;
import com.jpmorrsn.fbp.engine.InputPort;
import com.jpmorrsn.fbp.engine.OutPort;
import com.jpmorrsn.fbp.engine.OutputPort;
import com.jpmorrsn.fbp.engine.Packet;

import edu.kpi.fbp.javafbp.ParameterizedComponent;

@InPort(Multiplicator.PORT_IN)
@OutPort(value = Multiplicator.PORT_OUT, arrayPort = true)
public class Multiplicator extends ParameterizedComponent {

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