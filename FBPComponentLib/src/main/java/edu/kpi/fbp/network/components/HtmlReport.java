package edu.kpi.fbp.network.components;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.jpmorrsn.fbp.engine.InPort;
import com.jpmorrsn.fbp.engine.InputPort;
import com.jpmorrsn.fbp.engine.Packet;

import edu.kpi.fbp.javafbp.ParameterizedComponent;
import edu.kpi.fbp.network.datastucts.HtmlNode;
import edu.kpi.fbp.params.ComponentParameter;
import edu.kpi.fbp.params.ParameterBundle;
import edu.kpi.fbp.params.ParameterType;

@ComponentParameter(name = HtmlReport.PARAM_BUILD_DIR,  type = ParameterType.STRING, defaultValue = "reportBuild")
@InPort(value = HtmlReport.PORT_IN, arrayPort = true)
public class HtmlReport extends ParameterizedComponent {
  public static final String PORT_IN = "IN";

  public static final String PARAM_BUILD_DIR = "buildDir";

  private InputPort[] inPorts;

  private String builtDirectory;

  @Override
  public void setParameters(final ParameterBundle paramBundle) {
    builtDirectory = paramBundle.getString(PARAM_BUILD_DIR);
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void execute() throws Exception {
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
      for (final List<HtmlNode> list : nodes) {
        strBuilder.append(list.get(i).getHtml());
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
  }

}
