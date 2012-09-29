package org.kpi.fict.fbp;

import com.jpmorrsn.fbp.engine.Network;

/**
 * Test class.
 * @author Pustovit Michael, pustovitm@gmail.com
 */
public class Experiment extends Network {
  /**
   * Entry point.
   * @param args console params
   * @throws Exception some exceptions
   */
  public static void main(final String[] args) throws Exception {
    new Experiment().go();
  }

  /**
   * Network definition.
   */
  @Override
  protected void define() throws Exception {
    component("_Generate", org.kpi.fict.fbp.Generator.class);
    component("_Sum", org.kpi.fict.fbp.Summator.class);
    component("_Print_result", org.kpi.fict.fbp.PrintResult.class);
    
    connect(component("_Generate"), port("OUT"), component("_Sum"), port("IN"));
    connect(component("_Sum"), port("OUT"), component("_Print_result"), port("IN"));
  }
}
