package org.kpi.fict.fbp.sample;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.kpi.fict.fbp.javafbp.ParameterizedNetwork;
import org.kpi.fict.fbp.params.Parameter;
import org.kpi.fict.fbp.params.ParametersStore;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.CompactWriter;
import com.thoughtworks.xstream.io.xml.StaxDriver;

/**
 * Test class.
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
    final List<Parameter> parameters = new ArrayList<>();
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
    component("_Generate", org.kpi.fict.fbp.sample.Generator.class);
    component("_Sum", org.kpi.fict.fbp.sample.Summator.class);
    component("_Print_result", org.kpi.fict.fbp.sample.PrintResult.class);

    connect(component("_Generate"), port("OUT"), component("_Sum"), port("IN"));
    connect(component("_Sum"), port("OUT"), component("_Print_result"), port("IN"));
  }
}
