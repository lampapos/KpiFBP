package edu.kpi.fbp.network.components;

import com.jpmorrsn.fbp.engine.InPort;
import com.jpmorrsn.fbp.engine.InputPort;
import com.jpmorrsn.fbp.engine.OutPort;
import com.jpmorrsn.fbp.engine.OutputPort;
import com.jpmorrsn.fbp.engine.Packet;

import edu.kpi.fbp.javafbp.ParameterizedComponent;
import edu.kpi.fbp.network.datastucts.HtmlNode;
import edu.kpi.fbp.params.ComponentParameter;
import edu.kpi.fbp.params.ParameterBundle;
import edu.kpi.fbp.params.ParameterType;

@InPort(HtmlLabeler.PORT_IN)
@OutPort(value = HtmlLabeler.PORT_OUT, type = HtmlNode.class)
@ComponentParameter(name = HtmlLabeler.PARAM_TEXT, type = ParameterType.STRING, defaultValue = "")
public class HtmlLabeler extends ParameterizedComponent {
  public static final String PORT_IN = "IN";
  public static final String PORT_OUT = "OUT";

  public static final String PARAM_TEXT = "text";

  private static final String HTML_TEMPLATE = "<p>%s %s</p>";

  private InputPort inPort;
  private OutputPort outPort;

  private String text;

  @Override
  protected void execute() throws Exception {
    Packet<?> pack;
    while ((pack = inPort.receive()) != null) {
      final String body = String.format(HTML_TEMPLATE, text, pack.getContent().toString());

      outPort.send(create(new HtmlNode() {
        public String getHtml() {
          return body;
        }
      }));

      drop(pack);
    }

    inPort.close();
    outPort.close();
  }

  @Override
  protected void openPorts() {
    inPort = openInput(PORT_IN);
    outPort = openOutput(PORT_OUT);
  }

  @Override
  public void setParameters(final ParameterBundle arg0) {
    text = arg0.getString(PARAM_TEXT);
  }

}
