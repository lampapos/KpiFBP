package edu.kpi.fbp.network.components;

import com.jpmorrsn.fbp.engine.Component;
import com.jpmorrsn.fbp.engine.InPort;
import com.jpmorrsn.fbp.engine.InPorts;
import com.jpmorrsn.fbp.engine.InputPort;
import com.jpmorrsn.fbp.engine.OutPort;
import com.jpmorrsn.fbp.engine.OutputPort;
import com.jpmorrsn.fbp.engine.Packet;

import edu.kpi.fbp.network.datastucts.HtmlNode;
import edu.kpi.fbp.params.ComponentParameter;
import edu.kpi.fbp.params.ParamUtils;
import edu.kpi.fbp.params.ParameterType;

/**
 * Html labeler (output: label + value).
 *
 * @author Pustovit Michael, pustovitm@gmail.com
 */
@InPorts({
  @InPort(HtmlLabeler.PORT_IN),
  @InPort(value = HtmlLabeler.PORT_TEXT, type = String.class)
})
@OutPort(value = HtmlLabeler.PORT_OUT, type = HtmlNode.class)
@ComponentParameter(port = HtmlLabeler.PORT_TEXT, type = ParameterType.STRING, defaultValue = "")
public class HtmlLabeler extends Component {
  public static final String PORT_IN = "IN";
  public static final String PORT_TEXT = "TEXT";
  public static final String PORT_OUT = "OUT";

  private static final String HTML_TEMPLATE = "<p>%s %s</p>";

  private InputPort inPort;
  private InputPort inPortText;
  private OutputPort outPort;

  @Override
  protected void execute() throws Exception {
    final String text = ParamUtils.readParam(this, inPortText);

    Packet<?> pack;
    while ((pack = inPort.receive()) != null) {
      final String body = String.format(HTML_TEMPLATE, text, pack.getContent().toString());

      outPort.send(create(new HtmlNode() {
        @Override
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
    inPortText = openInput(PORT_TEXT);
    outPort = openOutput(PORT_OUT);
  }

}
