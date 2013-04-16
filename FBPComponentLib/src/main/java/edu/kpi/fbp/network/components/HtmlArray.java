package edu.kpi.fbp.network.components;

import com.jpmorrsn.fbp.engine.InPort;
import com.jpmorrsn.fbp.engine.OutPort;

import edu.kpi.fbp.network.datastucts.HtmlNode;
import edu.kpi.fbp.network.datastucts.NamedArray;

/**
 * Component which builds HTML representation of named array.
 *
 * @author Pustovit Michael, pustovitm@gmail.com
 */
@InPort(value = SingleNumberStatComponent.PORT_IN, type = NamedArray.class)
@OutPort(value = SingleNumberStatComponent.PORT_OUT, type = HtmlNode.class)
public class HtmlArray extends SingleNumberStatComponent<NamedArray<?>, HtmlNode> {

  @Override
  protected HtmlNode strategy(final NamedArray<?> in) {
    final StringBuilder strBuilder = new StringBuilder();

    strBuilder.append("<html>");
    strBuilder.append("<table border=\"1\">");
    strBuilder.append("<tr><th>");
    strBuilder.append(in.getName());
    strBuilder.append("</tr></th>");

    for (final Object o : in) {
      strBuilder.append("<tr><td>");
      strBuilder.append(o.toString());
      strBuilder.append("</tr></td>");
    }

    strBuilder.append("</table>");
    strBuilder.append("</html>");

    return new HtmlNode(0, strBuilder.toString());
  }

}
