package edu.kpi.fbp.network.components;

import java.io.File;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;

import com.jpmorrsn.fbp.engine.InPort;
import com.jpmorrsn.fbp.engine.InputPort;
import com.jpmorrsn.fbp.engine.OutPort;
import com.jpmorrsn.fbp.engine.OutputPort;
import com.jpmorrsn.fbp.engine.Packet;

import edu.kpi.fbp.javafbp.ParameterizedComponent;
import edu.kpi.fbp.network.datastucts.HtmlNode;
import edu.kpi.fbp.network.datastucts.NamedArray;
import edu.kpi.fbp.params.ComponentParameter;
import edu.kpi.fbp.params.ComponentParameters;
import edu.kpi.fbp.params.ParameterBundle;
import edu.kpi.fbp.params.ParameterType;

/**
 *
 *
 * @author Pustovit Michael, pustovitm@gmail.com
 */
@ComponentParameters({
  @ComponentParameter(name = FrequencyChartBuilder.PARAM_IMG_FILE_NAME, type = ParameterType.STRING, defaultValue = "histogram%d.png"),
  @ComponentParameter(name = FrequencyChartBuilder.PARAM_RANGES_COUNT, type = ParameterType.INTEGER, defaultValue = "10"),
  @ComponentParameter(name = FrequencyChartBuilder.PARAM_BUILD_FOLDER, type = ParameterType.STRING, defaultValue = "reportBuild")
})
@InPort(value = FrequencyChartBuilder.PORT_IN, type = NamedArray.class)
@OutPort(value = FrequencyChartBuilder.PORT_OUT, type = HtmlNode.class)
public class FrequencyChartBuilder extends ParameterizedComponent {

  public static final String PARAM_IMG_FILE_NAME = "imgFileName";
  public static final String PARAM_RANGES_COUNT = "rangesCount";
  public static final String PARAM_BUILD_FOLDER = "buildFolder";

  public static final String PORT_IN = "IN";
  public static final String PORT_OUT = "OUT";

  private static final String HTML_TEMPLATE = "<img src=\"%s\">";

  private static final int DIAGRAM_HEIGHT = 400;
  private static final int DIAGRAM_WIDTH = 600;

  private String imgFileName;
  private int rangesCount;
  private String buildFolderName;

  private InputPort inPort;
  private OutputPort outPort;

  private int id = 0;

  @Override
  public void setParameters(final ParameterBundle paramBundle) {
    imgFileName = paramBundle.getString(PARAM_IMG_FILE_NAME);
    rangesCount = paramBundle.getInt(PARAM_RANGES_COUNT);
    buildFolderName = paramBundle.getString(PARAM_BUILD_FOLDER);
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void execute() throws Exception {
    Packet<NamedArray<Double>> pack;

    while ((pack = inPort.receive()) != null) {
      final NamedArray<Double> inArray = pack.getContent();

      if (inArray == null) {
        drop(pack);
        return;
      }

      final HistogramDataset dataset = new HistogramDataset();
      dataset.setType(HistogramType.FREQUENCY);

      int nulls = 0;
      for (final Double d : inArray) {
        if (d == null) {
          nulls++;
        }
      }

      final double[] doub = new double[inArray.size() - nulls];

      int i = 0;
      for (final Double d : inArray) {
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
        public String getHtml() {
          return String.format(HTML_TEMPLATE, imgName);
        }
      }));
    }

    outPort.close();
  }

  @Override
  protected void openPorts() {
    inPort = openInput(PORT_IN);
    outPort = openOutput(PORT_OUT);
  }

}
