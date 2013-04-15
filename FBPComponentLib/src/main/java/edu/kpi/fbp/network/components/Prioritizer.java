package edu.kpi.fbp.network.components;

import com.jpmorrsn.fbp.engine.InPort;
import com.jpmorrsn.fbp.engine.InPorts;
import com.jpmorrsn.fbp.engine.InputPort;
import com.jpmorrsn.fbp.engine.OutPort;

import edu.kpi.fbp.network.datastucts.HtmlNode;
import edu.kpi.fbp.params.ComponentParameter;
import edu.kpi.fbp.params.ParameterType;

@InPorts({
  @InPort(value = SingleNumberStatComponent.PORT_IN, type = HtmlNode.class),
  @InPort(value = Prioritizer.PORT_PRIORITY, type = Integer.class),
})

@OutPort(value = SingleNumberStatComponent.PORT_OUT, type = HtmlNode.class)

@ComponentParameter(port = Prioritizer.PORT_PRIORITY, type = ParameterType.INTEGER, defaultValue = "0")
public class Prioritizer extends SingleNumberStatComponent<HtmlNode, HtmlNode> {
  public static final String PORT_PRIORITY = "PRIORITY";

  private InputPort inPortPriority;

  private int priority;

  @Override
  protected void execute() throws Exception {
    priority = readParam(inPortPriority);

    super.execute();
  }

  @Override
  protected HtmlNode strategy(final HtmlNode in) {
    in.setPriority(priority);
    return new HtmlNode(in.getPriority(), in.getHtml());
  }

  @Override
  protected void openPorts() {
    super.openPorts();
    inPortPriority = openInput(PORT_PRIORITY);
  }

}