package edu.kpi.fbp;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

import org.junit.Assert;
import org.junit.Test;

import edu.kpi.fbp.utils.ComponentsObserver;

/**
 * Component observer tests.
 * @author Pustovit Michael, pustovitm@gmail.com
 */
public final class ComponentsObserverTest {
  /**
   * Components observer test.
   * @throws ClassNotFoundException the resources file can be not found
   * @throws IOException the URLClassLoader can raise this exception
   */
  @Test
  public void componentObserverTest() throws ClassNotFoundException, IOException {
    final File componentsDir = new File("src/test/resources/components/");
    final ComponentsObserver obs = ComponentsObserver.create(componentsDir);

    Assert.assertEquals(3, obs.getAvailableComponentsSet().size());

    final URLClassLoader cl = new URLClassLoader(new URL [] {componentsDir.toURI().toURL()});
    final Class<?> clazz = cl.loadClass("edu.kpi.fbp.network.Summator");
    Assert.assertEquals(clazz, obs.getAvailableComponentsSet().get("edu.kpi.fbp.network.Summator").getComponentClass());
    cl.close();
  }
}
