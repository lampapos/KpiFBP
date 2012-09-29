package org.kpi.fict.fbp;

import com.jpmorrsn.fbp.engine.Component;
import com.jpmorrsn.fbp.engine.ComponentDescription;
import com.jpmorrsn.fbp.engine.OutPort;
import com.jpmorrsn.fbp.engine.OutputPort;
import com.jpmorrsn.fbp.engine.Packet;

@ComponentDescription("Generates stream of packets under control of a counter")
@OutPort(value = "OUT", description = "Generated stream", type = Integer.class)
public class Generator extends Component {

  OutputPort outport;

  @Override
  protected void execute() {
    for (int i = 0; i < 100; i++) {
      @SuppressWarnings("unchecked")
      Packet<Integer> p = create(2);
      outport.send(p);
    }
  }

  @Override
  protected void openPorts() {
    outport = openOutput("OUT");
  }
}
