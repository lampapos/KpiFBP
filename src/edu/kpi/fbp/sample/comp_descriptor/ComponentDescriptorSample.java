package edu.kpi.fbp.sample.comp_descriptor;

import edu.kpi.fbp.javafbp.ComponentDescriptor;
import edu.kpi.fbp.sample.network.Generator;

/**
 * Networ component descriptor sample.
 * @author Pustovit Michael, pustovitm@gmail.com
 */
public final class ComponentDescriptorSample {
  private ComponentDescriptorSample() {
    // do nothing
  }

  /**
   * Entry point.
   * @param args command line args
   */
  public static void main(final String[] args) {
    System.out.println("Description: " + ComponentDescriptor.getComponentDescription(Generator.class));
    System.out.println("MustRun: " + ComponentDescriptor.isComponentMustRun(Generator.class));
    System.out.println("Parameters: " + ComponentDescriptor.getParameters(Generator.class));
    System.out.println("Priority: " + ComponentDescriptor.getComponentPriority(Generator.class));
    System.out.println("InPorts: " + ComponentDescriptor.getInputPorts(Generator.class));
    System.out.println("OutPorts: " + ComponentDescriptor.getOutputPorts(Generator.class));
  }
}
