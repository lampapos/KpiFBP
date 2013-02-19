package edu.kpi.fbp;

import edu.kpi.fbp.javafbp.ParameterizedNetwork;
import edu.kpi.fbp.params.ParametersStore;

/**
 * Just sample generated network.
 */
public class SampleNetwork extends ParameterizedNetwork {
  /**
   * @param paramStore the parameters store
   */
  public SampleNetwork(final ParametersStore paramStore) {
    super(paramStore);
  }

  /**
   * Network definition.
   * @exception Exception any exceptions
   */
  @Override
  protected void define() throws Exception {
    // Components
    component("_Generate", edu.kpi.fbp.network.Generator.class);
    component("_Sum", edu.kpi.fbp.network.Summator.class);
    component("_Print_result", edu.kpi.fbp.network.PrintResult.class);

    // Links
    connect("_Generate.OUT", "_Sum.IN");
    connect("_Sum.OUT", "_Print_result.IN");
  }
}
