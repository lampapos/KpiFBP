package edu.kpi.fbp.network.components;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.jpmorrsn.fbp.engine.Component;
import com.jpmorrsn.fbp.engine.InPort;
import com.jpmorrsn.fbp.engine.InPorts;
import com.jpmorrsn.fbp.engine.InputPort;
import com.jpmorrsn.fbp.engine.Packet;

import edu.kpi.fbp.network.datastucts.HtmlNode;
import edu.kpi.fbp.params.ComponentParameter;
import edu.kpi.fbp.params.ParamUtils;
import edu.kpi.fbp.params.ParameterType;

/**
 * The HTML report builder.
 *
 * @author Pustovit Michael, pustovitm@gmail.com
 */
@InPorts({
  @InPort(value = HtmlReport.PORT_IN, arrayPort = true),
  @InPort(value = HtmlReport.PORT_BUILD_DIR, type = String.class)
})

@ComponentParameter(port = HtmlReport.PORT_BUILD_DIR,  type = ParameterType.STRING, defaultValue = "reportBuild")
public class HtmlReport extends Component {
  public static final String PORT_IN = "IN";
  public static final String PORT_BUILD_DIR = "BUILD_DIR";

  private InputPort[] inPorts;

  private InputPort inPortDirectory;

  @SuppressWarnings("unchecked")
  @Override
  protected void execute() throws Exception {
    final String builtDirectory = ParamUtils.readParam(this, inPortDirectory);

    final List<List<HtmlNode>> nodes = new ArrayList<List<HtmlNode>>();

    for (final InputPort port : inPorts) {
      final List<HtmlNode> sameTypeNodes = new ArrayList<HtmlNode>();

      Packet<HtmlNode> pack;
      while ((pack = port.receive()) != null) {
        sameTypeNodes.add(pack.getContent());
        drop(pack);
      }

      nodes.add(sameTypeNodes);
      port.close();
    }

    final StringBuilder strBuilder = new StringBuilder();
    strBuilder.append("<html>\n");

    for (int i = 0; i < nodes.get(0).size(); i++) {
      final List<HtmlNode> htmlBlock = new ArrayList<HtmlNode>();
      for (final List<HtmlNode> list : nodes) {
        htmlBlock.add(list.get(i));
      }

      Collections.sort(htmlBlock, new Comparator<HtmlNode>() {
        public int compare(final HtmlNode o1, final HtmlNode o2) {
          return o1.getPriority() - o2.getPriority();
        }
      });

      for (final HtmlNode node : htmlBlock) {
        strBuilder.append(node.getHtml());
        strBuilder.append("\n");
      }
    }

    strBuilder.append("</html>\n");

    final File buildDir = new File(builtDirectory);
    if (!buildDir.exists()) {
      buildDir.mkdirs();
    }

    final FileWriter writer = new FileWriter(new File(builtDirectory + File.separator + "report.html"));
    writer.write(strBuilder.toString());
    writer.close();
  }

  @Override
  protected void openPorts() {
    inPorts = openInputArray(PORT_IN);
    inPortDirectory = openInput(PORT_BUILD_DIR);
  }

}
