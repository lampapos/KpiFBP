package edu.kpi.fbp.sample.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.kpi.fbp.model.ComponentModel;
import edu.kpi.fbp.model.LinkModel;
import edu.kpi.fbp.model.NetworkModel;
import edu.kpi.fbp.sample.network.Generator;
import edu.kpi.fbp.sample.network.PrintResult;
import edu.kpi.fbp.sample.network.Summator;
import edu.kpi.fbp.utils.XmlIo;

/**
 * Sample class which builds simple network and saves it in XML form.
 * @author Pustovit Michael, pustovitm@gmail.com
 */
public final class ModelBuilder {

  private ModelBuilder() {
    // do nothing
  }

  /**
   * Entry point.
   * @param args program arguments
   */
  public static void main(final String[] args) {
    final List<ComponentModel> components = new ArrayList<ComponentModel>();
    final List<LinkModel> links = new ArrayList<LinkModel>();

    components.add(new ComponentModel(Generator.class.getCanonicalName(), "_Generate", null, "http://example.com"));
    components.add(new ComponentModel(Summator.class.getCanonicalName(), "_Sum", null, "http://example.com"));
    components.add(new ComponentModel(PrintResult.class.getCanonicalName(), "_Print_result", null, "http://example.com"));

    links.add(new LinkModel("_Generate", "OUT", "_Sum", "IN"));
    links.add(new LinkModel("_Sum", "OUT", "_Print_result", "IN"));

    final NetworkModel netModel = new NetworkModel(components, links, null);

    final String modelStr = XmlIo.serialize(netModel);

    try {
      final FileWriter wr = new FileWriter(new File("res/out.xml"));
      wr.write(modelStr);
      wr.close();
    } catch (final IOException e) {
      e.printStackTrace();
    }


    final NetworkModel deserializedModel = XmlIo.deserialize(new File("res/out.xml"), NetworkModel.class);
    System.out.println(deserializedModel);
  }
}
