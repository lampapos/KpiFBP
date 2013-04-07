package edu.kpi.fbp;

import static org.junit.Assert.assertEquals;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;

import org.junit.Test;

import edu.kpi.fbp.model.NetworkModel;
import edu.kpi.fbp.utils.JarBuilder;
import edu.kpi.fbp.utils.XmlIo;

/**
 * Not test but just sample.
 * @author Pustovit Michael, pustovitm@gmail.com
 */
public class JarBuilderTest {

  /**
   * Build jar sample.
   * @throws IOException exception which can be raised during building
   */
  @Test
  public void jarBuildSample() throws IOException {
    final ClassLoader classLoader = JarBuilderTest.class.getClassLoader();
    final BufferedInputStream inNetworkModelStream =
        new BufferedInputStream(classLoader.getResourceAsStream("out_test.xml"));
    final NetworkModel model = XmlIo.deserialize(inNetworkModelStream, NetworkModel.class);

    final File resJarFile = new File("/home/mih/ResJar.jar");

    JarBuilder.buildAndSaveJar(model, resJarFile);
    assertEquals(true, true);
  }

}
