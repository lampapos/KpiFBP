package edu.kpi.fbp.sample;

import java.io.File;

import edu.kpi.fbp.model.NetworkModel;
import edu.kpi.fbp.utils.NetworkStarter;
import edu.kpi.fbp.utils.XmlIo;

/**
 * Netwok starter sample (we starts network from it's object model).
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
    final NetworkModel deserializedModel = XmlIo.deserialize(new File("res/out.xml"), NetworkModel.class);

    try {
      NetworkStarter.startNetwork(deserializedModel);
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }
}
