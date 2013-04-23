package edu.kpi.fbp;

import java.io.File;

import org.junit.Assert;

import edu.kpi.fbp.model.NetworkModel;
import edu.kpi.fbp.utils.NetworkStarter;
import edu.kpi.fbp.utils.XmlIo;

/**
 * Fake test of network starter sample (we starts network from it's object model).
 * @author Pustovit Michael, pustovitm@gmail.com
 */
public final class NetworkStarterSample {
  private NetworkStarterSample() {
    // do nothing
  }

  /**
   * Entry point.
   * @param args the console args
   */
  public static void main(final String[] args) {
    final NetworkModel deserializedModel = XmlIo.deserialize(new File("src/test/resources/out_test.xml"), NetworkModel.class);

    try {
      NetworkStarter.startNetwork(deserializedModel, new File("src/test/resources/components/"), null);
    } catch (final Exception e) {
      e.printStackTrace();
    }

    Assert.assertEquals(true, true);
  }
}
