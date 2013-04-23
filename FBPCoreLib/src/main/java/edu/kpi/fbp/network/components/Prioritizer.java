package edu.kpi.fbp.network.components;

import com.jpmorrsn.fbp.engine.ComponentDescription;
import com.jpmorrsn.fbp.engine.InPort;
import com.jpmorrsn.fbp.engine.InPorts;
import com.jpmorrsn.fbp.engine.InputPort;
import com.jpmorrsn.fbp.engine.OutPort;

import edu.kpi.fbp.network.datastucts.HtmlNode;
import edu.kpi.fbp.params.ComponentParameter;
import edu.kpi.fbp.params.ParamUtils;
import edu.kpi.fbp.params.ParameterType;

/**
 * Component sets priority of the HTML node. HtmlReport sort nodes in this priority (ellements at the
 * very beginning have lowest priority).
 *
 * @author Pustovit Michael, pustovitm@gmail.com
 */
@InPorts({
  @InPort(value = SingleNumberStatComponent.PORT_IN, type = HtmlNode.class),
  @InPort(value = Prioritizer.PORT_PRIORITY, type = Integer.class),
})

@OutPort(value = SingleNumberStatComponent.PORT_OUT, type = HtmlNode.class)

@ComponentParameter(port = Prioritizer.PORT_PRIORITY, type = ParameterType.INTEGER, defaultValue = "0")
@ComponentDescription(
    "Component sets priority of the HTML node. HtmlReport sort nodes in this priority (ellements at the\n"
  + "very beginning have lowest priority).")
public class Prioritizer extends SingleNumberStatComponent<HtmlNode, HtmlNode> {
  public static final String PORT_PRIORITY = "PRIORITY";

  private InputPort inPortPriority;

  private int priority;

  @Override
  protected void execute() throws Exception {
    priority = ParamUtils.readParam(this, inPortPriority);

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
