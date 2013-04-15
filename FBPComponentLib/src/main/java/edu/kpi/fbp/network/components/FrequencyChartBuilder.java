package edu.kpi.fbp.network.components;

import java.io.File;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;

import com.jpmorrsn.fbp.engine.InPort;
import com.jpmorrsn.fbp.engine.InPorts;
import com.jpmorrsn.fbp.engine.InputPort;
import com.jpmorrsn.fbp.engine.OutPort;
import com.jpmorrsn.fbp.engine.OutputPort;
import com.jpmorrsn.fbp.engine.Packet;

import edu.kpi.fbp.javafbp.ParameterizedComponent;
import edu.kpi.fbp.network.datastucts.HtmlNode;
import edu.kpi.fbp.network.datastucts.NamedArray;
import edu.kpi.fbp.params.ComponentParameter;
import edu.kpi.fbp.params.ComponentParameters;
import edu.kpi.fbp.params.ParameterType;

/**
 *
 *
 * @author Pustovit Michael, pustovitm@gmail.com
 */
@InPorts({
  @InPort(value = FrequencyChartBuilder.PORT_IN, type = NamedArray.class),
  @InPort(value = FrequencyChartBuilder.PORT_IMG_FILE_NAME, type = String.class),
  @InPort(value = FrequencyChartBuilder.PORT_RANGES_COUNT, type = Integer.class),
  @InPort(value = FrequencyChartBuilder.PORT_BUILD_FOLDER, type = String.class)
})

@OutPort(value = FrequencyChartBuilder.PORT_OUT, type = HtmlNode.class)

@ComponentParameters({
  @ComponentParameter(port = FrequencyChartBuilder.PORT_IMG_FILE_NAME, type = ParameterType.STRING, defaultValue = "histogram%d.png"),
  @ComponentParameter(port = FrequencyChartBuilder.PORT_RANGES_COUNT, type = ParameterType.INTEGER, defaultValue = "10"),
  @ComponentParameter(port = FrequencyChartBuilder.PORT_BUILD_FOLDER, type = ParameterType.STRING, defaultValue = "reportBuild")
})
public class FrequencyChartBuilder extends ParameterizedComponent {

  public static final String PORT_IMG_FILE_NAME = "imgFileName";
  public static final String PORT_RANGES_COUNT = "rangesCount";
  public static final String PORT_BUILD_FOLDER = "buildFolder";

  public static final String PORT_IN = "IN";
  public static final String PORT_OUT = "OUT";

  private static final String HTML_TEMPLATE = "<img src=\"%s\">";

  private static final int DIAGRAM_HEIGHT = 400;
  private static final int DIAGRAM_WIDTH = 600;

  private InputPort inPortImgFileName;
  private InputPort inPortRangesCount;
  private InputPort inPortBuildFolderName;

  private InputPort inPort;
  private OutputPort outPort;

  private int id = 0;

  @SuppressWarnings("unchecked")
  @Override
  protected void execute() throws Exception {
    final String imgFileName = readParam(inPortImgFileName);
    final int rangesCount = readParam(inPortRangesCount);
    final String buildFolderName = readParam(inPortBuildFolderName);

    Packet<NamedArray<Float>> pack;

    while ((pack = inPort.receive()) != null) {
      final NamedArray<Float> inArray = pack.getContent();

      if (inArray == null) {
        drop(pack);
        return;
      }

      final HistogramDataset dataset = new HistogramDataset();
      dataset.setType(HistogramType.FREQUENCY);

      int nulls = 0;
      for (final Float d : inArray) {
        if (d == null) {
          nulls++;
        }
      }

      final double[] doub = new double[inArray.size() - nulls];

      int i = 0;
      for (final Float d : inArray) {
        if (d == null) {
          continue;
        } else {
          doub[i] = d;
          i++;
        }
      }

      dataset.addSeries("main", doub, rangesCount);

      final JFreeChart chart = ChartFactory.createHistogram(
          "Frequency histogram",    // chart title
          "Range",                  // domain axis label
          "Entries count",          // range axis label
          dataset,                  // data
          PlotOrientation.VERTICAL, // orientation
          false,                    // include legend
          true,                     // tooltips
          false                     // URLs?
          );

      final File buildDir = new File(buildFolderName);
      if (!buildDir.exists()) {
        buildDir.mkdirs();
      }

      final String imgName = String.format(imgFileName, id);
      final String imgPath = buildFolderName + File.separator + imgName;
      ChartUtilities.saveChartAsPNG(new File(imgPath), chart, DIAGRAM_WIDTH, DIAGRAM_HEIGHT);
      id++;

      drop(pack);

      outPort.send(create(new HtmlNode() {
        @Override
        public String getHtml() {
          return String.format(HTML_TEMPLATE, imgName);
        }
      }));
    }

    outPort.close();
  }

  @Override
  protected void openPorts() {
    inPortImgFileName = openInput(PORT_IMG_FILE_NAME);
    inPortRangesCount = openInput(PORT_RANGES_COUNT);
    inPortBuildFolderName = openInput(PORT_BUILD_FOLDER);

    inPort = openInput(PORT_IN);
    outPort = openOutput(PORT_OUT);
  }

}
