package edu.kpi.fbp;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import edu.kpi.fbp.model.ComponentModel;
import edu.kpi.fbp.model.LinkModel;
import edu.kpi.fbp.model.NetworkModel;
import edu.kpi.fbp.network.Generator;
import edu.kpi.fbp.network.PrintResult;
import edu.kpi.fbp.network.Summator;
import edu.kpi.fbp.utils.XmlIo;

/**
 * Model serialization and deserialization to/from XML tests.
 *
 * $$Тести серіалізації та десеріалізації моделі мережі у XML.$$
 *
 * @author Pustovit Michael, pustovitm@gmail.com
 */
public final class ModelBuilderTest {

  /** The result model constructor XML (model which was serialized into XML). */
  private static final String RESULT_XML =
        "<network name=\"SampleNetwork\">\n"
      + "  <components>\n"
      + "    <component className=\"edu.kpi.fbp.network.Generator\" name=\"_Generate\" sourceUrl=\"http://example.com\"/>\n"
      + "    <component className=\"edu.kpi.fbp.network.Summator\" name=\"_Sum\" sourceUrl=\"http://example.com\"/>\n"
      + "    <component className=\"edu.kpi.fbp.network.PrintResult\" name=\"_Print_result\" sourceUrl=\"http://example.com\"/>\n"
      + "  </components>\n"
      + "  <links>\n"
      + "    <link fromComponent=\"_Generate\" fromPort=\"OUT\" toComponent=\"_Sum\" toPort=\"IN\"/>\n"
      + "    <link fromComponent=\"_Sum\" fromPort=\"OUT\" toComponent=\"_Print_result\" toPort=\"IN\"/>\n"
      + "  </links>\n"
      + "  <extra/>\n"
      + "</network>";

  private NetworkModel constructTestModel() {
    final List<ComponentModel> components = new ArrayList<ComponentModel>();
    final List<LinkModel> links = new ArrayList<LinkModel>();

    components.add(new ComponentModel(Generator.class.getCanonicalName(), "_Generate", null, "http://example.com"));
    components.add(new ComponentModel(Summator.class.getCanonicalName(), "_Sum", null, "http://example.com"));
    components.add(new ComponentModel(PrintResult.class.getCanonicalName(), "_Print_result", null, "http://example.com"));

    links.add(new LinkModel("_Generate", "OUT", "_Sum", "IN"));
    links.add(new LinkModel("_Sum", "OUT", "_Print_result", "IN"));

    final NetworkModel netModel = new NetworkModel("SampleNetwork", components, links, null);
    return netModel;
  }

  /**
   * Serialization of object model to XML.
   * @throws IOException we work with files, so something may go in wrong way (:
   */
  @Test
  public void modelSerializationTest() throws IOException {
    final NetworkModel netModel = constructTestModel();
    final String modelStr = XmlIo.serialize(netModel);
    final StringWriter wr = new StringWriter();
    wr.write(modelStr);
    Assert.assertEquals(RESULT_XML, wr.toString());
    wr.close();
  }

  /**
   * Deserialization from XML to object model.
   */
  @Test
  public void modelDeserializationTest() {
    final NetworkModel deserializedModel = XmlIo.deserialize(new File("src/test/resources/out.xml"), NetworkModel.class);
    Assert.assertEquals(constructTestModel(), deserializedModel);
  }
}
