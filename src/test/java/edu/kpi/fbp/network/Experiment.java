package edu.kpi.fbp.network;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.CompactWriter;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import edu.kpi.fbp.javafbp.ParameterizedNetwork;
import edu.kpi.fbp.params.Parameter;
import edu.kpi.fbp.params.ParametersStore;

/**
 * Test class.
 *
 * $$Тестовий клас.$$
 *
 * @author Pustovit Michael, pustovitm@gmail.com
 */
public class Experiment extends ParameterizedNetwork {
  /**
   * @param paramStore the parameters store
   */
  public Experiment(final ParametersStore paramStore) {
    super(paramStore);
  }

  /**
   * Entry point.
   * @param args console params
   * @throws Exception some exceptions
   */
  public static void main(final String[] args) throws Exception {
    final List<Parameter> parameters = new ArrayList<Parameter>();
    parameters.add(new Parameter("count", "30"));

    final ParametersStore paramStore =
        new ParametersStore.Builder()
        .addComponentConfiguration("_Generate", parameters)
        .build();

    final XStream stream = new XStream(new StaxDriver());
    stream.autodetectAnnotations(true);

    final StringWriter sw = new StringWriter();
    stream.marshal(paramStore,  new CompactWriter(sw));

    System.out.println(sw.toString());

    new Experiment(paramStore).go();
  }

  /**
   * Network definition.
   * @exception Exception any exceptions
   */
  @Override
  protected void define() throws Exception {
    component("_Generate", edu.kpi.fbp.network.Generator.class);
    component("_Sum", edu.kpi.fbp.network.Summator.class);
    component("_Print_result", edu.kpi.fbp.network.PrintResult.class);

    connect("_Generate.OUT", "_Sum.IN");
    connect("_Sum.OUT", "_Print_result.IN");
  }
}
