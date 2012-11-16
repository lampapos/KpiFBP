package edu.kpi.fbp.sample;

import java.io.File;

import edu.kpi.fbp.utils.ComponentsObserver;

/**
 * Component observer sample.
 * @author Pustovit Michael, pustovitm@gmail.com
 */
public final class ComponentsObserverSample {
  private ComponentsObserverSample() {
    // just empty constructor
  }

  /**
   * Entry point.
   * @param args the console params
   */
  public static void main(final String[] args) {
    final ComponentsObserver obs = ComponentsObserver.create(new File("components"));
    System.out.println(obs.getAvailableComponentsSet());
  }
}
