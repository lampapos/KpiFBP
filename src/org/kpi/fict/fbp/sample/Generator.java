package org.kpi.fict.fbp.sample;

import org.kpi.fict.fbp.javafbp.ParameterizedComponent;
import org.kpi.fict.fbp.params.ComponentParameter;
import org.kpi.fict.fbp.params.ParameterBundle;
import org.kpi.fict.fbp.params.ParameterType;

import com.jpmorrsn.fbp.engine.ComponentDescription;
import com.jpmorrsn.fbp.engine.OutPort;
import com.jpmorrsn.fbp.engine.OutputPort;
import com.jpmorrsn.fbp.engine.Packet;

/**
 * Simple generator.
 * @author Pustovit Michael, pustovitm@gmail.com
 */
@ComponentDescription("Generates stream of packets under control of a counter")
@OutPort(value = "OUT", description = "Generated stream", type = Integer.class)
@ComponentParameter(name = Generator.PARAM_COUNT_NAME, type = ParameterType.INTEGER, defaultValue = "100")
public class Generator extends ParameterizedComponent {

  /** Name of parameter "count". */
  public static final String PARAM_COUNT_NAME = "count";

  /** Output number count. */
  private int generationCount;

  /** Output port. */
  OutputPort outport;

  @Override
  protected void execute() {
    for (int i = 0; i < generationCount; i++) {
      @SuppressWarnings("unchecked")
      final Packet<Integer> p = create(2);
      outport.send(p);
    }
  }

  @Override
  protected void openPorts() {
    outport = openOutput("OUT");
  }

  @Override
  public void setParameters(final ParameterBundle bundle) {
    generationCount = bundle.getInt(PARAM_COUNT_NAME);
  }
}
