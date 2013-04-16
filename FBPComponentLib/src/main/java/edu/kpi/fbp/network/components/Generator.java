package edu.kpi.fbp.network.components;


import com.jpmorrsn.fbp.engine.ComponentDescription;
import com.jpmorrsn.fbp.engine.InPort;
import com.jpmorrsn.fbp.engine.InputPort;
import com.jpmorrsn.fbp.engine.OutPort;
import com.jpmorrsn.fbp.engine.OutputPort;
import com.jpmorrsn.fbp.engine.Packet;

import edu.kpi.fbp.javafbp.ParameterizedComponent;
import edu.kpi.fbp.params.ComponentParameter;
import edu.kpi.fbp.params.ParameterType;

/**
 * Simple generator.
 *
 * $$Генератор констант.$$
 *
 * @author Pustovit Michael, pustovitm@gmail.com
 */
@ComponentDescription("Generates stream of packets under control of a counter")
@OutPort(value = Generator.PORT_OUT, description = "Generated stream", type = Integer.class)
@InPort(value = Generator.PORT_IN_COUNT, description = "Generated packets count", type = Integer.class)
@ComponentParameter(port = Generator.PORT_IN_COUNT, type = ParameterType.INTEGER, defaultValue = "100")
public class Generator extends ParameterizedComponent {

  public static final String PORT_OUT = "OUT";
  public static final String PORT_IN_COUNT = "COUNT";

  /** Output number count. */
  private int generationCount;

  /** Output port. */
  OutputPort outport;
  InputPort countPort;

  @SuppressWarnings("unchecked")
  @Override
  protected void execute() {
    final Packet<Integer> countPacket = countPort.receive();
    generationCount = countPacket.getContent();
    drop(countPacket);

    for (int i = 0; i < generationCount; i++) {
      final Packet<Integer> p = create(2);
      outport.send(p);
    }
  }

  @Override
  protected void openPorts() {
    outport = openOutput("OUT");
    countPort = openInput(PORT_IN_COUNT);
  }

}
