package edu.kpi.fbp.network.components;

import com.jpmorrsn.fbp.engine.Component;
import com.jpmorrsn.fbp.engine.InPort;
import com.jpmorrsn.fbp.engine.InPorts;
import com.jpmorrsn.fbp.engine.InputPort;
import com.jpmorrsn.fbp.engine.OutPort;
import com.jpmorrsn.fbp.engine.OutputPort;
import com.jpmorrsn.fbp.engine.Packet;

@InPorts({
  @InPort("IN0"),
  @InPort("IN1"),
  @InPort("IN2")
})
@OutPort("OUT")
public class Concentrator extends Component {

  private InputPort in0, in1, in2;
  private OutputPort out;

  @Override
  protected void execute() throws Exception {
    Packet<Object> pack;
    while ((pack = in0.receive()) != null) {
      out.send(create(pack.getContent()));
      drop(pack);
    }
    in0.close();

    while ((pack = in1.receive()) != null) {
      out.send(create(pack.getContent()));
      drop(pack);
    }
    in1.close();

    while ((pack = in2.receive()) != null) {
      out.send(create(pack.getContent()));
      drop(pack);
    }
    in2.close();

    out.close();
  }

  @Override
  protected void openPorts() {
    in0 = openInput("in0");
    in1 = openInput("in1");
    in2 = openInput("in2");
    out = openOutput("out");
  }

}
