package edu.kpi.fbp;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import edu.kpi.fbp.utils.ComponentsObserver;

/**
 * Component observer tests.
 * @author Pustovit Michael, pustovitm@gmail.com
 */
public final class ComponentsObserverTest {
  private static final int OVERAL_COMPONENTS_COUNT = 11;

  /**
   * Components observer test.
   * @throws ClassNotFoundException the resources file can be not found
   * @throws IOException the URLClassLoader can raise this exception
   */
  @Test
  public void componentObserverTest() throws ClassNotFoundException, IOException {
    final File componentsDir = new File("src/test/resources/components/");
    final ComponentsObserver obs = ComponentsObserver.create(componentsDir);

    Assert.assertEquals(OVERAL_COMPONENTS_COUNT, obs.getAvailableComponentsSet().size());

//    final URLClassLoader cl = new URLClassLoader(new URL [] {new URL("file:///windows/docs/prog/diploma/KpiFBP/FBPUtils/src/test/resources/components/FBPComponentLib-1.0.jar")});
//    final Class<?> clazz = cl.loadClass("edu.kpi.fbp.network.components.Generator");
//    Assert.assertEquals(clazz, obs.getAvailableComponentsSet().get("edu.kpi.fbp.network.components.Generator").getComponentClass());
//
//    Assert.assertNotEquals(clazz,
//        obs.getAvailableComponentsSet().get("edu.kpi.fbp.network.Summator").getComponentClass());
//    cl.close();
  }
}
