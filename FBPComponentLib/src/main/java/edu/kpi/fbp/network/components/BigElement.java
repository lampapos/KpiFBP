package edu.kpi.fbp.network.components;

import com.jpmorrsn.fbp.engine.InPort;
import com.jpmorrsn.fbp.engine.InPorts;
import com.jpmorrsn.fbp.engine.OutPort;
import com.jpmorrsn.fbp.engine.OutPorts;

import edu.kpi.fbp.javafbp.ParameterizedComponent;
import edu.kpi.fbp.params.ComponentParameter;
import edu.kpi.fbp.params.ComponentParameters;
import edu.kpi.fbp.params.ParameterBundle;
import edu.kpi.fbp.params.ParameterType;

/**
 * Sample element with ports, array ports and parameters.
 *
 * @author Pustovit Michael, pustovitm@gmail.com
 */
@InPorts({
    @InPort(value = "InPort1", type = Integer.class, description = "Simple in port"),
    @InPort(value = "InPort2", type = String.class),
    @InPort(value = "InSECOND_ARR*", type = Byte.class, fixedSize = true, setDimension = 4)})
@OutPorts({
  @OutPort(value = "OutPort1", type = String.class),
  @OutPort(value = "OutPort2", type = Byte.class),
  @OutPort(value = "OutPort3", type = Long[].class)
})
@ComponentParameters({
  @ComponentParameter(name = "Param1", type = ParameterType.BOOLEAN, defaultValue = "true"),
  @ComponentParameter(name = "Param2", type = ParameterType.STRING, defaultValue = "abc"),
  @ComponentParameter(name = "Param3", type = ParameterType.FLOAT, defaultValue = "1.3"),
  @ComponentParameter(name = "Param3", type = ParameterType.INTEGER, defaultValue = "10"),
})
public class BigElement extends ParameterizedComponent {

  @Override
  public void setParameters(final ParameterBundle arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  protected void execute() throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  protected void openPorts() {
    // TODO Auto-generated method stub

  }

}
