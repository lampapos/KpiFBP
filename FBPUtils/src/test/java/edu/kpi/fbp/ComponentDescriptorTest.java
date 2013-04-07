package edu.kpi.fbp;

import org.junit.Assert;
import org.junit.Test;

import edu.kpi.fbp.javafbp.ComponentDescriptor;
import edu.kpi.fbp.network.Generator;

/**
 * Network component descriptor sample.
 *
 * $$Приклад дескриптора компонента.$$
 *
 * @author Pustovit Michael, pustovitm@gmail.com
 */
public final class ComponentDescriptorTest {
  /**
   * Entry point.
   */
  @Test
  public void componentDescriptorTest() {
    final ComponentDescriptor desc = ComponentDescriptor.buildDescriptor(Generator.class);

    Assert.assertEquals("Generates stream of packets under control of a counter", desc.getDescription());
    Assert.assertEquals(false, desc.isMustRun());

  }
}
